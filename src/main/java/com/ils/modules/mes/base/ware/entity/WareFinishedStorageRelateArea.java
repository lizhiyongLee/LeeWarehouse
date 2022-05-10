package com.ils.modules.mes.base.ware.entity;

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
 * @Description: 完工仓位
 * @Author: Tian
 * @Date:   2020-12-04
 * @Version: V1.0
 */
@Data
@TableName("mes_ware_finished_storage_relate_area")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="mes_ware_finished_storage_relate_area对象", description="完工仓位")
public class WareFinishedStorageRelateArea extends ILSEntity {
	private static final long serialVersionUID = 1L;
    
	/**区域id*/
	@Excel(name = "区域id", width = 15)
    @ApiModelProperty(value = "区域id", position = 2)
    @TableField("area_id")
	private String areaId;
	/**完工仓位*/
	@Excel(name = "完工仓位", width = 15)
    @ApiModelProperty(value = "完工仓位", position = 3)
    @TableField("finished_storage")
	private String finishedStorage;
}
