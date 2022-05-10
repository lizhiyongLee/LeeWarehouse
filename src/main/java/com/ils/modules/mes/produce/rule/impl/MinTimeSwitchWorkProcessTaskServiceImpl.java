package com.ils.modules.mes.produce.rule.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ils.common.exception.ILSBootException;
import com.ils.modules.mes.base.craft.vo.RouteVO;
import com.ils.modules.mes.base.product.entity.ItemBom;
import com.ils.modules.mes.base.product.entity.Product;
import com.ils.modules.mes.base.product.mapper.ItemBomMapper;
import com.ils.modules.mes.base.product.mapper.ProductMapper;
import com.ils.modules.mes.base.schedule.entity.ScheduleAutoRuleConfigure;
import com.ils.modules.mes.constants.MesCommonConstant;
import com.ils.modules.mes.enums.AutoScheOrderRuleEnum;
import com.ils.modules.mes.enums.SymbolsEnum;
import com.ils.modules.mes.enums.WorkflowTypeEnum;
import com.ils.modules.mes.produce.entity.WorkOrder;
import com.ils.modules.mes.produce.entity.WorkProcessTask;
import com.ils.modules.mes.produce.mapper.WorkOrderLineMapper;
import com.ils.modules.mes.produce.mapper.WorkOrderMapper;
import com.ils.modules.mes.produce.mapper.WorkProcessTaskMapper;
import com.ils.modules.mes.produce.rule.WorkProcessTaskRuleService;
import com.ils.modules.mes.produce.vo.AutoScheParam;
import com.ils.modules.mes.produce.vo.AutoScheProcessVO;
import com.ils.modules.mes.produce.vo.AutoScheWorkProcessVO;
import com.ils.modules.mes.produce.vo.TempAutoScheProcessVO;
import com.ils.modules.mes.util.NumUtil;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 最小换型时间规则服务
 *
 * @author: fengyi
 * @date: 2021年2月25日 下午3:58:43
 */
@Slf4j
@Service
public class MinTimeSwitchWorkProcessTaskServiceImpl implements WorkProcessTaskRuleService {

    /**
     * 100w
     */
    private static final long ONE_MILLION = 1000000;

    /**
     * select 关键字
     */
    private static final String SELECT = " select ";

    /**
     * from 关键字
     */
    private static final String FROM_ = " from ";
    /**
     * dual 关键字
     */
    private static final String DUAL = " dual ";

    /**
     * union all 关键字
     */
    private static final String UNION_ALL = " union all ";

    /**
     * mapkey key
     */
    public static final String MAP_KEY = "mapKey";
    /**
     * priorityAvg 平均优先级
     */
    public static final String PRIORITY_AVG = "priorityAvg";
    /**
     * planStartTimeAvg 平均开始时间
     */
    public static final String PLAN_START_TIME_AVG = "planStartTimeAvg";
    /**
     * planEndTimeAvg 平均结束时间
     */
    public static final String PLAN_END_TIME_AVG = "planEndTimeAvg";
    /**
     * order by 排序关键字
     */
    public static final String ORDER_BY = " order by ";
    /**
     * x 临时表名
     */
    public static final String X = " x ";
    /**
     * DESC 降序
     */
    public static final String DESC = " DESC ";
    /**
     * ASC 升序
     */
    public static final String ASC = " ASC ";
    /**
     * 小组 最小单位 不再区分 产出物料相同 工序顺序相同
     */
    public static final String IN_GROUP = "inGroup";
    /**
     * 中组 按照工序顺序区分 中组间的数据产出物料相同, 工序顺序不同  中组内的数据产出物料相同 工序顺序相同
     */
    public static final String MID_GROUP = "midGroup";
    /**
     * 组间 按照物料区分 大组间的数据产物物料不同 大组内的数据产出物料相同
     */
    public static final String BIG_GROUP = "bigGroup";

    @Resource
    private WorkOrderMapper workOrderMapper;

    @Resource
    private WorkOrderLineMapper workOrderLineMapper;

    @Resource
    private WorkProcessTaskMapper workProcessTaskMapper;

    @Resource
    private ProductMapper productMapper;

