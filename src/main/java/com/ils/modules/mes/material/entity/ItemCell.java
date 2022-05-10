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

import java.math.BigDecimal;
import java.util.Date;


/**
 * @Description: 物料单元
 * @Author: wyssss
 * @Date: 2020-11-17
 * @Version: V1.0
 */
@Data
@TableName("mes_item_cell")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "mes_item_cell对象", description = "物料单元")
public class ItemCell extends ILSEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 标签码
     */
    @Excel(name = "标签码", width = 15)
    @ApiModelProperty(value = "标签码", position = 2)
    @TableField("qrcode")
    private String qrcode;
    /**
     * 父标签码
     */
    @Excel(name = "父标签码", width = 15)
    @ApiModelProperty(value = "父标签码", position = 3)
    @TableField("father_qrcode")
    private String fatherQrcode;
    /**
     * 入库批号/生产批号
     */
    @Excel(name = "入库批号/生产批号", width = 15)
    @ApiModelProperty(value = "入库批号/生产批号", position = 4)
    @TableField("batch")
    private String batch;
    /**
     * 序列号:用于相同批次，产出需要区分不同序列使用
     */
    @Excel(name = "序列号:用于相同批次，产出需要区分不同序列使用", width = 15)
    @ApiModelProperty(value = "序列号:用于相同批次，产出需要区分不同序列使用", position = 5)
    @TableField("seq_num")
    private String seqNum;
    /**
     * 物料id
     */
    @Excel(name = "物料id", width = 15)
    @ApiModelProperty(value = "物料id", position = 6)
    @TableField("item_id")
    private String itemId;
    /**
     * 物料编码
     */
    @Excel(name = "物料编码", width = 15)
    @ApiModelProperty(value = "物料编码", position = 7)
    @TableField("item_code")
    @KeyWord
    private String itemCode;
    /**
     * 物料名称
     */
    @Excel(name = "物料名称", width = 15)
    @ApiModelProperty(value = "物料名称", position = 8)
    @TableField("item_name")
    @KeyWord
    private String itemName;
    /**
     * 规格
     */
    @Excel(name = "规格", width = 15)
    @ApiModelProperty(value = "规格", position = 9)
    @TableField("spec")
    @KeyWord
    private String spec;
    /**
     * 数量
     */
    @Excel(name = "数量", width = 15)
    @ApiModelProperty(value = "数量", position = 10)
    @TableField("qty")
    private BigDecimal qty;
    /**
     * 单位，以生成时进入表中时第一次业务单位存储。
     */
    @Excel(name = "单位ID", width = 15)
    @ApiModelProperty(value = "单位，以生成时进入表中时第一次业务单位存储。", position = 11)
    @TableField("unit_id")
    private String unitId;
    /**
     * 单位名称，以生成时进入表中时第一次业务单位存储。
     */
    @Excel(name = "单位名称", width = 15)
    @ApiModelProperty(value = "单位名称，以生成时进入表中时第一次业务单位存储。", position = 12)
    @TableField("unit_name")
    private String unitName;
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
    @TableField("hope_in_storage_code")
    private String hopeInStorageCode;
    /**
     * 期望入库位置名称
     */
    @Excel(name = "期望入库位置名称", width = 15)
    @ApiModelProperty(value = "期望入库位置名称", position = 21)
    @TableField("hope_in_storage_name")
    private String hopeInStorageName;
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
     * 1,一等品；2、二等品；3、三等品。作为可以灵活配置的数据字典。
     */
    @Excel(name = "1,一等品；2、二等品；3、三等品。作为可以灵活配置的数据字典。", width = 15, dicCode = "mesItemLevel")
    @ApiModelProperty(value = "1,一等品；2、二等品；3、三等品。作为可以灵活配置的数据字典。", position = 26)
    @TableField("item_level")
    @Dict(dicCode = "mesItemLevel")
    private String itemLevel;
    /**
     * 销售id
     */
    @Excel(name = "销售id", width = 15)
    @ApiModelProperty(value = "销售id", position = 27)
    @TableField("sale_order_id")
    private String saleOrderId;
    /**
     * 销售订单号
     */
    @Excel(name = "销售订单号", width = 15)
    @ApiModelProperty(value = "销售订单号", position = 28)
    @TableField("sale_order_no")
    private String saleOrderNo;
    /**
     * 工单id
     */
    @Excel(name = "工单id", width = 15)
    @ApiModelProperty(value = "工单id", position = 29)
    @TableField("order_id")
    private String orderId;
    /**
     * 工单号
     */
    @Excel(name = "工单号", width = 15)
    @ApiModelProperty(value = "工单号", position = 30)
    @TableField("order_no")
    private String orderNo;
    /**
     * 供应商id
     */
    @Excel(name = "供应商id", width = 15)
    @ApiModelProperty(value = "供应商id", position = 31)
    @TableField("supplier_id")
    private String supplierId;
    /**
     * 供应商名称
     */
    @Excel(name = "供应商名称", width = 15)
    @ApiModelProperty(value = "供应商名称", position = 32)
    @TableField("supplier_name")
    private String supplierName;
    /**
     * 供应商批号
     */
    @Excel(name = "供应商批号", width = 15)
    @ApiModelProperty(value = "供应商批号", position = 33)
    @TableField("supplier_batch")
    private String supplierBatch;
    /**
     * 客户id
     */
    @Excel(name = "客户id", width = 15)
    @ApiModelProperty(value = "客户id", position = 34)
    @TableField("customer_id")
    private String customerId;
    /**
     * 客户名称
     */
    @Excel(name = "客户名称", width = 15)
    @ApiModelProperty(value = "客户名称", position = 35)
    @TableField("customer_name")
    private String customerName;
    /**
     * 客户批号
     */
    @Excel(name = "客户批号", width = 15)
    @ApiModelProperty(value = "客户批号", position = 36)
    @TableField("customer_batch")
    private String customerBatch;
    /**
     * 生产日期
     */
    @Excel(name = "生产日期", width = 15, format = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "生产日期", position = 37)
    @TableField("produce_date")
    private Date produceDate;
    /**
     * 有效期
     */
    @Excel(name = "有效期", width = 15, format = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "有效期", position = 38)
    @TableField("valid_date")
    private Date validDate;
    /**
     * 已处理工序
     */
    @Excel(name = "已处理工序", width = 15, dictTable = "mes_work_process_task",dicCode = "id",dicText = "process_code")
    @ApiModelProperty(value = "已处理工序，工序任务id", position = 39)
    @TableField("from_process")
    @Dict(dictTable = "mes_work_process_task",dicCode = "id",dicText = "process_code")
    private String fromProcess;
    /**
     * 如果下道处理工序为end，那这个码就完成了所有工序。是一个完工品。预留以后使用
     */
    @Excel(name = "如果下道处理工序为end，那这个码就完成了所有工序。是一个完工品。预留以后使用", width = 15, dictTable = "mes_work_process_task",dicCode = "id",dicText = "process_name")
    @ApiModelProperty(value = "如果下道处理工序为end，那这个码就完成了所有工序。是一个完工品。预留以后使用。工序任务id", position = 40)
    @TableField("now_process")
    @Dict(dictTable = "mes_process",dicCode = "id",dicText = "process_name")
    private String nowProcess;
    /**
     * 1、库存品；2、在制品；2、完工未入库品
     */
    @Excel(name = "1、库存品；2、在制品；2、完工未入库品", width = 15, dicCode = "mesItemType")
    @ApiModelProperty(value = "1、库存品；2、在制品；2、完工未入库品", position = 41)
    @TableField("item_type")
    @Dict(dicCode = "mesItemType")
    private String itemType;
    /**
     * 管理方式：1，标签管理；2，批次管理；3、无码管理。
     */
    @Excel(name = "管理方式：1，标签管理；2，批次管理；3、无码管理。", width = 15, dicCode = "mesManageWay")
    @ApiModelProperty(value = "管理方式：1，标签管理；2，批次管理；3、无码管理。", position = 42)
    @TableField("manage_way")
    @Dict(dicCode = "mesManageWay")
    private String manageWay;
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
