package com.ils.modules.mes.produce.vo;

import com.ils.modules.mes.produce.entity.WorkPlanTask;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;
import java.util.Objects;

/**
 * @Description:派工任务VO
 * @author: fengyi
 * @date: 2020年11月23日 下午4:51:28
 */
@Setter
@Getter
@ToString(callSuper = true)
public class WorkPlanTaskVO extends WorkPlanTask {

    private static final long serialVersionUID = 1L;

    private String employeeIds;

    private String employeeName;

    private String saleOrderNo;

    private String taskStatus;

    private String workShopId;
}
