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
 * @Description: 报告模板
 * @Author: Tian
 * @Date:   2020-12-10
 * @Version: V1.0
 */
@Data
@TableName("mes_report_template")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="mes_report_template对象", description="报告模板")
public class ReportTemplate extends ILSEntity {
	private static final long serialVersionUID = 1L;
    
	/**设备模板名称*/
	@Excel(name = "设备模板名称", width = 15)
    @ApiModelProperty(value = "设备模板名称", position = 2)
    @TableField("template_name")
	private String templateName;
	/**1、生产报告模板；2、设备报告模板；*/
	@Excel(name = "1、生产报告模板；2、设备报告模板；", width = 15, dicCode = "mesTemplateType")
    @ApiModelProperty(value = "1、生产报告模板；2、设备报告模板；", position = 3)
    @TableField("template_type")
	@Dict(dicCode = "mesTemplateType")
	private String templateType;
	/**备注*/
	@Excel(name = "备注", width = 15)
    @ApiModelProperty(value = "备注", position = 4)
    @TableField("note")
	private String note;
}
