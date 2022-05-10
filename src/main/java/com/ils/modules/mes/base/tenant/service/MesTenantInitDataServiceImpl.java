package com.ils.modules.mes.base.tenant.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ils.common.system.util.TenantContext;
import com.ils.modules.mes.base.schedule.entity.ScheduleAutoRuleConfigure;
import com.ils.modules.mes.base.schedule.service.ScheduleAutoRuleConfigureService;
import com.ils.modules.message.entity.BussSmsConfigDetail;
import com.ils.modules.message.entity.BussSmsConfigHead;
import com.ils.modules.message.entity.SmsImplConfig;
import com.ils.modules.message.entity.SysMessageTemplate;
import com.ils.modules.message.mapper.BussSmsConfigDetailMapper;
import com.ils.modules.message.mapper.BussSmsConfigHeadMapper;
import com.ils.modules.message.service.BussSmsConfigDetailService;
import com.ils.modules.message.service.BussSmsConfigHeadService;
import com.ils.modules.message.service.ISysMessageTemplateService;
import com.ils.modules.message.service.SmsImplConfigService;
import com.ils.modules.message.vo.BussSmsConfigDetailVO;
import com.ils.modules.system.entity.*;
import com.ils.modules.system.mapper.CodeGeneratorDetailMapper;
import com.ils.modules.system.mapper.CodeGeneratorMapper;
import com.ils.modules.system.mapper.UreportFileMapper;
import com.ils.modules.system.service.*;
import com.ils.modules.system.vo.TenantVO;
import org.apache.ibatis.logging.Log;
import org.apache.ibatis.logging.LogFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.util.*;

/**
 * @author lishaojie
 * @description
 * @date 2021/9/28 10:19
 */
@Service
public class MesTenantInitDataServiceImpl implements TenantInitDataService {


    @Autowired
    private ScheduleAutoRuleConfigureService scheduleAutoRuleConfigureService;
    @Autowired
    private TenantService tenantService;
    @Autowired
    private BizConfigService bizConfigService;
    @Autowired
    private ISysMessageTemplateService sysMessageTemplateService;
    @Autowired
    private BussSmsConfigHeadService bussSmsConfigHeadService;
    @Autowired
    private BussSmsConfigDetailService bussSmsConfigDetailService;
    @Autowired
    private BussSmsConfigHeadMapper bussSmsConfigHeadMapper;
    @Autowired
    private BussSmsConfigDetailMapper bussSmsConfigDetailMapper;
    @Autowired
    private CodeGeneratorMapper codeGeneratorMapper;
    @Autowired
    private CodeGeneratorDetailMapper codeGeneratorDetailMapper;
    @Autowired
    private UreportFileMapper ureportFileMapper;
    @Autowired
    private ReportConfigService reportConfigService;
    @Autowired
    private ExcelHeaderService excelHeaderService;

    private final String[] ignoreProperty = "id,createBy,createTime,updateBy,updateTime,deleted,tenantId".split(",");

