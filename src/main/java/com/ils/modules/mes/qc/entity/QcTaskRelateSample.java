package com.ils.modules.mes.qc.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.ils.common.system.base.entity.ILSEntity;
import com.ils.framework.poi.excel.annotation.Excel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.math.BigDecimal;


/**
 * @Description: 质检任务关联物料样本
 * @Author: Tian
 * @Date:   2021-03-04
 * @Version: V1.0
 */
@Data
@TableName("mes_qc_task_relate_sample")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="mes_qc_task_relate_sample对象", description="质检任务关联物料样本")
public class QcTaskRelateSample extends ILSEntity {
	private static final long serialVersionUID = 1L;
    
	/**质检任务id*/
	@Excel(name = "质检任务id", width = 15)
    @ApiModelProperty(value = "质检任务id", position = 2)
    @TableField("qc_task_id")
	private String qcTaskId;
	/**标签码*/
	@Excel(name = "标签码", width = 15)
    @ApiModelProperty(value = "标签码", position = 3)
    @TableField("qrcode")
	private String qrcode;
	/**入库批号/生产批号*/
	@Excel(name = "入库批号/生产批号", width = 15)
    @ApiModelProperty(value = "入库批号/生产批号", position = 4)
    @TableField("batch")
	private String batch;
	/**物料id*/
	@Excel(name = "物料id", width = 15)
    @ApiModelProperty(value = "物料id", position = 5)
    @TableField("item_id")
	private String itemId;
	/**物料编码*/
	@Excel(name = "物料编码", width = 15)
    @ApiModelProperty(value = "物料编码", position = 6)
    @TableField("item_code")
	private String itemCode;
	/**物料名称*/
	@Excel(name = "物料名称", width = 15)
    @ApiModelProperty(value = "物料名称", position = 7)
    @TableField("item_name")
	private String itemName;
	/**数量*/
	@Excel(name = "数量", width = 15)
    @ApiModelProperty(value = "数量", position = 8)
    @TableField("qty")
	private BigDecimal qty;
	/**单位*/
	@Excel(name = "单位", width = 15)
    @ApiModelProperty(value = "单位", position = 9)
    @TableField("unit")
	private String unit;
	/**单位名称，以生成时进入表中时第一次业务单位存储。*/
	@Excel(name = "单位名称，以生成时进入表中时第一次业务单位存储。", width = 15)
    @ApiModelProperty(value = "单位名称，以生成时进入表中时第一次业务单位存储。", position = 10)
    @TableField("unit_name")
	private String unitName;
	/**对于在仓库中的才需要位置；原材料发到线边仓，也有个线边仓的位置。在制品不用位置表示，填入process。出库未入库的无位置状态1、有仓位编码的写入编码id;2、出库无位置的写入null;3、在制品写入process,最后一道工序也写入null标记生产完，与出库类似；*/
	@Excel(name = "对于在仓库中的才需要位置；原材料发到线边仓，也有个线边仓的位置。在制品不用位置表示，填入process。出库未入库的无位置状态1、有仓位编码的写入编码id;2、出库无位置的写入null;3、在制品写入process,最后一道工序也写入null标记生产完，与出库类似；", width = 15)
    @ApiModelProperty(value = "对于在仓库中的才需要位置；原材料发到线边仓，也有个线边仓的位置。在制品不用位置表示，填入process。出库未入库的无位置状态1、有仓位编码的写入编码id;2、出库无位置的写入null;3、在制品写入process,最后一道工序也写入null标记生产完，与出库类似；", position = 11)
    @TableField("storage_id")
	private String storageId;
}
