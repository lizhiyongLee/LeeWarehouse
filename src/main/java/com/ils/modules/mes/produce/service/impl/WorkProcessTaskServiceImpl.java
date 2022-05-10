package com.ils.modules.mes.produce.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.ils.modules.mes.base.craft.entity.RouteLine;
import com.ils.modules.mes.base.craft.entity.RouteLineStation;
import com.ils.modules.mes.base.product.entity.ProductLine;
import com.ils.modules.mes.base.product.entity.ProductRouteStation;
import com.ils.modules.mes.enums.WorkOrderLineRelatedTypeEnum;
import com.ils.modules.mes.produce.entity.WorkOrderLine;
import com.ils.modules.mes.produce.entity.WorkOrderLineStation;
import com.ils.modules.mes.produce.service.WorkOrderLineService;
import com.ils.modules.mes.produce.service.WorkOrderLineStationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ils.modules.mes.base.craft.service.RouteLineService;
import com.ils.modules.mes.base.craft.service.RouteLineStationService;
import com.ils.modules.mes.base.factory.service.WorkShopService;
import com.ils.modules.mes.base.product.service.ProductLineService;
import com.ils.modules.mes.base.product.service.ProductRouteStationService;
import com.ils.modules.mes.enums.WorkflowTypeEnum;
import com.ils.modules.mes.produce.entity.WorkOrder;
import com.ils.modules.mes.produce.entity.WorkProcessTask;
import com.ils.modules.mes.produce.mapper.WorkOrderLineMapper;
import com.ils.modules.mes.produce.mapper.WorkOrderMapper;
import com.ils.modules.mes.produce.mapper.WorkProcessTaskMapper;
import com.ils.modules.mes.produce.service.WorkProcessTaskService;
import com.ils.modules.mes.produce.vo.AutoScheWorkProcessVO;
import com.ils.modules.mes.produce.vo.WorkProcessTaskVO;
import com.ils.modules.mes.util.CommonUtil;
import com.ils.modules.mes.util.TreeNode;

/**
 * @Description: 工单工序任务
 * @Author: hezhigang
 * @Date: 2020-11-18
 * @Version: V1.0
 */
@Service
public class WorkProcessTaskServiceImpl extends ServiceImpl<WorkProcessTaskMapper, WorkProcessTask> implements WorkProcessTaskService {

    @Autowired
    private WorkOrderMapper workOrderMapper;

    @Autowired
    private WorkOrderLineService workOrderLineService;

    @Autowired
    private ProductLineService productLineService;

    @Autowired
    private RouteLineService routeLineService;

    @Autowired
    private RouteLineStationService routeLineStationService;

    @Autowired
    private ProductRouteStationService productRouteStationService;

    @Autowired
    private WorkShopService workShopService;

