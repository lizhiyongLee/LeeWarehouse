package com.ils.modules.mes.qc.entity;

import java.io.Serializable;
import java.util.Date;
import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.ils.common.aspect.annotation.Dict;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;
import com.ils.framework.poi.excel.annotation.Excel;
import com.ils.common.system.base.entity.ILSEntity;


/**
 * @Description: 质检任务关联质检标准
 * @Author: Tian
 * @Date:   2021-03-04
 * @Version: V1.0
 */
@Data
@TableName("mes_qc_task_item_standard")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="mes_qc_task_item_standard对象", description="质检任务关联质检标准")
public class QcTaskItemStandard extends ILSEntity {
	private static final long serialVersionUID = 1L;
    
	/**质检任务id*/
	@Excel(name = "质检任务id", width = 15)
    @ApiModelProperty(value = "质检任务id", position = 2)
    @TableField("qc_task_id")
	private String qcTaskId;
	/**质检项id*/
	@Excel(name = "质检项id", width = 15)
    @ApiModelProperty(value = "质检项id", position = 3)
    @TableField("item_id")
	private String itemId;
	/**质检项名称*/
	@Excel(name = "质检项名称", width = 15)
    @ApiModelProperty(value = "质检项名称", position = 4)
    @TableField("item_name")
	private String itemName;
	/**1,>;2,<;3、=；4、>=;5、<=;6、区间；7、人工判断；8、手工输入；9、允差；*/
	@Excel(name = "1,>;2,<;3、=；4、>=;5、<=;6、区间；7、人工判断；8、手工输入；9、允差；", width = 15)
    @ApiModelProperty(value = "1,>;2,<;3、=；4、>=;5、<=;6、区间；7、人工判断；8、手工输入；9、允差；", position = 5)
    @TableField("qc_standard")
	private String qcStandard;
	/**最小值*/
	@Excel(name = "最小值", width = 15)
    @ApiModelProperty(value = "最小值", position = 6)
    @TableField("min_value")
	private BigDecimal minValue;
	/**最大值*/
	@Excel(name = "最大值", width = 15)
    @ApiModelProperty(value = "最大值", position = 7)
    @TableField("max_value")
	private BigDecimal maxValue;
	/**等于值*/
	@Excel(name = "等于值", width = 15)
    @ApiModelProperty(value = "等于值", position = 8)
    @TableField("equel_value")
	private BigDecimal equelValue;
	/**标准值*/
	@Excel(name = "标准值", width = 15)
    @ApiModelProperty(value = "标准值", position = 9)
    @TableField("standard_value")
	private BigDecimal standardValue;
	/**上偏差*/
	@Excel(name = "上偏差", width = 15)
    @ApiModelProperty(value = "上偏差", position = 10)
    @TableField("up_diaviation")
	private BigDecimal upDiaviation;
	/**下偏差*/
	@Excel(name = "下偏差", width = 15)
    @ApiModelProperty(value = "下偏差", position = 11)
    @TableField("down_diaviation")
	private BigDecimal downDiaviation;
	/**单位*/
	@Excel(name = "单位", width = 15, dicCode = "id",dicText = "unit_name",dictTable = "mes_unit")
    @ApiModelProperty(value = "单位", position = 12)
    @TableField("value_unit")
	@Dict(dicCode = "id",dicText = "unit_name",dictTable = "mes_unit")
	private String valueUnit;
}
