package com.ils.modules.mes.base.factory.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ils.common.system.base.mapper.ILSMapper;
import com.ils.modules.mes.base.factory.entity.WorkCalendar;
import com.ils.modules.mes.base.factory.vo.WorkCalendarParamsVO;
import com.ils.modules.mes.base.factory.vo.WorkCalendarVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Description: 工作日历
 * @Author: fengyi
 * @Date: 2020-10-19
 * @Version: V1.0
 */
public interface WorkCalendarMapper extends ILSMapper<WorkCalendar> {

    /**
     * 根据条件查询工作日历以及班次
     *
     * @param queryWrapper
     * @return List<WorkCalendarVO>
     * @date 2020年10月21日
     */
    List<WorkCalendarVO> queryWorkCalendarList(@Param("ew") QueryWrapper queryWrapper);

    /**
     * 查询单条设置数据
     *
     * @param queryWrapper
     * @return List<WorkCalendarParamsVO>
     * @date 2021年1月29日
     */
    List<WorkCalendarParamsVO> querySingleInitData(@Param("ew") QueryWrapper queryWrapper);

    /**
     * 查询出该天班次最早开始时间
     *
     * @param type      工作日历类型
     * @param stationId 工位id
     * @param workDate  工作日期
     * @author niushuai
     * @date: 2021/6/16 13:15:06
     * @return: {@link String}
     */
    String getMinStartDateTimeDualWorkDate(String type, String stationId, String workDate);
}