    /**
     * 最小换型时间排序
     * <pre>
     *     针对用户选择的工序任务进行排序
     *      1、找出工序任务关联的工单的计划开始时间 开始时间相同的分为一组 (大组)
     *          若没有计划开始时间 则没有计划开始时间的分为一组(记为大组) 放置到list的最后位置
     *         大组按照(年月日)自然升序排列
     *
     *      2、大组(n个中组)分好后, 顺序不会再发生改变, 继续排每一个中组的顺序 (包括中组间以及中组内)
     *          大组是有n个中组构成的 中组划分规则: '同一产品' 且 '工序归属工艺路线相同'
     *              >> 中组间排序
     *                  1) 首先计算出每个中组的 平均优先级 平均开始时间占比 平均结束时间占比 三个条件
     *                  2) 这三个至于谁先谁后按照用户传入规则来判断 举例如下
     *                       按照平均优先级降级排序
     *                         针对平均优先级相同的 按照平均开始时间占比 升序排列
     *                           针对平均开始时间相同的 按照平均结束时间占比 升序排列
     *                  3) 经以上步骤可确定中组间排序
     *              >> 中组内排序
     *                  1) 中组由于还不是最小单位, 因此继续拆分 按照同一工序顺序继续拆分(记为小组)
     *
     *      3、中组(n个小组)分好后, 顺序不会再发生改变, 继续排每一个小组的顺序 (包括小组间以及小组内)
     *          小组按照同一物料的同一工艺路线的同一工序分组 不同小组的工序顺序是不同的
     *              >> 小组间排序
     *                  1) 使用工序顺序进行小组间的排序
     *              >> 小组内排序
     *                  1) 小组内排序优先级按照各工序任务所在工单优先级 所在工单计划开始时间 所在工单计划结束时间 三个条件
     *                      这三个至于谁先谁后按照用户传入规则来判断 举例如下
     *                        按照平均优先级降级排序
     *                          针对平均优先级相同的 按照平均开始时间占比 升序排列
     *                            针对平均开始时间相同的 按照平均结束时间占比 升序排列
     *                  2) 经以上步骤可确定小组内排序
     *      4、大组、中组、小组均排序完毕
     *          按照大组 中组 小组的顺序进行组合得到最终的list数据
     * </pre>
     *
     * @param autoScheParam 排序前的工序任务
     * @author niushuai
     * @date: 2021/11/8 10:14:43
     * @return: {@link List<AutoScheWorkProcessVO>} 排序过的工序任务
     */
    @Override
    public List<AutoScheWorkProcessVO> getSortWorkProcessTaskList(AutoScheParam autoScheParam) {
        ScheduleAutoRuleConfigure autoRuleConfigure = autoScheParam.getScheAutoRuleConfigure();

        // 返回结果集
        List<AutoScheWorkProcessVO> resultWorkProcessTaskList = new ArrayList<>();

        List<TempAutoScheProcessVO> pageWorkProcessTaskList = this.convertTempList(autoScheParam.getLstAutoScheProcessVO());

        // 获取用户选择的待排程工序任务id集合 查询使用
        List<String> workOrderIdList = pageWorkProcessTaskList.stream().map(TempAutoScheProcessVO::getOrderId).collect(Collectors.toList());

        // 查询工单数据
        QueryWrapper<WorkOrder> workOrderQueryWrapper = new QueryWrapper<>();
        workOrderQueryWrapper.in("id", workOrderIdList);
        List<WorkOrder> workOrderList = workOrderMapper.selectList(workOrderQueryWrapper);
        Map<String, WorkOrder> workOrderMap = workOrderList.stream().collect(Collectors.toMap(WorkOrder::getId, Function.identity()));

        // 查询涉及到的工艺路线
        List<RouteVO> routeList = workOrderLineMapper.selectRouteByOrderId(workOrderIdList);
        Map<String, RouteVO> workOrderLineMap = routeList.stream().collect(Collectors.toMap(RouteVO::getOrderId, Function.identity()));

        // 填充优先级字段 填充开始与结束时间字段 且拆分时间后分别填充
        this.fillTempFieldVal(workOrderList, workOrderMap, workOrderLineMap, pageWorkProcessTaskList);

        // 开始分组

        // 先找出要分多少组 按照 planStartTimeDate 相同的为一组
        Map<String, List<TempAutoScheProcessVO>> pageGroupByListMap = pageWorkProcessTaskList.stream().filter(item -> !item.lastGroupSeq()).collect(Collectors.groupingBy(TempAutoScheProcessVO::getPlanStartTimeDate));

        // 计划开始时间为空的筛选出来为一组 且为最后一组
        List<TempAutoScheProcessVO> lastGroupWorkProcessList = pageWorkProcessTaskList.stream().filter(TempAutoScheProcessVO::lastGroupSeq).collect(Collectors.toList());

        log.info("bigGroup -> keySize: {} | nullGroup -> listSize: {}", pageGroupByListMap.size(), lastGroupWorkProcessList.size());

        // 大组分配完毕, 开始x循环大组 对小组进行排序
        Set<String> pageDateKeySet = pageGroupByListMap.keySet();

        ArrayList<String> bigGroupSortedKeyList = new ArrayList<>(pageDateKeySet);
        Collections.sort(bigGroupSortedKeyList, (one, another) -> {
            long diff = DateUtil.parseDate(one).getTime() - DateUtil.parseDate(another).getTime();
            return diff > 0 ? 1 : -1;
        });

        bigGroupSortedKeyList.forEach(key ->{
            List<TempAutoScheProcessVO> currGroupList = pageGroupByListMap.get(key);
            // 计算小组间数据
            bigGroupItemsRun(key, currGroupList, resultWorkProcessTaskList, autoRuleConfigure);
        });

        // 没有计划开始时间的大组数据非空时才进行操作
        if (CollectionUtil.isNotEmpty(lastGroupWorkProcessList)) {
            // 将大组的最后一组也做同样的操作
            bigGroupItemsRun(null, lastGroupWorkProcessList, resultWorkProcessTaskList, autoRuleConfigure);
        }

        this.printSort(resultWorkProcessTaskList);
        // if (null != autoScheParam) {
        //     throw new ILSBootException("P-AU-0087");
        // }

        List<WorkProcessTask> processTaskList = workProcessTaskMapper.selectBatchIds(resultWorkProcessTaskList.stream().map(AutoScheWorkProcessVO::getId).collect(Collectors.toList()));
        Map<String, WorkProcessTask> processTaskMap = processTaskList.stream().collect(Collectors.toMap(WorkProcessTask::getId, Function.identity()));

        // 按照 Map<工序任务id, 工序对象> 集合
        Map<String, AutoScheProcessVO> workProcessMap = autoScheParam.getLstAutoScheProcessVO().stream().collect(Collectors.toMap(AutoScheProcessVO::getProcessTaskId, Function.identity()));

        this.finalDealFieldValue(resultWorkProcessTaskList, processTaskMap, workProcessMap, workOrderMap);

        return resultWorkProcessTaskList;
    }

