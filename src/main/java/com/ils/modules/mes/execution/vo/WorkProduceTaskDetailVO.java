package com.ils.modules.mes.execution.vo;

import java.util.Date;
import java.util.List;

import com.ils.modules.mes.execution.entity.WorkProduceTaskPara;
import com.ils.modules.mes.material.entity.ItemCell;
import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ils.common.aspect.annotation.Dict;
import com.ils.modules.mes.execution.entity.WorkProduceTask;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @Description: web 端执行任务编辑详情VO
 * @author: fengyi
 * @date: 2021年1月14日 上午10:47:36
 */
@Setter
@Getter
@ToString(callSuper = true)
public class WorkProduceTaskDetailVO extends WorkProduceTask {

    /** 执行人员id */
    private String employeeIds;

    /** 执行人员名称 */
    private String employeeNames;
    /** 工艺类型 */
    @Dict(dicCode = "mesWorkflowType")
    private String workflowType;
    /** 引用的物料清单id */
    @Dict(dictTable = "mes_item_bom", dicCode = "id", dicText = "version")
    private String bomId;
    /** 工艺路线id */
    @Dict(dictTable = "mes_route", dicCode = "id", dicText = "route_name")
    private String routeId;
    /** 产品bomid */
    @Dict(dictTable = "mes_product", dicCode = "id", dicText = "version")
    private String productId;
    /** 计划开始时间 */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date workOrderPlanStartTime;
    /** 计划结束时间 */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date workOrderPlanEndTime;

    /** 1、计划2、已排程3、已下发4、开工、5、完工6、取消 */
    @Dict(dicCode = "mesWorkOrderStatus")
    private String status;
    /** 投料清单 */
    private List<GroupMaterialRecordVO> lstGroupMaterialRecordVO;

    /** 产出清单 */
    private List<WorkProduceTaskInfoVO> lstWorkProduceTaskInfoVO;

    /** 联产出清单 */
    private List<ItemCell> itemCellList;

    /** 参数模板 */
    private List<WorkProduceTaskPara> workProduceTaskParaList;

    public String getProcessFull() {
        return this.getSeq() + "/" + this.getProcessCode() + "/" + this.getProcessName();
    }

    public String getItemFull() {
        return this.getItemCode() + "/" + this.getItemName();
    }

    public String getStationFull() {
        return this.getStationCode() + "/" + this.getStationName();
    }

}
