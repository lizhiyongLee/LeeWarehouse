package com.ils.modules.mes.qc.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ils.common.exception.ILSBootException;
import com.ils.common.system.base.entity.ILSEntity;
import com.ils.common.system.util.TenantContext;
import com.ils.common.system.vo.LoginUser;
import com.ils.modules.mes.base.factory.service.WorkShopService;
import com.ils.modules.mes.base.factory.vo.NodeVO;
import com.ils.modules.mes.base.material.entity.Item;
import com.ils.modules.mes.base.material.mapper.ItemMapper;
import com.ils.modules.mes.base.qc.entity.QcMethod;
import com.ils.modules.mes.base.qc.entity.QcMethodDetail;
import com.ils.modules.mes.base.qc.mapper.QcMethodDetailMapper;
import com.ils.modules.mes.base.qc.mapper.QcMethodMapper;
import com.ils.modules.mes.base.ware.entity.WareStorage;
import com.ils.modules.mes.base.ware.mapper.WareStorageMapper;
import com.ils.modules.mes.base.ware.service.WareStorageService;
import com.ils.modules.mes.base.ware.vo.WareStorageTreeVO;
import com.ils.modules.mes.config.service.DefineFieldValueService;
import com.ils.modules.mes.constants.MesCommonConstant;
import com.ils.modules.mes.constants.TableCodeConstants;
import com.ils.modules.mes.enums.*;
import com.ils.modules.mes.execution.entity.WorkProduceTask;
import com.ils.modules.mes.execution.mapper.WorkProduceTaskMapper;
import com.ils.modules.mes.execution.service.WorkProduceTaskService;
import com.ils.modules.mes.material.entity.ItemCell;
import com.ils.modules.mes.material.mapper.ItemCellMapper;
import com.ils.modules.mes.material.vo.DashBoardDataVO;
import com.ils.modules.mes.qc.entity.*;
import com.ils.modules.mes.qc.mapper.*;
import com.ils.modules.mes.qc.service.QcTaskService;
import com.ils.modules.mes.qc.vo.*;
import com.ils.modules.mes.util.CommonUtil;
import com.ils.modules.mes.util.MapKeyComparator;
import com.ils.modules.mes.util.TreeNode;
import com.ils.modules.message.service.MsgHandleServer;
import com.ils.modules.message.vo.MsgParamsVO;
import com.ils.modules.system.entity.User;
import com.ils.modules.system.mapper.UserMapper;
import com.ils.modules.system.service.CodeGeneratorService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @Description: ????????????
 * @Author: Conner
 * @Date: 2021-03-01
 * @Version: V1.0
 */
@Service
public class QcTaskServiceImpl extends ServiceImpl<QcTaskMapper, QcTask> implements QcTaskService {