    /**
     * 最终统一处理需要的值
     *
     * @param resultWorkProcessTaskList
     * @param processTaskMap
     * @param workProcessMap
     * @param workOrderMap
     * @author niushuai
     * @date: 2021/11/16 15:47:51
     * @return: void
     */
    private void finalDealFieldValue(List<AutoScheWorkProcessVO> resultWorkProcessTaskList, Map<String, WorkProcessTask> processTaskMap, Map<String, AutoScheProcessVO> workProcessMap, Map<String, WorkOrder> workOrderMap) {
        for (AutoScheWorkProcessVO processVO : resultWorkProcessTaskList) {
            // 将用户输入的排产数量和任务数量 设置到查询到的工序任务中
            AutoScheProcessVO autoScheProcessVO = workProcessMap.get(processVO.getId());
            processVO.setSchePlanQty(autoScheProcessVO.getPlanQty());
            processVO.setTaskSum(autoScheProcessVO.getTaskSum());

            WorkProcessTask workProcessTask = processTaskMap.get(processVO.getId());

            Optional.ofNullable(workProcessTask).orElseThrow(() -> new ILSBootException("P-AU-1002", processVO.getOrderNo(), processVO.getId()));

            processVO.setUnit(workProcessTask.getUnit());
            processVO.setSpec(workProcessTask.getSpec());
            processVO.setPriorCode(workProcessTask.getPriorCode());
            processVO.setNextCode(workProcessTask.getNextCode());
            processVO.setLinkType(workProcessTask.getLinkType());

            processVO.setPlanQty(workProcessTask.getPlanQty());
            processVO.setScheduledQty(workProcessTask.getScheduledQty());
            processVO.setPublishQty(workProcessTask.getPublishQty());
            processVO.setCompletedQty(workProcessTask.getCompletedQty());

            processVO.setTenantId(workProcessTask.getTenantId());
            processVO.setCreateBy(workProcessTask.getCreateBy());
            processVO.setCreateTime(workProcessTask.getCreateTime());
            processVO.setUpdateBy(workProcessTask.getUpdateBy());
            processVO.setUpdateTime(workProcessTask.getUpdateTime());
            processVO.setDeleted(workProcessTask.getDeleted());

            // 设置product_id 产品bomid mes_product
            WorkOrder workOrder = workOrderMap.get(processVO.getOrderId());
            Optional.ofNullable(workOrder).orElseThrow(() -> new ILSBootException("P-AU-1003", processVO.getOrderNo(), processVO.getOrderId()));
            processVO.setProductId(workOrder.getProductId());
        }
    }

    private void printSort(List<AutoScheWorkProcessVO> resultWorkProcessTaskList) {
        log.info("print sort 最小换型时间顺序: start----------------------------------------------------------");
        for (AutoScheWorkProcessVO item : resultWorkProcessTaskList) {
            log.info("print sort fullItem: {}, fullProcess: {}, workFlowType: {}, orderId: {}, orderNo: {}, processId: {}", item.getFullItem(), item.getFullProcess(), item.getWorkflowType(), item.getOrderId(), item.getOrderNo(), item.getProcessId());
        }
        log.info("print sort 最小换型时间顺序: over----------------------------------------------------------");
    }

