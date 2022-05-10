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
 * @author lishaojie
 * @description
 * @date 2021/10/15 13:23
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("mes_process_para_head")
@ApiModel(value = "mes_process_para_head", description = "工序关联工艺参数主表")
public class ProcessParaHead extends ILSEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 工序id
     */
    @TableField("process_id")
    @ApiModelProperty(value = "工序id", position = 2)
    @Excel(name = "工序", width = 15, dictTable = "mes_process", dicCode = "id", dicText = "process_name")
    @Dict( dictTable = "mes_process", dicCode = "id", dicText = "process_name")
    private String processId;

    /**
     * 参数模板id
     */
    @TableField("para_temp_id")
    @ApiModelProperty(value = "参数模板id", position = 3)
    @Excel(name = "参数模板id", width = 15)
    private String paraTempId;

    /**
     * 参数模板名称
     */
    @TableField("para_temp_name")
    @ApiModelProperty(value = "参数模板名称", position = 4)
    @Excel(name = "参数模板名称", width = 15)
    private String paraTempName;

    /**
     * 模板分类:1,下发模板；2，监控模板。
     */
    @TableField("para_temp_type")
    @ApiModelProperty(value = "模板分类:1,下发模板；2，监控模板。", position = 5)
    @Dict(dicCode = "mesParaType")
    @Excel(name = "模板分类", width = 15, dicCode = "mesParaType")
    private String paraTempType;

    /**
     * 备注
     */
    @Excel(name = "备注", width = 15)
    @TableField("note")
    @ApiModelProperty(value = "备注", position = 6)
    private String note;

    /**
     * 状态 ：1，启用，0停用；
     */
    @Excel(name = "状态 ：1，启用，0停用；", width = 15, dicCode = "mesStatus")
    @TableField("status")
    @ApiModelProperty(value = "状态 ：1，启用，0停用；", position = 7)
    @Dict(dicCode = "mesStatus")
    private String status;
}
