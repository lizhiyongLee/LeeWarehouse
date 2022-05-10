package com.ils.modules.mes.base.machine.entity;

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
 * @Description: 备件库位
 * @Author: Tian
 * @Date: 2021-02-24
 * @Version: V1.0
 */
@Data
@TableName("mes_spare_parts_storage")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "mes_spare_parts_storage对象", description = "备件库位")
public class SparePartsStorage extends ILSEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 1、一级仓位；2二级仓位；
     */
    @Excel(name = "仓位类型", width = 15)
    @ApiModelProperty(value = "1、一级仓位；2二级仓位；", position = 2)
    @TableField("storage_type")
    @Dict(dicCode = "mesStorageType")
    private String storageType;
    /**
     * 仓位编码
     */
    @Excel(name = "仓位编码", width = 15)
    @ApiModelProperty(value = "仓位编码", position = 3)
    @TableField("storage_code")
    private String storageCode;
    /**
     * 仓位名称
     */
    @Excel(name = "仓位名称", width = 15)
    @ApiModelProperty(value = "仓位名称", position = 4)
    @TableField("storage_name")
    private String storageName;
    /**
     * 二维码
     */
    @Excel(name = "二维码", width = 15)
    @ApiModelProperty(value = "二维码", position = 5)
    @TableField("qrcode")
    private String qrcode;
    /**
     * 1,与上级一致；2，单独启用；
     */
    @Excel(name = "仓库质量管理", width = 15,dicCode = "mesQccontrol")
    @ApiModelProperty(value = "1,与上级一致；2，单独启用；", position = 6)
    @TableField("is_qccontrol")
    @Dict(dicCode = "mesQccontrol")
    private String qccontrol;
    /**
     * 由字段“质量管理”触发   1、合格；2、待检；3、不合格  （这三个选项支持多选）多选保存为jason格式
     */
    @Excel(name = "质量管理", width = 15)
    @ApiModelProperty(value = " 由字段“质量管理”触发   1、合格；2、待检；3、不合格  （这三个选项支持多选）多选保存为jason格式", position = 7)
    @TableField("qc_status")
    private String qcStatus;
    /**
     * 1、仓库；2、线边仓3，一级仓位；
     */
    @Excel(name = "上级仓库类型", width = 15)
    @ApiModelProperty(value = "1、仓库；2、线边仓3，一级仓位；", position = 8)
    @TableField("up_storage_type")
    private String upStorageType;
    /**
     * 上级位置编码
     */
    @Excel(name = "上级位置编码", width = 15)
    @ApiModelProperty(value = "上级位置编码", position = 9)
    @TableField("up_storage_code")
    private String upStorageCode;
    /**
     * 上级位置名称
     */
    @Excel(name = "上级位置名称", width = 15)
    @ApiModelProperty(value = "上级位置名称", position = 10)
    @TableField("up_storage_name")
    private String upStorageName;
    /**
     * 上级位置id
     */
    @Excel(name = "上级位置id", width = 15)
    @ApiModelProperty(value = "上级位置id", position = 11)
    @TableField("up_storage_id")
    private String upStorageId;
    /**
     * 附件
     */
    @Excel(name = "附件", width = 15)
    @ApiModelProperty(value = "附件", position = 12)
    @TableField("attach")
    private String attach;
    /**
     * 备注
     */
    @Excel(name = "备注", width = 15)
    @ApiModelProperty(value = "备注", position = 13)
    @TableField("note")
    private String note;
    /**
     * 状态 ：1，启用，0停用；   修改停用和启用时都要校验下级是否全部停用或启用
     */
    @Excel(name = "状态", width = 15)
    @ApiModelProperty(value = "状态 ：1，启用，0停用；   修改停用和启用时都要校验下级是否全部停用或启用", position = 14)
    @TableField("status")
    @Dict(dicCode = "mesStatus")
    private String status;
}
