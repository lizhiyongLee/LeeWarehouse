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
 * @Description: 读数配置
 * @Author: Tian
 * @Date:   2020-10-28
 * @Version: V1.0
 */
@Data
@TableName("mes_machine_dataconfig")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="mes_machine_dataconfig对象", description="读数配置")
public class MachineDataconfig extends ILSEntity {
	private static final long serialVersionUID = 1L;
    
	/**1、生产数据；2、工艺数据；3、设备数据4、质量数据；*/
	@Excel(name = "读数类型", width = 15,dicCode = "mesDataType")
    @ApiModelProperty(value = "1、生产数据；2、工艺数据；3、设备数据4、质量数据；", position = 2)
    @TableField("data_type")
	@Dict(dicCode = "mesDataType")
	private String dataType;
	/**读数名称*/
	@Excel(name = "读数名称", width = 15)
    @ApiModelProperty(value = "读数名称", position = 3)
    @TableField("data_name")
	private String dataName;
	/**读数单位*/
	@Excel(name = "读数单位", width = 15,dictTable = "mes_unit",dicText = "unit_name",dicCode = "id")
    @ApiModelProperty(value = "读数单位", position = 4)
    @TableField("data_unit")
	@Dict(dictTable = "mes_unit",dicText = "unit_name",dicCode = "id")
	private String dataUnit;
	/**读数地址*/
	@Excel(name = "读数地址", width = 15)
    @ApiModelProperty(value = "读数地址", position = 5)
    @TableField("data_adress")
	private String dataAdress;

	@Excel(name = "备注", width = 15)
	@ApiModelProperty(value = "备注", position = 4)
	@TableField("note")
	private String note;
}
