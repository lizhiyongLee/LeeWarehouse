package com.ils.modules.mes.material.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.ils.modules.mes.material.entity.ItemCell;
import lombok.*;

/**
 * 标签码物料追溯
 *
 * @author Anna.
 * @date 2021/1/13 16:41
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString(callSuper = true)
public class QrCodeItemCellFollowVO extends ItemCell {

    private static final long serialVersionUID = 1;
    /**
     * 生产人员
     */
    private String workers;
    /**
     * 生产工位
     */
    private String workStation;
    /**
     * 工位id
     */
    @TableField("station_id")
    private String stationId;
    /**
     * Accept time
     */
    private String acceptTime;
}
