package com.ils.modules.mes.base.factory.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ils.modules.mes.base.factory.entity.WorkCalendar;
import com.ils.modules.mes.base.factory.vo.WorkCalendarParamsVO;
import com.ils.modules.mes.base.factory.vo.WorkCalendarVO;

import java.util.List;

/**
 * @Description: 工作日历
 * @Author: fengyi
 * @Date: 2020-10-19
 * @Version: V1.0
 */
public interface WorkCalendarService extends IService<WorkCalendar> {

    /**
     * 添加
     *
     * @param workCalendarParamsVO
     */
    public void saveWorkCalendar(WorkCalendarParamsVO workCalendarParamsVO);

    /**
     * 修改
     *
     * @param workCalendar
     */
    public void updateWorkCalendar(WorkCalendar workCalendar);

    /**
     * 删除
     *
     * @param id
     */
    public void delWorkCalendar(String id);

    /**
     * 批量删除
     *
     * @param idList
     */
    public void delBatchWorkCalendar(List<String> idList);

    /**
     * 根据设置批量新增
     *
     * @param workCalendarParamsVO
     */
    public void addBatch(WorkCalendarParamsVO workCalendarParamsVO);

    /**
     * 单条重置
     *
     * @param workCalendarParamsVO
     */
    public void singleReset(WorkCalendarParamsVO workCalendarParamsVO);

    /**
     * 根据条件查询工作日历以及班次
     *
     * @param queryWrapper
     * @return List<WorkCalendarVO>
     * @date 2020年10月21日
     */
    List<WorkCalendarVO> queryWorkCalendarList(QueryWrapper queryWrapper);

    /**
     * 查询单条设置数据
     *
     * @param queryWrapper
     * @param tenantId
     * @return List<WorkCalendarParamsVO>
     * @date 2021年1月29日
     */
    List<WorkCalendarParamsVO> querySingleInitData(QueryWrapper queryWrapper,String tenantId);

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
