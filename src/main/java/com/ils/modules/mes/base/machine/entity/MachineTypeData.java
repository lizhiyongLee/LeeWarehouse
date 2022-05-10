package com.ils.modules.mes.base.machine.entity;

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
 * @Description: 设备关联读数
 * @Author: Tian
 * @Date:   2020-10-30
 * @Version: V1.0
 */
@Data
@TableName("mes_machine_type_data")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="mes_machine_type_data对象", description="设备关联读数")
public class MachineTypeData extends ILSEntity {
	private static final long serialVersionUID = 1L;
    
	/**设备类型id*/
	@Excel(name = "设备类型id", width = 15)
    @ApiModelProperty(value = "设备类型id", position = 2)
    @TableField("machine_type_id")
	private String machineTypeId;
	/**读数id*/
	@Excel(name = "读数id", width = 15)
    @ApiModelProperty(value = "读数id", position = 3)
    @TableField("data_id")
	private String dataId;
	/**读数类型*/
	@Excel(name = "读数类型", width = 15)
	@ApiModelProperty(value = "读数类型", position = 4)
	@TableField("data_type")
	@Dict(dicCode = "mesDataType")
	private String dataType;
	/**读数名称*/
	@Excel(name = "读数名称", width = 15)
    @ApiModelProperty(value = "读数名称", position = 5)
    @TableField("data_name")
	private String dataName;
	/**单位*/
	@Excel(name = "单位", width = 15)
    @ApiModelProperty(value = "单位", position = 6)
    @TableField("unit")
	@Dict(dictTable = "mes_unit",dicText = "unit_name",dicCode = "id")
	private String unit;
	/**读数地址*/
	@Excel(name = "读数地址", width = 15)
    @ApiModelProperty(value = "读数地址", position = 7)
    @TableField("data_adress")
	private String dataAdress;
}
