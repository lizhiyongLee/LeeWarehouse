package com.ils.modules.mes.produce.service.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.lang.UUID;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ils.common.exception.ILSBootException;
import com.ils.common.util.BigDecimalUtils;
import com.ils.common.util.RedisUtil;
import com.ils.common.util.SpringContextUtils;
import com.ils.modules.mes.base.schedule.entity.ScheduleAutoRuleConfigure;
import com.ils.modules.mes.config.aspect.StopWatchUtil;
import com.ils.modules.mes.enums.AutoScheProcessRuleEnum;
import com.ils.modules.mes.enums.AutoScheSplitRuleEnum;
import com.ils.modules.mes.enums.AutoScheStationRuleEnum;
import com.ils.modules.mes.produce.entity.WorkProcessTask;
import com.ils.modules.mes.produce.mapper.WorkProcessTaskMapper;
import com.ils.modules.mes.produce.rule.FindStationRuleService;
import com.ils.modules.mes.produce.rule.AbstractTaskSplitRuleService;
import com.ils.modules.mes.produce.rule.WorkProcessTaskRuleService;
import com.ils.modules.mes.produce.rule.impl.*;
import com.ils.modules.mes.produce.service.AutoScheService;
import com.ils.modules.mes.produce.util.AutoScheContextEnv;
import com.ils.modules.mes.produce.vo.AutoScheParam;
import com.ils.modules.mes.produce.vo.AutoScheProcessVO;
import com.ils.modules.mes.produce.vo.AutoScheWorkProcessVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @Description: 自动排程
 * @Author: fengyi
 * @Date: 2021-01-28
 * @Version: V1.0
 */
@Slf4j
@Service
public class AutoScheServiceImpl extends ServiceImpl<WorkProcessTaskMapper, WorkProcessTask> implements AutoScheService {


    @Autowired
    private RedisUtil redisUtil;

    /**
     * 工单维度分布式锁前缀
     */
    private final String WORK_ORDER_LOCK_PREFIX = "work_order_";

    /**
     * 分布式锁过期时间
     */
    private final long EXPIRE_TIME = 50000L;

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void autoSche(AutoScheParam autoScheParam) {
        //获取工单id，上锁
        List<AutoScheProcessVO> lstAutoScheProcessVO = autoScheParam.getLstAutoScheProcessVO();
        Map<String, String> orderIdMap = new HashMap<>(8);
        lstAutoScheProcessVO.forEach(autoScheProcessVO -> orderIdMap.put(autoScheProcessVO.getOrderId(), autoScheProcessVO.getOrderNo()));

        Map<String, String> lockMap = new HashMap<String, String>(8);
        String lockKey = null;
        String requestId = null;
        try {
            for (String orderId : orderIdMap.keySet()) {
                lockKey = WORK_ORDER_LOCK_PREFIX + orderId;
                requestId = UUID.fastUUID().toString();
                if (!lockMap.containsKey(lockKey)) {
                    if (!redisUtil.tryGetDistributedLock(lockKey, requestId, EXPIRE_TIME)) {
                        throw new ILSBootException("P-OW-0011", orderIdMap.get(orderId), "");
                    }
                    lockMap.put(lockKey, requestId);
                }
            }
            //执行自动排程
            this.authSche(autoScheParam);
        } finally {
            for (Map.Entry<String, String> entry : lockMap.entrySet()) {
                redisUtil.releaseDistributedLock(entry.getKey(), entry.getValue());
            }
        }
    }

