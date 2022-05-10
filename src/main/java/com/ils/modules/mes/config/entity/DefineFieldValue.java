package com.ils.modules.mes.config.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.ils.common.system.base.entity.ILSEntity;
import com.ils.framework.poi.excel.annotation.Excel;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;


/**
 * @Description: 自定义字段值存储
 * @Author: fengyi
 * @Date: 2020-10-13
 * @Version: V1.0
 */
@Data
@TableName("mes_define_field_value")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="mes_define_field_value对象", description="自定义字段值存储")
public class DefineFieldValue extends ILSEntity {
	private static final long serialVersionUID = 1L;
    
	/**数据库表名*/
	@Excel(name = "数据库表名", width = 15)
    @ApiModelProperty(value = "数据库表名", position = 2)
    @TableField("table_code")
	private String tableCode;
	/**字段id*/
	@Excel(name = "字段id", width = 15)
    @ApiModelProperty(value = "字段id", position = 3)
    @TableField("field_id")
	private String fieldId;
	/**主表数据行id*/
	@Excel(name = "主表数据行id", width = 15)
    @ApiModelProperty(value = "主表数据行id", position = 4)
    @TableField("main_id")
	private String mainId;
	/**值*/
	@Excel(name = "值", width = 15)
    @ApiModelProperty(value = "值", position = 5)
    @TableField("data_value")
	private String dataValue;
}