    /**
     * 大组间划分数据
     *
     * @param key                       当前大组key yyyy-MM-dd 最后一组为 null
     * @param currGroupList             当前大组结果集list
     * @param resultWorkProcessTaskList 结果集list
     * @param autoRuleConfigure         用户传入规则
     * @author niushuai
     * @date: 2021/11/9 14:04:47
     * @return: void
     */
    private void bigGroupItemsRun(String key, List<TempAutoScheProcessVO> currGroupList, List<AutoScheWorkProcessVO> resultWorkProcessTaskList, ScheduleAutoRuleConfigure autoRuleConfigure) {
        log.info("bigGroupItemsRun -> key: {}, size: {}", key, currGroupList.size());

        // 大组内的年月日是相等的, 看 itemId 即产出物料一致且为同一工艺路线的再分为一中组
        // bigGroup_物料id_物料code_工艺id_工艺code
        // Map<String, List<TempAutoScheProcessVO>> bigGroupItemsListMap = currGroupList.stream().collect(
        //         Collectors.groupingBy(item -> BIG_GROUP + SymbolsEnum.AT_3.getSymbol()
        //                 + item.getItemId() + SymbolsEnum.AT_3.getSymbol()
        //                 + item.getItemCode() + SymbolsEnum.AT_3.getSymbol()
        //                 + item.getRouteId() + SymbolsEnum.AT_3.getSymbol()
        //                 + item.getRouteCode()));

        Map<String, List<TempAutoScheProcessVO>> bigGroupItemsListMap = currGroupList.stream().collect(
                Collectors.groupingBy(item -> StrUtil.join(SymbolsEnum.AT_3.getSymbol(), BIG_GROUP
                        , item.getItemId(), item.getItemCode()
                        , item.getRouteId(), item.getRouteCode())));

        // 将map转换为list 此时未进行排序, 仅仅是转换为GroupItemDTO的list
        List<GroupItemDTO> bigGroupItemsDTOList = new ArrayList<>(bigGroupItemsListMap.size());
        Set<String> bigGroupKeySet = bigGroupItemsListMap.keySet();

        bigGroupKeySet.forEach(itemKey -> {
            // itemKey = bigGroup_物料id_物料code_工艺id_工艺code
            List<TempAutoScheProcessVO> list = bigGroupItemsListMap.get(itemKey);
            bigGroupItemsDTOList.add(new GroupItemDTO(BIG_GROUP, itemKey, list));
        });

        /*
            将大组分为物料相同且为同一工艺路线的中组
             先进行中组与中组之间的排序
             中组与中组之间的排序
                按照用户规则进行排序
         */
        // 拼接 sql 利用 union all
        List<String> sortedKeyList = null;
        if (1 == bigGroupItemsDTOList.size()) {
            sortedKeyList = Collections.singletonList(bigGroupItemsDTOList.get(0).getMapKey());
        } else {
            sortedKeyList = this.useSqlSort(bigGroupItemsDTOList, autoRuleConfigure, BIG_GROUP);
        }

        // 完成中组之间的排序 遍历进行中组之内的排序
        sortedKeyList.forEach(itemKey -> {
            // 此处itemKey是有序的 不同于上面的 mapKeySet.forEach 因此此处重新赋值bigGroupItemsDTOList也会变成有序的list
            List<TempAutoScheProcessVO> list = bigGroupItemsListMap.get(itemKey);

            // 得到有序的各个中组list 再通过循环进行中组内各小组的排序
            // 在每一个中组(相同产物, 相同工艺路线)内的排序 包含小组间与小组内的排序
            this.midGroupItemsRun(itemKey, list, resultWorkProcessTaskList, autoRuleConfigure);
        });
    }


