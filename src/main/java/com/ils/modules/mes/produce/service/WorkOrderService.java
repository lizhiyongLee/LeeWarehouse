package com.ils.modules.mes.produce.service;

import java.util.List;
import java.util.Set;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ils.modules.mes.base.product.vo.ResultDataVO;
import com.ils.modules.mes.execution.entity.WorkProduceRecord;
import com.ils.modules.mes.produce.entity.WorkOrder;
import com.ils.modules.mes.produce.entity.WorkOrderLinePara;
import com.ils.modules.mes.produce.vo.*;

/**
 * @Description: 工单
 * @Author: fengyi
 * @Date: 2020-11-12
 * @Version: V1.0
 */
public interface WorkOrderService extends IService<WorkOrder> {

    /**
     * 工单新增
     *
     * @param workOrderVO
     * @return WorkOrder
     * @date 2021年1月25日
     */
    public WorkOrder saveMain(WorkOrderVO workOrderVO);

    /**
     * 工单更新
     *
     * @param workOrderVO
     * @date 2021年1月25日
     */
    public void updateMain(WorkOrderVO workOrderVO);

    /**
     * 取消工单
     *
     * @param id 工单ID
     * @date 2020年11月17日
     */
    public void cancelWorkOrder(String id);

    /**
     * 查询工单详细信息
     *
     * @param id
     * @return
     * @date 2021年1月25日
     */
    public WorkOrderVO queryWorkOrderDetail(String id);

    /**
     * 根据销售订单行批量新增工单
     *
     * @param lstBatchWorkOrderVO
     * @date 2021年1月26日
     */
    public void batchSaleAdd(List<BatchWorkOrderVO> lstBatchWorkOrderVO);

    /**
     * 子工单获取
     *
     * @param workOrderVO 父工单
     * @return List<WorkOrderVO> 子工单列表
     * @date 2021年1月26日
     */
    public void generateSubWorkOrderTree(WorkOrderVO workOrderVO);

    /**
     * 子工单录入
     *
     * @param workOrderVO  工单
     * @param fatherId     父工单id（无父工单时为null）
     * @param workOrderSet 工单id
     * @date 2021年1月26日
     */
    public void addBySubWorkOrder(WorkOrderVO workOrderVO, String fatherId, Set<WorkOrder> workOrderSet);

    /**
     * 工单列表录入
     *
     * @param workOrderVOList 工单列表
     * @date 2021年1月26日
     */
    void addByWorkOrderList(List<WorkOrderVO> workOrderVOList);

    /**
     * 查询工单及其子工单列表
     *
     * @param id 工单列表
     * @return
     */
    WorkOrderDetailVO queryWorkOrderListDetail(String id);

    /**
     * 查询子工单
     *
     * @param id
     * @param workOrderProgressVOList
     * @param workOrderProcessProgressVOList
     * @return
     */
    WorkOrderVO querySubWorkOrder(String id, List<WorkOrderProgressVO> workOrderProgressVOList, List<WorkOrderProcessProgressVO> workOrderProcessProgressVOList);

    /**
     * 完工-关闭工单
     *
     * @param idList
     */
    void finish(List<String> idList);

    /**
     * 查询在制工单进度
     *
     * @return
     */
    List<WorkOrderDetailVO> getProgressProductsProcess();


    /**
     * 获取工单对应工艺路线或者产品bom对应的参数模板
     *
     * @param processId
     * @param productId
     * @param routeId
     * @param workOrderLineId
     * @param seq
     * @return
     */
    List<WorkOrderLinePara> listParaByWorkflowType(String processId, String productId, String routeId, String workOrderLineId, Integer seq);

    /**
     * 获取工单对应工序或者产品bom对应的质检任务(新增)
     *
     * @param processId
     * @param productId
     * @param routeId
     * @return
     */
    ResultDataVO listQcMethodByWorkflowType(String processId, String productId, String routeId);

    /**
     * 获取工单对应工序或者产品bom对应的质检任务(详情或者编辑)
     *
     * @param orderId
     * @param processId
     * @return
     */
    ResultDataVO listQcMethodByOrderId(String processId, String orderId);


    /**
     * 物料需求报表
     *
     * @param page
     * @param requiredMaterialVO
     * @return
     */
    Page<RequiredMaterialVO> getRequiredMaterial(Page<RequiredMaterialVO> page, RequiredMaterialVO requiredMaterialVO);

    /**
     * 发送消息
     *
     * @param workOrder
     */
    void sendMsg(WorkOrder workOrder);
}
