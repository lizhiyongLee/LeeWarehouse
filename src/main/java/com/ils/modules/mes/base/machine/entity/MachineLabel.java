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
 * @Description: 设备标签
 * @Author: Tian
 * @Date:   2020-10-27
 * @Version: V1.0
 */
@Data
@TableName("mes_machine_label")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="mes_machine_label对象", description="设备标签")
public class MachineLabel extends ILSEntity {
	private static final long serialVersionUID = 1L;
    
	/**1、设备分类 2、设备等级*/
	@Excel(name = "标注类型", width = 15,dicCode = "mesLabelType")
    @ApiModelProperty(value = "1、设备分类 2、设备等级", position = 2)
    @TableField("label_type")
	@Dict(dicCode = "mesLabelType")
	private String labelType;
	/**标签名称*/
	@Excel(name = "标签名称", width = 15)
    @ApiModelProperty(value = "标签名称", position = 3)
    @TableField("machine_label_name")
	private String machineLabelName;
	/**备注*/
	@Excel(name = "备注", width = 15)
    @ApiModelProperty(value = "备注", position = 4)
    @TableField("note")
	private String note;
}
