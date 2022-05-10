package com.ils.modules.mes.material.vo;

import com.ils.common.system.base.entity.ILSEntity;
import lombok.*;

/**
 * 转移记录详情
 *
 * @author Anna.
 * @date 2021/6/10 9:36
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString(callSuper = true)
public class TransferRecordVO extends ILSEntity {
    /**
     * 操作时间
     */
    private String time;
    /**
     * 操作类型
     */
    private String type;
    /**
     * 操作人
     */
    private String employee;
    /**
     * 位置信息
     */
    private String location;
}
