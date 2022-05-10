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
 * @Description: 参数模板明细表
 * @Author: Tian
 * @Date: 2021-10-15
 * @Version: V1.0
 */
@Data
@TableName("mes_para_template_detail")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "mes_para_template_detail对象", description = "参数模板明细表")
public class ParaTemplateDetail extends ILSEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 参数模板id
     */
    @ApiModelProperty(value = "参数模板id", position = 2)
    @TableField("para_temp_id")
    private String paraTempId;
    /**
     * 参数项id
     */
    @ApiModelProperty(value = "参数项id", position = 3)
    @TableField("para_id")
    private String paraId;
    /**
     * 参数项名称
     */
    @Excel(name = "参数项名称", width = 15)
    @ApiModelProperty(value = "参数项名称", position = 4)
    @TableField("para_name")
    private String paraName;
    /**
     * 1、数值；2、百分比；3、开关；4、公式；5、下拉单选。
     */
    @Excel(name = "参数类型", width = 15, dicCode = "mesParameterType")
    @ApiModelProperty(value = "1、数值；2、百分比；3、开关；4、公式；5、下拉单选。", position = 5)
    @TableField("para_type")
    @Dict(dicCode = "mesParameterType")
    private String paraType;
    /**
     * 1,>;2,<;3、=；4、>=;5、<=;6、区间；7、允差；
     */
    @Excel(name = "参数标准类型", width = 15)
    @ApiModelProperty(value = "1,>;2,<;3、=；4、>=;5、<=;6、区间；7、允差；", position = 6)
    @TableField("para_standard")
    @Dict(dicCode = "mesParaStandardNumb")
    private String paraStandard;
    /**
     * 备注
     */
    @Excel(name = "备注", width = 15)
    @ApiModelProperty(value = "备注", position = 7)
    @TableField("note")
    private String note;
}
