package com.ils.modules.mes.base.qc.entity;

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

import java.math.BigDecimal;

/**
 * @Description: 质检方案
 * @Author: fengyi
 * @Date: 2020-10-20
 * @Version: V1.0
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("mes_qc_method")
@ApiModel(value="mes_qc_method对象", description="质检方案")
public class QcMethod extends ILSEntity {
    private static final long serialVersionUID = 1L;
    
	/**质检方案名称*/
    @Excel(name = "质检方案名称", width = 15)
    @TableField("method_name")
    @ApiModelProperty(value = "质检方案名称", position = 2)
    @KeyWord
    private String methodName;
	/**触发方式:1,自动触发；0，非自动触发；*/
    @Excel(name = "自动生成任务", width = 15,dicCode = "mesIsOrNot")
    @TableField("is_auto")
    @ApiModelProperty(value = "触发方式:1,自动触发；0，非自动触发；", position = 3)
	private String auto;
	/**质检类型:1,入厂检；2，出厂检；3，首检；4，生产检，5，巡检，6，普通检*/
    @Excel(name = "质检类型", width = 15,dicCode = "mesQcType")
    @TableField("qc_type")
    @ApiModelProperty(value = "质检类型:1,入厂检；2，出厂检；3，首检；4，生产检，5，巡检，6，普通检", position = 4)
    @Dict(dicCode = "mesQcType")
    private String qcType;
	/**质检方式:1、全检，2、比例抽检；3，固定抽检，4、自定义抽检*/
    @Excel(name = "质检方式", width = 15,dicCode = "mesQcWay")
    @TableField("qc_way")
    @ApiModelProperty(value = "质检方式:1、全检，2、比例抽检；3，固定抽检，4、自定义抽检", position = 5)
    @Dict(dicCode = "mesQcWay")
    private String qcWay;
	/**质检数量*/
    @Excel(name = "质检数量", width = 15)
    @TableField("qc_qty")
    @ApiModelProperty(value = "质检数量", position = 6)
	private BigDecimal qcQty;
	/**记录方式:1、单体记录，2、质检项记录；3，次品项记录*/
    @Excel(name = "记录方式", width = 15,dicCode = "mesRecordWay")
    @TableField("record_way")
    @ApiModelProperty(value = "记录方式:1、单体记录，2、质检项记录；3，次品项记录", position = 7)
    @Dict(dicCode = "mesRecordWay")
    private String recordWay;
	/**1，不报废；0，报废；*/
    @Excel(name = "报废性检查", width = 15,dicCode = "mesIsOrNot")
    @TableField("is_waste")
    @ApiModelProperty(value = "1，不报废；0，报废；", position = 8)
	private String waste;
	/**判定维度:1、判定单次样本；2、判定整个批次*/
    @Excel(name = "判定维度", width = 15,dicCode = "mesJudgeType")
    @TableField("judge_type")
    @ApiModelProperty(value = "判定维度:1、判定单次样本；2、判定整个批次", position = 9)
    @Dict(dicCode = "mesJudgeType")
    private String judgeType;
	/**特别提示*/
    @Excel(name = "特别提示", width = 15)
    @TableField("note")
    @ApiModelProperty(value = "特别提示", position = 10)
	private String note;
	/**状态 ：1，启用，0停用；*/
    @Excel(name = "状态", width = 15,dicCode = "mesStatus")
    @TableField("status")
    @ApiModelProperty(value = "状态 ：1，启用，0停用；", position = 11)
    @Dict(dicCode = "mesStatus")
	private String status;
    /**1.可为空，2.不可为空*/
    @Excel(name = "质检值全部填写校验",dicCode = "mesAllowNull")
    @TableField("is_allow_null")
    @Dict(dicCode = "mesAllowNull")
    @ApiModelProperty(value = "1.可为空，2.不可为空", position = 11)
    private String allowNull;

}
