package com.ils.modules.mes.base.factory.entity;

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

/**
 * @Description: 工位
 * @Author: fengyi
 * @Date: 2020-10-14
 * @Version: V1.0
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("mes_workstation")
@ApiModel(value="mes_workstation对象", description="工位")
public class Workstation extends ILSEntity {
    private static final long serialVersionUID = 1L;
    
	/**工位编码*/
    @Excel(name = "工位编码", width = 15)
    @TableField("station_code")
    @ApiModelProperty(value = "工位编码", position = 2)
	private String stationCode;
	/**工位名称*/
    @Excel(name = "工位名称", width = 15)
    @TableField("station_name")
    @ApiModelProperty(value = "工位名称", position = 3)
	private String stationName;
	/**二维码*/
    @Excel(name = "二维码", width = 15)
    @TableField("qrcode")
    @ApiModelProperty(value = "二维码", position = 4)
	private String qrcode;
	/**1，工厂；2，车间；3，产线；4，工位*/
    @Excel(name = "1，工厂；2，车间；3，产线；4，工位", width = 15, dicCode = "mesWorkstationType")
    @TableField("up_area_type")
    @ApiModelProperty(value = "1，工厂；2，车间；3，产线；4，工位", position = 5)
    @Dict(dicCode = "mesWorkstationType")
	private String upAreaType;
	/**上级区域*/
    @Excel(name = "上级区域名称", width = 15)
    @TableField("up_area_name")
    @ApiModelProperty(value = "上级区域名称", position = 6)
	private String upAreaName;
	/**上级区域*/
    @Excel(name = "上级区域id", width = 15)
    @TableField("up_area")
    @ApiModelProperty(value = "上级区域", position = 7)
	private String upArea;
	/**负责人*/
    @Excel(name = "负责人id", width = 15)
    @TableField("duty_person")
    @ApiModelProperty(value = "负责人id", position = 8)
	private String dutyPerson;
	/**负责人*/
    @Excel(name = "负责人名称", width = 15)
    @TableField("duty_person_name")
    @ApiModelProperty(value = "负责人名称", position = 9)
	private String dutyPersonName;
	/**是否允许多任务：1,是；0，否；*/
    @Excel(name = "是否允许多任务：1,是；0，否；", width = 15, dicCode = "mesYesZero")
    @TableField("is_multistation")
    @ApiModelProperty(value = "是否允许多任务：1,是；0，否；", position = 10)
    @Dict(dicCode = "mesYesZero")
	private String multistation;
	/**报工设备*/
    @Excel(name = "报工设备", width = 15)
    @TableField("equipment")
    @ApiModelProperty(value = "报工设备", position = 11)
    @Dict(dictTable = "mes_machine" ,dicCode = "id",dicText = "machine_name")
	private String equipment;
	/**附件*/
    @Excel(name = "附件", width = 15)
    @TableField("attach")
    @ApiModelProperty(value = "附件", position = 12)
	private String attach;
	/**备注*/
    @Excel(name = "备注", width = 15)
    @TableField("note")
    @ApiModelProperty(value = "备注", position = 13)
	private String note;
	/**状态 ：1，启用，0停用；*/
    @Excel(name = "状态 ：1，启用，0停用；", width = 15, dicCode = "mesStatus")
    @TableField("status")
    @ApiModelProperty(value = "状态 ：1，启用，0停用；", position = 14)
    @Dict(dicCode = "mesStatus")
    private String status;
}
