package com.ils.modules.mes.execution.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.ils.common.aspect.annotation.Dict;
import com.ils.common.system.base.entity.ILSEntity;
import com.ils.framework.poi.excel.annotation.Excel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.math.BigDecimal;


/**
 * @Description: 投料记录
 * @Author: fengyi
 * @Date: 2020-12-10
 * @Version: V1.0
 */
@Data
@TableName("mes_work_produce_material_record")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="mes_work_produce_material_record对象", description="投料记录")
public class WorkProduceMaterialRecord extends ILSEntity {
	private static final long serialVersionUID = 1L;
    
	/**执行生产任务id*/
	@Excel(name = "执行生产任务id", width = 15)
    @ApiModelProperty(value = "执行生产任务id", position = 2)
    @TableField("produce_task_id")
	private String produceTaskId;
	/**1、投料；2、撤料；*/
	@Excel(name = "1、投料；2、撤料；", width = 15)
    @ApiModelProperty(value = "1、投料；2、撤料；", position = 3)
    @TableField("feed_type")
	private String feedType;
	/**物料id*/
	@Excel(name = "物料id", width = 15)
    @ApiModelProperty(value = "物料id", position = 4)
    @TableField("item_id")
	private String itemId;
	/**物料编码*/
	@Excel(name = "物料编码", width = 15)
    @ApiModelProperty(value = "物料编码", position = 5)
    @TableField("item_code")
	private String itemCode;
	/**物料名称*/
	@Excel(name = "物料名称", width = 15)
    @ApiModelProperty(value = "物料名称", position = 6)
    @TableField("item_name")
	private String itemName;
	/**批号*/
	@Excel(name = "批号", width = 15)
    @ApiModelProperty(value = "批号", position = 7)
    @TableField("batch_no")
	private String batchNo;
	/**二维码*/
	@Excel(name = "二维码", width = 15)
    @ApiModelProperty(value = "二维码", position = 8)
    @TableField("qrcode")
	private String qrcode;
	/**撤回二维码*/
	@Excel(name = "撤回二维码", width = 15)
    @ApiModelProperty(value = "撤回二维码", position = 9)
    @TableField("back_qrcode")
	private String backQrcode;
	/**物料单元id*/
	@Excel(name = "物料单元id", width = 15)
    @ApiModelProperty(value = "物料单元id", position = 10)
    @TableField("item_cell_state_id")
	private String itemCellStateId;
    /** 投料质量状态 */
    @Excel(name = "投料质量状态", width = 15, dicCode = "mesQcStatus")
    @ApiModelProperty(value = "投料质量状态", position = 10)
    @TableField("qc_status")
	@Dict(dicCode = "mesQcStatus")
    private String qcStatus;
	/**投料数量*/
	@Excel(name = "投料数量", width = 15)
    @ApiModelProperty(value = "投料数量", position = 11)
    @TableField("feed_qty")
	private BigDecimal feedQty;
	/**投后余量*/
	@Excel(name = "投后余量", width = 15)
    @ApiModelProperty(value = "投后余量", position = 12)
    @TableField("remain_qty")
	private BigDecimal remainQty;
	/**扣料数量*/
	@Excel(name = "扣料数量", width = 15)
    @ApiModelProperty(value = "扣料数量", position = 13)
    @TableField("used_qty")
	private BigDecimal usedQty;
	/**投料位置*/
	@Excel(name = "投料位置", width = 15)
    @ApiModelProperty(value = "投料位置", position = 14)
    @TableField("feed_storage_id")
	private String feedStorageId;

    /** 单位 */
    @Excel(name = "单位", width = 15, dicCode = "id",dictTable = "mes_unit",dicText = "unit_name")
    @ApiModelProperty(value = "单位", position = 18)
    @TableField("unit")
	@Dict(dicCode = "id",dictTable = "mes_unit",dicText = "unit_name")
    private String unit;

	/** 关联标准作业步骤id */
	@Excel(name = "关联标准作业步骤", width = 15)
	@ApiModelProperty(value = "关联标准作业步骤id", position = 19)
	@TableField("sop_step_id")
	private String sopStepId;

	/** 关联标准作业控件id */
	@Excel(name = "关联标准作业控件", width = 15)
	@ApiModelProperty(value = "关联标准作业控件id", position = 20)
	@TableField("sop_control_id")
	private String sopControlId;

	/**备注*/
	@Excel(name = "备注", width = 15)
    @ApiModelProperty(value = "备注", position = 21)
    @TableField("note")
	private String note;

	/**单位名称*/
	@Excel(name = "单位名称", width = 15)
	@ApiModelProperty(value = "单位名称", position = 22)
	@TableField("unit_name")
	private String unitName;
}
