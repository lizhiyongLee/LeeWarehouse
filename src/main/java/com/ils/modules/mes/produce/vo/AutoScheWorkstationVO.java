package com.ils.modules.mes.produce.vo;

import cn.hutool.core.date.DateUtil;
import com.ils.modules.mes.base.factory.entity.Workstation;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

/**
 * @Description: 自动排程工位缓存VO
 * @author: fengyi
 */
@Setter
@Getter
@ToString(callSuper = true)
public class AutoScheWorkstationVO extends Workstation {
    private static final long serialVersionUID = 1L;

    /**
     * 工位上排程的最晚时间
     */
    @SuppressWarnings({"unchecked "})
    private long maxTime;

    private String maxTimeStr;

    /**
     * 工位上排程的最晚物料ID
     */
    private String itemId;

    public void setMaxTime(long maxTime) {
        this.maxTime = maxTime;
        this.maxTimeStr = DateUtil.formatDateTime(new Date(maxTime));
    }

    public String getFullStation() {
        return this.getStationCode() + "/" + this.getStationName();
    }
}
