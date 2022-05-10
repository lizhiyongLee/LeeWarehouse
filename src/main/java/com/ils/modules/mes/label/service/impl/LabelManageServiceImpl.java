package com.ils.modules.mes.label.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ils.common.exception.ILSBootException;
import com.ils.common.system.query.QueryGenerator;
import com.ils.modules.mes.base.factory.entity.Supplier;
import com.ils.modules.mes.base.factory.entity.Unit;
import com.ils.modules.mes.base.factory.service.SupplierService;
import com.ils.modules.mes.base.factory.service.UnitService;
import com.ils.modules.mes.base.material.entity.Item;
import com.ils.modules.mes.base.material.service.ItemService;
import com.ils.modules.mes.base.ware.entity.WareStorage;
import com.ils.modules.mes.base.ware.service.WareStorageService;
import com.ils.modules.mes.enums.LabelFromEnum;
import com.ils.modules.mes.enums.LabelUseStatusEnum;
import com.ils.modules.mes.enums.ReceiptStatusEnum;
import com.ils.modules.mes.enums.ZeroOrOneEnum;
import com.ils.modules.mes.label.entity.LabelManage;
import com.ils.modules.mes.label.entity.LabelManageLine;
import com.ils.modules.mes.label.mapper.LabelManageLineMapper;
import com.ils.modules.mes.label.mapper.LabelManageMapper;
import com.ils.modules.mes.label.service.LabelManageService;
import com.ils.modules.mes.label.vo.LabelManageVO;
import com.ils.modules.mes.util.CommonUtil;
import com.ils.modules.mes.util.SnowflakeConfig;
import com.ils.modules.system.entity.CodeGenerator;
import com.ils.modules.system.service.CodeGeneratorService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author lishaojie
 * @description
 * @date 2021/7/13 10:54
 */
@Slf4j
@Service
public class LabelManageServiceImpl extends ServiceImpl<LabelManageMapper, LabelManage> implements LabelManageService {

    @Autowired
    private LabelManageMapper labelManageMapper;
    @Autowired
    private LabelManageLineMapper labelManageLineMapper;
    @Autowired
    private ItemService itemService;
    @Autowired
    private UnitService unitService;
    @Autowired
    private SupplierService supplierService;
    @Autowired
    private CodeGeneratorService codeGeneratorService;
    @Autowired
    private WareStorageService wareStorageService;

    @Autowired
    SnowflakeConfig snowflakeConfig;


