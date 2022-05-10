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

import java.util.Date;

/**
 * @author lishaojie
 * @description
 * @date 2021/10/27 13:49
 */
@Data
@TableName("mes_item_container_transfer_record")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "mes_item_container_transfer_record对象", description = "载具转移记录")
public class ItemContainerTransferRecord extends ILSEntity {
    /**
     * 载具关联物料主表id
     */
    @Excel(name = "载具关联物料主表id", width = 15)
    @ApiModelProperty(value = "载具关联物料主表id", position = 2)
    @TableField("container_manage_id")
    private String containerManageId;

    /**
     * 1、已出库；2、已入库；
     */
    @Excel(name = "1、已出库；2、已入库；", width = 15)
    @ApiModelProperty(value = "1、已出库；2、已入库；", position = 3)
    @TableField("transfer_status")
    @Dict(dicCode = "mesTransferStatus")
    private String transferStatus;

    /**
     * 父标签码
     */
    @Excel(name = "父标签码", width = 15)
    @ApiModelProperty(value = "父标签码", position = 3)
    @TableField("father_qrcode")
    private String fatherQrcode;

    /**
     * 载具标签码
     */
    @Excel(name = "载具标签码", width = 15)
    @ApiModelProperty(value = "载具标签码", position = 2)
    @TableField("container_qrcode")
    private String containerQrcode;
    /**
     * 载具编码
     */
    @Excel(name = "载具编码", width = 15)
    @ApiModelProperty(value = "载具编码", position = 2)
    @TableField("container_code")
    private String containerCode;
    /**
     * 载具名称
     */
    @Excel(name = "载具名称", width = 15)
    @ApiModelProperty(value = "载具名称", position = 2)
    @TableField("container_name")
    private String containerName;
    /**
     * 载具标签码
     */
    @Excel(name = "载具标签码", width = 15)
    @ApiModelProperty(value = "载具标签码", position = 3)
    @TableField("container_status")
    @Dict(dicCode = "mesContainerStatus")
    private String containerStatus;

    /**
     * 事务id
     */
    @Excel(name = "事务id", width = 15)
    @ApiModelProperty(value = "事务id", position = 7)
    @TableField("event_id")
    private String eventId;
    /**
     * 事务名称
     */
    @Excel(name = "事务名称", width = 15)
    @ApiModelProperty(value = "事务名称", position = 8)
    @TableField("event_name")
    @KeyWord
    private String eventName;
    /**
     * 事务对象
     */
    @Excel(name = "事务对象", width = 15)
    @ApiModelProperty(value = "事务对象", position = 9)
    @TableField("event_object")
    @Dict(dicCode = "mesEventObject")
    private String eventObject;
    /**
     * 单据编码
     */
    @Excel(name = "单据编码", width = 15)
    @ApiModelProperty(value = "单据编码", position = 10)
    @TableField("bill_code")
    @KeyWord
    private String billCode;
    /**
     * 出库位置编码
     */
    @Excel(name = "出库位置编码", width = 15)
    @ApiModelProperty(value = "出库位置编码", position = 17)
    @TableField("out_storage_code")
    @KeyWord
    private String outStorageCode;
    /**
     * 出库位置名称
     */
    @Excel(name = "出库位置名称", width = 15)
    @ApiModelProperty(value = "出库位置名称", position = 18)
    @TableField("out_storage_name")
    @KeyWord
    private String outStorageName;
    /**
     * 发出人
     */
    @Excel(name = "发出人", width = 15)
    @ApiModelProperty(value = "发出人", position = 19)
    @TableField("out_storage_employee")
    private String outStorageEmployee;
    /**
     * 出库时间
     */
    @Excel(name = "出库时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "出库时间", position = 20)
    @TableField("out_time")
    private Date outTime;
    /**
     * 出库备注
     */
    @Excel(name = "出库备注", width = 15)
    @ApiModelProperty(value = "出库备注", position = 21)
    @TableField("out_note")
    private String outNote;
    /**
     * 期望入库位置编码
     */
    @Excel(name = "期望入库位置编码", width = 15)
    @ApiModelProperty(value = "期望入库位置编码", position = 22)
    @TableField("hope_in_house_code")
    private String hopeInHouseCode;
    /**
     * 期望入库位置名称
     */
    @Excel(name = "期望入库位置名称", width = 15)
    @ApiModelProperty(value = "期望入库位置名称", position = 23)
    @TableField("hope_in_house_name")
    private String hopeInHouseName;
    /**
     * 接收仓位编码
     */
    @Excel(name = "接收仓位编码", width = 15)
    @ApiModelProperty(value = "接收仓位编码", position = 24)
    @TableField("in_storage_code")
    private String inStorageCode;
    /**
     * 接收仓位名称
     */
    @Excel(name = "接收仓位名称", width = 15)
    @ApiModelProperty(value = "接收仓位名称", position = 25)
    @TableField("in_storage_name")
    private String inStorageName;
    /**
     * 接收人
     */
    @Excel(name = "接收人", width = 15)
    @ApiModelProperty(value = "接收人", position = 26)
    @TableField("in_storage_employee")
    private String inStorageEmployee;
    /**
     * 接收时间
     */
    @Excel(name = "接收时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "接收时间", position = 27)
    @TableField("in_time")
    private Date inTime;
    /**
     * 接收备注
     */
    @Excel(name = "接收备注", width = 15)
    @ApiModelProperty(value = "接收备注", position = 28)
    @TableField("in_note")
    private String inNote;
    /**
     * 质量状态
     */
    @Excel(name = "质量状态", width = 15, dicCode = "mesQcStatus")
    @ApiModelProperty(value = "质量状态", position = 31)
    @TableField("quality_status")
    @Dict(dicCode = "mesQcStatus")
    private String qualityStatus;
    /**
     * 关联标准作业步骤id
     */
    @Excel(name = "关联标准作业步骤", width = 15)
    @ApiModelProperty(value = "关联标准作业步骤id", position = 18)
    @TableField("sop_step_id")
    private String sopStepId;
    /**
     * 关联标准作业控件id
     */
    @Excel(name = "关联标准作业控件", width = 15)
    @ApiModelProperty(value = "关联标准作业控件id", position = 18)
    @TableField("sop_control_id")
    private String sopControlId;
    /**
     * 关联生产任务id
     */
    @Excel(name = "关联生产任务id", width = 15)
    @ApiModelProperty(value = "关联生产任务id", position = 18)
    @TableField("task_id")
    private String taskId;
}
