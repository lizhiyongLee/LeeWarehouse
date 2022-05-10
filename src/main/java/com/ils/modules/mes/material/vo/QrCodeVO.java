package com.ils.modules.mes.material.vo;

import lombok.*;

import java.math.BigDecimal;

/**
 * 标签码收料时接收传递的参数
 *
 * @author Anna.
 * @date 2021/1/12 13:41
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class QrCodeVO {
    private static final long serialVersionUID = 1L;
    /** 标签码*/
    private String qrcode;
    /** 数量*/
    private BigDecimal qty;
}
