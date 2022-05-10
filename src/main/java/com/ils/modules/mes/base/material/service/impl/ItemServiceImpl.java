package com.ils.modules.mes.base.material.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ils.common.exception.ILSBootException;
import com.ils.modules.mes.base.factory.entity.Unit;
import com.ils.modules.mes.base.factory.service.UnitService;
import com.ils.modules.mes.base.material.entity.*;
import com.ils.modules.mes.base.material.mapper.*;
import com.ils.modules.mes.base.material.service.ItemService;
import com.ils.modules.mes.base.material.vo.ItemQualityVO;
import com.ils.modules.mes.base.material.vo.ItemSupplierVO;
import com.ils.modules.mes.base.material.vo.ItemVO;
import com.ils.modules.mes.config.service.DefineFieldValueService;
import com.ils.modules.mes.constants.MesCommonConstant;
import com.ils.modules.mes.constants.TableCodeConstants;
import com.ils.modules.mes.util.CommonUtil;
import com.ils.modules.quartz.entity.QuartzJob;
import com.ils.modules.quartz.service.IQuartzJobService;
import com.ils.modules.system.entity.User;
import com.ils.modules.system.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.quartz.SchedulerException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @Description: 物料定义
 * @Author: fengyi
 * @Date: 2020-10-23
 * @Version: V1.0
 */
@Service
public class ItemServiceImpl extends ServiceImpl<ItemMapper, Item> implements ItemService {

    @Autowired
    private ItemMapper itemMapper;
    @Autowired
    private ItemUnitMapper itemUnitMapper;
    @Autowired
    private ItemStockMapper itemStockMapper;
    @Autowired
    private ItemQualityEmployeeMapper itemQualityEmployeeMapper;
    @Autowired
    private ItemQualityMapper itemQualityMapper;
    @Autowired
    private ItemSupplierMapper itemSupplierMapper;
    @Autowired
    private UnitService unitService;
    @Autowired
    private UserService sysUserService;
    @Autowired
    private DefineFieldValueService defineFieldValueService;
    @Autowired
    private IQuartzJobService quartzJobService;

