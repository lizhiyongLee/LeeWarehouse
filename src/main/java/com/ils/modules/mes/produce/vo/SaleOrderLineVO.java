package com.ils.modules.mes.produce.vo;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ils.common.aspect.annotation.Dict;
import com.ils.modules.mes.produce.entity.SaleOrderLine;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @Description: 物料行VO
 * @author: fengyi
 * @date: 2021年1月22日 下午1:13:29
 */
@Setter
@Getter
@ToString(callSuper = true)
public class SaleOrderLineVO extends SaleOrderLine {

    /** 客户 */
    private String customerName;

    @ApiModelProperty(value = "订单创建时间", hidden = true)
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date orderCreateTime;

    @ApiModelProperty(value = "更新时间", hidden = true)
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date orderUpdateTime;
    /** 订单状态 */
    @Dict(dicCode = "mesSaleOrderStatus")
    private String orderStatus;
}