    private static final String[] IGNORE_STRING = {"id", "createBy", "createTime", "updateBy", "updateTime", "deleted"};
    @Autowired
    private QcTaskRelateTotalMapper qcTaskRelateTotalMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private WareStorageMapper wareStorageMapper;
    @Autowired
    private QcTaskEmployeeMapper qcTaskEmployeeMapper;
    @Autowired
    private QcTaskReportMapper qcTaskReportMapper;
    @Autowired
    private QcMethodDetailMapper qcMethodDetailMapper;
    @Autowired
    private QcTaskItemStandardMapper qcTaskItemStandardMapper;
    @Autowired
    private ItemCellMapper itemCellMapper;
    @Autowired
    private CodeGeneratorService codeGeneratorService;
    @Autowired
    private QcTaskRelateSampleMapper qcTaskRelateSampleMapper;
    @Autowired
    private QcTaskReportItemValueMapper qcTaskReportItemValueMapper;
    @Autowired
    private QcMethodMapper qcMethodMapper;
    @Autowired
    private ItemMapper itemMapper;
    @Autowired
    private QcStateAjustRecordMapper qcStateAjustRecordMapper;
    @Autowired
    private WorkProduceTaskMapper workProduceTaskMapper;
    @Autowired
    private MsgHandleServer msgHandleServer;
    @Autowired
    private DefineFieldValueService defineFieldValueService;
    @Autowired
    private WareStorageService wareStorageService;
    @Autowired
    private WorkShopService workShopService;
    @Autowired
    @Lazy
    private WorkProduceTaskService workProduceTaskService;

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public QcTask saveQcTask(QcTaskSaveVO qcTaskSaveVO) {
        //??????????????????
        QcTask qcTask = new QcTask();
        BeanUtils.copyProperties(qcTaskSaveVO, qcTask);
        //????????????????????????
        String qcTaskCode = codeGeneratorService.getNextCode(CommonUtil.getTenantId(), MesCommonConstant.QC_TASK_CODE, qcTask);
        qcTask.setTaskCode(qcTaskCode)
                .setExeStatus(QcTaskExeStatusEnum.NOT_START.getValue());
        //????????????????????????
        qcTask.setAuditStatus(AuditStatusEnum.AUDIT_NEW.getValue());
        //????????????????????????
        setQcTaskSampleAndTotalQty(qcTaskSaveVO.getQcTaskRelateTotalList(), qcTask);
        //?????????????????????????????????
        setRelatedTaskInfo(qcTask);
        //?????????????????????????????????
        setItemNameAndCode(qcTask);
        //????????????
        baseMapper.insert(qcTask);
        //????????????????????????Ids?????????
        String taskEmployeeIds = qcTaskSaveVO.getTaskEmployeeIds();
        List<User> userList = new ArrayList<>();
        //??????????????????????????????????????????????????????
        String[] ids = taskEmployeeIds.split(",");
        User checkUser = userMapper.selectById(ids[0]);
        //?????????????????????null?????????????????????
        if (StringUtils.isNoneBlank(taskEmployeeIds)) {
            QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
            if (ids.length == 1 && null == checkUser) {
                userQueryWrapper.eq("role_id", taskEmployeeIds);
            } else {
                userQueryWrapper.in("id", Arrays.asList(ids));
            }
            userList = userMapper.selectList(userQueryWrapper);
        }
        //??????????????????????????????,?????????????????????????????????????????????
        if (!CommonUtil.isEmptyOrNull(userList)) {
            for (User user : userList) {
                QcTaskEmployee qcTaskEmployee = new QcTaskEmployee();
                if (ids.length == 1) {
                    qcTaskEmployee.setUsingType(QcTaskEmployeeUsingTypeEnum.ASSIGNED_RECEIVE.getValue());
                } else {
                    qcTaskEmployee.setUsingType(QcTaskEmployeeUsingTypeEnum.DEFAULT.getValue());
                }
                qcTaskEmployee.setEmployeeCode(user.getOrgCode());
                qcTaskEmployee.setEmployeeId(user.getId());
                qcTaskEmployee.setEmployeeName(user.getRealname());
                qcTaskEmployee.setExcuteTaskId(qcTask.getId());
                qcTaskEmployeeMapper.insert(qcTaskEmployee);
            }
        }
        //????????????????????????,????????????????????????
        List<QcTaskRelateTotal> qcTaskRelateTotalList = qcTaskSaveVO.getQcTaskRelateTotalList();
        if (!CommonUtil.isEmptyOrNull(qcTaskRelateTotalList)) {
            qcTaskRelateTotalList.forEach(qcTaskRelateTotal -> {
                //??????????????????id
                qcTaskRelateTotal.setQcTaskId(qcTask.getId());
                //????????????????????????????????????
                qcTaskRelateTotalMapper.insert(qcTaskRelateTotal);
            });
        }
        //???????????????????????????????????????????????????
        LambdaQueryWrapper<QcMethodDetail> qcMethodDetailLambdaQueryWrapper = new LambdaQueryWrapper<>();
        qcMethodDetailLambdaQueryWrapper.eq(QcMethodDetail::getMehtodId, qcTask.getMethodId());
        List<QcMethodDetail> qcMethodDetails = qcMethodDetailMapper.selectList(qcMethodDetailLambdaQueryWrapper);
        for (QcMethodDetail qcMethodDetail : qcMethodDetails) {
            QcTaskItemStandard qcTaskItemStandardForSave = new QcTaskItemStandard();
            //create_by , create_time , update_by , update_time , is_deleted
            BeanUtils.copyProperties(qcMethodDetail, qcTaskItemStandardForSave, IGNORE_STRING);
            //????????????Id
            qcTaskItemStandardForSave.setQcTaskId(qcTask.getId());
            qcTaskItemStandardMapper.insert(qcTaskItemStandardForSave);
        }
        qcTaskSaveVO.setId(qcTask.getId());
        defineFieldValueService.saveDefineFieldValue(qcTaskSaveVO.getLstDefineFields(),
                TableCodeConstants.QC_TASK_TABLE_CODE, qcTask.getId());
        this.sendMsg(qcTask, taskEmployeeIds);
        return qcTask;
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void updateQcTask(QcTaskSaveVO qcTaskSaveVO) {
        QcTask qcTask = baseMapper.selectById(qcTaskSaveVO.getId());
        //??????????????????????????????
        qcTaskEmployeeMapper.delByExcuteTaskId(qcTaskSaveVO.getId());
        //????????????????????????
        String taskEmployeeIds = qcTaskSaveVO.getTaskEmployeeIds();
        String[] ids = taskEmployeeIds.split(",");
        //??????????????????????????????
        for (String employeeId : ids) {
            QcTaskEmployee qcTaskEmployee = new QcTaskEmployee();
            User user = userMapper.selectById(employeeId);
            if (ZeroOrOneEnum.ONE.getIcode() == ids.length) {
                qcTaskEmployee.setUsingType(QcTaskEmployeeUsingTypeEnum.ASSIGNED_RECEIVE.getValue());
            } else {
                qcTaskEmployee.setUsingType(QcTaskEmployeeUsingTypeEnum.DEFAULT.getValue());
            }
            qcTaskEmployee.setEmployeeCode(user.getOrgCode());
            qcTaskEmployee.setEmployeeId(user.getId());
            qcTaskEmployee.setEmployeeName(user.getRealname());
            qcTaskEmployee.setExcuteTaskId(qcTaskSaveVO.getId());
            qcTaskEmployeeMapper.insert(qcTaskEmployee);
        }
        //??????????????????
        WareStorage wareStorage = wareStorageMapper.selectById(qcTask.getLocationId());
        if (wareStorage != null) {
            qcTask.setLocationCode(wareStorage.getStorageCode());
            qcTask.setLocationName(wareStorage.getStorageName());
        }
        baseMapper.updateById(qcTask);
        qcTaskSaveVO.setId(qcTask.getId());
        defineFieldValueService.saveDefineFieldValue(qcTaskSaveVO.getLstDefineFields(),
                TableCodeConstants.QC_TASK_TABLE_CODE, qcTask.getId());
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public IPage<QcTaskVO> listPage(Page<QcTaskVO> page, QueryWrapper<QcTaskVO> queryWrapper) {
        return baseMapper.listPage(page, queryWrapper);
    }

    @Override
    public IPage<QcTaskVO> receivedQcTaskListPage(Page<QcTask> page, QueryWrapper<QcTask> queryWrapper, String userId, String qcLocationName) {

        if (StringUtils.isNotBlank(qcLocationName)) {
            List<String> qcLocationNameList = new ArrayList<>();
            //??????
            QueryWrapper<NodeVO> stationQueryWrapper = new QueryWrapper<NodeVO>();
            stationQueryWrapper.eq("a.tenant_id", CommonUtil.getTenantId());
            List<NodeVO> lstNodeVO = workShopService.queryInstitutionList(stationQueryWrapper);
            List<String> stationNameList = workProduceTaskService.getStationNameList(Arrays.asList(qcLocationName.split(",")), lstNodeVO);
            //??????
            QueryWrapper<WareStorageTreeVO> storageQueryWrapper = new QueryWrapper<>();
            storageQueryWrapper.eq("b.tenant_id", CommonUtil.getTenantId());
            List<WareStorageTreeVO> wareStorageTreeVOList = wareStorageMapper.queryReceivingGoodsTreeStorage(storageQueryWrapper);
            List<String> storageNameList = wareStorageService.getStorageNameList(Arrays.asList(qcLocationName.split(",")), wareStorageTreeVOList);

            qcLocationNameList.addAll(stationNameList);
            qcLocationNameList.addAll(storageNameList);

            if (CommonUtil.isEmptyOrNull(qcLocationNameList)) {
                queryWrapper.eq(ZeroOrOneEnum.ONE.getStrCode(), ZeroOrOneEnum.ZERO.getStrCode());
            } else {
                queryWrapper.in("location_name", qcLocationNameList);
            }
        }

        return baseMapper.receivedQcTaskListPage(page, queryWrapper, userId);
    }

    @Override
    public List<TreeNode> queryTaskLocation(String name, String status) {
        List<TreeNode> treeNodeList = wareStorageService.queryReceivingGoodsTreeStorage(name, status);
        List<TreeNode> lstNodeTreeVO = workShopService.queryInstitutionTreeList(status, name);
        List<TreeNode> resultList = new ArrayList<>();
        resultList.addAll(treeNodeList);
        resultList.addAll(lstNodeTreeVO);
        return resultList;
    }

    @Override
    public IPage<QcTaskVO> toReceiveQcTaskListPage(Page<QcTask> page, QueryWrapper<QcTask> queryWrapper, String userId) {
        return baseMapper.toReceiveQcTaskListPage(page, queryWrapper, userId);
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public QcTaskDetailVO queryQcTaskDetail(String id) {
        QcTask qcTask = baseMapper.selectById(id);
        QcTaskDetailVO qcTaskDetailVO = new QcTaskDetailVO();
        BeanUtils.copyProperties(qcTask, qcTaskDetailVO);
        //??????????????????
        QcMethod qcMethod = qcMethodMapper.selectById(qcTask.getMethodId());
        qcTaskDetailVO.setQcMethod(qcMethod);
        //????????????????????????????????????
        List<QcTaskRelateTotalVO> qcTaskRelateTotalVOList = queryQcTaskSampleAndTotalQty(id);
        qcTaskDetailVO.setQcTaskRelateTotalVOList(qcTaskRelateTotalVOList);
        //????????????????????????
        QueryWrapper<QcTaskReport> qcTaskReportQueryWrapper = new QueryWrapper<>();
        qcTaskReportQueryWrapper.eq("qc_task_id", id);
        QcTaskReport qcTaskReport = qcTaskReportMapper.selectOne(qcTaskReportQueryWrapper);
        qcTaskDetailVO.setQcTaskReport(qcTaskReport);
        //??????????????????
        String employees = baseMapper.queryTaskEmployeeByTaskId(id);
        qcTaskDetailVO.setTaskEmployees(employees);
        //???????????????????????????
        LambdaQueryWrapper<QcTaskItemStandard> qcTaskItemStandardLambdaQueryWrapper = new LambdaQueryWrapper<>();
        qcTaskItemStandardLambdaQueryWrapper.eq(QcTaskItemStandard::getQcTaskId, id);
        List<QcTaskItemStandard> qcTaskItemStandards = qcTaskItemStandardMapper.selectList(qcTaskItemStandardLambdaQueryWrapper);
        qcTaskDetailVO.setQcTaskItemStandardList(qcTaskItemStandards);
        //???????????? ?????????????????????
        List<List<QcTaskReportItemValue>> taskValueLine = queryQctaskItemRecordValue(qcTask, qcTaskItemStandards);
        qcTaskDetailVO.setTaskValueLine(taskValueLine);
        //???????????????????????????
        Item item = itemMapper.selectById(qcTask.getItemId());
        qcTaskDetailVO.setItemMainUnit(item.getMainUnit());
        //?????????????????????????????????
        String manageWay = qcTaskItemManageWay(item);
        //?????????????????????
        BigDecimal planQty = queryPlanQty(qcTask);
        qcTaskDetailVO.setPlanQty(planQty);
        //????????????????????????
        qcTaskDetailVO.setManageWay(manageWay);
        //??????????????????????????????,??????????????????
        if (qcTaskReport != null) {
            qcTaskDetailVO.setGoodQty(qcTaskReport.getGoodQty());
            qcTaskDetailVO.setBadQty(qcTaskReport.getBadQty());
        }
        return qcTaskDetailVO;
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void updataQcTaskSampleQty(UpdataQcTaskSample updataQcTaskSample) {
        //?????????????????????????????????????????????
        qcTaskRelateSampleMapper.delByQcTaskId(updataQcTaskSample.getTaskId());
        qcTaskRelateTotalMapper.delByQcTaskId(updataQcTaskSample.getTaskId());
        //????????????
        List<QcTaskRelateTotalVO> qcTaskRelateTotalVOList = updataQcTaskSample.getQcTaskRelateTotalVO();
        if (!CommonUtil.isEmptyOrNull(qcTaskRelateTotalVOList)) {
            for (QcTaskRelateTotalVO qcTaskRelateTotalVO : qcTaskRelateTotalVOList) {
                //???qcTaskRelateTotalVO ????????????id???createTime,updataTime???????????????????????????
                setBaseProperty(qcTaskRelateTotalVO);
                qcTaskRelateTotalVO.setQcTaskId(updataQcTaskSample.getTaskId());
                //??????????????????????????????????????????????????????
                if (ZeroOrOneEnum.ONE.getStrCode().equals(qcTaskRelateTotalVO.getSample())) {
                    QcTaskRelateSample qcTaskRelateSample = new QcTaskRelateSample();
                    BeanUtils.copyProperties(qcTaskRelateTotalVO, qcTaskRelateSample);
                    qcTaskRelateSampleMapper.insert(qcTaskRelateSample);
                }
                qcTaskRelateTotalMapper.insert(qcTaskRelateTotalVO);
            }
        }
        //???????????????????????????????????????????????????????????????????????????????????????????????????
        updataTaskReportItemValue(updataQcTaskSample.getTaskId(), updataQcTaskSample.getSampleQty());
        //??????????????????????????????????????????
        QcTask qcTask = baseMapper.selectById(updataQcTaskSample.getTaskId());
        qcTask.setSampleQty(updataQcTaskSample.getSampleQty());
        qcTask.setTotalQty(updataQcTaskSample.getTotalQty());
        baseMapper.updateById(qcTask);
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void executeQcTask(QcTaskExecuteVO qcTaskExecuteVO) {
        //??????????????????
        qcTaskReportItemValueMapper.delByQcTaskId(qcTaskExecuteVO.getQcTaskId());
        qcTaskReportMapper.delByQcTaskId(qcTaskExecuteVO.getQcTaskId());
        //save????????????
        QcTaskReport qcTaskReport = qcTaskExecuteVO.getQcTaskReport();
        String reportId = null;
        if (qcTaskReport != null) {
            qcTaskReportMapper.insert(qcTaskReport);
            reportId = qcTaskReport.getId();
        }
        //save?????????????????????
        List<QcTaskReportItemValue> qcTaskReportItemValueList = qcTaskExecuteVO.getQcTaskReportItemValueList();
        for (QcTaskReportItemValue qcTaskReportItemValue : qcTaskReportItemValueList) {
            qcTaskReportItemValue.setQcTaskReportId(reportId);
            qcTaskReportItemValueMapper.insert(qcTaskReportItemValue);
        }

        //??????????????????????????????
        QcTask task = baseMapper.selectById(qcTaskExecuteVO.getQcTaskId());
        if (QcTaskExeStatusEnum.NOT_START.getValue().equals(task.getExeStatus())) {
            task.setExeStatus(QcTaskExeStatusEnum.EXE_QC_TASK.getValue())
                    .setRealStartTime(new Date());
        }
        if (qcTaskReport != null) {
            task.setQcResult(qcTaskReport.getQcResult());
        }
        baseMapper.updateById(task);
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void endTask(QcTaskEndVO qcTaskEndVO) {
        //??????????????????
        qcTaskReportMapper.delByQcTaskId(qcTaskEndVO.getQcTaskId());
        QcTask task = baseMapper.selectById(qcTaskEndVO.getQcTaskId());
        //save????????????
        QcTaskReport qcTaskReport = qcTaskEndVO.getQcTaskReport();
        qcTaskReport.setQcTaskId(qcTaskEndVO.getQcTaskId());
        qcTaskReportMapper.insert(qcTaskReport);
        task.setQcResult(qcTaskReport.getQcResult());
        //??????????????????????????????,?????????????????????
        task.setExeStatus(QcTaskExeStatusEnum.FINISH.getValue())
                .setRealEndTime(new Date());
        //????????????????????????
        updataQcstatus(qcTaskEndVO);
        baseMapper.updateById(task);
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void cancelTask(List<String> ids) {
        ids.forEach(id -> {
            QcTask check = baseMapper.selectById(id);
            //?????????????????????????????????
            if (!QcTaskExeStatusEnum.NOT_START.getValue().equals(check.getExeStatus())) {
                throw new ILSBootException("P-QC-0095");
            }
            QcTask qcTask = new QcTask();
            qcTask.setExeStatus(QcTaskExeStatusEnum.CANCEL.getValue());
            LambdaUpdateWrapper<QcTask> qcTaskLambdaUpdateWrapper = new LambdaUpdateWrapper<>();
            qcTaskLambdaUpdateWrapper.eq(QcTask::getId, id);
            baseMapper.update(qcTask, qcTaskLambdaUpdateWrapper);
        });

    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void takeTask(String id) {
        LoginUser user = CommonUtil.getLoginUser();
        QueryWrapper<QcTaskEmployee> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("excute_task_id", id);
        List<QcTaskEmployee> qcTaskEmployeeList =
                qcTaskEmployeeMapper.selectList(queryWrapper);
        if (CommonUtil.isEmptyOrNull(qcTaskEmployeeList)) {
            QcTaskEmployee entity = new QcTaskEmployee();
            entity.setExcuteTaskId(id);
            entity.setUsingType(QcTaskEmployeeUsingTypeEnum.SELF_RECEIVE.getValue());
            entity.setEmployeeId(user.getId());
            entity.setEmployeeName(user.getRealname());
            qcTaskEmployeeMapper.insert(entity);
        } else {
            Map<String, List<QcTaskEmployee>> collect = qcTaskEmployeeList.stream().collect(Collectors.groupingBy(QcTaskEmployee::getEmployeeId));
            List<QcTaskEmployee> qcTaskEmployees = collect.get(user.getId());
            //?????????????????????????????????????????????????????????????????????????????????
            if (CommonUtil.isEmptyOrNull(qcTaskEmployeeList)) {
                throw new ILSBootException("????????????????????????");
            } else {
                QcTaskEmployee qcTaskEmployee = qcTaskEmployees.get(0);
                if (QcTaskEmployeeUsingTypeEnum.SELF_RECEIVE.getValue().equals(qcTaskEmployee.getUsingType())
                        || QcTaskEmployeeUsingTypeEnum.ASSIGNED_RECEIVE.getValue().equals(qcTaskEmployee.getUsingType())) {
                    throw new ILSBootException("????????????????????????");
                }
                //?????????????????????????????????
                qcTaskEmployee.setUsingType(QcTaskEmployeeUsingTypeEnum.SELF_RECEIVE.getValue());
                qcTaskEmployeeMapper.updateById(qcTaskEmployee);
                //???????????????????????????
            }
        }
    }

    @Override
    public void adjustItemCellQcStatus(List<ItemCell> itemCellList, String qcStatus, String note) {
        for (ItemCell itemCellBefor : itemCellList) {
            ItemCell itemCell = itemCellMapper.selectById(itemCellBefor.getId());
            QcStateAjustRecord qcStateAjustRecord = new QcStateAjustRecord();
            qcStateAjustRecord.setAreaCode(itemCell.getAreaCode())
                    .setStorageId(itemCell.getStorageId())
                    .setStorageCode(itemCell.getStorageCode())
                    .setStorageName(itemCell.getStorageName())
                    .setAreaName(itemCell.getAreaName())
                    .setHouseCode(itemCell.getHouseCode())
                    .setHouseName(itemCell.getHouseName())
                    .setBatch(itemCell.getBatch())
                    .setQrcode(itemCell.getQrcode())
                    .setQcStatusBefore(itemCell.getQcStatus())
                    .setQcStatusAfter(qcStatus)
                    .setItemCellId(itemCell.getId())
                    .setItemCode(itemCell.getItemCode())
                    .setItemName(itemCell.getItemName())
                    .setNote(note)
                    .setQty(itemCell.getQty())
                    .setSpec(itemCell.getSpec())
                    .setUnitName(itemCell.getUnitName())
                    .setUnitId(itemCell.getUnitId());
            qcStateAjustRecordMapper.insert(qcStateAjustRecord);
            //????????????
            itemCell.setQcStatus(qcStatus);
            itemCellMapper.updateById(itemCell);
        }
    }

    @Override
    public List<ItemCell> queryItemCellByExecutionTask(String id) {
        return itemCellMapper.queryItemCellByExecutionTask(id);
    }


    @Override
    public XrControlChartTableVO getXRControlChartData(XrControlChartVO xrControlChartVO) {
        XrControlChartTableVO xrControlChartTableVO = new XrControlChartTableVO();
        List<QcTask> qcTaskList = getQcTaskList(xrControlChartVO);

        List<String> ids = qcTaskList.stream().map(ILSEntity::getId).collect(Collectors.toList());

        if (CollectionUtil.isEmpty(ids)) {
            throw new ILSBootException("P-QC-0093");
        }

        //??????????????????id??????????????????????????????
        QueryWrapper<QcTaskReportItemValue> valueQueryWrapper = new QueryWrapper<>();
        valueQueryWrapper.in("qc_task_id", ids);
        valueQueryWrapper.eq("item_id", xrControlChartVO.getControlItem());
        valueQueryWrapper.isNotNull("qc_check_value");
        List<QcTaskReportItemValue> qcTaskReportItemValueList = qcTaskReportItemValueMapper.selectList(valueQueryWrapper);

        // ?????????????????????
        JSONArray xrTableData = new JSONArray();
        Map<Integer, List<QcTaskReportItemValue>> map = qcTaskReportItemValueList.stream().collect(Collectors.groupingBy(QcTaskReportItemValue::getSampleSeq));
        for (Integer seq : map.keySet()) {
            //???????????????
            JSONObject rowData = new JSONObject();
            rowData.put("seq", "??????" + seq);
            for (QcTaskReportItemValue cellData : map.get(seq)) {
                rowData.put(cellData.getQcTaskId(), cellData.getQcCheckValue());
            }
            xrTableData.add(rowData);
        }

        Map<String, List<QcTaskReportItemValue>> taskMap = qcTaskReportItemValueList.stream().collect(Collectors.groupingBy(QcTaskReportItemValue::getQcTaskId));

        JSONObject averageData = new JSONObject();
        averageData.put("seq", "??????");
        JSONObject poorData = new JSONObject();
        poorData.put("seq", "??????");

        for (String temp : taskMap.keySet()) {
            Double average = taskMap.get(temp).stream().collect(Collectors.averagingDouble(qcTaskReportItemValue -> Double.valueOf(qcTaskReportItemValue.getQcCheckValue() == null ? "0" : qcTaskReportItemValue.getQcCheckValue())));
            Double max = Double.valueOf(taskMap.get(temp).stream().max(Comparator.comparing(qcTaskReportItemValue -> qcTaskReportItemValue.getQcCheckValue())).get().getQcCheckValue());
            Double min = Double.valueOf(taskMap.get(temp).stream().min(Comparator.comparing(qcTaskReportItemValue -> qcTaskReportItemValue.getQcCheckValue())).get().getQcCheckValue());
            Double val3 = max - min;
            averageData.put(temp, average);
            poorData.put(temp, val3);
        }

        xrTableData.add(averageData);
        xrTableData.add(poorData);

        xrControlChartTableVO.setTableValue(xrTableData);
        xrControlChartTableVO.setQcTaskList(qcTaskList);
        return xrControlChartTableVO;
    }

    @Override
    public ControlChartTableVO getXCharTableData(XrControlChartVO xrControlChartVO) {

        ControlChartTableVO controlChartTableVO = new ControlChartTableVO();

        //?????????????????????????????????????????????
        List<QcTaskReportItemValue> qcTaskReportItemValueList = getqcTaskReportItemValueList(xrControlChartVO);

        //??????????????????id??????
        Map<String, List<QcTaskReportItemValue>> map = qcTaskReportItemValueList.stream().collect(Collectors.groupingBy(QcTaskReportItemValue::getQcTaskId));

        Map<String, List<QcTaskReportItemValue>> sortMapByKey = sortMapByKey(map);


        //X??????????????????
        List<DashBoardDataVO> dashBoardDataVOList = new ArrayList<>();

        Double totalAverage = qcTaskReportItemValueList.stream().collect(Collectors.averagingDouble(qcTaskReportItemValue -> Double.valueOf(qcTaskReportItemValue.getQcCheckValue() == null ? "0" : qcTaskReportItemValue.getQcCheckValue())));

        double var1 = 0;
        long var2 = 0;
        double average = 0;
        double var5 = 0;
        try {
            for (String qcTaskId : sortMapByKey.keySet()) {

                DashBoardDataVO dashBoardDataVO = new DashBoardDataVO();

                //????????????
                average = map.get(qcTaskId).stream().collect(Collectors.averagingDouble(item -> Double.valueOf(item.getQcCheckValue() == null ? "0" : item.getQcCheckValue())));

                dashBoardDataVO.setName(baseMapper.selectById(qcTaskId).getTaskCode());
                dashBoardDataVO.setValue(new BigDecimal(average));

                //??????????????????????????????
                var1 = var1 + Math.pow(average - totalAverage, 2);

                dashBoardDataVOList.add(dashBoardDataVO);
            }
            //???????????????
            double var3 = Math.sqrt(var1 / Double.valueOf(map.size()));

            controlChartTableVO.setLowControlLine(totalAverage - 3 * var3);
            controlChartTableVO.setHighControlLine(totalAverage + 3 * var3);
            controlChartTableVO.setAverage(totalAverage);
            controlChartTableVO.setBoardDataVOList(dashBoardDataVOList);
        } catch (NumberFormatException e) {
            log.error("?????????????????????double?????????????????????");
        }
        return controlChartTableVO;
    }

    @Override
    public ControlChartTableVO getRCharTableData(XrControlChartVO xrControlChartVO) {
        ControlChartTableVO controlChartTableVO = new ControlChartTableVO();

        //?????????????????????????????????????????????
        List<QcTaskReportItemValue> qcTaskReportItemValueList = getqcTaskReportItemValueList(xrControlChartVO);

        if (CollectionUtil.isEmpty(qcTaskReportItemValueList)) {
            return controlChartTableVO;
        }
        //??????????????????id??????
        Map<String, List<QcTaskReportItemValue>> map = qcTaskReportItemValueList.stream().collect(Collectors.groupingBy(QcTaskReportItemValue::getQcTaskId));

        //??????
        Map<String, List<QcTaskReportItemValue>> sortMapByKey = sortMapByKey(map);

        //X??????????????????
        List<DashBoardDataVO> dashBoardDataVOList = new ArrayList<>();

        //????????????
        List<Double> doubleList = new ArrayList<>(16);

        double var1 = 0;
        double var2 = 0;
        double var4 = 0;
        double var5 = 0;
        Double count = Double.valueOf(map.size());

        try {
            for (String qcTaskId : sortMapByKey.keySet()) {

                DashBoardDataVO dashBoardDataVO = new DashBoardDataVO();

                //?????????
                Double max = Double.valueOf(map.get(qcTaskId).stream().max(Comparator.comparing(qcTaskReportItemValue -> qcTaskReportItemValue.getQcCheckValue())).get().getQcCheckValue());
                Double min = Double.valueOf(map.get(qcTaskId).stream().min(Comparator.comparing(qcTaskReportItemValue -> qcTaskReportItemValue.getQcCheckValue())).get().getQcCheckValue());
                Double val3 = max - min;
                doubleList.add(val3);

                //????????????
                var5 = var5 + val3;

                dashBoardDataVO.setName(baseMapper.selectById(qcTaskId).getTaskCode());
                dashBoardDataVO.setValue(new BigDecimal(val3));

                dashBoardDataVOList.add(dashBoardDataVO);
            }

            //????????????????????????
            var2 = var5 / count;

            //???????????????
            for (Double value : doubleList) {
                var1 = var1 + Math.pow(var2 - value, 2);
            }

            var4 = Math.sqrt(var1 / count);

            controlChartTableVO.setLowControlLine(var2 - 3 * var4);
            controlChartTableVO.setHighControlLine(var2 + 3 * var4);
            controlChartTableVO.setAverage(var2);
            controlChartTableVO.setBoardDataVOList(dashBoardDataVOList);
        } catch (NumberFormatException e) {
            log.error("?????????????????????double?????????????????????");
        }

        return controlChartTableVO;
    }

    /**
     * ????????????????????????
     *
     * @param qcTask
     * @param qcTaskRelateTotalList
     */
    private void setQcTaskSampleAndTotalQty(List<QcTaskRelateTotal> qcTaskRelateTotalList, QcTask qcTask) {
        if (!CommonUtil.isEmptyOrNull(qcTaskRelateTotalList)) {
            //?????????????????????
            BigDecimal sum = new BigDecimal(0);
            for (QcTaskRelateTotal taskRelateTotal : qcTaskRelateTotalList) {
                BigDecimal qty = taskRelateTotal.getQty();
                sum = sum.add(qty);
            }
            //???????????????
            qcTask.setTotalQty(sum);
        }
    }

    private BigDecimal queryPlanQty(QcTask qcTask) {
        BigDecimal bigDecimal = null;
        String methodId = qcTask.getMethodId();
        QcMethod qcMethod = qcMethodMapper.selectById(methodId);
        BigDecimal totalQty = qcTask.getTotalQty();
        String qcWay = qcMethod.getQcWay();
        if (totalQty == null || totalQty.compareTo(new BigDecimal(0)) == 0) {
            if (QcWayEnum.FIXED_CHECK.getValue().equals(qcWay)) {
                //????????????
                bigDecimal = qcMethod.getQcQty();
            } else {
                bigDecimal = new BigDecimal(0);
            }
        } else {
            if (QcWayEnum.ALL_CHECK.getValue().equals(qcWay)) {
                bigDecimal = totalQty;
            } else if (QcWayEnum.PROPORTION_CHECK.getValue().equals(qcWay)) {
                //???????????????
                bigDecimal = totalQty.multiply(qcMethod.getQcQty());
            } else if (QcWayEnum.FIXED_CHECK.getValue().equals(qcWay)) {
                //????????????
                bigDecimal = qcMethod.getQcQty();
            } else if (QcWayEnum.CUSTOM_CHECK.getValue().equals(qcWay)) {
                bigDecimal = new BigDecimal(0);
            }
        }

        return bigDecimal;
    }

    /**
     * ????????????????????????
     *
     * @param id
     * @return
     */
    private List<QcTaskRelateTotalVO> queryQcTaskSampleAndTotalQty(String id) {
        List<QcTaskRelateTotalVO> qcTaskRelateTotalVOList = new ArrayList<>(16);
        QueryWrapper<QcTaskRelateTotal> qcTaskRelateTotalQueryWrapper = new QueryWrapper<>();
        qcTaskRelateTotalQueryWrapper.eq("qc_task_id", id);

        List<QcTaskRelateTotal> qcTaskRelateTotals = qcTaskRelateTotalMapper.selectList(qcTaskRelateTotalQueryWrapper);
        QueryWrapper<QcTaskRelateSample> qcTaskRelateSampleQueryWrapper = new QueryWrapper<>();
        qcTaskRelateSampleQueryWrapper.eq("qc_task_id", id);
        List<QcTaskRelateSample> qcTaskRelateSamples = qcTaskRelateSampleMapper.selectList(qcTaskRelateSampleQueryWrapper);
        Map<String, List<QcTaskRelateSample>> qctaskRelateSamplesMap = qcTaskRelateSamples.stream().collect(Collectors.groupingBy(QcTaskRelateSample::getQrcode));

        for (QcTaskRelateTotal qcTaskRelateTotal : qcTaskRelateTotals) {
            QcTaskRelateTotalVO qcTaskRelateTotalVO = new QcTaskRelateTotalVO();
            BeanUtils.copyProperties(qcTaskRelateTotal, qcTaskRelateTotalVO);
            if (qctaskRelateSamplesMap.get(qcTaskRelateTotalVO.getQrcode()) != null) {
                qcTaskRelateTotalVO.setSample(ZeroOrOneEnum.ONE.getStrCode());
            } else {
                qcTaskRelateTotalVO.setSample(ZeroOrOneEnum.ZERO.getStrCode());
            }
            qcTaskRelateTotalVOList.add(qcTaskRelateTotalVO);
        }
        return qcTaskRelateTotalVOList;
    }

    /**
     * ????????????????????????
     *
     * @param item
     * @return
     */
    private String qcTaskItemManageWay(Item item) {
        String manageWay = null;
        //????????????????????????????????????????????????
        if (ZeroOrOneEnum.ZERO.getStrCode().equals(item.getQrcode())) {
            if (ZeroOrOneEnum.ZERO.getStrCode().equals(item.getBatch())) {
                manageWay = ItemManageWayEnum.NONE_MANAGE.getValue();
            } else {
                manageWay = ItemManageWayEnum.BATCH_MANAGE.getValue();
            }
        } else {
            manageWay = ItemManageWayEnum.QRCODE_MANAGE.getValue();
        }
        return manageWay;
    }

    /**
     * ?????????????????????????????????
     *
     * @param qcTask
     * @param qcTaskItemStandards
     * @return
     */
    private List<List<QcTaskReportItemValue>> queryQctaskItemRecordValue(QcTask qcTask, List<QcTaskItemStandard> qcTaskItemStandards) {
        LambdaQueryWrapper<QcTaskReportItemValue> qcTaskReportItemValueLambdaQueryWrapper = new LambdaQueryWrapper<>();
        qcTaskReportItemValueLambdaQueryWrapper.eq(QcTaskReportItemValue::getQcTaskId, qcTask.getId());
        List<QcTaskReportItemValue> qcTaskReportItemValues = qcTaskReportItemValueMapper.selectList(qcTaskReportItemValueLambdaQueryWrapper);
        List<List<QcTaskReportItemValue>> taskValueLine = new ArrayList<>(16);
        Map<Integer, List<QcTaskReportItemValue>> collect =
                qcTaskReportItemValues.stream().collect(Collectors.groupingBy(QcTaskReportItemValue::getSampleSeq));
        int size = collect.size();
        if (qcTask.getSampleQty() != null && qcTask.getSampleQty().intValue() > 0) {
            for (List<QcTaskReportItemValue> map : collect.values()) {
                taskValueLine.add(map);
            }
            //?????????????????????????????????????????????????????????????????????????????????????????????????????????
            if (new BigDecimal(collect.size()).compareTo(qcTask.getSampleQty()) == -1) {
                for (int i = collect.size(); i < qcTask.getSampleQty().intValue(); i++) {
                    List<QcTaskReportItemValue> qcTaskReportItemValueList = new ArrayList<>(16);
                    size++;
                    for (QcTaskItemStandard qcTaskItemStandard : qcTaskItemStandards) {
                        QcTaskReportItemValue qcTaskReportItemValue = new QcTaskReportItemValue();
                        qcTaskReportItemValue.setQcTaskId(qcTask.getId())
                                .setQcTaskStandardId(qcTaskItemStandard.getId())
                                .setItemId(qcTaskItemStandard.getItemId())
                                .setSampleSeq(size);
                        qcTaskReportItemValueList.add(qcTaskReportItemValue);
                    }
                    taskValueLine.add(qcTaskReportItemValueList);
                }
            }
        }
        return taskValueLine;
    }

    /**
     * ??????????????????
     *
     * @param qcTaskEndVO
     */
    @Transactional(rollbackFor = RuntimeException.class)
    private void updataQcstatus(QcTaskEndVO qcTaskEndVO) {
        //??????????????????
        QcTask qcTask = baseMapper.selectById(qcTaskEndVO.getQcTaskId());
        //??????????????????
        QcMethod qcMethod = qcMethodMapper.selectById(qcTask.getMethodId());
        //??????????????????
        Item item = itemMapper.selectById(qcTask.getItemId());
        //??????????????????
        String manageWay = this.qcTaskItemManageWay(item);
        //????????????
        String judgeType = qcMethod.getJudgeType();
        //????????????
        String qcResult = qcTaskEndVO.getQcTaskReport().getQcResult();
        //??????????????????
        ItemCell itemCell = new ItemCell();
        //?????????????????????1??????????????????????????????????????????0?????????????????????????????????
        itemCell.setQcStatus(qcResult);
        //??????????????????????????????????????????????????????
        if (ItemManageWayEnum.QRCODE_MANAGE.getValue().equals(manageWay)) {
            if (!QcMethodJudgeTypeEnum.NOT_UPDATA_QCSTATUS.getValue().equals(judgeType)) {
                QueryWrapper<QcTaskRelateTotal> qcTaskRelateTotalQueryWrapper = new QueryWrapper<>();
                qcTaskRelateTotalQueryWrapper.eq("qc_task_id", qcTask.getId());
                List<QcTaskRelateTotal> qcTaskRelateTotals = qcTaskRelateTotalMapper.selectList(qcTaskRelateTotalQueryWrapper);
                String qrcodes = qcTaskRelateTotals.stream().map(QcTaskRelateTotal::getQrcode).collect(Collectors.joining(","));
                List<String> qrcodeList = Arrays.asList(qrcodes.split(","));
                UpdateWrapper<ItemCell> itemCellUpdateWrapper = new UpdateWrapper<>();
                itemCellUpdateWrapper.in("qrcode", qrcodeList);
                //???????????????????????????????????????
                itemCellMapper.update(itemCell, itemCellUpdateWrapper);
            }
        } else {
            if (QcMethodJudgeTypeEnum.JUDGE_BY_BATCH.getValue().equals(judgeType)) {
                List<ItemCell> itemCellList = qcTaskEndVO.getItemCellList();
                if (!CommonUtil.isEmptyOrNull(itemCellList)) {
                    //????????????????????????id????????????
                    String itemCellIdStrs = itemCellList.stream().map(ItemCell::getId).collect(Collectors.joining(","));
                    List<String> itemCellIdList = Arrays.asList(itemCellIdStrs.split(","));
                    UpdateWrapper<ItemCell> itemCellUpdateWrapper = new UpdateWrapper<>();
                    itemCellUpdateWrapper.in("id", itemCellIdList);
                    itemCellMapper.update(itemCell, itemCellUpdateWrapper);
                }
            }
        }
    }

    private void setBaseProperty(ILSEntity ilsEntity) {
        ilsEntity.setCreateBy(null)
                .setCreateTime(null)
                .setDeleted(null)
                .setId(null)
                .setTenantId(null)
                .setUpdateTime(null)
                .setUpdateBy(null);
    }

    /**
     * ????????????????????????
     *
     * @param taskId
     * @param sampleQty
     */
    private void updataTaskReportItemValue(String taskId, BigDecimal sampleQty) {
        QcTask qcTask = baseMapper.selectById(taskId);
        List<Integer> seqList = new ArrayList<>(16);
        if (sampleQty.compareTo(qcTask.getSampleQty() == null ? new BigDecimal(0) : qcTask.getSampleQty()) == -1) {
            for (int i = sampleQty.intValue() + 1; i <= (qcTask.getSampleQty() == null ? new BigDecimal(0) : qcTask.getSampleQty()).intValue(); i++) {
                seqList.add(i);
            }
            qcTaskReportItemValueMapper.delBySeqAndTaskId(qcTask.getId(), seqList);
        }
    }

    private void setRelatedTaskInfo(QcTask qcTask) {
        //???????????????????????????
        if (ZeroOrOneEnum.ONE.getStrCode().equals(qcTask.getRelatedReceiptType())) {
            WorkProduceTask workProduceTask = workProduceTaskMapper.selectById(qcTask.getRelatedReceiptId());
            //?????????????????????
            qcTask.setRelatedReceiptId(workProduceTask.getId())
                    .setRelatedReceiptCode(workProduceTask.getTaskCode())
                    .setBatchNo(workProduceTask.getBatchNo())
                    .setOrderId(workProduceTask.getOrderId())
                    .setOrderNo(workProduceTask.getOrderNo())
                    .setProcessCode(workProduceTask.getProcessCode())
                    .setProcessId(workProduceTask.getProcessId())
                    .setProcessName(workProduceTask.getProcessName())
                    .setShiftId(workProduceTask.getShiftId())
                    .setItemId(workProduceTask.getItemId())
                    .setSeq(workProduceTask.getSeq())
                    .setPlanDate(workProduceTask.getPlanDate());
        }
    }

    /**
     * @param qcTask
     */
    private void setItemNameAndCode(QcTask qcTask) {
        if (StringUtils.isBlank(qcTask.getItemName()) || StringUtils.isBlank(qcTask.getItemCode())) {
            Item item = itemMapper.selectById(qcTask.getItemId());
            qcTask.setItemName(item.getItemName());
            qcTask.setItemCode(item.getItemCode());
        }
    }

    private void sendMsg(QcTask qcTask, String taskEmployeeIds) {
        QcTask msgQcTask = new QcTask();
        BeanUtils.copyProperties(qcTask, msgQcTask);
        List<String> receiverIds = Arrays.asList(taskEmployeeIds.split(","));
        String qcType = "";
        for (QcTaskQcTypeEnum value : QcTaskQcTypeEnum.values()) {
            if (value.getValue().equals(msgQcTask.getQcType())) {
                qcType = value.getDesc();
            }
        }
        msgQcTask.setQcType(qcType);
        MsgParamsVO msgParamsVO = new MsgParamsVO(receiverIds, null, null, msgQcTask.getCreateBy(), msgQcTask);
        msgParamsVO.setTenantId(TenantContext.getTenant());
        if (StringUtils.isNotBlank(msgQcTask.getLocationName())) {
            msgHandleServer.sendMsg(MesCommonConstant.MSG_QC_TASK, MesCommonConstant.INFORM, msgParamsVO);
        } else {
            msgHandleServer.sendMsg(MesCommonConstant.MSG_QC_TASK_NO_STATION, MesCommonConstant.INFORM, msgParamsVO);
        }
    }


    private List<QcTask> getQcTaskList(XrControlChartVO xrControlChartVO) {
        List<QcTask> newQcTaskList = new ArrayList<>();

        //?????????????????????
        //Map<String, QcTask> qcTaskMap = null;
        QueryWrapper<QcTask> qcTaskQueryWrapper = new QueryWrapper<>();
        qcTaskQueryWrapper.eq("item_name", xrControlChartVO.getItemName());
        qcTaskQueryWrapper.eq("method_id", xrControlChartVO.getQcMethod());
        qcTaskQueryWrapper.eq("is_deleted", ZeroOrOneEnum.ZERO.getIcode());
        qcTaskQueryWrapper.eq("exe_status", QcTaskExeStatusEnum.FINISH.getValue());
        qcTaskQueryWrapper.orderByAsc("id");
        if (xrControlChartVO.getStartTime() != null) {
            qcTaskQueryWrapper.eq("real_start_time", xrControlChartVO.getStartTime());
        }
        if (xrControlChartVO.getEndTime() != null) {
            qcTaskQueryWrapper.eq("real_end_time", xrControlChartVO.getEndTime());
        }
        if (StringUtils.isNotEmpty(xrControlChartVO.getLocation())) {
            qcTaskQueryWrapper.eq("location_id", xrControlChartVO.getLocation());
        }
        qcTaskQueryWrapper.orderByDesc("create_time");

        List<QcTask> qcTaskList = baseMapper.selectList(qcTaskQueryWrapper);

        //?????????????????????????????????0
        if (CollectionUtil.isEmpty(qcTaskList)) {
            return newQcTaskList;
        }

        //??????????????????????????????25???????????????25???
        newQcTaskList = qcTaskList.size() > 25 ? qcTaskList.subList(0, 25) : qcTaskList;
        List<String> ids = newQcTaskList.stream().map(ILSEntity::getId).collect(Collectors.toList());

        return newQcTaskList;
    }


    private List<QcTaskReportItemValue> getqcTaskReportItemValueList(XrControlChartVO xrControlChartVO) {
        List<QcTaskReportItemValue> qcTaskReportItemValueList = new ArrayList<>(16);

        List<QcTask> qcTaskList = getQcTaskList(xrControlChartVO);

        //?????????????????????????????????0
        if (CollectionUtil.isEmpty(qcTaskList)) {
            throw new ILSBootException("P-QC-0093");
        }

        List<String> ids = qcTaskList.stream().map(ILSEntity::getId).collect(Collectors.toList());
        //??????????????????id??????????????????????????????
        QueryWrapper<QcTaskReportItemValue> valueQueryWrapper = new QueryWrapper<>();
        valueQueryWrapper.in("qc_task_id", ids);
        valueQueryWrapper.isNotNull("qc_check_value");
        valueQueryWrapper.eq("item_id", xrControlChartVO.getControlItem());
        valueQueryWrapper.orderByAsc("qc_task_id");
        qcTaskReportItemValueList = qcTaskReportItemValueMapper.selectList(valueQueryWrapper);
        if (CollectionUtil.isEmpty(qcTaskReportItemValueList)) {
            throw new ILSBootException("P-QC-0093");
        }
        return qcTaskReportItemValueList;
    }


    /**
     * ?????? Map???key????????????
     *
     * @param map
     * @return
     */
    public Map<String, List<QcTaskReportItemValue>> sortMapByKey(Map<String, List<QcTaskReportItemValue>> map) {
        if (map == null || map.isEmpty()) {
            return null;
        }

        Map<String, List<QcTaskReportItemValue>> sortMap = new TreeMap<String, List<QcTaskReportItemValue>>(
                new MapKeyComparator());

        sortMap.putAll(map);

        return sortMap;
    }

}