    private final String IGNORE_PROPERTY = "id,createBy,createTime,updateBy,updateTime,deleted,tenantId";
    private static final String DEFAULT_CODE = "QRCODE_";
    private static final String DEFAULT_RULE = "默认规则";

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void saveMain(LabelManage labelManage) {
        labelManage.setReceiptStatus(ReceiptStatusEnum.NEW.getValue());
        //补充数据
        Item item = itemService.getById(labelManage.getItemId());
        Unit unit = unitService.getById(labelManage.getUnitId());
        String supplierId = labelManage.getSupplierId();
        if (StringUtils.isNotBlank(supplierId)) {
            Supplier supplier = supplierService.getById(supplierId);
            labelManage.setSupplierCode(supplier.getSupplierCode());
            labelManage.setSupplierName(supplier.getSupplierName());
        }

        labelManage.setItemCode(item.getItemCode());
        labelManage.setItemName(item.getItemName());
        if (StringUtils.isBlank(labelManage.getLabelFrom())) {
            labelManage.setLabelFrom(LabelFromEnum.MANUAL.getValue());
        }
        if (StringUtils.isBlank(labelManage.getLabelRule())) {
            labelManage.setLabelRule(DEFAULT_RULE);
        }
        labelManageMapper.insert(labelManage);
        //子表插入
        String[] ignoreProperty = IGNORE_PROPERTY.split(",");
        String labelRule = labelManage.getLabelRule();
        String id = labelManage.getId();
        Integer labelCount = labelManage.getLabelCount();
        for (int i = 0; i < labelCount; i++) {
            LabelManageLine labelManageLine = new LabelManageLine();
            BeanUtils.copyProperties(labelManage, labelManageLine, ignoreProperty);
            labelManageLine.setLabelManageId(id);
            labelManageLine.setUnitId(unit.getId());
            labelManageLine.setUnitName(unit.getUnitName());
            labelManageLine.setPrintStatus(ZeroOrOneEnum.ZERO.getStrCode());
            labelManageLine.setUseStatus(LabelUseStatusEnum.UNUSED.getValue());
            String qrcode;
            if (!DEFAULT_RULE.equals(labelRule)) {
                qrcode = codeGeneratorService.getNextCode(CommonUtil.getTenantId(), labelRule, null);
            } else {
                qrcode = DEFAULT_CODE + snowflakeConfig.snowflakeId();
            }
            labelManageLine.setQrcode(qrcode);
            labelManageLineMapper.insert(labelManageLine);
        }
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void updateMain(LabelManage labelManage) {
        String storageCode = labelManage.getStorageCode();
        QueryWrapper<WareStorage> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("storage_code", storageCode);
        WareStorage wareStorage = wareStorageService.getOne(queryWrapper);
        if (null != wareStorage) {
            labelManage.setStorageCode(wareStorage.getStorageCode());
            labelManage.setStorageName(wareStorage.getStorageName());
        }
        labelManageMapper.updateById(labelManage);
        Date produceDate = labelManage.getProduceDate();
        Date validDate = labelManage.getValidDate();
        List<LabelManageLine> labelManageLineList = labelManageLineMapper.selectByMainId(labelManage.getId());
        //日期更新时，更新标签行日期
        if (null != produceDate || null != validDate) {
            labelManageLineList.forEach(labelManageLine -> {
                labelManageLine.setProduceDate(produceDate);
                labelManageLine.setValidDate(validDate);
                labelManageLineMapper.updateById(labelManageLine);
            });
        }
        //仓位更新，同步更新行数据
        if (null != wareStorage) {
            labelManageLineList.forEach(labelManageLine -> {
                if (!labelManageLine.getUseStatus().equals(LabelUseStatusEnum.UNUSED.getValue())) {
                    labelManageLine.setStorageCode(storageCode);
                    labelManageLine.setStorageName(wareStorage.getStorageName());
                    labelManageLineMapper.updateById(labelManageLine);
                }
            });
        }
        //供应商更新，同步更新行数据
        String supplierId = labelManage.getSupplierId();
        Supplier supplier = supplierService.getById(supplierId);
        if (null != supplier) {
            labelManageLineList.forEach(labelManageLine -> {
                if (!labelManageLine.getUseStatus().equals(LabelUseStatusEnum.UNUSED.getValue())) {
                    labelManageLine.setSupplierCode(supplier.getSupplierCode());
                    labelManageLine.setSupplierName(supplier.getSupplierName());
                    labelManageLine.setSupplierId(supplier.getId());
                    labelManageLine.setSupplierBatch(labelManage.getSupplierBatch());
                    labelManageLineMapper.updateById(labelManageLine);
                }
            });
        }
    }

    @Override
    public LabelManageVO getDetailById(String id) {
        LabelManage labelManage = labelManageMapper.selectById(id);
        List<CodeGenerator> codeGeneratorList = codeGeneratorService.list();
        //规则编码替换为名称
        String codeGeneratorName = DEFAULT_RULE;
        for (CodeGenerator codeGenerator : codeGeneratorList) {
            if (labelManage.getLabelRule().equals(codeGenerator.getGeneratorCode())) {
                codeGeneratorName = codeGenerator.getGeneratorName();
            }
        }
        labelManage.setLabelRule(codeGeneratorName);
        List<LabelManageLine> labelManageLineList = labelManageLineMapper.selectByMainId(id);
        LabelManageVO labelManageVO = new LabelManageVO();
        BeanUtils.copyProperties(labelManage, labelManageVO);
        labelManageVO.setLabelManageLineList(labelManageLineList);
        //计算数值
        Integer printedQuantity = 0;
        Integer usedQuantity = 0;
        Integer cancelQuantity = 0;
        for (LabelManageLine labelManageLine : labelManageLineList) {
            if (LabelUseStatusEnum.CANCEL.getValue().equals(labelManageLine.getUseStatus())) {
                cancelQuantity++;
            }
            if (LabelUseStatusEnum.USED.getValue().equals(labelManageLine.getUseStatus())) {
                usedQuantity++;
            }
            if (ZeroOrOneEnum.ONE.getStrCode().equals(labelManageLine.getPrintStatus())) {
                printedQuantity++;
            }
        }
        labelManageVO.setCancelQuantity(cancelQuantity);
        labelManageVO.setUsedQuantity(usedQuantity);
        labelManageVO.setPrintedQuantity(printedQuantity);
        return labelManageVO;
    }

    @Override
    public Page<LabelManageVO> queryPageList(LabelManage labelManage, Integer pageNo, Integer pageSize, String startTime, String endTime, HttpServletRequest req) {
        QueryWrapper<LabelManage> queryWrapper = QueryGenerator.initQueryWrapper(labelManage, req.getParameterMap());
        if (StringUtils.isNotBlank(startTime) && StringUtils.isNotBlank(endTime)) {
            try {
                Date startDate = DateUtils.parseDate(startTime, "yyyy-MM-dd", "yyyy-MM-dd HH:mm:ss");
                Date endDate = DateUtils.parseDate(endTime, "yyyy-MM-dd", "yyyy-MM-dd HH:mm:ss");
                queryWrapper.between("create_time", startDate, endDate);
            } catch (ParseException e) {
                throw new ILSBootException("日期转换失败!");
            }
        }
        Page<LabelManage> page = new Page<>(pageNo, pageSize);
        IPage<LabelManage> pageList = this.page(page, queryWrapper);
        //转换为VO，获取已打印等数值
        Page<LabelManageVO> dataPage = new Page<>(pageNo, pageSize);
        ArrayList<LabelManageVO> records = new ArrayList<>();
        dataPage.setRecords(records);
        pageList.getRecords().forEach(queryLabelManage -> {
            LabelManageVO detailById = getDetailById(queryLabelManage.getId());
            //去除行数据，详情页再显示
            detailById.setLabelManageLineList(null);
            dataPage.getRecords().add(detailById);
        });
        dataPage.setCurrent(page.getCurrent());
        dataPage.setSize(page.getSize());
        dataPage.setTotal(page.getTotal());
        dataPage.setPages(page.getPages());
        dataPage.setOrders(page.getOrders());
        return dataPage;
    }


    @Override
    public void completionBatch(List<String> ids) {
        for (String id : ids) {
            LabelManage labelManage = labelManageMapper.selectById(id);
            labelManage.setReceiptStatus(ReceiptStatusEnum.END.getValue());
            labelManageMapper.updateById(labelManage);
        }
    }
}
