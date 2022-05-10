package com.ils.modules.mes.base.factory.entity;

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
 * @Description: 班组
 * @Author: fengyi
 * @Date: 2020-10-15
 * @Version: V1.0
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("mes_team")
@ApiModel(value="mes_team对象", description="班组")
public class Team extends ILSEntity {
    private static final long serialVersionUID = 1L;
    
	/**班组名称*/
    @Excel(name = "班组名称", width = 15)
    @TableField("team_name")
    @ApiModelProperty(value = "班组名称", position = 2)
	private String teamName;
	/**班组编码*/
    @Excel(name = "班组编码", width = 15)
    @TableField("team_code")
    @ApiModelProperty(value = "班组编码", position = 3)
	private String teamCode;
	/**状态 ：1，启用，0停用；*/
    @Excel(name = "状态 ：1，启用，0停用；", width = 15, dicCode = "mesStatus")
    @TableField("status")
    @ApiModelProperty(value = "状态 ：1，启用，0停用；", position = 4)
    @Dict(dicCode = "mesStatus")
	private String status;
	/**备注*/
    @Excel(name = "备注", width = 15)
    @TableField("note")
    @ApiModelProperty(value = "备注", position = 5)
	private String note;
}