    /**
     * 中组排序
     *
     * @param key                       key
     * @param currGroupList             当前组数据集合
     * @param resultWorkProcessTaskList 最终结果集
     * @param autoRuleConfigure         用户规则
     * @author niushuai
     * @date: 2021/11/11 17:06:35
     * @return: void
     */
    private void midGroupItemsRun(String key, List<TempAutoScheProcessVO> currGroupList, List<AutoScheWorkProcessVO> resultWorkProcessTaskList, ScheduleAutoRuleConfigure autoRuleConfigure) {
        log.info("midGroupItemsRun -> key: {}, size: {}", key, currGroupList.size());

        // 中组内排序 即中组划分为小组(此时已经为同一物料同一工艺的数据，按照工序划分得到n个小组)后 对小组间与小组内的排序
        // Map<String, List<TempAutoScheProcessVO>> midGroupItemsListMap = currGroupList.stream().collect(
        //         Collectors.groupingBy(item -> MID_GROUP + SymbolsEnum.AT_3.getSymbol()
        //                 + item.getItemId() + SymbolsEnum.AT_3.getSymbol()
        //                 + item.getItemCode() + SymbolsEnum.AT_3.getSymbol()
        //                 + item.getRouteId() + SymbolsEnum.AT_3.getSymbol()
        //                 + item.getRouteCode() + SymbolsEnum.AT_3.getSymbol()
        //                 + item.getSeq()));

        Map<String, List<TempAutoScheProcessVO>> midGroupItemsListMap = currGroupList.stream().collect(
                Collectors.groupingBy(item -> StrUtil.join(SymbolsEnum.AT_3.getSymbol(), MID_GROUP
                        , item.getItemId(), item.getItemCode()
                        , item.getRouteId(), item.getRouteCode()
                        , item.getSeq())));

        // 小组间采用工序顺序排序
        // 将map转换为list 此时未进行排序, 仅仅是转换为GroupItemDTO的list
        List<GroupItemDTO> midGroupItemsDTOList = new ArrayList<>(midGroupItemsListMap.size());
        midGroupItemsListMap.keySet().forEach(itemKey -> {
            // itemKey = midGroup_物料id_物料code_工艺id_工艺code_工序顺序seq
            List<TempAutoScheProcessVO> list = midGroupItemsListMap.get(itemKey);
            midGroupItemsDTOList.add(new GroupItemDTO(MID_GROUP, itemKey, list));
        });

        // 将小组间顺序按照工艺顺序排列好 工艺顺序升序
        Collections.sort(midGroupItemsDTOList);

        // 小组内采用用户规则排序
        midGroupItemsDTOList.forEach(item -> this.inGroupItemsRun(
                StrUtil.join(SymbolsEnum.AT_3.getSymbol(), IN_GROUP,
                        item.getItemId(), item.getItemCode(),
                        item.getRouteId(), item.getRouteCode(),
                        item.getSeq()),
                item.getList(), resultWorkProcessTaskList, autoRuleConfigure));
    }

    /**
     * 小组内排序
     * <br/>
     * 组内数据有个共性是物料相同, 工艺相同, 工序相同, 不同的是 所属工单 orderId不同
     *
     * @param itemKey                   当前组key
     * @param currGroupList             小组内的数据集合
     * @param resultWorkProcessTaskList 最终结果集合
     * @param autoRuleConfigure         用户输入规则
     * @author niushuai
     * @date: 2021/11/9 16:18:00
     * @return: void
     */
    private void inGroupItemsRun(String itemKey, List<TempAutoScheProcessVO> currGroupList, List<AutoScheWorkProcessVO> resultWorkProcessTaskList, ScheduleAutoRuleConfigure autoRuleConfigure) {
        // 小组内排序 仍然采用拼接sql来进行排序
        log.info("inGroupItemsRun itemKey: {}", itemKey);

        // 直接拼接sql 查询即可, 不再需要额外操作了
        List<GroupItemDTO> inGroupDTOList = new ArrayList<>(currGroupList.size());
        for (int i = 0; i < currGroupList.size(); i++) {
            TempAutoScheProcessVO item = currGroupList.get(i);

            // 用工序任务id做key
            inGroupDTOList.add(new GroupItemDTO(IN_GROUP, StrUtil.join(SymbolsEnum.AT_3.getSymbol(), IN_GROUP, item.getProcessTaskId()), Arrays.asList(item)));
        }

        // 已排序的工序任务id
        List<String> sortedKeyList = null;
        if (1 == inGroupDTOList.size()) {
            sortedKeyList = Collections.singletonList(inGroupDTOList.get(0).getMapKey());
        } else {
            sortedKeyList = this.useSqlSort(inGroupDTOList, autoRuleConfigure, IN_GROUP);
        }

        // 将currGroupList转换为 Map<processTaskId, TempAutoScheProcessVO>
        Map<String, TempAutoScheProcessVO> groupMap = currGroupList.stream().collect(Collectors.toMap(
                item -> StrUtil.join(SymbolsEnum.AT_3.getSymbol(), IN_GROUP, item.getProcessTaskId()), Function.identity()));

        for (String key : sortedKeyList) {
            // 复制属性
            TempAutoScheProcessVO tempAutoScheProcessVO = groupMap.get(key);
            AutoScheWorkProcessVO target = new AutoScheWorkProcessVO();
            BeanUtil.copyProperties(tempAutoScheProcessVO, target);
            target.setId(tempAutoScheProcessVO.getProcessTaskId());
            target.setWorkflowType(WorkflowTypeEnum.matchWorkflowType(tempAutoScheProcessVO.getRouteType()).getValue());
            // 添加到最终结果集中 此处添加的已经为有序添加
            resultWorkProcessTaskList.add(target);
        }
    }

