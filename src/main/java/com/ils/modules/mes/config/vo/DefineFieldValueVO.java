/**
 * @Title: DefineFieldValueVO.java
 * @Package: com.ils.modules.mes.config.vo
 * @author: fengyi
 * @date: 2020年10月13日 下午2:10:10
 */
package com.ils.modules.mes.config.vo;

import com.ils.framework.poi.excel.annotation.Excel;
import com.ils.modules.mes.config.entity.DefineFieldValue;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * @Description: 业务单据自定义字段值
 * @author: fengyi
 * @date: 2020年10月13日 下午2:10:10
 * @param:
 */
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
public class DefineFieldValueVO extends DefineFieldValue {
    /** 字段名 */
    @Excel(name = "字段名", width = 15)
    @ApiModelProperty(value = "字段名", position = 3)
    private String fieldName;
    /** 字段描述 */
    @Excel(name = "字段描述", width = 15)
    @ApiModelProperty(value = "字段描述", position = 3)
    private String fieldDesc;
    /** 1,int;2,char; */
    @Excel(name = "字段类型1,int;2,char;", width = 15)
    @ApiModelProperty(value = "1,int;2,char;", position = 4)
    private String fieldType;
    /** 字段约束 */
    @Excel(name = "字段约束", width = 15)
    @ApiModelProperty(value = "字段约束", position = 5)
    private Integer scope;
}