    /**
     * 自动排程
     *
     * @param autoScheParam
     */
    private void authSche(AutoScheParam autoScheParam) {
        // this.logInfo(autoScheParam);
        // 基础数据对象 本次排程涉及到的基础数据存放位置
        AutoScheContextEnv autoScheContextEnv = new AutoScheContextEnv();
        // 请求参数设置到基础数据对象中 赋值 autoScheParam
        autoScheContextEnv.setAutoScheParam(autoScheParam);

        StopWatchUtil.start("排工序顺序");
        // 根据配置获取排序规则实例
        WorkProcessTaskRuleService workProcessTaskRuleService = this.getWorkProcessTaskRuleService(autoScheParam.getScheAutoRuleConfigure());

        // 获取对应排序规则下的工序任务有序列表 赋值 lstAutoScheWorkProcessVO
        List<AutoScheWorkProcessVO> lstAutoScheWorkProcessVO = workProcessTaskRuleService.getSortWorkProcessTaskList(autoScheParam);
        // 将准备好的数据放入基础数据对象中
        autoScheContextEnv.setLstAutoScheWorkProcessVO(lstAutoScheWorkProcessVO);
        StopWatchUtil.stop("排工序顺序");

        // 排程数量校验
        this.validatePlanQty(lstAutoScheWorkProcessVO);

        StopWatchUtil.start("初始化工位数据");
        // 工位搜索规则
        FindStationRuleService findStationRuleService = this.getFindStationRuleService(autoScheParam.getScheAutoRuleConfigure());

        // 初始化工位 赋值 workstationIds workstationMap processWorkstationMap
        findStationRuleService.initWorkstation(autoScheContextEnv);
        // 获取所有工位上在基准时间之后的任务 赋值 autoScheProcessTaskTimeMap
        findStationRuleService.initWorkPlanTask(autoScheContextEnv);
        StopWatchUtil.stop("初始化工位数据");

        StopWatchUtil.start("拆分任务");
        AbstractTaskSplitRuleService abstractTaskSplitRuleService = this.getTaskSplitRuleService(autoScheParam.getScheAutoRuleConfigure());
        abstractTaskSplitRuleService.splitTask(autoScheContextEnv);
        StopWatchUtil.stop("拆分任务");

        AtomicInteger index = new AtomicInteger(1);
        log.info("排程结果: -------------------------------------------------------------------------------");
        autoScheContextEnv.getWaitSavingWorkPlanTaskList().forEach(item -> log.info("{}, {}, {}", JSONUtil.toJsonStr(item), DateUtil.formatDateTime(item.getPlanStartTime()), DateUtil.formatDateTime(item.getPlanEndTime())));
        log.info("排程结果: -------------------------------------------------------------------------------");
        autoScheContextEnv.getWaitSavingWorkPlanTaskList().forEach(item -> System.out.println(StrUtil.join(" ", index.getAndIncrement(), item.getProcessTaskId(), item.getProcessName(), item.getStationName(), "计划数量: ", item.getPlanQty(), "开始时间: ", DateUtil.formatDateTime(item.getPlanStartTime()), "结束时间: ", DateUtil.formatDateTime(item.getPlanEndTime()))));
        log.info("排程结果: -------------------------------------------------------------------------------");

        // 将拆分后的任务按顺序插入数据库
        // 统一生成code
        StopWatchUtil.start("统一生成编码保存");
        abstractTaskSplitRuleService.finalDealPlanTask(autoScheContextEnv.getWaitSavingWorkPlanTaskList());
        StopWatchUtil.stop("统一生成编码保存");
    }


    private void logInfo(AutoScheParam autoScheParam) {
        log.info("开始自动排程");
        log.info("基准时间: {}", DateUtil.formatDateTime(autoScheParam.getBaseTime()));
        ScheduleAutoRuleConfigure rule = autoScheParam.getScheAutoRuleConfigure();
        log.info("排序规则：{}-{}, 数据排序规则：{}-{} 工位查找规则：{}-{}, 拆分规则：{}-{}",
                rule.getProcessRule(), AutoScheProcessRuleEnum.getDescByValue(rule.getProcessRule()),
                rule.getOrderRule(), " | ",
                rule.getStationRule(), AutoScheStationRuleEnum.getDescByValue(rule.getStationRule()),
                rule.getSplitRule(), AutoScheSplitRuleEnum.getDescByValue(rule.getSplitRule())
        );
        List<AutoScheProcessVO> lstAutoScheProcessVO = autoScheParam.getLstAutoScheProcessVO();
        log.info("排程任务数：{}", lstAutoScheProcessVO.size());

        log.info("=============================================================================================");
        for (AutoScheProcessVO scheProcessVO : lstAutoScheProcessVO) {
            log.info("待排程任务id: {}, 工序id: {}, 工序code: {}, 工序name: {}", scheProcessVO.getProcessTaskId(), scheProcessVO.getProcessId(), scheProcessVO.getProcessCode(), scheProcessVO.getProcessName());
            log.info("物料id: {}, 物料code: {}, 物料name: {}", scheProcessVO.getItemId(), scheProcessVO.getItemCode(), scheProcessVO.getItemName());
            log.info("顺序seq: {}, 本次排程数量planQty: {}, 任务数taskSum: {}", scheProcessVO.getSeq(), scheProcessVO.getPlanQty(), scheProcessVO.getTaskSum());
            log.info("计划工单orderId: {}, 计划工单orderNo: {}, 生产批号batchNo: {}", scheProcessVO.getOrderId(), scheProcessVO.getOrderNo(), scheProcessVO.getBatchNo());
            log.info("=============================================================================================");
        }
    }

    /**
     * 根据配置获取排序规则实例
     *
     * @param scheduleAutoRuleConfigure 规则
     * @author niushuai
     * @date: 2021/6/8 10:01:06
     * @return: {@link WorkProcessTaskRuleService}
     */
    private WorkProcessTaskRuleService getWorkProcessTaskRuleService(ScheduleAutoRuleConfigure scheduleAutoRuleConfigure) {
        WorkProcessTaskRuleService workProcessTaskRuleService;
        if (AutoScheProcessRuleEnum.COMMON_RULE.getValue().equals(scheduleAutoRuleConfigure.getProcessRule())) {
            workProcessTaskRuleService = SpringContextUtils.getBean(CommonWorkProcessTaskServiceImpl.class);
        } else if (AutoScheProcessRuleEnum.MIN_TIME_SWITCH_RULE.getValue().equals(scheduleAutoRuleConfigure.getProcessRule())) {
            workProcessTaskRuleService = SpringContextUtils.getBean(MinTimeSwitchWorkProcessTaskServiceImpl.class);
        } else {
            // 未知的
            throw new ILSBootException("P-AU-0071", scheduleAutoRuleConfigure.getProcessRule());
        }
        // log.info("排序规则实例 - workProcessTaskRuleService: {}", workProcessTaskRuleService.getClass().getName());
        return workProcessTaskRuleService;
    }