    /**
     * 拼接sql 然后返回key的顺序
     *
     * @param groupItemDTOList  数据集合
     * @param autoRuleConfigure 配置规则
     * @param tmpTableName      临时表名称
     * @author niushuai
     * @date: 2021/11/9 17:10:22
     * @return: {@link List<String>}
     */
    private List<String> useSqlSort(List<GroupItemDTO> groupItemDTOList, ScheduleAutoRuleConfigure autoRuleConfigure, String tmpTableName) {

        if (CollectionUtil.isEmpty(groupItemDTOList)) {
            throw new ILSBootException("P-AU-1005", tmpTableName);
        }

        StringBuilder builder = new StringBuilder();

        for (int i = 0; i < groupItemDTOList.size(); i++) {
            GroupItemDTO dto = groupItemDTOList.get(i);
            // select 'mapKey' mapKey, 'priorityAvg' priorityAvg, 'planStartTimeAvg' planStartTimeAvg, 'planEndTimeAvg' planEndTimeAvg from dual
            builder.append(SELECT)
                    .append(completeSymbol(dto.getMapKey(), true))
                    .append(MAP_KEY)
                    .append(SymbolsEnum.COMMA.getSymbol())

                    .append(completeSymbol(dto.getPriorityAvg(), false))
                    .append(PRIORITY_AVG)
                    .append(SymbolsEnum.COMMA.getSymbol())

                    .append(completeSymbol(dto.getPlanStartTimeAvg(), false))
                    .append(PLAN_START_TIME_AVG)
                    .append(SymbolsEnum.COMMA.getSymbol())

                    .append(completeSymbol(dto.getPlanEndTimeAvg(), false))
                    .append(PLAN_END_TIME_AVG)

                    .append(FROM_).append(DUAL);

            // 最后一个不再添加union all关键字
            if (i != groupItemDTOList.size() - 1) {
                builder.append(UNION_ALL);
            }
        }

        log.info("buildSql: {}", builder.toString());

        String orderSql = this.buildOrderSql(autoRuleConfigure);

        // final sql 外层包裹一层 查询
        StringBuilder finalBuilder = new StringBuilder();
        finalBuilder.append(SELECT)
                .append(MAP_KEY)
                .append(FROM_)
                .append(SymbolsEnum.BRACKETS_LEFT.getSymbol())
                .append(builder.toString())
                .append(orderSql)
                .append(SymbolsEnum.BRACKETS_RIGHT.getSymbol())
                .append(tmpTableName);

        String sql = finalBuilder.toString();
        log.info("finalSql: {}", sql);

        // 执行拼接的sql
        List<String> sortedMapKeyList = workOrderMapper.executeSql(sql);
        log.info("sorted key list: {}", sortedMapKeyList);
        return sortedMapKeyList;
    }


    /**
     * 按照用户传入的规则构造order by语句
     *
     * @param autoRuleConfigure 用户传入规则
     * @author niushuai
     * @date: 2021/11/9 15:29:58
     * @return: {@link String} order by 语句
     */
    private String buildOrderSql(ScheduleAutoRuleConfigure autoRuleConfigure) {
        StringBuilder orderBuilder = new StringBuilder(ORDER_BY);
        String[] orderRules = autoRuleConfigure.getOrderRule().split(MesCommonConstant.COMMA_SIGN);
        for (String orderRule : orderRules) {
            // 1 按工单优先级降序；
            // 2 按工单计划时间升序；
            // 3 按工单计划结束时间升序。
            AutoScheOrderRuleEnum type = AutoScheOrderRuleEnum.getRuleEnum(orderRule);
            switch (type) {
                case PRIORITY_RULE:
                    // 按工单优先级降序
                    orderBuilder.append(PRIORITY_AVG).append(DESC).append(SymbolsEnum.COMMA.getSymbol());
                    // queryWrapper.orderByDesc("a.level");
                    break;
                case STARTTIME_ASE_RULE:
                    // 按工单计划开始时间升序
                    orderBuilder.append(PLAN_START_TIME_AVG).append(ASC).append(SymbolsEnum.COMMA.getSymbol());
                    // queryWrapper.orderByAsc("IF(ISNULL(a.plan_start_time), 1, 0)");
                    // queryWrapper.orderByAsc("a.plan_start_time");
                    break;
                case ENDTIME_ASE_RULE:
                    // 按工单计划结束时间升序
                    orderBuilder.append(PLAN_END_TIME_AVG).append(ASC).append(SymbolsEnum.COMMA.getSymbol());
                    // queryWrapper.orderByAsc("IF(ISNULL(a.plan_end_time), 1, 0)");
                    // queryWrapper.orderByAsc("a.plan_end_time");
                    break;
                default:
            }
        }

        String orderSql = orderBuilder.toString();
        // 去除最后一个逗号
        if (orderSql.endsWith(SymbolsEnum.COMMA.getSymbol())) {
            orderSql = orderSql.substring(0, orderSql.length() - 1);
        }
        log.info("orderSql: {}", orderSql);
        return orderSql;
    }

