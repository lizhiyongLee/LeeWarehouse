package com.ils.modules.mes.base.machine.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.ils.common.aspect.annotation.Dict;
import com.ils.common.aspect.annotation.KeyWord;
import com.ils.common.system.base.entity.ILSEntity;
import com.ils.framework.poi.excel.annotation.Excel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;


/**
 * @Description: 设备类型
 * @Author: Tian
 * @Date: 2020-10-29
 * @Version: V1.0
 */
@Data
@TableName("mes_machine_type")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "mes_machine_type对象", description = "设备类型")
public class MachineType extends ILSEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 类型名称
     */
    @Excel(name = "类型名称", width = 15)
    @ApiModelProperty(value = "类型名称", position = 2)
    @TableField("type_name")
    @KeyWord
    private String typeName;
    /**
     * 工装绑定:1,绑定；0、不绑定；
     */
    @Excel(name = "工装绑定", width = 15, dicCode = "mesYesZero")
    @ApiModelProperty(value = "工装绑定:1,绑定；0、不绑定；", position = 3)
    @TableField("is_bind")
    @Dict(dicCode = "mesYesZero")
    private String bind;
    /**
     * 关联mes_machine_label表id
     */
    @Excel(name = "设备标注", width = 15, dictTable = "mes_machine_label", dicCode = "id", dicText = "machine_label_name")
    @ApiModelProperty(value = "关联mes_machine_label表id", position = 4)
    @TableField("label_id")
    @Dict(dictTable = "mes_machine_label", dicCode = "id", dicText = "machine_label_name")
    private String labelId;
    /**
     * 是否开启清洁:1,开启；0、不开启；
     */
    @Excel(name = "是否开启清洁", width = 15, dicCode = "mesYesZero")
    @ApiModelProperty(value = "是否开启清洁:1,开启；0、不开启；", position = 5)
    @TableField("is_clean")
    @Dict(dicCode = "mesYesZero")
    private String clean;
    /**
     * cleanValid
     */
    @Excel(name = "清洁效期", width = 15)
    @ApiModelProperty(value = "cleanValid", position = 6)
    @TableField("clean_valid")
    private Integer cleanValid;
    /**
     * 1,天；2，小时；
     */
    @Excel(name = "效期单位", width = 15, dicCode = "mesValidUnit")
    @ApiModelProperty(value = "1,天；2，小时；", position = 7)
    @TableField("valid_unit")
    @Dict(dicCode = "mesValidUnit")
    private String validUnit;
    /**
     * 1、需要，0，不需要
     */
    @Excel(name = "扫描确认", width = 15, dicCode = "mesYesZero")
    @ApiModelProperty(value = "1、需要，0，不需要", position = 8)
    @TableField("is_scan")
    @Dict(dicCode = "mesYesZero")
    private String scan;
    /**
     * 1,需要；0，不需要
     */
    @Excel(name = "完成验收", width = 15, dicCode = "mesYesZero")
    @ApiModelProperty(value = "1,需要；0，不需要", position = 9)
    @TableField("is_confirm")
    @Dict(dicCode = "mesYesZero")
    private String confirm;
    /**
     * 模板id
     */
    @Excel(name = "报告模板", width = 15, dicCode = "id",dictTable = "mes_report_template",dicText = "template_name")
    @ApiModelProperty(value = "模板id", position = 10)
    @TableField("template_id")
    @Dict(dicCode = "id",dictTable = "mes_report_template",dicText = "template_name")
    private String templateId;
    /**
     * 1、提前1小时，2、提前8小时；3、提前1天；4、提前1周；5、不提醒；
     */
    @Excel(name = "提醒设置", width = 15)
    @ApiModelProperty(value = "1、提前1小时，2、提前8小时；3、提前1天；4、提前1周；5、不提醒；", position = 11)
    @TableField("ahead_type")
    @Dict(dicCode = "mesAheadType")
    private String aheadType;
    /**
     * 状态 ：1，启用，0停用；
     */
    @Excel(name = "状态", width = 15)
    @ApiModelProperty(value = "状态 ：1，启用，0停用；", position = 12)
    @TableField("status")
    @Dict(dicCode = "mesStatus")
    private String status;
    /**
     * 备注
     */
    @Excel(name = "备注", width = 15)
    @ApiModelProperty(value = "备注", position = 13)
    @TableField("note")
    private String note;
}
