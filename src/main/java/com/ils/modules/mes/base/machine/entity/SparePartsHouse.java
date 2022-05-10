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
 * @Description: 备件库
 * @Author: Tian
 * @Date: 2021-02-23
 * @Version: V1.0
 */
@Data
@TableName("mes_spare_parts_house")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "mes_spare_parts_house对象", description = "备件库")
public class SparePartsHouse extends ILSEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 仓库编码
     */
    @Excel(name = "仓库编码", width = 15)
    @ApiModelProperty(value = "仓库编码", position = 4)
    @TableField("house_code")
    private String houseCode;
    /**
     * 仓库名称
     */
    @Excel(name = "仓库名称", width = 15)
    @ApiModelProperty(value = "仓库名称", position = 5)
    @TableField("house_name")
    private String houseName;
    /**
     * 二维码
     */
    @Excel(name = "二维码", width = 15)
    @ApiModelProperty(value = "二维码", position = 6)
    @TableField("qrcode")
    private String qrcode;
    /**
     * 1,是；0，否；
     */
    @Excel(name = "仓库质量管理", width = 15,dicCode = "mesQcconrol")
    @ApiModelProperty(value = "1,是；0，否；", position = 7)
    @TableField("is_qcconrol")
    @Dict(dicCode = "mesQcconrol")
    private String qcconrol;
    /**
     * 由字段“质量管理”触发   1、合格；2、不合格；3、待检  （这三个选项支持多选）多选保存为jason格式
     */
    @Excel(name = "选择质量管理类型", width = 15, dicCode = "mesQcStatus", multiReplace = true)
    @ApiModelProperty(value = " 由字段“质量管理”触发   1、合格；2、不合格；3、待检  （这三个选项支持多选）多选保存为jason格式", position = 8)
    @TableField("qc_status")
    @Dict(dicCode = "mesQcStatus")
    private String qcStatus;
    /**
     * 1，是；0，否；
     */
    @Excel(name = "仓库容量管理", width = 15, dicCode = "mesCapacity")
    @ApiModelProperty(value = "1，是；0，否；", position = 9)
    @TableField("is_capacity")
    @Dict(dicCode = "mesCapacity")
    private String capacity;
    /**
     * 由库容管理触发  1，最大库存检查；2、最小库存检查；3、安全库存检查（支持多选）保存为jason
     */
    @Excel(name = "容量管理类型", width = 15, dicCode = "mesCapacityItem", multiReplace = true)
    @ApiModelProperty(value = " 由库容管理触发  1，最大库存检查；2、最小库存检查；3、安全库存检查（支持多选）保存为jason", position = 10)
    @TableField("capacity_item")
    @Dict(dicCode = "mesCapacityItem")
    private String capacityItem;
    /**
     * 状态 ：1，启用，0停用；
     */
    @Excel(name = "状态", width = 15, dicCode = "mesStatus")
    @ApiModelProperty(value = "状态 ：1，启用，0停用；", position = 11)
    @TableField("status")
    @Dict(dicCode = "mesStatus")
    private String status;
    /**
     * 附件
     */
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
}