    private String completeSymbol(Object key, boolean flag) {
        if (flag) {
            return SymbolsEnum.SPACE.getSymbol() + SymbolsEnum.SINGLE_LINK.getSymbol() + key + SymbolsEnum.SINGLE_LINK.getSymbol() + SymbolsEnum.SPACE.getSymbol();
        } else {
            return SymbolsEnum.SPACE.getSymbol() + key + SymbolsEnum.SPACE.getSymbol();
        }
    }

    /**
     * 填充工序任务关联工单字段到工序任务对象上
     *
     *
     * @param workOrderList
     * @param workOrderMap
     * @param workOrderLineMap
     * @param pageWorkProcessTaskList
     * @author niushuai
     * @date: 2021/11/8 17:38:35
     * @return: void
     */
    private void fillTempFieldVal(List<WorkOrder> workOrderList, Map<String, WorkOrder> workOrderMap, Map<String, RouteVO> workOrderLineMap, List<TempAutoScheProcessVO> pageWorkProcessTaskList) {

        Map<String, String> bomVersionMap = new HashMap<>();
        List<String> productIdList = workOrderList.stream().filter(item -> StrUtil.isNotEmpty(item.getProductId())).map(WorkOrder::getProductId).collect(Collectors.toList());
        if (CollUtil.isNotEmpty(productIdList)) {
            List<Product> productList = productMapper.selectBatchIds(productIdList);
            bomVersionMap = productList.stream().collect(Collectors.toMap(Product::getId, Product::getVersion));
        }

        // 填充相关字段
        for (TempAutoScheProcessVO item : pageWorkProcessTaskList) {
            // 计划开始时间 计划结束时间 工单优先级
            this.workOrderFieldSet(workOrderMap, item);
            // 工艺路线相关字段
            this.workOrderLineFieldSet(workOrderLineMap, bomVersionMap, item);
        }
    }

    private void workOrderLineFieldSet(Map<String, RouteVO> routeMap, Map<String, String> bomVersionMap, TempAutoScheProcessVO item) {

        // 按照workFlowType分为 1-工艺 2-工艺+BOM 3-bom
        // 其中1和2 是按照工艺路线id  route_id
        // 3是按照 product_id
        if (WorkflowTypeEnum.PRODUCT_BOM.getCode().equals(item.getRouteType())) {
            // 为3的时候 将product_id 视为 route_id 且由于之前已经赋值 此处跳过即可
            item.setRouteCode(bomVersionMap.getOrDefault(item.getRouteId(), "bom_version"));
            return;
        }

        RouteVO routeVO = routeMap.get(item.getOrderId());
        item.setRouteId(routeVO.getId());
        item.setRouteCode(routeVO.getRouteCode());
        item.setRouteName(routeVO.getRouteName());
    }

    private void workOrderFieldSet(Map<String, WorkOrder> workOrderMap, TempAutoScheProcessVO item) {
        WorkOrder workOrder = workOrderMap.get(item.getOrderId());
        Date planStartTime = workOrder.getPlanStartTime();
        Date planEndTime = workOrder.getPlanEndTime();
        if (null != planStartTime) {
            // 计划开始时间
            DateTime ofStart = DateTime.of(planStartTime);
            item.setPlanStartTime(ofStart);
            item.setPlanStartTimeDate(ofStart.toDateStr());
            item.setPlanStartTimeTime(ofStart.toTimeStr());
        }
        if (null != planEndTime) {
            // 计划结束时间
            DateTime ofEnd = DateTime.of(planEndTime);
            item.setPlanEndTime(ofEnd);
            item.setPlanEndTimeDate(ofEnd.toDateStr());
            item.setPlanEndTimeTime(ofEnd.toTimeStr());
        }
        // 当前工序任务所属工单优先级
        if (null != workOrder.getLevel()) {
            item.setLevel(Long.valueOf(workOrder.getLevel()));
        } else {
            // level为空设置为 -100w 最低优先级
            item.setLevel(-ONE_MILLION);
        }

        item.setRouteType(WorkflowTypeEnum.matchCode(workOrder.getWorkflowType()).getCode());
        item.setRouteId(workOrder.getProductId());
    }

