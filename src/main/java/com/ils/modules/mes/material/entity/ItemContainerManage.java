package com.ils.modules.mes.material.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.ils.common.aspect.annotation.Dict;
import com.ils.common.aspect.annotation.KeyWord;
import com.ils.common.system.base.entity.ILSEntity;
import com.ils.framework.poi.excel.annotation.Excel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * @author lishaojie
 * @description
 * @date 2021/10/25 10:51
 */
@Data
@TableName("mes_item_container_manage")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "mes_item_container_manage对象", description = "载具管理")
public class ItemContainerManage extends ILSEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 载具id
     */
    @Excel(name = "载具id", width = 15)
    @ApiModelProperty(value = "载具id", position = 2)
    @TableField("container_id")
    private String containerId;
    /**
     * 载具编码
     */
    @Excel(name = "载具编码", width = 15)
    @ApiModelProperty(value = "载具编码", position = 2)
    @TableField("container_code")
    private String containerCode;
    /**
     * 载具标签码
     */
    @Excel(name = "载具标签码", width = 15)
    @ApiModelProperty(value = "载具标签码", position = 3)
    @TableField("container_qrcode")
    private String containerQrcode;
    /**
     * 载具名称
     */
    @Excel(name = "载具名称", width = 15)
    @ApiModelProperty(value = "载具名称", position = 3)
    @TableField("container_name")
    private String containerName;

    /**
     * 父标签码
     */
    @Excel(name = "父标签码", width = 15)
    @ApiModelProperty(value = "父标签码", position = 3)
    @TableField("father_qrcode")
    private String fatherQrcode;

    /**
     * 对于在仓库中的才需要位置；原材料发到线边仓，也有个线边仓的位置。在制品不用位置表示，填入process。出库未入库的无位置状态
     * 1、有仓位编码的写入编码id;2、出库无位置的写入null;3、在制品写入process,最后一道工序也写入null标记生产完，与出库类似；
     */
    @Excel(name = "对于在仓库中的才需要位置；原材料发到线边仓，也有个线边仓的位置。在制品不用位置表示，填入process。出库未入库的无位置状态1、有仓位编码的写入编码id;2、出库无位置的写入null;3、在制品写入process,最后一道工序也写入null标记生产完，与出库类似；", width = 15)
    @ApiModelProperty(value = "对于在仓库中的才需要位置；原材料发到线边仓，也有个线边仓的位置。在制品不用位置表示，填入process。出库未入库的无位置状态1、有仓位编码的写入编码id;2、出库无位置的写入null;3、在制品写入process,最后一道工序也写入null标记生产完，与出库类似；", position = 13)
    @TableField("storage_id")
    private String storageId;
    /**
     * 仓位编码
     */
    @Excel(name = "仓位编码", width = 15)
    @ApiModelProperty(value = "仓位编码", position = 14)
    @TableField("storage_code")
    private String storageCode;
    /**
     * 仓位名称
     */
    @Excel(name = "仓位名称", width = 15)
    @ApiModelProperty(value = "仓位名称", position = 15)
    @TableField("storage_name")
    private String storageName;
    /**
     * 位置一级仓位编码
     */
    @Excel(name = "位置一级仓位编码", width = 15)
    @ApiModelProperty(value = "位置一级仓位编码", position = 16)
    @TableField("area_code")
    private String areaCode;
    /**
     * 位置一级仓位名称
     */
    @Excel(name = "位置一级仓位名称", width = 15)
    @ApiModelProperty(value = "位置一级仓位名称", position = 17)
    @TableField("area_name")
    private String areaName;
    /**
     * 仓库编码
     */
    @Excel(name = "仓库编码", width = 15)
    @ApiModelProperty(value = "仓库编码", position = 18)
    @TableField("house_code")
    @KeyWord
    private String houseCode;
    /**
     * 仓库名称
     */
    @Excel(name = "仓库名称", width = 15)
    @ApiModelProperty(value = "仓库名称", position = 19)
    @TableField("house_name")
    @KeyWord
    private String houseName;
    /**
     * 期望入库位置编码
     */
    @Excel(name = "期望入库位置编码", width = 15)
    @ApiModelProperty(value = "期望入库位置编码", position = 20)
    @TableField("hope_in_ware_house_code")
    private String hopeInWareHouseCode;
    /**
     * 期望入库位置名称
     */
    @Excel(name = "期望入库位置名称", width = 15)
    @ApiModelProperty(value = "期望入库位置名称", position = 21)
    @TableField("hope_in_ware_house_name")
    private String hopeInWareHouseName;
    /**
     * 1、仓储中2、转运中;3、在制
     */
    @Excel(name = "1、仓储中2、转运中;3、在制", width = 15, dicCode = "mesPositionStatus")
    @ApiModelProperty(value = "1、仓储中2、转运中;3、在制", position = 22)
    @TableField("position_status")
    @Dict(dicCode = "mesPositionStatus")
    private String positionStatus;
    /**
     * 1、厂内；2、已发货；3、已投产；4、已置空；5、已退料；6、已报废；
     */
    @Excel(name = "1、厂内；2、已发货；3、已投产；4、已置空；5、已退料；6、已报废；", width = 15, dicCode = "mesQrcodeStatus")
    @ApiModelProperty(value = "1、厂内；2、已发货；3、已投产；4、已置空；5、已退料；6、已报废；", position = 23)
    @TableField("qrcode_status")
    @Dict(dicCode = "mesQrcodeStatus")
    private String qrcodeStatus;
    /**
     * 质量状态：1、合格；2、不合格；3、待检。
     */
    @Excel(name = "质量状态：1、合格；2、不合格；3、待检。", width = 15, dicCode = "mesQcStatus")
    @ApiModelProperty(value = "质量状态：1、合格；2、待检；3、不合格。", position = 24)
    @TableField("qc_status")
    @Dict(dicCode = "mesQcStatus")
    private String qcStatus;
    /**
     * 1、质检中；2、盘点中；3、无业务；4、生产中；5、生产完；
     */
    @Excel(name = "1、质检中；2、盘点中；3、无业务；4、生产中；5、生产完；", width = 15, dicCode = "mesBusinessStatus")
    @ApiModelProperty(value = "1、质检中；2、盘点中；3、无业务；4、生产中；5、生产完；", position = 25)
    @TableField("business_status")
    @Dict(dicCode = "mesBusinessStatus")
    private String businessStatus;


    /**
     * 1、空载；2、满载；3、可载。
     */
    @Excel(name = "1、空载；2、满载；3、可载。", width = 15, dicCode = "mesContainerStatus")
    @ApiModelProperty(value = "1、空载；2、满载；3、可载。", position = 25)
    @TableField("container_status")
    @Dict(dicCode = "mesContainerStatus")
    private String containerStatus;

    /**
     * 1、登入；0、登出。
     */
    @Excel(name = "1、登入；0、登出。", width = 15, dicCode = "mesLoginStatus")
    @ApiModelProperty(value = "1、登入；0、登出。；", position = 25)
    @TableField("login_status")
    @Dict(dicCode = "mesLoginStatus")
    private String loginStatus;
    /**
     * 上次质检时间
     */
    @Excel(name = "上次质检时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "上次质检时间", position = 43)
    @TableField("qc_time")
    private Date qcTime;
    /**
     * 上次盘点时间
     */
    @Excel(name = "上次盘点时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "上次盘点时间", position = 44)
    @TableField("inventory_check_time")
    private Date inventoryCheckTime;
    /**
     * 附件
     */
    @Excel(name = "附件", width = 15)
    @ApiModelProperty(value = "附件", position = 45)
    @TableField("attach")
    private String attach;
    /**
     * 备注
     */
    @Excel(name = "备注", width = 15)
    @ApiModelProperty(value = "备注", position = 46)
    @TableField("note")
    private String note;
}
