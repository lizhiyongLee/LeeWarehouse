package com.ils.modules.mes.base.ware.entity;

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
 * @Description: 仓库定义
 * @Author: Tian
 * @Date:   2020-11-05
 * @Version: V1.0
 */
@Data
@TableName("mes_ware_house")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="mes_ware_house对象", description="仓库定义")
public class WareHouse extends ILSEntity {
	private static final long serialVersionUID = 1L;
    
	/**1、仓库；2、线边仓*/
	@Excel(name = "1、仓库；2、线边仓", width = 15, dicCode = "mesType")
    @ApiModelProperty(value = "1、仓库；2、线边仓", position = 2)
    @TableField("type")
	@Dict(dicCode = "mesType")
	private String type;
	/**车间id:如果选中线边仓，线边仓是需要关联一个车间的。*/
	@Excel(name = "车间id:如果选中线边仓，线边仓是需要关联一个车间的。", width = 15, dictTable = "mes_work_shop", dicCode = "id", dicText = "shop_name")
    @ApiModelProperty(value = "车间id:如果选中线边仓，线边仓是需要关联一个车间的。", position = 3)
    @TableField("work_shop_id")
	@Dict(dictTable = "mes_work_shop", dicCode = "id", dicText = "shop_name")
	private String workShopId;
	/**仓库编码*/
	@Excel(name = "仓库编码", width = 15)
    @ApiModelProperty(value = "仓库编码", position = 4)
    @TableField("house_code")
	private String houseCode;
	/**仓库名称*/
	@Excel(name = "仓库名称", width = 15)
    @ApiModelProperty(value = "仓库名称", position = 5)
    @TableField("house_name")
	private String houseName;
	/**二维码*/
	@Excel(name = "二维码", width = 15)
    @ApiModelProperty(value = "二维码", position = 6)
    @TableField("qrcode")
	private String qrcode;
	/**1,是；0，否；*/
	@Excel(name = "1,是；0，否；", width = 15, dicCode = "mesQcconrol")
    @ApiModelProperty(value = "1,是；0，否；", position = 7)
    @TableField("is_qcconrol")
	@Dict(dicCode = "mesQcconrol")
	private String qcconrol;
	/** 由字段“质量管理”触发   1、合格；2、不合格；3、待检  （这三个选项支持多选多选保存为jason格式*/
	@Excel(name = "选择质量管理类型", width = 15, dicCode = "mesQcStatus", multiReplace = true)
    @ApiModelProperty(value = " 由字段“质量管理”触发   1、合格；2、不合格；3、待检  （这三个选项支持多选多选保存为jason格式", position = 8)
    @TableField("qc_status")
	@Dict(dicCode = "mesQcStatus")
	private String qcStatus;
	/**1，是；0，否；*/
	@Excel(name = "1，是；0，否；", width = 15, dicCode = "mesCapacity")
    @ApiModelProperty(value = "1，是；0，否；", position = 9)
    @TableField("is_capacity")
	@Dict(dicCode = "mesCapacity")
	private String capacity;
	/** 由库容管理触发  1，最大库存检查；2、最小库存检查；3、安全库存检查（支持多选）保存为jason*/
	@Excel(name = "库容管理类型", width = 15, dicCode = "mesCapacityItem", multiReplace = true)
    @ApiModelProperty(value = " 由库容管理触发  1，最大库存检查；2、最小库存检查；3、安全库存检查（支持多选）保存为jason", position = 10)
    @TableField("capacity_item")
	@Dict(dicCode = "mesCapacityItem")
	private String capacityItem;
	/**状态 ：1，启用，0停用；*/
	@Excel(name = "状态 ：1，启用，0停用；", width = 15, dicCode = "mesStatus")
    @ApiModelProperty(value = "状态 ：1，启用，0停用；", position = 11)
    @TableField("status")
	@Dict(dicCode = "mesStatus")
	private String status;
	/**附件*/
	@Excel(name = "附件", width = 15)
    @ApiModelProperty(value = "附件", position = 12)
    @TableField("attach")
	private String attach;
	/**备注*/
	@Excel(name = "备注", width = 15)
    @ApiModelProperty(value = "备注", position = 13)
    @TableField("note")
	private String note;
}
