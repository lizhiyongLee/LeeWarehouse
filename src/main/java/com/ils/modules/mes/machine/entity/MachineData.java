package com.ils.modules.mes.machine.entity;

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
 * @Description: 设备类型关联读数
 * @Author: Tian
 * @Date:   2020-11-17
 * @Version: V1.0
 */
@Data
@TableName("mes_machine_data")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="mes_machine_data对象", description="设备类型关联读数")
public class MachineData extends ILSEntity {
	private static final long serialVersionUID = 1L;
    
	/**设备id*/
    @ApiModelProperty(value = "设备id", position = 2)
    @TableField("machine_id")
	private String machineId;
	/**读数id*/
    @ApiModelProperty(value = "读数id", position = 3)
    @TableField("data_id")
	@Dict(dictTable = "mes_machine_dataconfig",dicCode = "id",dicText = "data_name")
	private String dataId;
	/**读数名称*/
	@Excel(name = "读数名称", width = 15)
    @ApiModelProperty(value = "读数名称", position = 4)
    @TableField("data_name")
	private String dataName;
	/**单位*/
	@Excel(name = "单位", width = 15,dicCode = "id",dictTable = "mes_unit",dicText = "unit_name")
    @ApiModelProperty(value = "单位", position = 5)
    @TableField("unit")
	@Dict(dicCode = "id",dictTable = "mes_unit",dicText = "unit_name")
	private String unit;
	/**读数地址*/
	@Excel(name = "读数地址", width = 15)
    @ApiModelProperty(value = "读数地址", position = 6)
    @TableField("data_adress")
	private String dataAdress;
}
