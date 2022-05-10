package com.ils.modules.mes.base.craft.entity;

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
 * @Description: 参数模板主表
 * @Author: Tian
 * @Date: 2021-10-15
 * @Version: V1.0
 */
@Data
@TableName("mes_para_template_head")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "mes_para_template_head对象", description = "参数模板主表")
public class ParaTemplateHead extends ILSEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 参数模板名称
     */
    @Excel(name = "参数模板名称", width = 15)
    @ApiModelProperty(value = "参数模板名称", position = 2)
    @TableField("para_temp_name")
    private String paraTempName;
    /**
     * 模板分类:1,下发模板；2，监控模板。
     */
    @Excel(name = "模板分类", width = 15,dicCode = "mesParaType")
    @ApiModelProperty(value = "模板分类:1,下发模板；2，监控模板。", position = 3)
    @TableField("para_temp_type")
    @Dict(dicCode = "mesParaType")
    private String paraTempType;
    /**
     * 备注
     */
    @Excel(name = "备注", width = 15)
    @ApiModelProperty(value = "备注", position = 4)
    @TableField("note")
    private String note;
    /**
     * 状态 ：1，启用，0停用；
     */
    @Excel(name = "状态", width = 15,dicCode = "mesStatus")
    @ApiModelProperty(value = "状态 ：1，启用，0停用；", position = 5)
    @TableField("status")
    @Dict(dicCode = "mesStatus")
    private String status;
}
