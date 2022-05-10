package com.ils.modules.mes.base.factory.entity;

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
 * @Description: 报告模板控件
 * @Author: Tian
 * @Date:   2020-12-10
 * @Version: V1.0
 */
@Data
@TableName("mes_report_template_control")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="mes_report_template_control对象", description="报告模板控件")
public class ReportTemplateControl extends ILSEntity {
	private static final long serialVersionUID = 1L;
    
	/**设备报告模板id*/
	@Excel(name = "设备报告模板id", width = 15)
    @ApiModelProperty(value = "设备报告模板id", position = 2)
    @TableField("report_template_id")
	private String reportTemplateId;
	/**1、分组；2、文本；3、单选框；4、检查项；5、任务项；6、读数；7、照片；8、时间。*/
	@Excel(name = "1、分组；2、文本；3、单选框；4、检查项；5、任务项；6、读数；7、照片；8、时间。", width = 15)
    @ApiModelProperty(value = "1、分组；2、文本；3、单选框；4、检查项；5、任务项；6、读数；7、照片；8、时间。", position = 3)
    @TableField("control_type")
	private String controlType;
	/**控件名称*/
	@Excel(name = "控件名称", width = 15)
    @ApiModelProperty(value = "控件名称", position = 4)
    @TableField("control_name")
	private String controlName;
	/**设备报告模板父控件id：只支持一级，即分组下不可以再放分组;对于没放在分组下的控件，此字段为0；否则写分组控件id。*/
	@Excel(name = "设备报告模板父控件id：只支持一级，即分组下不可以再放分组;对于没放在分组下的控件，此字段为0；否则写分组控件id。", width = 15)
    @ApiModelProperty(value = "设备报告模板父控件id：只支持一级，即分组下不可以再放分组;对于没放在分组下的控件，此字段为0；否则写分组控件id。", position = 5)
    @TableField("father_control_id")
	private String fatherControlId;
	/**排版序号*/
	@Excel(name = "排版序号", width = 15)
    @ApiModelProperty(value = "排版序号", position = 6)
    @TableField("seq")
	private Integer seq;
	/**0,否；1，是。*/
	@Excel(name = "0,否；1，是。", width = 15)
    @ApiModelProperty(value = "0,否；1，是。", position = 7)
    @TableField("is_required_field")
	private String requiredField;
	/**1、普通提示；2、警示提示。*/
	@Excel(name = "1、普通提示；2、警示提示。", width = 15)
    @ApiModelProperty(value = "1、普通提示；2、警示提示。", position = 8)
    @TableField("remind_type")
	private String remindType;
	/**提示语*/
	@Excel(name = "提示语", width = 15)
    @ApiModelProperty(value = "提示语", position = 9)
    @TableField("remind_word")
	private String remindWord;
	/**针对文本框可设置：  0，否；1，是。*/
	@Excel(name = "针对文本框可设置：  0，否；1，是。", width = 15)
    @ApiModelProperty(value = "针对文本框可设置：  0，否；1，是。", position = 10)
    @TableField("is_multiple_row")
	private String multipleRow;
	/**选项值,以逗号隔开，供单选框使用。*/
	@Excel(name = "选项值,以逗号隔开，供单选框使用。", width = 15)
    @ApiModelProperty(value = "选项值,以逗号隔开，供单选框使用。", position = 11)
    @TableField("option_value")
	private String optionValue;
	/**针检查项可设置：  0，关闭；1，开启。*/
	@Excel(name = "针检查项可设置：  0，关闭；1，开启。", width = 15)
    @ApiModelProperty(value = "针检查项可设置：  0，关闭；1，开启。", position = 12)
    @TableField("dis_condition")
	private String disCondition;
	/**备注*/
	@Excel(name = "备注", width = 15)
    @ApiModelProperty(value = "备注", position = 13)
    @TableField("note")
	private String note;
}
