package com.ils.modules.mes.base.sop.entity;

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
 * @Description: sop模板表头
 * @Author: Tian
 * @Date: 2021-07-15
 * @Version: V1.0
 */
@Data
@TableName("mes_sop_template")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "mes_sop_template对象", description = "sop模板表头")
public class SopTemplate extends ILSEntity {
    private static final long serialVersionUID = 1L;

    /**
     * SOP模板编号
     */
    @Excel(name = "SOP模板编号", width = 15)
    @ApiModelProperty(value = "SOP模板编号", position = 2)
    @TableField("template_code")
    private String templateCode;
    /**
     * SOP模板名称
     */
    @Excel(name = "SOP模板名称", width = 15)
    @ApiModelProperty(value = "SOP模板名称", position = 3)
    @TableField("template_name")
    private String templateName;
    /**
     * 1、产品BOM；2、工艺路线；
     */
    @Excel(name = "1、产品BOM；2、工艺路线；", width = 15, dicCode = "mesSopTemplateType")
    @ApiModelProperty(value = "1、产品BOM；2、工艺路线；", position = 4)
    @TableField("template_type")
    @Dict(dicCode = "mesSopTemplateType")
    private String templateType;
    /**
     * 1、对于实体类型为产品BOM，对应产品BOM编码；2、对于实体类型为工艺路线对应工艺路线编码；
     */
    @Excel(name = "1、对于实体类型为产品BOM，对应产品BOM编码；2、对于实体类型为工艺路线对应工艺路线编码；", width = 15)
    @ApiModelProperty(value = "1、对于实体类型为产品BOM，对应产品BOM编码；2、对于实体类型为工艺路线对应工艺路线编码；", position = 5)
    @TableField("entity_code")
    private String entityCode;
    /**
     * 1、对于实体类型为产品BOM，对应产品BOM名称；2、对于实体类型为工艺路线对应工艺路线名称；
     */
    @Excel(name = "1、对于实体类型为产品BOM，对应产品BOM名称；2、对于实体类型为工艺路线对应工艺路线名称；", width = 15)
    @ApiModelProperty(value = "1、对于实体类型为产品BOM，对应产品BOM名称；2、对于实体类型为工艺路线对应工艺路线名称；", position = 6)
    @TableField("entity_name")
    private String entityName;
    /**
     * 模板实体对应实体明细id
     */
    @Excel(name = "模板实体对应实体明细id", width = 15)
    @ApiModelProperty(value = "模板实体对应实体明细id", position = 7)
    @TableField("entity_line_id")
    private String entityLineId;
    /**
     * 模板实体工序编码
     */
    @Excel(name = "模板实体工序编码", width = 15)
    @ApiModelProperty(value = "模板实体工序编码", position = 7)
    @TableField("process_code")
    private String processCode;
    /**
     * 模板实体工序名称
     */
    @Excel(name = "模板实体工序名称", width = 15)
    @ApiModelProperty(value = "模板实体工序名称", position = 8)
    @TableField("process_name")
    private String processName;
    /**
     * 版本
     */
    @Excel(name = "版本", width = 15)
    @ApiModelProperty(value = "版本", position = 9)
    @TableField("version")
    private String version;
    /**
     * 状态 ：1，启用，0停用；
     */
    @Excel(name = "状态 ：1，启用，0停用；", width = 15, dicCode = "mesStatus")
    @ApiModelProperty(value = "状态 ：1，启用，0停用；", position = 10)
    @TableField("status")
    @Dict(dicCode = "mesStatus")
    private String status;
    /**
     * 备注
     */
    @Excel(name = "备注", width = 15)
    @ApiModelProperty(value = "备注", position = 11)
    @TableField("note")
    private String note;
}