    /**
     * 定时任务实现类
     */
    public static final String STOCK_AND_VALID_JOB = "com.ils.modules.mes.base.material.service.impl.ItemStockSingleJob";
    public static final String ID = "ItemStockSingleJob";

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public Item saveMain(ItemVO itemVO) {

        this.checkCondition(itemVO);

        Item item = new Item();
        BeanUtils.copyProperties(itemVO, item);
        itemMapper.insert(item);

        List<ItemUnit> itemUnitList = itemVO.getItemUnitList();
        int i = 0;
        if (!CollectionUtils.isEmpty(itemUnitList)) {
            for (ItemUnit entity : itemUnitList) {
                //外键设置
                entity.setSeq(++i);
                entity.setItemId(item.getId());
                itemUnitMapper.insert(entity);
            }
        }

        ItemStock itemStock = itemVO.getItemStock();
        if (null != itemStock) {
            itemStock.setItemId(item.getId());
            itemStockMapper.insert(itemStock);
        }

        if (StringUtils.isNoneBlank(itemVO.getQualityEmployeeIds())) {
            String[] qualityEmployeeIds = itemVO.getQualityEmployeeIds().split(",");
            for (String userId : qualityEmployeeIds) {
                ItemQualityEmployee entity = new ItemQualityEmployee();
                entity.setEmployeeId(userId);
                User user = sysUserService.getById(userId);
                entity.setEmployeeName(user.getRealname());
                //外键设置
                entity.setItemId(item.getId());
                itemQualityEmployeeMapper.insert(entity);
            }
        }

        List<ItemQualityVO> itemQualityList = itemVO.getItemQualityList();
        if (!CollectionUtils.isEmpty(itemQualityList)) {
            for (ItemQuality entity : itemQualityList) {
                //外键设置
                entity.setItemId(item.getId());
                itemQualityMapper.insert(entity);
            }
        }

        List<ItemSupplierVO> itemSupplierList = itemVO.getItemSupplierList();
        if (!CollectionUtils.isEmpty(itemSupplierList)) {
            for (ItemSupplier entity : itemSupplierList) {
                //外键设置
                entity.setItemId(item.getId());
                itemSupplierMapper.insert(entity);
            }
        }
        itemVO.setId(item.getId());
        defineFieldValueService.saveDefineFieldValue(itemVO.getLstDefineFields(),
                TableCodeConstants.ITEM_TABLE_CODE, item.getId());
        return item;
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void updateMain(ItemVO itemVO) {

        this.checkCondition(itemVO);

        Item item = new Item();
        BeanUtils.copyProperties(itemVO, item);
        itemMapper.updateById(item);

        //1.先删除子表数据
        itemUnitMapper.deleteByMainId(item.getId());
        itemStockMapper.deleteByMainId(item.getId());
        itemQualityEmployeeMapper.deleteByMainId(item.getId());
        itemQualityMapper.deleteByMainId(item.getId());
        itemSupplierMapper.deleteByMainId(item.getId());

        List<ItemUnit> itemUnitList = itemVO.getItemUnitList();
        int i = 0;
        for (ItemUnit entity : itemUnitList) {
            // 外键设置
            entity.setSeq(++i);
            entity.setItemId(item.getId());
            itemUnitMapper.insert(entity);
        }

        ItemStock itemStock = itemVO.getItemStock();
        itemStock.setItemId(item.getId());
        itemStockMapper.insert(itemStock);

        if (StringUtils.isNoneBlank(itemVO.getQualityEmployeeIds())) {
            String[] qualityEmployeeIds = itemVO.getQualityEmployeeIds().split(",");
            for (String userId : qualityEmployeeIds) {
                ItemQualityEmployee entity = new ItemQualityEmployee();
                entity.setEmployeeId(userId);
                User user = sysUserService.getById(userId);
                entity.setEmployeeName(user.getRealname());
                // 外键设置
                entity.setItemId(item.getId());
                itemQualityEmployeeMapper.insert(entity);
            }
        }


        List<ItemQualityVO> itemQualityList = itemVO.getItemQualityList();
        for (ItemQuality entity : itemQualityList) {
            // 外键设置
            entity.setItemId(item.getId());
            itemQualityMapper.insert(entity);
        }

        List<ItemSupplierVO> itemSupplierList = itemVO.getItemSupplierList();
        for (ItemSupplier entity : itemSupplierList) {
            // 外键设置
            entity.setItemId(item.getId());
            itemSupplierMapper.insert(entity);
        }

        itemVO.setId(item.getId());
        defineFieldValueService.saveDefineFieldValue(itemVO.getLstDefineFields(),
                TableCodeConstants.ITEM_TABLE_CODE, item.getId());

        this.createMaintenanceQuartzJob(item.getTenantId());
    }

    /**
     * 创建定时任务
     *
     * @param id
     */
    public void createMaintenanceQuartzJob(String tenantId) {
        QuartzJob quartzJob = quartzJobService.getById(ID);
        if (null == quartzJob) {
            String baseCron = "0 */1 * * * ? *";
            quartzJob = new QuartzJob();
            quartzJob.setId(ID);
            quartzJob.setCronExpression(baseCron);
            quartzJob.setJobNameKey(STOCK_AND_VALID_JOB);
            quartzJob.setJobClassName(STOCK_AND_VALID_JOB);
            quartzJob.setStatus(MesCommonConstant.STATUS_NORMAL);
            Set<String> tenantIdSet = new HashSet<>();
            tenantIdSet.add(tenantId);
            quartzJob.setParameter(JSONObject.toJSONString(tenantIdSet));
            quartzJobService.saveAndScheduleJob(quartzJob);
        } else {
            String parameter = quartzJob.getParameter();
            Set<String> tenantIdSet = new HashSet<>();
            List<String> tenantIdList = JSONArray.parseArray(parameter, String.class);
            if (!CommonUtil.isEmptyOrNull(tenantIdList)) {
                tenantIdSet = new HashSet<>(tenantIdList);
            }
            tenantIdSet.add(tenantId);
            quartzJob.setParameter(JSONObject.toJSONString(tenantIdSet));
            try {
                quartzJobService.editAndScheduleJob(quartzJob);
            } catch (SchedulerException e) {
                throw new ILSBootException("执行任务失败");
            }
        }
    }

    /**
     * 保存或者更新的时候条件检验
     *
     * @param itemVO
     * @date 2020年11月17日
     */
    private void checkCondition(ItemVO itemVO) {
        // 编码不重复
        QueryWrapper<Item> queryWrapper = new QueryWrapper<Item>();
        queryWrapper.eq("item_code", itemVO.getItemCode());
        if (StringUtils.isNoneBlank(itemVO.getId())) {
            queryWrapper.ne("id", itemVO.getId());
        }
        Item obj = baseMapper.selectOne(queryWrapper);
        if (obj != null) {
            throw new ILSBootException("B-FCT-0009");
        }

    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void delMain(String id) {
        itemUnitMapper.deleteByMainId(id);
        itemStockMapper.deleteByMainId(id);
        itemQualityEmployeeMapper.deleteByMainId(id);
        itemQualityMapper.deleteByMainId(id);
        itemSupplierMapper.deleteByMainId(id);
        itemMapper.deleteById(id);
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void delBatchMain(List<String> idList) {
        for (Serializable id : idList) {
            itemUnitMapper.deleteByMainId(id.toString());
            itemStockMapper.deleteByMainId(id.toString());
            itemQualityEmployeeMapper.deleteByMainId(id.toString());
            itemQualityMapper.deleteByMainId(id.toString());
            itemSupplierMapper.deleteByMainId(id.toString());
            itemMapper.deleteById(id);
        }
    }

    @Override
    public List<Unit> queryItemUnitListByMainId(String mainId) {
        List<ItemUnit> itemUnitList = itemUnitMapper.selectByMainId(mainId);
        String mainUnit = null;
        if (null != itemUnitList && itemUnitList.size() > 0) {
            mainUnit = itemUnitList.get(0).getMainUnit();
        } else {
            Item item = this.getById(mainId);
            mainUnit = item.getMainUnit();
        }

        List<String> unitIds =
                itemUnitList.stream().map(itemUnit -> itemUnit.getConvertUnit()).collect(Collectors.toList());
        if (StringUtils.isNotBlank(mainUnit)) {
            unitIds.add(mainUnit);
        }
        if (unitIds.isEmpty()) {
            return new ArrayList<Unit>(0);
        }
        QueryWrapper<Unit> queryWrapper = new QueryWrapper();
        queryWrapper.in("id", unitIds);
        List<Unit> lstUnit = unitService.list(queryWrapper);
        return lstUnit;
    }

    @Override
    public List<ItemUnit> queryItemUnitByItemId(String itemId) {
        List<ItemUnit> itemUnitList = itemUnitMapper.selectByMainId(itemId);
        ItemUnit itemUnit = new ItemUnit();
        if (null != itemUnitList && itemUnitList.size() > 0) {
            String mainUnit = itemUnitList.get(0).getMainUnit();
            itemUnit.setItemId(itemId);
            itemUnit.setConvertQty(BigDecimal.ONE);
            itemUnit.setConvertUnit(mainUnit);
            itemUnit.setMainUnit(mainUnit);
            itemUnit.setMainUnitQty(BigDecimal.ONE);
        } else {
            Item item = this.getById(itemId);
            itemUnit.setItemId(itemId);
            itemUnit.setConvertQty(BigDecimal.ONE);
            itemUnit.setConvertUnit(item.getMainUnit());
            itemUnit.setMainUnit(item.getMainUnit());
            itemUnit.setMainUnitQty(BigDecimal.ONE);
        }
        itemUnitList.add(itemUnit);
        return itemUnitList;
    }

    @Override
    public int queryBussDataByItemId(String itemId) {
        return baseMapper.queryBussDataByItemId(itemId);
    }

}