    @Autowired
    private WorkOrderLineStationService workOrderLineStationService;


    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void saveWorkProcessTask(WorkProcessTask workProcessTask) {
        baseMapper.insert(workProcessTask);
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void updateWorkProcessTask(WorkProcessTask workProcessTask) {
        baseMapper.updateById(workProcessTask);
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void delWorkProcessTask(String id) {
        baseMapper.deleteById(id);
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void delBatchWorkProcessTask(List<String> idList) {
        baseMapper.deleteBatchIds(idList);
    }

    @Override
    public IPage<WorkProcessTaskVO> listPage(Page<WorkProcessTaskVO> page,
                                             QueryWrapper<WorkProcessTaskVO> queryWrapper) {
        return baseMapper.listPage(page, queryWrapper);
    }

    @Override
    public List<WorkProcessTaskVO> listPageAll(QueryWrapper<WorkProcessTaskVO> queryWrapper) {
        return baseMapper.listPageAll(queryWrapper);
    }

    @Override
    public List<TreeNode> queryStationById(String id) {
        WorkProcessTask workProcessTask = this.getById(id);
        WorkOrder workOrder = workOrderMapper.selectById(workProcessTask.getOrderId());
        String workflowType = workOrder.getWorkflowType();

        QueryWrapper<WorkOrderLine> workOrderLineQueryWrapper = new QueryWrapper<>();
        workOrderLineQueryWrapper.eq("process_id", workProcessTask.getProcessId());
        workOrderLineQueryWrapper.eq("seq", workProcessTask.getSeq());
        workOrderLineQueryWrapper.eq("order_id", workOrder.getId());
        WorkOrderLine one = workOrderLineService.getOne(workOrderLineQueryWrapper);

        List<String> workstationIds = this.getWorkstationIds(one.getId(), workflowType, workProcessTask.getProcessId(), workProcessTask.getSeq());

        List<TreeNode> lsTreeNode = null;
        if (!CommonUtil.isEmptyOrNull(workstationIds)) {
            lsTreeNode = workShopService.queryAssignStationTreeList(workstationIds);
        } else {
            lsTreeNode = new ArrayList<TreeNode>(0);
        }

        return lsTreeNode;
    }

    @Override
    public List<AutoScheWorkProcessVO> querySortWorkProcessTask(QueryWrapper<AutoScheWorkProcessVO> queryWrapper) {
        return baseMapper.querySortWorkProcessTask(queryWrapper);
    }

     public List<String> getWorkstationIdsByAdd(String bussId, String workflowType, String processId, Integer seq) {
        List<String> workstationIds = null;
        // 产品BOM下的工位
        if (WorkflowTypeEnum.PRODUCT_BOM.getValue().equals(workflowType)) {
            QueryWrapper<ProductLine> productLineQuery = new QueryWrapper();
            productLineQuery.eq("product_id", bussId);
            productLineQuery.eq("seq", seq);
            productLineQuery.eq("process_id", processId);
            ProductLine productLine = productLineService.getOne(productLineQuery);

            QueryWrapper<ProductRouteStation> productRouteStationQuery = new QueryWrapper();
            productRouteStationQuery.eq("product_line_id", productLine.getId());
            List<ProductRouteStation> lstProductRouteStation =
                    productRouteStationService.list(productRouteStationQuery);
            workstationIds =
                    lstProductRouteStation.stream().map(item -> item.getStationId()).collect(Collectors.toList());
            // 工艺路线下的工位
        } else {
            QueryWrapper<RouteLine> routeLineQuery = new QueryWrapper();
            routeLineQuery.eq("route_id", bussId);
            routeLineQuery.eq("seq", seq);
            routeLineQuery.eq("process_id", processId);
            RouteLine routeLine = routeLineService.getOne(routeLineQuery);

            QueryWrapper<RouteLineStation> processStationQuery = new QueryWrapper();
            processStationQuery.eq("route_line_id", routeLine.getId());
            List<RouteLineStation> lstProcessStation = routeLineStationService.list(processStationQuery);

            workstationIds = lstProcessStation.stream().map(item -> item.getStationId()).collect(Collectors.toList());
        }
        return workstationIds;
    }

    @Override
    public List<String> getWorkstationIds(String bussId, String workflowType, String processId, Integer seq) {
        QueryWrapper<WorkOrderLineStation> workOrderLineStationQueryWrapper = new QueryWrapper<>();
        workOrderLineStationQueryWrapper.eq("process_id", processId);
        workOrderLineStationQueryWrapper.eq("work_order_line_id", bussId);
        workOrderLineStationQueryWrapper.eq("seq", seq);
        if (WorkflowTypeEnum.PRODUCT_BOM.getValue().equals(workflowType)) {
            workOrderLineStationQueryWrapper.eq("related_type", WorkOrderLineRelatedTypeEnum.PRODUCT_BOM.getValue());
        } else {
            workOrderLineStationQueryWrapper.eq("related_type", WorkOrderLineRelatedTypeEnum.ROUTE.getValue());
        }
        List<WorkOrderLineStation> list = workOrderLineStationService.list(workOrderLineStationQueryWrapper);
        List<String> workstationIds = new ArrayList<>();
        list.forEach(workOrderLineStation -> workstationIds.add(workOrderLineStation.getStationId()));
        return workstationIds;
    }

}
