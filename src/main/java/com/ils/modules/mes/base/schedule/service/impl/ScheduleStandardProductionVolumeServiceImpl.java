package com.ils.modules.mes.base.schedule.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ils.modules.mes.base.craft.entity.ProcessStation;
import com.ils.modules.mes.base.craft.entity.RouteLine;
import com.ils.modules.mes.base.craft.entity.RouteLineStation;
import com.ils.modules.mes.base.craft.service.ProcessService;
import com.ils.modules.mes.base.craft.service.ProcessStationService;
import com.ils.modules.mes.base.craft.service.RouteLineService;
import com.ils.modules.mes.base.craft.service.RouteLineStationService;
import com.ils.modules.mes.base.factory.entity.Workstation;
import com.ils.modules.mes.base.factory.service.WorkShopService;
import com.ils.modules.mes.base.factory.service.WorkstationService;
import com.ils.modules.mes.base.product.entity.ProductLine;
import com.ils.modules.mes.base.product.entity.ProductRouteStation;
import com.ils.modules.mes.base.product.service.ProductLineService;
import com.ils.modules.mes.base.product.service.ProductRouteStationService;
import com.ils.modules.mes.base.schedule.entity.ScheduleStandardProductionVolume;
import com.ils.modules.mes.base.schedule.mapper.ScheduleStandardProductionVolumeMapper;
import com.ils.modules.mes.base.schedule.service.ScheduleStandardProductionVolumeService;
import com.ils.modules.mes.constants.MesCommonConstant;
import com.ils.modules.mes.enums.StandardTypeEnum;
import com.ils.modules.mes.util.CommonUtil;
import com.ils.modules.mes.util.TreeNode;
import com.ils.modules.system.service.CodeGeneratorService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Description: 标准产能
 * @Author: fengyi
 * @Date: 2021-02-01
 * @Version: V1.0
 */
@Service
public class ScheduleStandardProductionVolumeServiceImpl extends ServiceImpl<ScheduleStandardProductionVolumeMapper, ScheduleStandardProductionVolume> implements ScheduleStandardProductionVolumeService {

    @Autowired
    private RouteLineStationService routeLineStationService;

    @Autowired
    private ProductRouteStationService productRouteStationService;

    @Autowired
    private ProcessStationService processStationService;

    @Autowired
    private WorkShopService workShopService;

    @Autowired
    private ProcessService processService;

    @Autowired
    private RouteLineService routeLineService;

    @Autowired
    private ProductLineService productLineService;

    @Autowired
    private WorkstationService workstationService;

