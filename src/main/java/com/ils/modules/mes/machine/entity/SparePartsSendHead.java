package com.ils.modules.mes.machine.entity;

import java.io.Serializable;
import java.util.Date;
import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
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
 * @Description: 备件入库表头
 * @Author: Tian
 * @Date:   2021-02-24
 * @Version: V1.0
 */
@Data
@TableName("mes_spare_parts_send_head")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="mes_spare_parts_send_head对象", description="备件入库表头")
public class SparePartsSendHead extends ILSEntity {
	private static final long serialVersionUID = 1L;
    
	/**出库单单号*/
	@Excel(name = "出库单单号", width = 15)
    @ApiModelProperty(value = "出库单单号", position = 2)
    @TableField("send_code")
	private String sendCode;
	/**1、维修领用；2、保养领用；*/
	@Excel(name = "1、维修领用；2、保养领用；", width = 15)
    @ApiModelProperty(value = "1、维修领用；2、保养领用；", position = 3)
    @TableField("send_type")
	private String sendType;
	/**库位编码*/
	@Excel(name = "库位编码", width = 15)
    @ApiModelProperty(value = "库位编码", position = 4)
    @TableField("out_storage_code")
	private String outStorageCode;
	/**库位名称*/
	@Excel(name = "库位名称", width = 15)
    @ApiModelProperty(value = "库位名称", position = 5)
    @TableField("out_storage_name")
	private String outStorageName;
	/**出库日期*/
	@Excel(name = "出库日期", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "出库日期", position = 6)
    @TableField("out_date")
	private Date outDate;
	/**出库人员*/
	@Excel(name = "出库人员", width = 15)
    @ApiModelProperty(value = "出库人员", position = 7)
    @TableField("employee")
	private String employee;
	/**备注*/
	@Excel(name = "备注", width = 15)
    @ApiModelProperty(value = "备注", position = 8)
    @TableField("note")
	private String note;
	/**1、新建；2、完成。*/
	@Excel(name = "1、新建；2、完成。", width = 15)
    @ApiModelProperty(value = "1、新建；2、完成。", position = 9)
    @TableField("status")
	private String status;
}
