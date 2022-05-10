package com.ils.modules.mes.base.material.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.joda.time.DateTimeComparator;
import org.joda.time.DateTimeFieldType;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ils.common.system.util.TenantContext;
import com.ils.common.util.BigDecimalUtils;
import com.ils.modules.mes.base.material.entity.Item;
import com.ils.modules.mes.base.material.entity.ItemStock;
import com.ils.modules.mes.base.material.entity.ItemUnit;
import com.ils.modules.mes.base.material.mapper.ItemMapper;
import com.ils.modules.mes.base.material.service.ItemStockService;
import com.ils.modules.mes.base.material.service.ItemUnitService;
import com.ils.modules.mes.constants.MesCommonConstant;
import com.ils.modules.mes.enums.ItemCellQrcodeStatusEnum;
import com.ils.modules.mes.enums.ItemQcStatusEnum;
import com.ils.modules.mes.enums.ZeroOrOneEnum;
import com.ils.modules.mes.material.entity.ItemCell;
import com.ils.modules.mes.material.service.ItemCellService;
import com.ils.modules.mes.util.CommonUtil;
import com.ils.modules.message.entity.SysMessage;
import com.ils.modules.message.service.ISysMessageService;
import com.ils.modules.message.service.MsgHandleServer;
import com.ils.modules.message.vo.MsgParamsVO;
import com.ils.modules.quartz.job.BaseJob;

/**
 * @author lishaojie
 * @description
 * @date 2021/9/7 14:30
 */
public class ItemStockSingleJob extends BaseJob {

    /**
     * 策略关联时间单位1.分钟
     */
    public static final String POLICY_TIME_UNIT_ONE = "1";
    /**
     * 策略关联时间单位2.小时
     */
    public static final String POLICY_TIME_UNIT_TWO = "2";
    /**
     * 策略关联时间单位3.日
     */
    public static final String POLICY_TIME_UNIT_THREE = "3";
    /**
     * 策略关联时间单位4.月
     */
    public static final String POLICY_TIME_UNIT_FOUR = "4";
    /**
     * 策略关联时间单位5.年
     */
    public static final String POLICY_TIME_UNIT_FIVE = "5";

    @Autowired
    private ItemCellService itemCellService;
    @Autowired
    private ItemStockService itemStockService;
    @Autowired
    private ItemUnitService itemUnitService;
    @Autowired
    private MsgHandleServer msgHandleServer;
    @Autowired
    private ItemMapper itemMapper;
    @Autowired
    private ISysMessageService sysMessageService;


    @Override
    public void executeBussion(String parameter) {
        //所有启用的物料id
        QueryWrapper<Item> itemQueryWrapper = new QueryWrapper<>();
        itemQueryWrapper.eq("status", ZeroOrOneEnum.ONE.getStrCode());
        List<String> tenantIdList = JSONArray.parseArray(parameter, String.class);
        for (String tenantId : tenantIdList) {
            TenantContext.setTenant(tenantId);
            List<Item> itemList = itemMapper.selectList(itemQueryWrapper);
            for (Item item : itemList) {
                String id = item.getId();
                //物料单元
                QueryWrapper<ItemCell> itemCellQueryWrapper = new QueryWrapper<ItemCell>();
                itemCellQueryWrapper.eq("item_id", id);
                itemCellQueryWrapper.eq("qrcode_status", ItemCellQrcodeStatusEnum.FACTORY.getValue());
                List<String> qcStatusList = new ArrayList<>();
                qcStatusList.add(ItemQcStatusEnum.QUALIFIED.getValue());
                qcStatusList.add(ItemQcStatusEnum.WAIT_TEST.getValue());
                itemCellQueryWrapper.in("qc_status", qcStatusList);
                List<ItemCell> itemCellList = itemCellService.list(itemCellQueryWrapper);
                //物料库存
                List<ItemStock> itemStocks = itemStockService.selectByMainId(id);
                if (!CommonUtil.isEmptyOrNull(itemStocks)) {
                    ItemStock itemStock = itemStocks.get(0);
                    //检查库存
                    if (null != itemStock.getSafetyStatus() && ZeroOrOneEnum.ONE.getStrCode().equals(itemStock.getSafetyStatus())) {
                        this.checkStock(id, itemCellList, itemStock, tenantId);
                    }
                    //检查有效期
                    if (null != itemStock.getValidTimeStatus() && ZeroOrOneEnum.ONE.getStrCode().equals(itemStock.getValidTimeStatus())) {
                        this.checkValidDate(itemCellList, itemStock, tenantId);
                    }
                }
            }
        }
    }

    private void checkValidDate(List<ItemCell> itemCellList, ItemStock itemStock, String tenantId) {
        Date now = new Date();
        for (ItemCell itemCell : itemCellList) {
            Date validDate = itemCell.getValidDate();
            Date checkDate = formatDate(itemStock.getWarnUnit(), -itemStock.getWarnTime(), validDate);
            //忽略单位：秒
            DateTimeComparator comparator = DateTimeComparator.getInstance(DateTimeFieldType.minuteOfHour());
            if (comparator.compare(checkDate, now) == 0) {
                sendMsg(itemCell, itemStock, tenantId);
            }
        }

    }

