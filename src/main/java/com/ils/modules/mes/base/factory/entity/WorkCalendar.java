package com.ils.modules.mes.base.factory.entity;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.ils.common.system.base.entity.ILSEntity;
import com.ils.framework.poi.excel.annotation.Excel;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;


/**
 * @Description: 工作日历
 * @Author: fengyi
 * @Date: 2020-10-19
 * @Version: V1.0
 */
@Data
@TableName("mes_work_calendar")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="mes_work_calendar对象", description="工作日历")
public class WorkCalendar extends ILSEntity {
	private static final long serialVersionUID = 1L;
    
	/**1,生产日历；2，设备日历；3，质量日历；*/
	@Excel(name = "1,生产日历；2，设备日历；3，质量日历；", width = 15)
    @ApiModelProperty(value = "1,生产日历；2，设备日历；3，质量日历；", position = 2)
    @TableField("type")
	private String type;
	/**班组名称*/
    @Excel(name = "日期", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "日期", position = 3)
    @TableField("workdate")
    private Date workdate;
	/**工作日；休息日；法定节假日；*/
	@Excel(name = "工作日；休息日；法定节假日；", width = 15)
    @ApiModelProperty(value = "工作日；休息日；法定节假日；", position = 4)
    @TableField("date_type")
	private String dateType;
	/**shiftId*/
	@Excel(name = "shiftId", width = 15)
    @ApiModelProperty(value = "shiftId", position = 5)
    @TableField("shift_id")
	private String shiftId;

    /** 工位ID */
    @Excel(name = "工位ID", width = 15)
    @TableField("station_id")
    @ApiModelProperty(value = "工位ID", position = 2)
    private String stationId;

    /** 工位编码 */
    @Excel(name = "工位编码", width = 15)
    @TableField("station_code")
    @ApiModelProperty(value = "工位编码", position = 2)
    private String stationCode;

    /** 工位名称 */
    @Excel(name = "工位名称", width = 15)
    @TableField("station_name")
    @ApiModelProperty(value = "工位名称", position = 3)
    private String stationName;

	/**备注*/
	@Excel(name = "备注", width = 15)
    @ApiModelProperty(value = "备注", position = 6)
    @TableField("note")
	private String note;
}
