package com.ils.modules.mes.base.schedule.entity;

import java.math.BigDecimal;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.ils.common.aspect.annotation.Dict;
import com.ils.common.system.base.entity.ILSEntity;
import com.ils.common.util.BigDecimalUtils;
import com.ils.framework.poi.excel.annotation.Excel;
import com.ils.modules.mes.enums.TimeUnitEnum;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;


/**
 * @Description: 动态准备时间
 * @Author: fengyi
 * @Date: 2021-02-05
 * @Version: V1.0
 */
@Data
@TableName("mes_schedule_prepare_time")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="mes_schedule_prepare_time对象", description="动态准备时间")
public class SchedulePrepareTime extends ILSEntity {
	private static final long serialVersionUID = 1L;
    
	/**编码*/
	@Excel(name = "编码", width = 15)
    @ApiModelProperty(value = "编码", position = 2)
    @TableField("prepare_code")
	private String prepareCode;
	/**工位id*/
	@Excel(name = "工位id", width = 15)
    @ApiModelProperty(value = "工位id", position = 3)
    @TableField("station_id")
	private String stationId;
	/**工位编码*/
	@Excel(name = "工位编码", width = 15)
    @ApiModelProperty(value = "工位编码", position = 4)
    @TableField("station_code")
	private String stationCode;
	/**工位名称*/
	@Excel(name = "工位名称", width = 15)
    @ApiModelProperty(value = "工位名称", position = 5)
    @TableField("station_name")
	private String stationName;
	/**更换前产出物料id*/
	@Excel(name = "更换前产出物料id", width = 15)
    @ApiModelProperty(value = "更换前产出物料id", position = 6)
    @TableField("source_item_id")
	private String sourceItemId;
	/**更换前产出物料编码*/
	@Excel(name = "更换前产出物料编码", width = 15)
    @ApiModelProperty(value = "更换前产出物料编码", position = 7)
    @TableField("source_item_code")
	private String sourceItemCode;
	/**更换前产出物料名称*/
	@Excel(name = "更换前产出物料名称", width = 15)
    @ApiModelProperty(value = "更换前产出物料名称", position = 8)
    @TableField("source_item_name")
	private String sourceItemName;
	/**更换后产出物料*/
	@Excel(name = "更换后产出物料", width = 15)
    @ApiModelProperty(value = "更换后产出物料", position = 9)
    @TableField("destination_item_id")
	private String destinationItemId;
	/**更换后产出物料编码*/
	@Excel(name = "更换后产出物料编码", width = 15)
    @ApiModelProperty(value = "更换后产出物料编码", position = 10)
    @TableField("destination_item_code")
	private String destinationItemCode;
	/**更换后产出物料名称*/
	@Excel(name = "更换后产出物料名称", width = 15)
    @ApiModelProperty(value = "更换后产出物料名称", position = 11)
    @TableField("destination_item_name")
	private String destinationItemName;
	/**动态准备时长*/
	@Excel(name = "动态准备时长", width = 15)
    @ApiModelProperty(value = "动态准备时长", position = 12)
    @TableField("prepare_duration")
	private BigDecimal prepareDuration;
	/**1、分钟；2、小时。*/
	@Excel(name = "1、分钟；2、小时。", width = 15)
    @ApiModelProperty(value = "1、分钟；2、小时。", position = 13)
    @TableField("unit_name")
	private String unitName;
	/**状态 ：1，启用，0停用；*/
	@Excel(name = "状态 ：1，启用，0停用；", width = 15, dicCode = "mesStatus")
    @ApiModelProperty(value = "状态 ：1，启用，0停用；", position = 14)
    @TableField("status")
    @Dict(dicCode = "mesStatus")
	private String status;
	/**备注*/
	@Excel(name = "备注", width = 15)
    @ApiModelProperty(value = "备注", position = 15)
    @TableField("note")
	private String note;

    /**
     * 
     * 工位全称
     * 
     * @return
     * @date 2021年2月5日
     */
    public String getStationFullName() {
        return this.stationCode + "/" + this.stationName;
    }

    /**
     * 
     * 来源物料全称
     * 
     * @return
     * @date 2021年2月5日
     */
    public String getSourceItemFullName() {
        return this.sourceItemCode + "/" + this.sourceItemName;
    }

    /**
     * 
     * 产后物料全称
     * 
     * @return
     * @date 2021年2月5日
     */
    public String getDestinationItemFullName() {
        return this.destinationItemCode + "/" + this.destinationItemName;
    }

    /**
     * 
     * 动态准备时长
     * 
     * @return
     * @date 2021年2月5日
     */
    public String getPrepareDurationDesc() {
        StringBuffer desc = new StringBuffer();
        desc.append(BigDecimalUtils.format(this.prepareDuration, "###.##"))
            .append(TimeUnitEnum.getEnumDesc(this.unitName));
        return desc.toString();
    }
}