    private void checkStock(String id, List<ItemCell> itemCellList, ItemStock itemStock, String tenantId) {
        //库存安全值不为0
        if (null != itemStock.getSafetyStock() && itemStock.getSafetyStock().compareTo(BigDecimal.ZERO) > 0) {
            //物料转换单位
            List<ItemUnit> itemUnitList = itemUnitService.selectByMainId(id);
            //每一个物料单元数据都转换为主单位后，计算当前库存值
            BigDecimal sumQty = BigDecimal.ZERO;
            BigDecimal checkQty = BigDecimal.ZERO;
            for (ItemCell itemCell : itemCellList) {
                for (ItemUnit itemUnit : itemUnitList) {
                    if (itemCell.getUnitId().equals(itemUnit.getConvertUnit())) {
                        sumQty = sumQty.add(BigDecimalUtils.divide(BigDecimalUtils.multiply(itemCell.getQty(), itemUnit.getConvertQty()), itemUnit.getMainUnitQty(), 6));
                    } else {
                        sumQty = sumQty.add(itemCell.getQty());
                    }
                }
            }
            //使用转换单位时，用入库单位计算安全库存
            for (ItemUnit itemUnit : itemUnitList) {
                if (null != itemStock.getInUnit() && itemStock.getInUnit().equals(itemUnit.getConvertUnit())) {
                    checkQty = BigDecimalUtils.divide(BigDecimalUtils.multiply(itemStock.getSafetyStock(), itemUnit.getConvertQty()), itemUnit.getMainUnitQty(), 6);
                }
            }
            //使用主单位时，计算安全库存
            if (checkQty.equals(BigDecimal.ZERO)) {
                checkQty = itemStock.getSafetyStock();
            }
            //比较库存
            if (checkQty.compareTo(sumQty) >= 0) {
                sendMsg(itemMapper.selectById(id), itemStock, sumQty, tenantId);
            }
        }
    }

    private void sendMsg(Item item, ItemStock itemStock, BigDecimal sumQty, String tenantId) {
        QueryWrapper<SysMessage> sysMessageQueryWrapper = new QueryWrapper<>();
        sysMessageQueryWrapper.like("es_receiver", itemStock.getItemManagerId());
        sysMessageQueryWrapper.like("es_param", "\"msgType\":\"" + MesCommonConstant.MSG_ITEM_SAFETY_STOCK);
        sysMessageQueryWrapper.orderByDesc("es_send_time");
        sysMessageQueryWrapper.last("limit 1");
        List<SysMessage> list = sysMessageService.list(sysMessageQueryWrapper);
        Date now = new Date();
        Date date = null;
        if (!CommonUtil.isEmptyOrNull(list)) {
            date = formatDate(itemStock.getSafetyWarnUnit(), itemStock.getSafetyWarnFrequency(), list.get(0).getEsSendTime());
        } else {
            date = now;
        }
        //上一条物料提醒加上提醒频率得到的下次提醒时间需要小于现在,允许2秒误差
        DateTimeComparator comparator = DateTimeComparator.getInstance(DateTimeFieldType.minuteOfHour());
        if (date != null && comparator.compare(date, now) <= 0) {
            JSONObject msg = new JSONObject();
            msg.put("sumQty", sumQty);
            msg.put("itemName", item.getItemName());
            msg.put("safetyStock", itemStock.getSafetyStock());
            msg.put("msgType", MesCommonConstant.MSG_ITEM_SAFETY_STOCK);

            List<String> receiverIds = Arrays.asList(itemStock.getItemManagerId().split(","));
            MsgParamsVO msgParamsVO = new MsgParamsVO(receiverIds, null, null, "系统消息", msg);
            msgParamsVO.setTenantId(tenantId);
            msgHandleServer.sendMsg(MesCommonConstant.MSG_ITEM_SAFETY_STOCK, MesCommonConstant.INFORM, msgParamsVO);
        }
    }

    private void sendMsg(ItemCell itemCell, ItemStock itemStock, String tenantId) {
        JSONObject msg = new JSONObject();
        msg.put("itemName", itemCell.getItemName());
        msg.put("validDate", DateFormatUtils.format(itemCell.getValidDate(), "yyy-MM-dd HH:mm:ss"));
        msg.put("storageName", itemCell.getStorageName());
        String code = "/";
        if (StringUtils.isNoneBlank(itemCell.getQrcode())) {
            code = itemCell.getQrcode();
        }
        if (StringUtils.isNoneBlank(itemCell.getBatch())) {
            code = itemCell.getBatch();
        }
        msg.put("code", code);
        List<String> receiverIds = Arrays.asList(itemStock.getItemManagerId().split(","));
        MsgParamsVO msgParamsVO = new MsgParamsVO(receiverIds, null, null, "系统消息", msg);
        msgParamsVO.setTenantId(tenantId);
        msgHandleServer.sendMsg(MesCommonConstant.MSG_ITEM_CELL_VALID, MesCommonConstant.INFORM, msgParamsVO);
    }

    public Date formatDate(String unit, Integer validTime, Date date) {
        Calendar instance = Calendar.getInstance();
        instance.setTime(date);
        switch (unit) {
            case POLICY_TIME_UNIT_ONE:
                instance.add(Calendar.MINUTE, validTime);
                return instance.getTime();
            case POLICY_TIME_UNIT_TWO:
                instance.add(Calendar.HOUR, validTime);
                return instance.getTime();
            case POLICY_TIME_UNIT_THREE:
                instance.add(Calendar.DATE, validTime);
                return instance.getTime();
            case POLICY_TIME_UNIT_FOUR:
                instance.add(Calendar.MONTH, validTime);
                return instance.getTime();
            case POLICY_TIME_UNIT_FIVE:
                instance.add(Calendar.YEAR, validTime);
                return instance.getTime();
            default:
                return null;
        }
    }
}