    @Autowired
    private CodeGeneratorService codeGeneratorService;

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public List<ScheduleStandardProductionVolume>
        saveScheduleStandardProductionVolume(ScheduleStandardProductionVolume scheduleStandardProductionVolume) {

        String standardType = scheduleStandardProductionVolume.getStandardType();
        String processExtendId = scheduleStandardProductionVolume.getProcessExtendId();
        if (StandardTypeEnum.PRODUCT_BOM.getValue().equals(standardType)) {
            ProductLine productLine = productLineService.getById(processExtendId);
            scheduleStandardProductionVolume.setProcessId(productLine.getProcessId());
            scheduleStandardProductionVolume.setProcessCode(productLine.getProcessCode());
            scheduleStandardProductionVolume.setProcessName(productLine.getProcessName());
            scheduleStandardProductionVolume.setProcessSeq(productLine.getSeq());
        } else if (StandardTypeEnum.ROUTE.getValue().equals(standardType)) {
            RouteLine routeLine = routeLineService.getById(processExtendId);
            scheduleStandardProductionVolume.setProcessId(routeLine.getProcessId());
            scheduleStandardProductionVolume.setProcessCode(routeLine.getProcessCode());
            scheduleStandardProductionVolume.setProcessName(routeLine.getProcessName());
            scheduleStandardProductionVolume.setProcessSeq(routeLine.getSeq());
        } else {
            com.ils.modules.mes.base.craft.entity.Process process = processService.getById(processExtendId);
            scheduleStandardProductionVolume.setProcessId(process.getId());
            scheduleStandardProductionVolume.setProcessCode(process.getProcessCode());
            scheduleStandardProductionVolume.setProcessName(process.getProcessName());
        }

        String[] stationIds = scheduleStandardProductionVolume.getStationId().split(",");
        QueryWrapper<Workstation> workstationWrapper = new QueryWrapper<Workstation>();
        workstationWrapper.in("id", Arrays.asList(stationIds));
        List<Workstation> lstWorkstation = workstationService.list(workstationWrapper);

        List<ScheduleStandardProductionVolume> lstStd =
            new ArrayList<ScheduleStandardProductionVolume>(lstWorkstation.size());

        List<ScheduleStandardProductionVolume> lstFailStd =
            new ArrayList<ScheduleStandardProductionVolume>(lstWorkstation.size());
        for (Workstation workstation : lstWorkstation) {
            ScheduleStandardProductionVolume entity = new ScheduleStandardProductionVolume();
            BeanUtils.copyProperties(scheduleStandardProductionVolume, entity);
            entity.setStationId(workstation.getId());
            entity.setStationCode(workstation.getStationCode());
            entity.setStationName(workstation.getStationName());
            boolean bResult = this.validateCondition(entity);
            if (!bResult) {
                lstFailStd.add(entity);
                continue;
            }
            CommonUtil.setSysParam(entity);
            // 生成单号
            String no =
                codeGeneratorService.getNextCode(CommonUtil.getTenantId(), MesCommonConstant.STANDARD_CODE, entity);
            entity.setStandardCode(no);

            lstStd.add(entity);
        }
        if (!CommonUtil.isEmptyOrNull(lstStd)) {
            baseMapper.insertBatchSomeColumn(lstStd);
        }
        return lstFailStd;
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public boolean
        updateScheduleStandardProductionVolume(ScheduleStandardProductionVolume scheduleStandardProductionVolume) {
        String standardType = scheduleStandardProductionVolume.getStandardType();
        String processExtendId = scheduleStandardProductionVolume.getProcessExtendId();
        if (StandardTypeEnum.PRODUCT_BOM.getValue().equals(standardType)) {
            ProductLine productLine = productLineService.getById(processExtendId);
            scheduleStandardProductionVolume.setProcessId(productLine.getProcessId());
            scheduleStandardProductionVolume.setProcessCode(productLine.getProcessCode());
            scheduleStandardProductionVolume.setProcessName(productLine.getProcessName());
            scheduleStandardProductionVolume.setProcessSeq(productLine.getSeq());
        } else if (StandardTypeEnum.ROUTE.getValue().equals(standardType)) {
            RouteLine routeLine = routeLineService.getById(processExtendId);
            scheduleStandardProductionVolume.setProcessId(routeLine.getProcessId());
            scheduleStandardProductionVolume.setProcessCode(routeLine.getProcessCode());
            scheduleStandardProductionVolume.setProcessName(routeLine.getProcessName());
            scheduleStandardProductionVolume.setProcessSeq(routeLine.getSeq());
        } else {
            com.ils.modules.mes.base.craft.entity.Process process = processService.getById(processExtendId);
            scheduleStandardProductionVolume.setProcessId(process.getId());
            scheduleStandardProductionVolume.setProcessCode(process.getProcessCode());
            scheduleStandardProductionVolume.setProcessName(process.getProcessName());
        }
        Workstation workstation = workstationService.getById(scheduleStandardProductionVolume.getStationId());
        scheduleStandardProductionVolume.setStationId(workstation.getId());
        scheduleStandardProductionVolume.setStationCode(workstation.getStationCode());
        scheduleStandardProductionVolume.setStationName(workstation.getStationName());
        boolean bResult = this.validateCondition(scheduleStandardProductionVolume);
        if (bResult) {
            baseMapper.updateById(scheduleStandardProductionVolume);
        }
        return bResult;
    }

    /**
     * 
     * 校验标准产能唯一
     * 
     * @param scheduleStandardProductionVolume
     * @date 2021年2月20日
     */
    private boolean validateCondition(ScheduleStandardProductionVolume scheduleStandardProductionVolume) {
        QueryWrapper<ScheduleStandardProductionVolume> queryWrapper = new QueryWrapper();
        String standardType = scheduleStandardProductionVolume.getStandardType();
        if (StandardTypeEnum.PRODUCT_BOM.getValue().equals(standardType)) {
            queryWrapper.eq("product_bom_id", scheduleStandardProductionVolume.getProductBomId());
        } else if (StandardTypeEnum.ROUTE.getValue().equals(standardType)) {
            queryWrapper.eq("route_id", scheduleStandardProductionVolume.getRouteId());
        }
        queryWrapper.eq("standard_type", standardType);
        queryWrapper.eq("process_id", scheduleStandardProductionVolume.getProcessId());
        queryWrapper.eq("station_id", scheduleStandardProductionVolume.getStationId());
        queryWrapper.eq("item_id", scheduleStandardProductionVolume.getItemId());

        if (StringUtils.isNoneBlank(scheduleStandardProductionVolume.getId())) {
            queryWrapper.ne("id", scheduleStandardProductionVolume.getId());
        }
        int count = baseMapper.selectCount(queryWrapper);
        if (count > 0) {
            return false;
        }
        return true;
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void delScheduleStandardProductionVolume(String id) {
        baseMapper.deleteById(id);
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void delBatchScheduleStandardProductionVolume(List<String> idList) {
        baseMapper.deleteBatchIds(idList);
    }

    @Override
    public List<TreeNode> queryStationById(String id, String standardType) {

        // 工位ID集合
        List<String> workstatioIds = null;

        // 产品BOM下的工位
        if (StandardTypeEnum.PRODUCT_BOM.getValue().equals(standardType)) {
            QueryWrapper<ProductRouteStation> productRouteStationQuery = new QueryWrapper();
            productRouteStationQuery.eq("product_line_id", id);
            List<ProductRouteStation> lstProductRouteStation =
                productRouteStationService.list(productRouteStationQuery);
            workstatioIds =
                lstProductRouteStation.stream().map(item -> item.getStationId()).collect(Collectors.toList());
            // 工艺路线下的工位
        } else if (StandardTypeEnum.ROUTE.getValue().equals(standardType)) {
            QueryWrapper<RouteLineStation> processStationQuery = new QueryWrapper();
            processStationQuery.eq("route_line_id", id);
            List<RouteLineStation> lstProcessStation = routeLineStationService.list(processStationQuery);
            workstatioIds = lstProcessStation.stream().map(item -> item.getStationId()).collect(Collectors.toList());
        } else {
            QueryWrapper<ProcessStation> processStationQuery = new QueryWrapper();
            processStationQuery.eq("process_id", id);
            List<ProcessStation> lstProcessStation = processStationService.list(processStationQuery);
            workstatioIds = lstProcessStation.stream().map(item -> item.getStationId()).collect(Collectors.toList());
        }
        List<TreeNode> lsTreeNode = null;
        if (!CommonUtil.isEmptyOrNull(workstatioIds)) {
            lsTreeNode = workShopService.queryAssignStationTreeList(workstatioIds);
        } else {
            lsTreeNode = new ArrayList<TreeNode>(0);
        }
        return lsTreeNode;
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void updateStatus(String id, String status) {
        QueryWrapper<ScheduleStandardProductionVolume> updateQueryWrapper = new QueryWrapper();
        updateQueryWrapper.eq("id", id);
        ScheduleStandardProductionVolume updateEntity = new ScheduleStandardProductionVolume();
        updateEntity.setStatus(status);
        baseMapper.update(updateEntity, updateQueryWrapper);

    }
}
