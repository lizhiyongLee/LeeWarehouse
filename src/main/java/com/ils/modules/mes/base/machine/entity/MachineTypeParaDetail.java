package com.ils.modules.mes.base.machine.entity;

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
 * @Description: 设备类型关联参数明细表
 * @Author: Tian
 * @Date: 2021-10-15
 * @Version: V1.0
 */
@Data
@TableName("mes_machine_type_para_detail")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "mes_machine_type_para_detail对象", description = "设备类型关联参数明细表")
public class MachineTypeParaDetail extends ILSEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 设备类型参数主表id
     */
    @Excel(name = "设备类型参数主表id", width = 15)
    @ApiModelProperty(value = "设备类型参数主表id", position = 2)
    @TableField("para_head_id")
    private String paraHeadId;
    /**
     * 设备类型id
     */
    @Excel(name = "设备类型id", width = 15)
    @ApiModelProperty(value = "设备类型id", position = 3)
    @TableField("machine_type_id")
    private String machineTypeId;
    /**
     * 参数项id
     */
    @Excel(name = "参数项id", width = 15)
    @ApiModelProperty(value = "参数项id", position = 4)
    @TableField("para_id")
    private String paraId;
    /**
     * 参数项名称
     */
    @Excel(name = "参数项名称", width = 15)
    @ApiModelProperty(value = "参数项名称", position = 5)
    @TableField("para_name")
    private String paraName;
    /**
     * 1、数值；2、百分比；3、开关；4、公式；5、下拉单选。
     */
    @Excel(name = "1、数值；2、百分比；3、开关；4、公式；5、下拉单选。", width = 15)
    @ApiModelProperty(value = "1、数值；2、百分比；3、开关；4、公式；5、下拉单选。", position = 6)
    @TableField("para_type")
    @Dict(dicCode = "mesParameterType")
    private String paraType;
    /**
     * 1,>;2,<;3、=；4、>=;5、<=;6、区间；7、允差；
     */
    @Excel(name = "1,>;2,<;3、=；4、>=;5、<=;6、区间；7、允差；", width = 15)
    @ApiModelProperty(value = "1,>;2,<;3、=；4、>=;5、<=;6、区间；7、允差；", position = 7)
    @TableField("para_standard")
    @Dict(dicCode = "mesParaStandardNumb")
    private String paraStandard;
    /**
     * 最小值
     */
    @Excel(name = "最小值", width = 15)
    @ApiModelProperty(value = "最小值", position = 8)
    @TableField("min_value")
    private BigDecimal minValue;
    /**
     * 最大值
     */
    @Excel(name = "最大值", width = 15)
    @ApiModelProperty(value = "最大值", position = 9)
    @TableField("max_value")
    private BigDecimal maxValue;
    /**
     * 等于值
     */
    @Excel(name = "等于值", width = 15)
    @ApiModelProperty(value = "等于值", position = 10)
    @TableField("equal_value")
    private BigDecimal equalValue;
    /**
     * 标准值
     */
    @Excel(name = "标准值", width = 15)
    @ApiModelProperty(value = "标准值", position = 11)
    @TableField("standard_value")
    private BigDecimal standardValue;
    /**
     * 上偏差
     */
    @Excel(name = "上偏差", width = 15)
    @ApiModelProperty(value = "上偏差", position = 12)
    @TableField("up_deviation")
    private BigDecimal upDeviation;
    /**
     * 下偏差
     */
    @Excel(name = "下偏差", width = 15)
    @ApiModelProperty(value = "下偏差", position = 13)
    @TableField("down_deviation")
    private BigDecimal downDeviation;
    /**
     * 单位
     */
    @Excel(name = "单位", width = 15)
    @ApiModelProperty(value = "单位", position = 14)
    @TableField("value_unit")
    @Dict(dictTable = "mes_unit", dicCode = "id", dicText = "unit_name")
    private String valueUnit;
    /**
     * 1、开启；0、关闭。
     */
    @Excel(name = "1、开启；0、关闭。", width = 15)
    @ApiModelProperty(value = "1、开启；0、关闭。", position = 15)
    @TableField("switch_value")
    @Dict(dicCode = "mesSwitch")
    private String switchValue;
    /**
     * 以半角逗号分开，供选择。
     */
    @Excel(name = "以半角逗号分开，供选择。", width = 15)
    @ApiModelProperty(value = "以半角逗号分开，供选择。", position = 16)
    @TableField("option_value")
    private String optionValue;
    /**
     * 公式表达式
     */
    @Excel(name = "公式表达式", width = 15)
    @ApiModelProperty(value = "公式表达式", position = 17)
    @TableField("formula")
    private Object formula;
    /**
     * 备注
     */
    @Excel(name = "备注", width = 15)
    @ApiModelProperty(value = "备注", position = 18)
    @TableField("note")
    private String note;
}