    /**
     * 转换为临时数据的vo AutoScheProcessVO to TempAutoScheProcessVO
     *
     * @param lstAutoScheProcessVO
     * @author niushuai
     * @date: 2021/11/8 17:28:43
     * @return: {@link List<TempAutoScheProcessVO>}
     */
    private List<TempAutoScheProcessVO> convertTempList(List<AutoScheProcessVO> lstAutoScheProcessVO) {

        List<TempAutoScheProcessVO> resultList = new ArrayList<>();

        for (AutoScheProcessVO autoScheProcessVO : lstAutoScheProcessVO) {
            TempAutoScheProcessVO target = new TempAutoScheProcessVO();
            BeanUtil.copyProperties(autoScheProcessVO, target);
            resultList.add(target);
        }

        return resultList;
    }

    /**
     * 小组VO对象
     *
     * @author niushuai
     * @date: 2021/11/8 17:15:32
     */
    @Data
    private static class GroupItemDTO implements Comparable<GroupItemDTO> {

        private String type;

        /**
         * mapKey
         */
        private String mapKey;

        /**
         * 工艺id
         */
        private String routeId;

        /**
         * 工艺编码
         */
        private String routeCode;

        /**
         * 物料id
         */
        private String itemId;

        /**
         * 物料编码
         */
        private String itemCode;

        /**
         * 工序顺序
         */
        private String seq;

        /**
         * 平均优先级
         */
        private Double priorityAvg = 0d;

        /**
         * 平均开始时间
         */
        private Double planStartTimeAvg = 0d;

        /**
         * 平均结束时间
         */
        private Double planEndTimeAvg = 0d;

        /**
         * 小组内的工序任务集合
         */
        private List<TempAutoScheProcessVO> list;

        public GroupItemDTO(String type, String mapKey, List<TempAutoScheProcessVO> list) {
            this.type = type;
            this.mapKey = mapKey;
            this.list = list;
            this.splitMapKey();
            this.init();
        }

        /**
         * 拆分规则并赋值
         *
         * @author niushuai
         * @date: 2021/11/9 10:53:42
         * @return: void
         */
        private void splitMapKey() {

            switch (this.type) {
                case BIG_GROUP:
                    // bigGroup_物料id_物料code_工艺id_工艺code
                    List<String> bigSplitTrim = StrUtil.splitTrim(this.mapKey, SymbolsEnum.AT_3.getSymbol(), 5);
                    this.setItemId(bigSplitTrim.get(1));
                    this.setItemCode(bigSplitTrim.get(2));
                    this.setRouteId(bigSplitTrim.get(3));
                    this.setRouteCode(bigSplitTrim.get(4));
                    break;
                case MID_GROUP:
                    // midGroup_物料id_物料code_工艺id_工艺code_seq
                    List<String> midSplitTrim = StrUtil.splitTrim(this.mapKey, SymbolsEnum.AT_3.getSymbol(), 6);
                    this.setItemId(midSplitTrim.get(1));
                    this.setItemCode(midSplitTrim.get(2));
                    this.setRouteId(midSplitTrim.get(3));
                    this.setRouteCode(midSplitTrim.get(4));
                    this.setSeq(midSplitTrim.get(5));
                    break;
                case IN_GROUP:
                    // inGroup_processKey 不拆分即可
                    break;
                default:
                    log.warn("未知的group type: {}", this.type);
            }


        }

        /**
         * 初始化三条数据
         *
         * @author niushuai
         * @date: 2021/11/8 17:21:22
         * @return: void
         */
        private void init() {
            if (CollectionUtil.isNotEmpty(list)) {

                long level = 0, startUseNum = 0, endUseNum = 0;
                for (TempAutoScheProcessVO item : list) {
                    level += item.getLevel();
                    // 时间非空才进行计算 否则计算为0
                    if (StrUtil.isNotEmpty(item.getPlanStartTimeTime())) {
                        startUseNum += DateUtil.timeToSecond(item.getPlanStartTimeTime());
                    } else {
                        // 为空 加上100w 防止为空出现0 然后在升序时将该数据出现在前面
                        startUseNum += ONE_MILLION;
                    }
                    if (StrUtil.isNotEmpty(item.getPlanEndTimeTime())) {
                        endUseNum += DateUtil.timeToSecond(item.getPlanEndTimeTime());
                    } else {
                        endUseNum += ONE_MILLION;
                    }
                }

                this.setPriorityAvg(NumUtil.avg(String.valueOf(level), list.size(), 2));
                this.setPlanStartTimeAvg(NumUtil.avg(String.valueOf(startUseNum), list.size(), 2));
                this.setPlanEndTimeAvg(NumUtil.avg(String.valueOf(endUseNum), list.size(), 2));
            }
        }

        @Override
        public int compareTo(GroupItemDTO o) {
            return Integer.valueOf(this.getSeq()) - Integer.valueOf(o.getSeq());
        }
    }

}
