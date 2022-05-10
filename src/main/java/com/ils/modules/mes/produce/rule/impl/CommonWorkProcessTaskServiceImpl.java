package com.ils.modules.mes.produce.rule.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ils.modules.mes.base.schedule.entity.ScheduleAutoRuleConfigure;
import com.ils.modules.mes.constants.MesCommonConstant;
import com.ils.modules.mes.enums.AutoScheOrderRuleEnum;
import com.ils.modules.mes.produce.rule.WorkProcessTaskRuleService;
import com.ils.modules.mes.produce.service.WorkProcessTaskService;
import com.ils.modules.mes.produce.vo.AutoScheParam;
import com.ils.modules.mes.produce.vo.AutoScheProcessVO;
import com.ils.modules.mes.produce.vo.AutoScheWorkProcessVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @Description: 订单常规排程规则实现类
 * @author: fengyi
 * @date: 2021年2月25日 上午11:26:57
 */
@Service
public class CommonWorkProcessTaskServiceImpl implements WorkProcessTaskRuleService {

    @Autowired
    private WorkProcessTaskService workProcessTaskService;

    /**
     * 常规排程获取工序有序列表
     *
     * @param autoScheParam 排程基本参数
     * @author niushuai
     * @date: 2021/5/6 14:06:52
     * @return: {@link List<AutoScheWorkProcessVO>}
     */
    @Override
    public List<AutoScheWorkProcessVO> getSortWorkProcessTaskList(AutoScheParam autoScheParam) {
        // 查询有序列表的wrapper
        QueryWrapper<AutoScheWorkProcessVO> queryWrapper = new QueryWrapper<AutoScheWorkProcessVO>();

        // 页面选择的待排程工序任务
        List<AutoScheProcessVO> lstAutoScheProcessVO = autoScheParam.getLstAutoScheProcessVO();

        // 获取用户选择的待排程工序任务id集合 查询使用
        List<String> workProcessIds = lstAutoScheProcessVO.stream().map(AutoScheProcessVO::getProcessTaskId).collect(Collectors.toList());

        // 按照 Map<工序id, 工序对象> 集合
        Map<String, AutoScheProcessVO> workProcessMap = lstAutoScheProcessVO.stream().collect(Collectors.toMap(AutoScheProcessVO::getProcessTaskId, Function.identity()));

        // 添加id为查询条件
        queryWrapper.in("b.id", workProcessIds);
        ScheduleAutoRuleConfigure scheduleAutoRuleConfigure = autoScheParam.getScheAutoRuleConfigure();

        // 构建排序sql  order by xxx
        /*
            ORDER BY a.level DESC,                                          工单优先级降序
                     IF(ISNULL(a.plan_start_time), 1, 0) ASC,               存在计划开始时间的再前面  不存在计划开始时间的在后面
                     a.plan_start_time ASC,                                 再按照计划开始时间升序排序
                     IF(ISNULL(a.plan_end_time), 1, 0) ASC,                 存在计划开始时间的再前面  不存在计划开始时间的在后面
                     a.plan_end_time ASC,                                   再按照计划开始时间升序排序
                     a.id ASC,                                              属于同一工单的在一组
                     b.seq ASC;                                             按照工艺路线中的顺序升序排序 此处会在生产工序任务时固定 修改工艺路线顺序只针对后续工序任务生效
         */
        String[] orderRules = scheduleAutoRuleConfigure.getOrderRule().split(MesCommonConstant.COMMA_SIGN);
        for (String orderRule : orderRules) {
            // 1 按工单优先级降序；
            // 2 按工单计划时间升序；
            // 3 按工单计划结束时间升序。
            this.buildOrderRuleSql(orderRule, queryWrapper);
        }
        queryWrapper.orderByAsc("a.id");
        queryWrapper.orderByAsc("b.seq");

        List<AutoScheWorkProcessVO> lstAutoScheWorkProcessVO = workProcessTaskService.querySortWorkProcessTask(queryWrapper);
        for (AutoScheWorkProcessVO processVO : lstAutoScheWorkProcessVO) {
            // 将用户输入的排产数量和任务数量 设置到查询到的工序任务中
            AutoScheProcessVO autoScheProcessVO = workProcessMap.get(processVO.getId());
            processVO.setSchePlanQty(autoScheProcessVO.getPlanQty());
            processVO.setTaskSum(autoScheProcessVO.getTaskSum());
        }
        // 经过排序后的工序任务列表
        return lstAutoScheWorkProcessVO;
    }

    /**
     * 添加规则SQL
     *
     * @param orderRule
     * @param queryWrapper
     * @date 2021年2月25日
     */
    private void buildOrderRuleSql(String orderRule, QueryWrapper queryWrapper) {
        AutoScheOrderRuleEnum type = AutoScheOrderRuleEnum.getRuleEnum(orderRule);
        switch (type) {
            case PRIORITY_RULE:
                // 按工单优先级降序
                queryWrapper.orderByDesc("a.level");
                break;
            case STARTTIME_ASE_RULE:
                // 按工单计划开始时间升序
                queryWrapper.orderByAsc("IF(ISNULL(a.plan_start_time), 1, 0)");
                queryWrapper.orderByAsc("a.plan_start_time");
                break;
            case ENDTIME_ASE_RULE:
                // 按工单计划结束时间升序
                queryWrapper.orderByAsc("IF(ISNULL(a.plan_end_time), 1, 0)");
                queryWrapper.orderByAsc("a.plan_end_time");
                break;
            default:
        }

    }
}
