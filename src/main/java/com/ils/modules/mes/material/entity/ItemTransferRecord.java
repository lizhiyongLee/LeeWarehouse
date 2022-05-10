package com.ils.modules.mes.material.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.ils.common.aspect.annotation.Dict;
import com.ils.common.aspect.annotation.KeyWord;
import com.ils.common.system.base.entity.ILSEntity;
import com.ils.framework.poi.excel.annotation.Excel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.util.Date;


/**
 * @Description: 转移记录
 * @Author: wyssss
 * @Date:   2020-11-18
 * @Version: V1.0
 */
@Data
@TableName("mes_item_transfer_record")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="mes_item_transfer_record对象", description="转移记录")
public class ItemTransferRecord extends ILSEntity {
	private static final long serialVersionUID = 1L;
    
	/**物料单元id*/
	@Excel(name = "物料单元id", width = 15)
    @ApiModelProperty(value = "物料单元id", position = 2)
    @TableField("item_cell_id")
	private String itemCellId;
	/**转移状态：1、在途；2、已入库；*/
	@Excel(name = "转移状态：1、在途；2、已入库；", width = 15, dicCode = "mesTransferStatus")
    @ApiModelProperty(value = "转移状态：1、在途；2、已入库；", position = 3)
    @TableField("transfer_status")
	@Dict(dicCode = "mesTransferStatus")
	private String transferStatus;
	/**父标签码*/
	@Excel(name = "父标签码", width = 15)
    @ApiModelProperty(value = "父标签码", position = 4)
    @TableField("father_qrcode")
	@KeyWord
	private String fatherQrcode;
	/**标签码*/
	@Excel(name = "标签码", width = 15)
    @ApiModelProperty(value = "标签码", position = 5)
    @TableField("qrcode")
	@KeyWord
	private String qrcode;
	/**批号*/
	@Excel(name = "批号", width = 15)
    @ApiModelProperty(value = "批号", position = 6)
    @TableField("batch")
	@KeyWord
	private String batch;
	/**事务id*/
	@Excel(name = "事务id", width = 15)
    @ApiModelProperty(value = "事务id", position = 7)
    @TableField("event_id")
	private String eventId;
	/**事务名称*/
	@Excel(name = "事务名称", width = 15)
    @ApiModelProperty(value = "事务名称", position = 8)
    @TableField("event_name")
	@KeyWord
	private String eventName;
	/**事务对象*/
	@Excel(name = "事务对象", width = 15)
    @ApiModelProperty(value = "事务对象", position = 9)
    @TableField("event_object")
	@Dict(dicCode = "mesEventObject")
	private String eventObject;
	/**单据编码*/
	@Excel(name = "单据编码", width = 15)
    @ApiModelProperty(value = "单据编码", position = 10)
    @TableField("bill_code")
	@KeyWord
	private String billCode;
	/**物料id*/
	@Excel(name = "物料id", width = 15)
    @ApiModelProperty(value = "物料id", position = 11)
    @TableField("item_id")
	private String itemId;
	/**物料编码*/
	@Excel(name = "物料编码", width = 15)
    @ApiModelProperty(value = "物料编码", position = 12)
    @TableField("item_code")
	@KeyWord
	private String itemCode;
	/**物料名称*/
	@Excel(name = "物料名称", width = 15)
    @ApiModelProperty(value = "物料名称", position = 13)
    @TableField("item_name")
	@KeyWord
	private String itemName;
	/**规格*/
	@Excel(name = "规格", width = 15)
	@ApiModelProperty(value = "规格", position = 13)
	@TableField("spec")
	private String spec;
	/**数量*/
	@Excel(name = "数量", width = 15)
    @ApiModelProperty(value = "数量", position = 14)
    @TableField("qty")
	private BigDecimal qty;
	/**单位*/
	@Excel(name = "单位", width = 15)
    @ApiModelProperty(value = "单位", position = 15)
    @TableField("unit_id")
	private String unitId;
	/**单位名称*/
	@Excel(name = "单位名称", width = 15)
    @ApiModelProperty(value = "单位名称", position = 16)
    @TableField("unit_name")
	private String unitName;
	/**出库位置编码*/
	@Excel(name = "出库位置编码", width = 15)
    @ApiModelProperty(value = "出库位置编码", position = 17)
    @TableField("out_storage_code")
	@KeyWord
	private String outStorageCode;
	/**出库位置名称*/
	@Excel(name = "出库位置名称", width = 15)
    @ApiModelProperty(value = "出库位置名称", position = 18)
    @TableField("out_storage_name")
	@KeyWord
	private String outStorageName;
	/**发出人*/
	@Excel(name = "发出人", width = 15)
    @ApiModelProperty(value = "发出人", position = 19)
    @TableField("out_storage_employee")
	private String outStorageEmployee;
	/**出库时间*/
	@Excel(name = "出库时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "出库时间", position = 20)
    @TableField("out_time")
	private Date outTime;
	/**出库备注*/
	@Excel(name = "出库备注", width = 15)
    @ApiModelProperty(value = "出库备注", position = 21)
    @TableField("out_note")
	private String outNote;
	/**期望入库位置编码*/
	@Excel(name = "期望入库位置编码", width = 15)
    @ApiModelProperty(value = "期望入库位置编码", position = 22)
    @TableField("hope_in_house_code")
	private String hopeInHouseCode;
	/**期望入库位置名称*/
	@Excel(name = "期望入库位置名称", width = 15)
    @ApiModelProperty(value = "期望入库位置名称", position = 23)
    @TableField("hope_in_house_name")
	private String hopeInHouseName;
	/**接收仓位编码*/
	@Excel(name = "接收仓位编码", width = 15)
    @ApiModelProperty(value = "接收仓位编码", position = 24)
    @TableField("in_storage_code")
	private String inStorageCode;
	/**接收仓位名称*/
	@Excel(name = "接收仓位名称", width = 15)
    @ApiModelProperty(value = "接收仓位名称", position = 25)
    @TableField("in_storage_name")
	private String inStorageName;
	/**接收人*/
	@Excel(name = "接收人", width = 15)
    @ApiModelProperty(value = "接收人", position = 26)
    @TableField("in_storage_employee")
	private String inStorageEmployee;
	/**接收时间*/
	@Excel(name = "接收时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "接收时间", position = 27)
    @TableField("in_time")
	private Date inTime;
	/**接收备注*/
	@Excel(name = "接收备注", width = 15)
    @ApiModelProperty(value = "接收备注", position = 28)
    @TableField("in_note")
	private String inNote;
	/**生产日期*/
	@Excel(name = "生产日期", width = 15)
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "生产日期", position = 29)
    @TableField("produce_date")
	private Date produceDate;
	/**有效期*/
	@Excel(name = "有效期", width = 15)
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "有效期", position = 30)
    @TableField("valid_date")
	private Date validDate;
	/**质量状态*/
	@Excel(name = "质量状态", width = 15, dicCode = "mesQcStatus")
    @ApiModelProperty(value = "质量状态", position = 31)
    @TableField("quality_status")
	@Dict(dicCode = "mesQcStatus")
	private String qualityStatus;
	/** 管理方式 */
	@Excel(name = "管理方式",width = 15, dicCode = "mesManageWay")
	@ApiModelProperty(value = "管理方式", position = 32)
	@TableField("manage_way")
	@Dict(dicCode = "mesManageWay")
	private String manageWay;

	/** 关联标准作业步骤id */
	@Excel(name = "关联标准作业步骤", width = 15)
	@ApiModelProperty(value = "关联标准作业步骤id", position = 18)
	@TableField("sop_step_id")
	private String sopStepId;

	/** 关联标准作业控件id */
	@Excel(name = "关联标准作业控件", width = 15)
	@ApiModelProperty(value = "关联标准作业控件id", position = 18)
	@TableField("sop_control_id")
	private String sopControlId;

	/** 关联生产任务id */
	@Excel(name = "关联生产任务id", width = 15)
	@ApiModelProperty(value = "关联生产任务id", position = 18)
	@TableField("task_id")
	private String taskId;
}
