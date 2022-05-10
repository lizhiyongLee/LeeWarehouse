package com.ils.modules.mes.constants;

import java.util.HashMap;

/**
 * @Description: mes 通用常量类
 * @author: fengyi
 * @date: 2020年11月3日 下午2:56:50
 */

public class MesCommonConstant {

    /**
     * 策略关联时间单位1.分钟
     */
    public static final String POLICY_TIME_UNIT_ONE = "1";
    /**
     * 策略关联时间单位2.小时
     */
    public static final String POLICY_TIME_UNIT_TWO = "2";
    /**
     * 策略关联时间单位3.日
     */
    public static final String POLICY_TIME_UNIT_THREE = "3";
    /**
     * 策略关联时间单位4.周
     */
    public static final String POLICY_TIME_UNIT_FOUR = "4";
    /**
     * 策略关联时间单位5.月
     */
    public static final String POLICY_TIME_UNIT_FIVE = "5";
    /**
     * 策略状态1 启用
     */
    public static final String POLICY_STATUS_ONE = "1";
    /**
     * 策略状态 0 停用
     */
    public static final String POLICY_STATUS_ZERO = "0";
    /**
     * 派工任务编号生成编码
     */
    public static final String PLAN_TASK_NO = "mesPlanTaskNumber";
    /**
     * 销售订单生成编码
     */
    public static final String SALE_ORDER_NO = "mesSaleOrderNumber";
    /**
     * 工单批号生成编码
     */
    public static final String MES_WORK_ORDER_NO = "mesWorkOrderNo";
    /**
     * 工单批号生成编码
     */
    public static final String WORK_ORDER_BATCH_NO = "mesWorkOrderBatch";
    /**
     * 标准产能编码
     */
    public static final String STANDARD_CODE = "mesStandardCode";
    /**
     * 动态准备时间编码
     */
    public static final String PREPARE_TIME_CODE = "mesPrepareTimeCode";
    /**
     * 派工任务指定用户
     */
    public static final String PLAN_TASK_USER_TYPE = "2";
    /**
     * 设备标注分类 1.设备分类
     */
    public static final String LABEL_TYPE_ONE = "1";
    /**
     * 设备维修中的设备标注分类 2.设备等级
     */
    public static final String LABEL_TYPE_TWO = "2";
    /**
     * 维修详情中 维修维修标识
     */
    public static final String MACHINE_REPAIR_TASK = "维修";
    /**
     * 维修任务日志记录中 日志类型
     */
    public static final String MACHINE_REPAIR_LOG_TYPE_ONE = "新建任务";
    /**
     * 错误提示
     */
    public static final String MESSAGE_TIP = "1";
    /**
     * 确认信息
     */
    public static final String MESSAGE_CONFIRM = "2";
    /**
     * 没有消息提示
     */
    public static final String MESSAGE_NO = "3";
    /**
     * 维保任务中时间单位 1：分钟
     */
    public static final String MAINTENANECE_TASK_PLAN_TIME_ONE = "分钟";
    /**
     * 维保任务中时间单位 2.小时
     */
    public static final String MAINTENANECE_TASK_PLAN_TIME_TWO = "小时";
    /**
     * 维保任务中时间单位 3.日
     */
    public static final String MAINTENANECE_TASK_PLAN_TIME_THREE = "日";
    /**
     * 维保任务中时间单位 4.周
     */
    public static final String MAINTENANECE_TASK_PLAN_TIME_FOUR = "周";
    /**
     * 维保任务中时间单位 5.月
     */
    public static final String MAINTENANECE_TASK_PLAN_TIME_FIVE = "月";
    /**
     * 保养任务和点检任务中的"每"
     */
    public static final String MAINTENANECE_TASK_WORD_MEI = "每";
    /**
     * 1。点检任务
     */
    public static final String MAINTENANECE_TASK_TYPE_SPOT_CHECK = "1";
    /**
     * 2。保养任务
     */
    public static final String MAINTENANECE_TASK_TYPE_MAINTENANECE = "2";
    /**
     * 1.关联仓位用来判断是否进行添加的标志
     */
    public static final String STORAGE_RELARE_AREA_ADD_ONE = "1";
    /**
     * 2 节点类型,Nodetype=2
     */
    public static final String NODE_TYPE_TWO = "2";
    /**
     * 3 节点类型为,Nodetype=3
     */
    public static final String NODE_TYPE_THREE = "3";
    /**
     * 4 节点类型,Nodetype=4
     */
    public static final String NODE_TYPE_FOUR = "4";
    /**
     * 生成任务排他类型配置编码
     */
    public static final String PRODUCE_TASK_TYPE_SWITCH = "ProduceTaskTypeSwitch";
    /**
     * 任务未开始允许投产
     */
    public static final String PRODUCE_TASK_NOTSTART_OPERATION_SWITCH = "ProduceTaskNotStartOperationSwitch";
    /**
     * 是否允许超投
     */
    public static final String PRODUCE_TASK_EXCEED_OPERATION_SWITCH = "ProduceTaskExceedOperationSwitch";
    /**
     * 任务报工不能超出计划
     */
    public static final String PRODUCE_TASK_EXCEED_PLAN_SWITCH = "ProduceTaskExceedPlanSwitch";
    /**
     * 前工序产出校验
     */
    public static final String PRODUCE_TASK_ADJUST_PREPROCESS_SWITCH = "ProduceTaskAdjustPreProcessSwitch";
    /**
     * 非首工序允许不合格产出
     */
    public static final String PRODUCE_RECORD_NOTFIRST_PREPROCESS_SWITCH = "ProduceRecordNotFirstProcessSwitch";
    /**
     * 开启校验前工序标签码报工数量
     */
    public static final String PRODUCE_RECORD_QRCODE_ADJUST_PREPROCESS_SWITCH =
            "ProduceRecordQrcodeAdjustPreProcessSwitch";
    /**
     * 设备状态： 启用
     */
    public static final String MACHINE_STATUS_ONE = "1";
    /**
     * 设备状态：停用
     */
    public static final String MACHINE_STATUS_ZERO = "0";
    /**
     * 设备日志类型  1.添加
     */
    public static final String MACHINE_TYPE_ADD = "1";
    /**
     * 设备日志类型  2.启用
     */
    public static final String MACHINE_TYPE_ENABLE = "2";
    /**
     * 设备日志类型  3.停用
     */
    public static final String MACHINE_TYPE_DISABLE = "3";
    /**
     * 设备日志类型  4.删除
     */
    public static final String MACHINE_TYPE_DELETE = "4";
    /**
     * 设备日志类型 5.修改
     */
    public static final String MACHINE_TYPE_UPDATE = "5";
    /**
     * 设备日志类型 6.创建维保任务
     */
    public static final String MACHINE_MAINTENANCE_TASK_ADD = "6";
    /**
     * 设备日志类型 7.执行维保任务
     */
    public static final String MACHINE_MAINTENANCE_TASK_EXECUTE = "7";
    /**
     * 设备日志类型 8.创建维修任务
     */
    public static final String MACHINE_REPAIR_TASK_ADD = "8";
    /**
     * 设备日志类型 9.执行维修任务
     */
    public static final String MACHINE_REPAIR_TASK_EXECUTE = "9";
    /**
     * 设备维修编码
     */
    public static final String MACHINE_REPAIR_TASK_CODE = "mesMachineRepairNumber";
    /**
     * 设备维护任务编码
     */
    public static final String MACHINE_MAINTENANCE_CODE = "mesMachineMaintenanceNumber";
    /**
     * 计划性停机 1
     */
    public static final String MACHINE_PLAN_STOP_TIME_NUMBER = "1";
    /**
     * 实际停机 2
     */
    public static final String MACHINE_REAL_STOP_TIME_NUMBER = "2";
    /**
     * 计划性停机
     */
    public static final String MACHINE_PLAN_STOP_TIME = "计划性停机";
    /**
     * 实际停机
     */
    public static final String MACHINE_REAL_STOP_TIME = "实际停机";
    /**
     * 报工产出页面
     */
    public static final String TASK_RECORD_PAGE_TYPE_ADD = "0";
    /**
     * 报工编辑页面
     */
    public static final String TASK_RECORD_PAGE_TYPE_EDIT = "1";
    /**
     * 报工标记页面
     */
    public static final String TASK_RECORD_PAGE_TYPE_FLAG = "2";
    /**
     * 字符串连接
     */
    public static final String FILED_JOIN = "/";
    /**
     * 逗号常量
     */
    public static final String COMMA_SIGN = ",";
    /**
     * 空格常量
     */
    public static final String SPACE_SIGN = " ";
    /**
     * 入库单号编码
     */
    public static final String SPARE_PARTS_RECEIPT_NO = "mesSparePartsReceiptAndSendCode";
    /**
     * 质检任务编码
     */
    public static final String QC_TASK_CODE = "mesQcTaskCode";
    /**
     * 库存出入库事务全局开关
     */
    public static final String INVENTORY_STORAGE_EVENT_RELATE_SWITH =
            "InventoryStorageEventRelateSwitch";
    /**
     * 标签出入库事务开关
     */
    public static final String TAG_STORAGE_EVENT_RELATE_SWITH =
            "TagStorageEventRelateSwitch";
    /**
     * 工单完工检查其下任务是否全都结束的开关
     */
    public static final String WORK_ORDER_FINISH_CHECK_SWITCH = "WorkOrderFinishCheckSwitch";
    /**
     * 向前查找工作日期天数
     */
    public static final String PREVIOUS_WORK_CALENDAR_DAYS = "PreviousWorkCalendarDays";
    /**
     * 新建工单通知业务模块编码
     */
    public static final String MSG_WORK_ORDER = "workOrder";
    /**
     * 工单审核通知业务模块编码
     */
    public static final String MSG_AUDIT_WORK_ORDER = "auditWorkOrder";
    /**
     * 新建生产任务通知业务模块编码
     */
    public static final String MSG_WORK_PRODUCE_TASK = "workProduceTask";
    /**
     * 物料安全库存通知业务模块编码
     */
    public static final String MSG_ITEM_SAFETY_STOCK = "itemSafetyStock";
    /**
     * 物料单元有效期预警通知业务模块编码
     */
    public static final String MSG_ITEM_CELL_VALID = "itemCellValid";
    /**
     * 物料单元有效期预警通知业务模块编码
     */
    public static final String MSG_REPAIR_TASK_NOTICE = "repairTaskNotice";
    /**
     * 新建质检任务通知业务模块编码
     */
    public static final String MSG_QC_TASK = "qcTask";
    /**
     * 新建质检任务（无工位）通知业务模块编码
     */
    public static final String MSG_QC_TASK_NO_STATION = "qcTaskNoStation";
    /**
     * 新建质检任务审核通知业务模块编码
     */
    public static final String MSG_AUDIT_QC_TASK = "auditQcTask";
    /**
     * sop步骤下一步通知业务模块编码
     */
    public static final String MSG_SOP_STEP = "sopStep";
    /**
     * 消息通知业务编码
     */
    public static final String INFORM = "inform";
    public static final HashMap<String, String> LABEL_IN = new HashMap<>();
    public static final HashMap<String, String> STORAGE_IN = new HashMap<>();
    public static final HashMap<String, String> FEED = new HashMap<>();
    public static final HashMap<String, String> OUTPUT = new HashMap<>();
    public static final HashMap<String, String> QC = new HashMap<>();
    public static final HashMap<String, String> LABEL_OUT = new HashMap<>();
    public static final HashMap<String, String> STORAGE_OUT = new HashMap<>();
    public static final HashMap<String, String> REPORT_TEMPLATE = new HashMap<>();
    public static final HashMap<String, String> JOINT_PRODUCT = new HashMap<>();
    public static final HashMap<String, String> PARA_TEMPLATE = new HashMap<>();
    /**
     * 工艺路线中工序第一道工序前置工序填充字段
     */
    public static String ROUTE_PROCESS_FIRST = "first";
    /**
     * 工艺路线中工序最后工序后续工序填充字段
     */
    public static String ROUTE_PROCESS_END = "end";
    /**
     * 仓位定义判断父节点id是否为零的代替字段
     */
    public static String WAER_STORAGE_ZERO = "0";
    /**
     * 设备管理中维修任务占用中维修类型填充字段
     */
    public static String MACHINE_REPARIE = "维修";
    /**
     * 维保定时任务
     */
    public static String SS_MM_HH_DD_MM_YYYY = "ss mm HH dd MM ? yyyy";
    /**
     * 定时任务状态
     */
    public static Integer STATUS_NORMAL = 0;
    /**
     * 定时任务计划执行人-定时任务创建
     */
    public static String MAINTENANECE_TASK_MAN = "定时任务创建";
    /**
     * 策略规则 1、固定周期
     */
    public static String POLICY_RULE_ONE = "1";
    /**
     * 策略规则 2、浮动周期
     */
    public static String POLICY_RULE_TWO = "2";
    /**
     * 策略规则 3、累计用度
     */
    public static String POLICY_RULE_THREE = "3";
    /**
     * 策略规则 4、固定用度
     */
    public static String POLICY_RULE_FOUR = "4";
    /**
     * 策略规则 5、手工创建
     */
    public static String POLICY_RULE_FIVE = "5";
    /**
     * 工单最小层数
     */
    public static Integer MIN_ORDER_LAYER = 1;
    /**
     * 工单最大层数
     */
    public static Integer MAX_ORDER_LAYER = 10;
    /**
     * 新增质检任务消息提醒,业务模块编码
     */
    public static String REPAIR_ADD_MODULE = "REPAIR_ADD_MODULE";
    /**
     * 新增质检任务消息提醒,业务操作编码
     */
    public static String REPAIR_ADD_OPERACTION = "REPAIR_ADD_OPERACTION";
    /**
     * 新增保养任务，业务模块编码
     */
    public static String MAINTENACE_ADD_MODULE = "MAINTENACE_ADD_MODULE";
    /**
     * 新增保养任务，业务操作编码
     */
    public static String MAINTENACE_ADD_OPERACTION = "MAINTENACE_ADD_OPERACTION";
    /**
     * 新增点检任务，业务模块编码
     */
    public static String CHECK_ADD_MODULE = "CHECK_ADD_MODULE";
    /**
     * 新增点检任务，业务操作编码
     */
    public static String CHECK_ADD_OPERACTION = "CHECK_ADD_OPERACTION";
    /**
     * SOP模板业务配置全局开关
     */
    public static final String SOP_TEMPLATE_SWITCH = "sopTemplateSwitch";
    /**
     * 工单号自动生成全局开关
     */
    public static final String WORK_ORDER_AUTO_NO_SWITCH = "WorkOrderAutoNoSwitch";

    /**
     * sop已领取过的质检控件任务
     */
    public static final String HAS_RECEIVED_QC_TASK = "1";
    /**
     * sop能领取质检任务控件
     */
    public static final String COULD_RECEIVED_QC_TASK = "2";
    /**
     * sop不能领取的质检任务控件
     */
    public static final String COULD_NOT_RECEIVED_QC_TASK = "3";

    static {
        LABEL_IN.put("1", "管控");
        LABEL_IN.put("2", "不管控");
        STORAGE_IN.put("1", "管控");
        STORAGE_IN.put("2", "不管控");

        LABEL_OUT.put("1", "管控");
        LABEL_OUT.put("2", "不管控");
        STORAGE_OUT.put("1", "管控");
        STORAGE_OUT.put("2", "不管控");
        FEED.put("1", "管控");
        FEED.put("2", "不管控");
        OUTPUT.put("2", "不管控");
        JOINT_PRODUCT.put("2", "不管控");
        QC.put("3", "首检");
        QC.put("4", "生产检");
        QC.put("5", "巡检");

        REPORT_TEMPLATE.put("0", "报告模板控件不需要控制逻辑");
        PARA_TEMPLATE.put("0", "报告模板控件不需要控制逻辑");

    }
}
