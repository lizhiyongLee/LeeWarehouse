package com.ils.modules.mes.base.craft.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.ils.common.system.base.entity.ILSEntity;
import com.ils.framework.poi.excel.annotation.Excel;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @Description: 工序关联质检
 * @Author: fengyi
 * @Date: 2020-10-28
 * @Version: V1.0
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("mes_process_qc_method")
@ApiModel(value="mes_process_qc_method对象", description="工序关联质检")
public class ProcessQcMethod extends ILSEntity {
    private static final long serialVersionUID = 1L;
    
	/**工序id*/
    @TableField("process_id")
    @ApiModelProperty(value = "工序id", position = 2)
	private String processId;
	/**质检方案id*/
    @Excel(name = "质检方案id", width = 15)
    @TableField("qc_method_id")
    @ApiModelProperty(value = "质检方案id", position = 3)
	private String qcMethodId;
	/**质检方案名称*/
    @Excel(name = "质检方案名称", width = 15)
    @TableField("qc_method_name")
    @ApiModelProperty(value = "质检方案名称", position = 4)
	private String qcMethodName;
	/**备注*/
    @Excel(name = "备注", width = 15)
    @TableField("note")
    @ApiModelProperty(value = "备注", position = 5)
	private String note;
}
