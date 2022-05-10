package com.ils.modules.mes.base.craft.entity;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.ils.common.aspect.annotation.Dict;
import com.ils.common.system.base.entity.ILSEntity;
import com.ils.framework.poi.excel.annotation.Excel;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @Description: 工艺路线主表
 * @Author: fengyi
 * @Date: 2020-11-02
 * @Version: V1.0
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("mes_route")
@ApiModel(value="mes_route对象", description="工艺路线主表")
public class Route extends ILSEntity {
    private static final long serialVersionUID = 1L;
    
	/**工艺路线编码*/
    @Excel(name = "工艺路线编码", width = 15)
    @TableField("route_code")
    @ApiModelProperty(value = "工艺路线编码", position = 2)
	private String routeCode;
	/**工艺路线名称*/
    @Excel(name = "工艺路线名称", width = 15)
    @TableField("route_name")
    @ApiModelProperty(value = "工艺路线名称", position = 3)
	private String routeName;
	/**生效日期*/
	@Excel(name = "生效日期", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @TableField("active_date")
    @ApiModelProperty(value = "生效日期", position = 4)
	private Date activeDate;
	/**失效日期*/
	@Excel(name = "失效日期", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @TableField("unable_date")
    @ApiModelProperty(value = "失效日期", position = 5)
	private Date unableDate;
	/**状态 ：1，启用，0停用；*/
    @Excel(name = "状态 ", width = 15,dicCode = "mesStatus")
    @TableField("status")
    @ApiModelProperty(value = "状态", position = 6)
    @Dict(dicCode = "mesStatus")
	private String status;
	/**备注*/
    @Excel(name = "备注", width = 15)
    @TableField("note")
    @ApiModelProperty(value = "备注", position = 7)
	private String note;
}