    @PostConstruct
    public void initMethod() {
        tenantService.registerInitDataService(this);
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void initData(TenantVO tenant) {
        String tenantId = TenantContext.getTenant();
        String newTenantId = tenant.getId();
        //编码规则
        this.initCodeGenerator(newTenantId);
        //业务配置
        QueryWrapper<BizConfig> bizConfigQueryWrapper = new QueryWrapper<>();
        bizConfigQueryWrapper.eq("tenant_id", tenantId);
        List<BizConfig> bizConfigList = bizConfigService.list(bizConfigQueryWrapper);
        bizConfigList.forEach(bizConfig -> {
            BizConfig newBizConfig = new BizConfig();
            BeanUtils.copyProperties(bizConfig, newBizConfig, ignoreProperty);
            newBizConfig.setTenantId(newTenantId);
            bizConfigService.saveBizConfig(newBizConfig);
        });
        //消息模板管理
        QueryWrapper<SysMessageTemplate> sysMessageTemplateQueryWrapper = new QueryWrapper<>();
        sysMessageTemplateQueryWrapper.eq("tenant_id", tenantId);
        List<SysMessageTemplate> sysMessageTemplateList = sysMessageTemplateService.list(sysMessageTemplateQueryWrapper);
        List<SysMessageTemplate> newSysMessageTemplateList = new ArrayList<>(sysMessageTemplateList.size());
        sysMessageTemplateList.forEach(sysMessageTemplate -> {
            SysMessageTemplate newSysMessageTemplate = new SysMessageTemplate();
            BeanUtils.copyProperties(sysMessageTemplate, newSysMessageTemplate, ignoreProperty);
            newSysMessageTemplate.setTenantId(newTenantId);
            sysMessageTemplateService.save(newSysMessageTemplate);
            newSysMessageTemplateList.add(newSysMessageTemplate);
        });
        //消息配置
        QueryWrapper<BussSmsConfigHead> bussSmsConfigHeadQueryWrapper = new QueryWrapper<>();
        bussSmsConfigHeadQueryWrapper.eq("tenant_id", tenantId);
        List<BussSmsConfigHead> bussSmsConfigHeadList = bussSmsConfigHeadService.list(bussSmsConfigHeadQueryWrapper);
        bussSmsConfigHeadList.forEach(bussSmsConfigHead -> {
            BussSmsConfigHead newBussSmsConfigHead = new BussSmsConfigHead();
            BeanUtils.copyProperties(bussSmsConfigHead, newBussSmsConfigHead, ignoreProperty);
            newBussSmsConfigHead.setTenantId(newTenantId);
            bussSmsConfigHeadMapper.insert(newBussSmsConfigHead);

            List<BussSmsConfigDetailVO> bussSmsConfigDetailVOList = bussSmsConfigDetailService.selectByMainId(bussSmsConfigHead.getId());
            //业务模板关联消息模板的初始化
            for (BussSmsConfigDetailVO bussSmsConfigDetailVO : bussSmsConfigDetailVOList) {
                //模板id更新为新租户的模板id
                newSysMessageTemplateList.forEach(sysMessageTemplate -> {
                    if (bussSmsConfigDetailVO.getTemplateCode().equals(sysMessageTemplate.getTemplateCode())) {
                        bussSmsConfigDetailVO.setTemplateId(sysMessageTemplate.getId());
                    }
                });
                BussSmsConfigDetailVO newBussSmsConfigDetailVO = new BussSmsConfigDetailVO();
                BeanUtils.copyProperties(bussSmsConfigDetailVO, newBussSmsConfigDetailVO, ignoreProperty);
                newBussSmsConfigDetailVO.setTenantId(newTenantId);
                newBussSmsConfigDetailVO.setHeadId(newBussSmsConfigHead.getId());
                this.bussSmsConfigDetailMapper.insert(newBussSmsConfigDetailVO);
            }
        });
        //初始化默认报表文件
        reportConfigService.initTenantData(newTenantId);
        // 初始化excel导入配置
        excelHeaderService.initTenantData(newTenantId);
        //初始化自动排程规则
        QueryWrapper<ScheduleAutoRuleConfigure> configureQueryWrapper = new QueryWrapper<>();
        configureQueryWrapper.eq("tenant_id", tenantId);
        List<ScheduleAutoRuleConfigure> lstRule = scheduleAutoRuleConfigureService.list(configureQueryWrapper);
        lstRule.forEach(rule -> {
            ScheduleAutoRuleConfigure newRule = new ScheduleAutoRuleConfigure();
            BeanUtils.copyProperties(rule, newRule, ignoreProperty);
            newRule.setTenantId(newTenantId);
            this.scheduleAutoRuleConfigureService.save(newRule);
        });
    }

    private void initCodeGenerator(String tenantId) {
        List<CodeGenerator> template = this.codeGeneratorMapper.selectTenantTemplate();
        List<CodeGeneratorDetail> detailTemplate = this.codeGeneratorDetailMapper.selectTenantTemplate();
        Map<String, String> idMap = new HashMap<>(16);

        template.forEach(codeGenerator -> {
            String key = codeGenerator.getId();
            codeGenerator.setId(null);
            codeGenerator.setTenantId(tenantId);
            codeGenerator.setDefaultGenerator(1);
            this.codeGeneratorMapper.insert(codeGenerator);
            idMap.put(key, codeGenerator.getId());
        });

        detailTemplate.forEach(codeGeneratorDetail -> {
            codeGeneratorDetail.setId(null);
            codeGeneratorDetail.setGeneratorId(idMap.get(codeGeneratorDetail.getGeneratorId()));
            codeGeneratorDetail.setTenantId(tenantId);
        });
        this.codeGeneratorDetailMapper.insertBatchSomeColumn(detailTemplate);
    }

}
