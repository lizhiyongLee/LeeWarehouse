package com.ils.modules.mes.base.product.entity;

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

import java.math.BigDecimal;

/**
 * @author lishaojie
 * @description
 * @date 2021/10/15 14:24
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("mes_product_route_para_detail")
@ApiModel(value = "mes_product_route_para_detail", description = "产品路线工序参数明细表")
public class ProductRouteParaDetail extends ILSEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 主表id
     */
    @TableField("para_head_id")
    @ApiModelProperty(value = "主表id", position = 2)
    @Excel(name = "主表id")
    private String paraHeadId;

    /**
     * 参数项id
     */
    @TableField("para_id")
    @ApiModelProperty(value = "参数项id", position = 3)
    @Excel(name = "参数项id", width = 15)
    private String paraId;

    /**
     * 参数项名称
     */
    @TableField("para_name")
    @ApiModelProperty(value = "参数项名称", position = 3)
    @Excel(name = "参数项名称", width = 15)
    private String paraName;

    /**
     * 1、数值；2、百分比；3、开关；4、公式；5、下拉单选。
     */
    @TableField("para_type")
    @ApiModelProperty(value = "参数类型：1、数值；2、百分比；3、开关；4、公式；5、下拉单选。", position = 3)
    @Excel(name = "参数项名称", width = 15, dicCode = "mesParameterType")
    @Dict(dicCode = "mesParameterType")
    private String paraType;

    /**
     * 1,>;2,<;3、=；4、>=;5、<=;6、区间；7、允差；
     */
    @Excel(name = "1,>;2,<;3、=；4、>=;5、<=;6、区间；7、允差；", width = 15, dicCode = "mesParaStandardNumb")
    @ApiModelProperty(value = "1,>;2,<;3、=；4、>=;5、<=;6、区间；7、允差；", position = 6)
    @TableField("para_standard")
    @Dict(dicCode = "mesParaStandardNumb")
    private String paraStandard;

    /**
     * 最小值
     */
    @Excel(name = "最小值", width = 15)
    @ApiModelProperty(value = "最小值", position = 7)
    @TableField("min_value")
    private BigDecimal minValue;

    /**
     * 最大值
     */
    @Excel(name = "最大值", width = 15)
    @ApiModelProperty(value = "最大值", position = 7)
    @TableField("max_value")
    private BigDecimal maxValue;

    /**
     * 等于值
     */
    @Excel(name = "等于值", width = 15)
    @ApiModelProperty(value = "等于值", position = 7)
    @TableField("equal_value")
    private BigDecimal equalValue;

    /**
     * 标准值
     */
    @Excel(name = "标准值", width = 15)
    @ApiModelProperty(value = "标准值", position = 7)
    @TableField("standard_value")
    private BigDecimal standardValue;

    /**
     * 上偏差
     */
    @Excel(name = "上偏差", width = 15)
    @ApiModelProperty(value = "上偏差", position = 7)
    @TableField("up_deviation")
    private BigDecimal upDeviation;

    /**
     * 下偏差
     */
    @Excel(name = "下偏差", width = 15)
    @ApiModelProperty(value = "下偏差", position = 7)
    @TableField("down_deviation")
    private BigDecimal downDeviation;

    /**
     * 单位
     */
    @Excel(name = "单位", width = 15)
    @ApiModelProperty(value = "单位", position = 7)
    @TableField("value_unit")
    @Dict(dictTable = "mes_unit", dicCode = "id", dicText = "unit_name")
    private String valueUnit;

    /**
     * 1、开启；0、关闭。
     */
    @Excel(name = "1、开启；0、关闭。", width = 15)
    @ApiModelProperty(value = "1、开启；0、关闭。", position = 7)
    @TableField("switch_value")
    @Dict(dicCode = "mesSwitch")
    private String switchValue;

    /**
     * 以半角逗号分开，供选择。
     */
    @Excel(name = "以半角逗号分开，供选择。", width = 15)
    @ApiModelProperty(value = "以半角逗号分开，供选择。", position = 7)
    @TableField("option_value")
    private String optionValue;

    /**
     * 公式表达式
     */
    @Excel(name = "公式表达式", width = 15)
    @ApiModelProperty(value = "公式表达式", position = 7)
    @TableField("formula")
    private String formula;

    /**
     * 备注
     */
    @Excel(name = "备注", width = 15)
    @ApiModelProperty(value = "备注", position = 7)
    @TableField("note")
    private String note;
}