    /**
     * 获取工位查找规则服务类
     *
     * @param scheduleAutoRuleConfigure 规则
     * @author niushuai
     * @date: 2021/6/8 10:00:29
     * @return: {@link FindStationRuleService}
     */
    private FindStationRuleService getFindStationRuleService(ScheduleAutoRuleConfigure scheduleAutoRuleConfigure) {
        FindStationRuleService findStationRuleService;
        if (AutoScheStationRuleEnum.N_RULE.getValue().equals(scheduleAutoRuleConfigure.getStationRule())) {
            findStationRuleService = SpringContextUtils.getBean(FindStationRuleNtypeServiceImpl.class);
        } else if (AutoScheStationRuleEnum.Z_RULE.getValue().equals(scheduleAutoRuleConfigure.getStationRule())) {
            findStationRuleService = SpringContextUtils.getBean(FindStationRuleZtypeServiceImpl.class);
        } else {
            throw new ILSBootException("P-AU-0075", scheduleAutoRuleConfigure.getStationRule());
        }
        // log.info("工位查找规则实例 - findStationRuleService: {}", findStationRuleService.getClass().getName());
        return findStationRuleService;
    }

    /**
     * 获取任务拆分规则服务类
     *
     * @param scheduleAutoRuleConfigure 规则
     * @author niushuai
     * @date: 2021/6/8 9:59:31
     * @return: {@link AbstractTaskSplitRuleService}
     */
    private AbstractTaskSplitRuleService getTaskSplitRuleService(ScheduleAutoRuleConfigure scheduleAutoRuleConfigure) {
        AbstractTaskSplitRuleService abstractTaskSplitRuleService;
        if (AutoScheSplitRuleEnum.NO_SPLIT_RULE.getValue().equals(scheduleAutoRuleConfigure.getSplitRule())) {
            // 无拆分任务
            abstractTaskSplitRuleService = SpringContextUtils.getBean(TaskNotSplitRuleServiceImpl.class);
        } else if (AutoScheSplitRuleEnum.SUM_SPLIT_RULE.getValue().equals(scheduleAutoRuleConfigure.getSplitRule())) {
            // 按数量拆分
            abstractTaskSplitRuleService = SpringContextUtils.getBean(TaskNumSplitRuleServiceImpl.class);
        } else if (AutoScheSplitRuleEnum.SHIFT_SPLIT_RULE.getValue().equals(scheduleAutoRuleConfigure.getSplitRule())) {
            // 按班组拆分
            abstractTaskSplitRuleService = SpringContextUtils.getBean(TaskShiftSplitRuleServiceImpl.class);
        } else {
            throw new ILSBootException("P-AU-0074", scheduleAutoRuleConfigure.getSplitRule());
        }
        log.info("任务拆分规则实例 - abstractTaskSplitRuleService: {}", abstractTaskSplitRuleService.getClass().getName());
        return abstractTaskSplitRuleService;
    }

    /**
     * 校验待排程工序任务数量
     * <p>本次计划排程数量不能超过剩余可排程数量</p>
     *
     * @param lstAutoScheWorkProcessVO
     */
    private void validatePlanQty(List<AutoScheWorkProcessVO> lstAutoScheWorkProcessVO) {
        for (AutoScheWorkProcessVO autoScheWorkProcessVO : lstAutoScheWorkProcessVO) {
            // 本次计划排程数量 = 任务数 * 单任务排程数量
            BigDecimal scheTotalQty = BigDecimalUtils.multiply(new BigDecimal(autoScheWorkProcessVO.getTaskSum()), autoScheWorkProcessVO.getSchePlanQty());
            // 剩余可排程数量 = 计划数量 - 已排程数量
            BigDecimal readyScheduleQty = BigDecimalUtils.subtract(autoScheWorkProcessVO.getPlanQty(), autoScheWorkProcessVO.getScheduledQty());
            // 本次排程数量是否超出计划总数量
            boolean overPlanQtyFlag = BigDecimalUtils.greaterThan(scheTotalQty, readyScheduleQty);
            if (overPlanQtyFlag) {
                throw new ILSBootException("P-OW-0008", autoScheWorkProcessVO.getOrderNo(),
                        autoScheWorkProcessVO.getProcessCode());
            }
        }
    }
}
