package com.ils.modules.mes.base.schedule.entity;

import java.math.BigDecimal;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.ils.common.aspect.annotation.Dict;
import com.ils.common.system.base.entity.ILSEntity;
import com.ils.common.util.BigDecimalUtils;
import com.ils.framework.poi.excel.annotation.Excel;
import com.ils.modules.mes.enums.TimeUnitEnum;
import com.ils.modules.mes.enums.VolumeTypeEnum;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;


/**
 * @Description: 标准产能
 * @Author: fengyi
 * @Date: 2021-02-01
 * @Version: V1.0
 */
@Data
@TableName("mes_schedule_standard_production_volume")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="mes_schedule_standard_production_volume对象", description="标准产能")
public class ScheduleStandardProductionVolume extends ILSEntity {
	private static final long serialVersionUID = 1L;

    /** 编码 */
    @Excel(name = "编码", width = 15)
    @ApiModelProperty(value = "编码", position = 2)
    @TableField("standard_code")
    private String standardCode;
    
	/**1、工序；2、工艺路线；3、生产BOM。*/
	@Excel(name = "1、工序；2、工艺路线；3、生产BOM。", width = 15, dicCode = "mesStandardType")
    @ApiModelProperty(value = "1、工序；2、工艺路线；3、生产BOM。", position = 2)
    @TableField("standard_type")
    @Dict(dicCode = "mesStandardType")
	private String standardType;
	/**产品BOM*/
	@Excel(name = "产品BOM", width = 15, dictTable = "mes_product", dicCode = "id", dicText = "version")
    @ApiModelProperty(value = "产品BOM", position = 3)
    @TableField("product_bom_id")
    @Dict(dictTable = "mes_product", dicCode = "id", dicText = "version")
	private String productBomId;
	/**工艺路线*/
	@Excel(name = "工艺路线", width = 15, dictTable = "mes_route", dicCode = "id", dicText = "route_name")
    @ApiModelProperty(value = "工艺路线", position = 4)
    @TableField("route_id")
    @Dict(dictTable = "mes_route", dicCode = "id", dicText = "route_name")
	private String routeId;

    /** 工序扩展id */
    @Excel(name = "工序扩展id", width = 15)
    @ApiModelProperty(value = "工序扩展id", position = 5)
    @TableField("process_extend_id")
    private String processExtendId;

	/**工序id*/
	@Excel(name = "工序id", width = 15)
    @ApiModelProperty(value = "工序id", position = 5)
    @TableField("process_id")
	private String processId;
	/**工序编码*/
	@Excel(name = "工序编码", width = 15)
    @ApiModelProperty(value = "工序编码", position = 6)
    @TableField("process_code")
	private String processCode;
	/**工序名称*/
	@Excel(name = "工序名称", width = 15)
    @ApiModelProperty(value = "工序名称", position = 7)
    @TableField("process_name")
	private String processName;
	/**工序序号*/
	@Excel(name = "工序序号", width = 15)
    @ApiModelProperty(value = "工序序号", position = 8)
    @TableField("process_seq")
    private Integer processSeq;
	/**工位id*/
	@Excel(name = "工位id", width = 15)
    @ApiModelProperty(value = "工位id", position = 9)
    @TableField("station_id")
	private String stationId;
	/**工位编码*/
	@Excel(name = "工位编码", width = 15)
    @ApiModelProperty(value = "工位编码", position = 10)
    @TableField("station_code")
	private String stationCode;
	/**工位名称*/
	@Excel(name = "工位名称", width = 15)
    @ApiModelProperty(value = "工位名称", position = 11)
    @TableField("station_name")
	private String stationName;
	/**产出物料id*/
	@Excel(name = "产出物料id", width = 15)
    @ApiModelProperty(value = "产出物料id", position = 12)
    @TableField("item_id")
	private String itemId;
	/**产出物料编码*/
	@Excel(name = "产出物料编码", width = 15)
    @ApiModelProperty(value = "产出物料编码", position = 13)
    @TableField("item_code")
	private String itemCode;
	/**产出物料名称*/
	@Excel(name = "产出物料名称", width = 15)
    @ApiModelProperty(value = "产出物料名称", position = 14)
    @TableField("item_name")
	private String itemName;
	/**1、产能；2、生产节拍。*/
	@Excel(name = "1、产能；2、生产节拍。", width = 15, dicCode = "mesVolumeType")
    @ApiModelProperty(value = "1、产能；2、生产节拍。", position = 15)
    @TableField("volume_type")
    @Dict(dicCode = "mesVolumeType")
	private String volumeType;
	/**0、秒；1、分；2、小时；3、天；*/
	@Excel(name = "0、秒；1、分；2、小时；3、天；", width = 15, dicCode = "mesStandardTimeUnit")
    @ApiModelProperty(value = "0、秒；1、分；2、小时；3、天；", position = 16)
    @TableField("time_unit")
    @Dict(dicCode = "mesStandardTimeUnit")
	private String timeUnit;
	/**时长*/
	@Excel(name = "时长", width = 15)
    @ApiModelProperty(value = "时长", position = 17)
    @TableField("time_period")
	private BigDecimal timePeriod;
	/**数量*/
	@Excel(name = "数量", width = 15)
    @ApiModelProperty(value = "数量", position = 18)
    @TableField("qty")
	private BigDecimal qty;
	/**单位*/
	@Excel(name = "单位", width = 15)
    @ApiModelProperty(value = "单位", position = 19)
    @TableField("unit")
	private String unit;
	/**状态 ：1，启用，0停用；*/
	@Excel(name = "状态 ：1，启用，0停用；", width = 15, dicCode = "mesStatus")
    @ApiModelProperty(value = "状态 ：1，启用，0停用；", position = 20)
    @TableField("status")
    @Dict(dicCode = "mesStatus")
	private String status;
	/**备注*/
	@Excel(name = "备注", width = 15)
    @ApiModelProperty(value = "备注", position = 21)
    @TableField("note")
	private String note;

    public String getProcessFullName() {
        String preFix = "";
        if(this.getProcessSeq()!=null) {
            preFix = this.getProcessSeq()+"/";
        }
        return preFix+this.getProcessCode()+"/"+this.getProcessName();
    }

    public String getStationFullName() {
        return this.getStationCode() + "/" + this.getStationName();
    }

    public String getVolumeTypeDesc() {
        StringBuffer desc =  new StringBuffer();
        
        if (VolumeTypeEnum.volume.getValue().equals(this.volumeType)) {
            desc.append("每").append(BigDecimalUtils.format(this.timePeriod, "###.##"))
                .append(TimeUnitEnum.getEnumDesc(this.getTimeUnit())).append("生产")
                .append(BigDecimalUtils.format(this.qty, "###.##")).append("物料");
        } else {
            desc.append(BigDecimalUtils.format(this.timePeriod, "###.##"))
                .append(TimeUnitEnum.getEnumDesc(this.getTimeUnit())).append("生产")
                .append("一物料");
        }

        return desc.toString();
    }
}
