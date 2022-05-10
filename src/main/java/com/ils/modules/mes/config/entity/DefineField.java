package com.ils.modules.mes.config.entity;

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
 * @Description: 自定义字段
 * @Author: fengyi
 * @Date: 2020-10-13
 * @Version: V1.0
 */
@Data
@TableName("mes_define_field")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="mes_define_field对象", description="自定义字段")
public class DefineField extends ILSEntity {
	private static final long serialVersionUID = 1L;
    
	/**数据库表名*/
	@Excel(name = "数据库表名", width = 15, dicCode = "mesTableCode")
    @ApiModelProperty(value = "数据库表名", position = 2)
    @TableField("table_code")
    @Dict(dicCode = "mesTableCode")
	private String tableCode;
	/**字段名*/
	@Excel(name = "字段名", width = 15)
    @ApiModelProperty(value = "字段名", position = 3)
    @TableField("field_name")
	private String fieldName;
    /** 字段名 */
    @Excel(name = "字段名描述", width = 15)
    @ApiModelProperty(value = "字段名", position = 3)
    @TableField("field_desc")
    private String fieldDesc;
	/**1,int;2,char;*/
	@Excel(name = "1,int;2,char;", width = 15, dicCode = "mesFieldType")
    @ApiModelProperty(value = "1,int;2,char;", position = 4)
    @TableField("field_type")
    @Dict(dicCode = "mesFieldType")
    private String fieldType;
	/**字段约束*/
	@Excel(name = "字段约束", width = 15)
    @ApiModelProperty(value = "字段约束", position = 5)
    @TableField("scope")
	private Integer scope;
	/**备注*/
	@Excel(name = "备注", width = 15)
    @ApiModelProperty(value = "备注", position = 6)
    @TableField("note")
	private String note;
}
