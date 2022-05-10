package com.ils.modules.mes.base.machine.entity;

import java.io.Serializable;
import java.util.Date;
import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.ils.common.aspect.annotation.Dict;
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
 * @Description: 故障原因
 * @Author: Tian
 * @Date:   2020-11-10
 * @Version: V1.0
 */
@Data
@TableName("mes_machine_fault_reason")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="mes_machine_fault_reason对象", description="故障原因")
public class MachineFaultReason extends ILSEntity {
	private static final long serialVersionUID = 1L;
    
	/**故障现象*/
	@Excel(name = "故障现象", width = 15)
    @ApiModelProperty(value = "故障现象", position = 2)
    @TableField("fault_reason")
	private String faultReason;
	/**故障原因代码*/
	@Excel(name = "故障原因代码", width = 15)
    @ApiModelProperty(value = "故障原因代码", position = 3)
    @TableField("fault_code")
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
