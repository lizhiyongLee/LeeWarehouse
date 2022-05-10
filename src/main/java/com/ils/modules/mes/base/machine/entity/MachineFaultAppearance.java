package com.ils.modules.mes.base.machine.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableField;
import com.ils.common.aspect.annotation.Dict;
import com.ils.common.aspect.annotation.KeyWord;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import com.ils.framework.poi.excel.annotation.Excel;
import com.ils.common.system.base.entity.ILSEntity;


/**
 * @Description: 故障现象
 * @Author: Tian
 * @Date:   2020-11-10
 * @Version: V1.0
 */
@Data
@TableName("mes_machine_fault_appearance")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="mes_machine_fault_appearance对象", description="故障现象")
public class MachineFaultAppearance extends ILSEntity {
	private static final long serialVersionUID = 1L;
    
	/**故障现象*/
	@Excel(name = "故障现象", width = 15)
    @ApiModelProperty(value = "故障现象", position = 2)
    @TableField("fault_appearance")
	@KeyWord
	private String faultAppearance;
	/**故障现象编码*/
	@Excel(name = "故障现象编码", width = 15)
    @ApiModelProperty(value = "故障现象编码", position = 3)
    @TableField("fault_code")
	@KeyWord
	private String faultCode;
	/**状态 ：1，启用，0停用；*/
	@Excel(name = "状态", width = 15,dicCode = "mesStatus")
    @ApiModelProperty(value = "状态 ：1，启用，0停用；", position = 4)
    @TableField("status")
	@Dict(dicCode = "mesStatus")
	private String status;
	/**备注*/
	@Excel(name = "备注", width = 15)
    @ApiModelProperty(value = "备注", position = 5)
    @TableField("note")
	private String note;
}
