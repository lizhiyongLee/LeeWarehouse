package com.ils.modules.mes.machine.vo;

import lombok.*;

/**
 * TODO 类描述
 *
 * @author Anna.
 * @date 2020/12/25 17:05
 */
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
public class MaintainStatus {
    private static final long serialVersionUID = 1L;
    /**
     * 任务id
     */
    private String id;
    /**
     * 维修状态
     */
    private String status;
    /**
     * 设备二维码
     */
    private String qrcode;
}
