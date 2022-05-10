package com.ils.modules.mes.base.factory.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ils.common.system.util.TenantContext;
import com.ils.modules.mes.base.factory.entity.WorkCalendar;
import com.ils.modules.mes.base.factory.entity.Workstation;
import com.ils.modules.mes.base.factory.mapper.WorkCalendarMapper;
import com.ils.modules.mes.base.factory.service.WorkCalendarService;
import com.ils.modules.mes.base.factory.service.WorkstationService;
import com.ils.modules.mes.base.factory.vo.WorkCalendarParamsVO;
import com.ils.modules.mes.base.factory.vo.WorkCalendarVO;
import com.ils.modules.mes.enums.WorkCalendarTypeEnum;
import com.ils.modules.mes.util.CommonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * @Description: 工作日历
 * @Author: fengyi
 * @Date: 2020-10-19
 * @Version: V1.0
 */
@Service
public class WorkCalendarServiceImpl extends ServiceImpl<WorkCalendarMapper, WorkCalendar> implements WorkCalendarService {

    @Autowired
    private WorkstationService workstationService;

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void saveWorkCalendar(WorkCalendarParamsVO workCalendarParamsVO) {

        String type = workCalendarParamsVO.getType();

        // 按条件先删除
        QueryWrapper<WorkCalendar> delWrapper = new QueryWrapper<WorkCalendar>();
        delWrapper.eq("tenant_id", CommonUtil.getTenantId());
        delWrapper.eq("type", type);
        delWrapper.eq("workdate", workCalendarParamsVO.getWorkDate());
        baseMapper.delete(delWrapper);

        List<WorkCalendar> lstWorkCalendar = new ArrayList<WorkCalendar>();

        String[] stationIds = null;
        List<Workstation> lstWorkstation = null;
        if (WorkCalendarTypeEnum.PRODUCTION.getValue().equals(type)) {
            stationIds = workCalendarParamsVO.getStationIds().split(",");
            QueryWrapper<Workstation> workstationWrapper = new QueryWrapper<Workstation>();
            workstationWrapper.in("id", Arrays.asList(stationIds));
            lstWorkstation = workstationService.list(workstationWrapper);
        }

        String[] shiftIds = workCalendarParamsVO.getShiftIds().split(",");
        for (String shiftId : shiftIds) {
            if (WorkCalendarTypeEnum.PRODUCTION.getValue().equals(type)) {
                for (Workstation workstation : lstWorkstation) {
                    WorkCalendar workCalendar = new WorkCalendar();
                    workCalendar.setWorkdate(workCalendarParamsVO.getWorkDate());
                    workCalendar.setDateType(workCalendarParamsVO.getDateType());
                    workCalendar.setType(workCalendarParamsVO.getType());
                    workCalendar.setTenantId(CommonUtil.getTenantId());
                    workCalendar.setShiftId(shiftId);
                    CommonUtil.setSysParam(workCalendar);
                    workCalendar.setStationId(workstation.getId());
                    workCalendar.setStationCode(workstation.getStationCode());
                    workCalendar.setStationName(workstation.getStationName());
                    lstWorkCalendar.add(workCalendar);
                }
            } else {
                WorkCalendar workCalendar = new WorkCalendar();
                workCalendar.setWorkdate(workCalendarParamsVO.getWorkDate());
                workCalendar.setDateType(workCalendarParamsVO.getDateType());
                workCalendar.setType(workCalendarParamsVO.getType());
                workCalendar.setTenantId(CommonUtil.getTenantId());
                workCalendar.setShiftId(shiftId);
                CommonUtil.setSysParam(workCalendar);
                lstWorkCalendar.add(workCalendar);
            }
        }

        baseMapper.insertBatchSomeColumn(lstWorkCalendar);
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void updateWorkCalendar(WorkCalendar workCalendar) {
        baseMapper.updateById(workCalendar);
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void delWorkCalendar(String id) {
        baseMapper.deleteById(id);
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void delBatchWorkCalendar(List<String> idList) {
        baseMapper.deleteBatchIds(idList);
    }

    @Override
    public List<WorkCalendarVO> queryWorkCalendarList(QueryWrapper queryWrapper) {
        return baseMapper.queryWorkCalendarList(queryWrapper);
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void addBatch(WorkCalendarParamsVO workCalendarParamsVO) {
        Date startDate = workCalendarParamsVO.getStartDate();
        Date endDate = workCalendarParamsVO.getEndDate();
        String type = workCalendarParamsVO.getType();
        String[] shiftIds = workCalendarParamsVO.getShiftIds().split(",");
        String weeks = workCalendarParamsVO.getWeeks();
        String dateType = workCalendarParamsVO.getType();
        String[] stationIds = null;
        List<Workstation> lstWorkstation = null;
        if (WorkCalendarTypeEnum.PRODUCTION.getValue().equals(type)) {
            stationIds = workCalendarParamsVO.getStationIds().split(",");
            QueryWrapper<Workstation> workstationWrapper = new QueryWrapper<Workstation>();
            workstationWrapper.in("id", Arrays.asList(stationIds));
            lstWorkstation = workstationService.list(workstationWrapper);
        }

        // 按条件先删除
        QueryWrapper<WorkCalendar> delWrapper = new QueryWrapper<WorkCalendar>();
        delWrapper.eq("tenant_id", CommonUtil.getTenantId());
        delWrapper.eq("type", type);
        if (stationIds != null && stationIds.length != 0) {
            delWrapper.in("station_id", Arrays.asList(stationIds));
        }
        delWrapper.between("workdate", startDate, endDate);
        baseMapper.delete(delWrapper);

        //
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(startDate);
        Date temp = startDate;
        List<WorkCalendar> lstWorkCalendar = new ArrayList<WorkCalendar>();
        while (temp.getTime() < endDate.getTime()) {
            Integer iWeek = calendar.get(Calendar.DAY_OF_WEEK);
            temp = calendar.getTime();
            if (weeks.indexOf(iWeek.toString()) >= 0) {
                for (String shiftId : shiftIds) {
                    if (WorkCalendarTypeEnum.PRODUCTION.getValue().equals(type)) {
                        for (Workstation workstation : lstWorkstation) {
                            WorkCalendar workCalendar = new WorkCalendar();
                            workCalendar.setWorkdate(temp);
                            workCalendar.setDateType(dateType);
                            workCalendar.setType(type);
                            workCalendar.setTenantId(CommonUtil.getTenantId());
                            workCalendar.setShiftId(shiftId);
                            workCalendar.setStationId(workstation.getId());
                            workCalendar.setStationCode(workstation.getStationCode());
                            workCalendar.setStationName(workstation.getStationName());
                            CommonUtil.setSysParam(workCalendar);
                            lstWorkCalendar.add(workCalendar);
                        }
                    } else {
                        WorkCalendar workCalendar = new WorkCalendar();
                        workCalendar.setWorkdate(temp);
                        workCalendar.setDateType(dateType);
                        workCalendar.setType(type);
                        workCalendar.setTenantId(CommonUtil.getTenantId());
                        workCalendar.setShiftId(shiftId);
                        CommonUtil.setSysParam(workCalendar);
                        lstWorkCalendar.add(workCalendar);
                    }
                }
            }

            calendar.add(Calendar.DAY_OF_MONTH, 1);
        }
        baseMapper.insertBatchSomeColumn(lstWorkCalendar);
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void singleReset(WorkCalendarParamsVO workCalendarParamsVO) {

        // 按条件先删除
        QueryWrapper<WorkCalendar> delWrapper = new QueryWrapper<WorkCalendar>();
        delWrapper.eq("tenant_id", CommonUtil.getTenantId());
        String type = workCalendarParamsVO.getType();
        if (WorkCalendarTypeEnum.PRODUCTION.getValue().equals(type)) {
            delWrapper.eq("station_id", workCalendarParamsVO.getStationIds());
        }
        delWrapper.eq("type", type);
        delWrapper.eq("workdate", workCalendarParamsVO.getWorkDate());
        baseMapper.delete(delWrapper);

    }

    @Override
    public List<WorkCalendarParamsVO> querySingleInitData(QueryWrapper queryWrapper,String tenantId) {
        TenantContext.setTenant(tenantId);
        return baseMapper.querySingleInitData(queryWrapper);
    }

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
    @Override
    public String getMinStartDateTimeDualWorkDate(String type, String stationId, String workDate) {
        return baseMapper.getMinStartDateTimeDualWorkDate(type, stationId, workDate);
    }
}
