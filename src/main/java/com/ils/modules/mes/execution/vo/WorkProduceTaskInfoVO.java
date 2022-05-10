package com.ils.modules.mes.execution.vo;

import java.math.BigDecimal;
import java.util.List;

import com.baomidou.mybatisplus.annotation.TableField;
import com.ils.common.util.BigDecimalUtils;
import com.ils.framework.poi.excel.annotation.Excel;
import com.ils.modules.mes.execution.entity.WorkProduceTask;

import com.ils.modules.mes.execution.entity.WorkProduceTaskPara;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @Description: 执行任务详细信息
 * @author: fengyi
 * @date: 2020年12月9日 下午1:37:13
 */
@Setter
@Getter
@ToString(callSuper = true)
public class WorkProduceTaskInfoVO extends WorkProduceTask {

    /** 产品ID */
    @Excel(name = "产品ID", width = 15)
    @ApiModelProperty(value = "产品ID", position = 9)
    private String orderItemId;
    /** 产品编码 */
    @Excel(name = "产品编码", width = 15)
    @ApiModelProperty(value = "产品编码", position = 10)
    private String orderItemCode;
    /** 产品名称 */
    @Excel(name = "产品名称", width = 15)
    @ApiModelProperty(value = "产品名称", position = 11)
    private String orderItemName;
    /** 产品规格 */
    @Excel(name = "产品规格", width = 15)
    @ApiModelProperty(value = "产品规格", position = 12)
    private String spec;
    /** 以jason形式存储生产主管对应的 id 及姓名； */
    @Excel(name = "以jason形式存储生产主管对应的 id 及姓名；", width = 15)
    @TableField("director")
    @ApiModelProperty(value = "以jason形式存储生产主管对应的 id 及姓名；", position = 18)
    private String director;
    /** 工序计划数量 */
    @Excel(name = "工序计划数量", width = 15)
    @ApiModelProperty(value = "工序计划数量", position = 16)
    private BigDecimal processPlanQty;

    /** 工序报工数量 */
    @Excel(name = "工序报工数量", width = 15)
    @ApiModelProperty(value = "工序报工数量", position = 16)
    private BigDecimal processCompletedQty;

    /** 员工id */
    @Excel(name = "员工id", width = 15)
    @ApiModelProperty(value = "员工id", position = 3)
    private String employeeId;
    /** 员工编码 */
    @Excel(name = "员工编码", width = 15)
    @ApiModelProperty(value = "员工编码", position = 4)
    private String employeeCode;
    /** 员工名称 */
    @Excel(name = "员工名称", width = 15)
    @ApiModelProperty(value = "员工名称", position = 5)
    private String employeeName;
    /** 1、指派领取；2、自己领取，3、交接任务；； */
    @Excel(name = " 1、指派领取；2、自己领取，3、交接任务；", width = 15)
    @ApiModelProperty(value = "1、指派领取；2、自己领取，3、交接任务；", position = 6)
    private String usingType;
    /** 备注 */
    @Excel(name = "备注", width = 15)
    @ApiModelProperty(value = "备注", position = 31)
    private String note;

    /** 启动批次管理：1，是；0，否； */
    private String batchFlag;
    /** 启动二维码1，是；0，否； */
    private String qrcodeFlag;

    /** 前一道工序 */
    private String priorCode;

    /** 后一道工序 */
    private String nextCode;

    /** 工艺路线 */
    private String workFlowType;
    /** 质检状态 */
    private String qcStatus;
    /** 参数模板 */
    private List<WorkProduceTaskPara> workProduceTaskParaList;

    public String getTaskProcessQtyInfo() {
        return BigDecimalUtils.format(this.getGoodQty(), "###0.###") + "/"
            + BigDecimalUtils.format(this.getPlanQty(), "###0.###");
    }

}
