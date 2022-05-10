package com.ils.modules.mes.machine.entity;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableField;
import com.ils.common.aspect.annotation.Dict;
import com.ils.common.aspect.annotation.KeyWord;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;
import com.ils.framework.poi.excel.annotation.Excel;
import com.ils.common.system.base.entity.ILSEntity;


/**
 * @Description: 设备管理
 * @Author: Tian
 * @Date:   2020-11-10
 * @Version: V1.0
 */
@Data
@TableName("mes_machine")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="mes_machine对象", description="设备管理")
public class Machine extends ILSEntity {
	private static final long serialVersionUID = 1L;
    
	/**设备名称*/
	@Excel(name = "设备名称", width = 15)
    @ApiModelProperty(value = "设备名称", position = 2)
    @TableField("machine_name")
	@KeyWord
	private String machineName;
	/**设备编码*/
	@Excel(name = "设备编码", width = 15)
    @ApiModelProperty(value = "设备编码", position = 3)
    @TableField("machine_code")
	private String machineCode;
	/**设备图片*/
    @ApiModelProperty(value = "设备图片", position = 4)
    @TableField("machine_picture")
	private String machinePicture;
	/**设备类型id*/
	@Excel(name = "设备类型", width = 15,dictTable = "mes_machine_type", dicCode = "id", dicText = "type_name")
    @ApiModelProperty(value = "设备类型id", position = 5)
    @TableField("machine_type_id")
	@Dict(dictTable = "mes_machine_type", dicCode = "id", dicText = "type_name")
	private String machineTypeId;
	/**设备分类*/
	@Excel(name = "设备分类", width = 15,dictTable = "mes_machine_label", dicCode = "id", dicText = "machine_label_name")
    @ApiModelProperty(value = "设备分类", position = 6)
    @TableField("label_id")
	@Dict(dictTable = "mes_machine_label", dicCode = "id", dicText = "machine_label_name")
	private String labelId;
	/**电子标签*/
	@Excel(name = "电子标签", width = 15)
    @ApiModelProperty(value = "电子标签", position = 7)
    @TableField("qr_code")
	private String qrCode;
	/**设备等级*/
	@Excel(name = "设备等级", width = 15,dictTable = "mes_machine_label", dicCode = "id", dicText = "machine_label_name")
    @ApiModelProperty(value = "设备等级", position = 8)
    @TableField("level_id")
	@Dict(dictTable = "mes_machine_label", dicCode = "id", dicText = "machine_label_name")
	private String levelId;
	/**车间*/
	@Excel(name = "车间", width = 15,dictTable = "mes_work_shop", dicCode = "id", dicText = "shop_name")
    @ApiModelProperty(value = "车间", position = 9)
    @TableField("work_shop_id")
	@Dict(dictTable = "mes_work_shop", dicCode = "id", dicText = "shop_name")
	private String workShopId;
	/**制造商*/
	@Excel(name = "制造商", width = 15,dictTable = "mes_machine_manufacturer", dicCode = "id", dicText = "manufacturer_name")
    @ApiModelProperty(value = "制造商", position = 10)
    @TableField("manufacturer_id")
	@Dict(dictTable = "mes_machine_manufacturer", dicCode = "id", dicText = "manufacturer_name")
	private String manufacturerId;
	/**型号*/
	@Excel(name = "型号", width = 15)
    @ApiModelProperty(value = "型号", position = 11)
    @TableField("model")
	private String model;
	/**序列号*/
	@Excel(name = "序列号", width = 15)
    @ApiModelProperty(value = "序列号", position = 12)
    @TableField("serial_num")
	private String serialNum;
	/**供应商出厂日期*/
	@Excel(name = "供应商出厂日期", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "供应商出厂日期", position = 13)
    @TableField("exit_date")
	private Date exitDate;
	/**进厂日期*/
	@Excel(name = "进厂日期", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "进厂日期", position = 14)
    @TableField("enter_date")
	private Date enterDate;
	/**首次启用日期*/
	@Excel(name = "首次启用日期", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "首次启用日期", position = 15)
    @TableField("first_turn_on_date")
	private Date firstTurnOnDate;
	/**状态 ：1，启用，0停用；*/
	@Excel(name = "状态", width = 15,dicCode = "mesStatus")
    @ApiModelProperty(value = "状态 ：1，启用，0停用；", position = 16)
    @TableField("status")
	@Dict(dicCode = "mesStatus")
	private String status;
	/**规格描述*/
	@Excel(name = "规格描述", width = 15)
    @ApiModelProperty(value = "规格描述", position = 17)
    @TableField("description")
	private String description;
	/**附件*/
    @ApiModelProperty(value = "附件", position = 18)
    @TableField("attach")
	private String attach;
}
