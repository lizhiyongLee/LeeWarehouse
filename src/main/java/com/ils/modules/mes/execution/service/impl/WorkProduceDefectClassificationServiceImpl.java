package com.ils.modules.mes.execution.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ils.common.exception.ILSBootException;
import com.ils.modules.mes.base.craft.entity.ProcessNgItem;
import com.ils.modules.mes.base.craft.service.ProcessNgItemService;
import com.ils.modules.mes.enums.PlanTaskExeStatusEnum;
import com.ils.modules.mes.execution.entity.WorkProduceDefectClassification;
import com.ils.modules.mes.execution.entity.WorkProduceTask;
import com.ils.modules.mes.execution.mapper.WorkProduceDefectClassificationMapper;
import com.ils.modules.mes.execution.service.WorkProduceDefectClassificationService;
import com.ils.modules.mes.execution.service.WorkProduceTaskService;
import com.ils.modules.mes.execution.vo.StatisticsDefectVO;
import com.ils.modules.mes.execution.vo.WorkProduceDefectClassificationVO;
import com.ils.modules.mes.util.CommonUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author lishaojie
 * @description
 * @date 2021/6/3 10:58
 */
@Service
public class WorkProduceDefectClassificationServiceImpl extends ServiceImpl<WorkProduceDefectClassificationMapper, WorkProduceDefectClassification> implements WorkProduceDefectClassificationService {

    @Autowired
    private WorkProduceTaskService produceTaskService;

    @Autowired
    private ProcessNgItemService processNgItemService;

    @Autowired
    private WorkProduceTaskService workProduceTaskService;

    /**
     * 使用beanUtil复制属性时需要忽略的属性
     */
    private final String IGNORE_PROPERTY = "id,createBy,createTime,updateBy,updateTime,deleted,tenantId";


    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void saveWorkProduceDefectClassification(WorkProduceDefectClassification workProduceDefectClassification) {
        WorkProduceTask workProduceTask = produceTaskService.getById(workProduceDefectClassification.getProduceTaskId());
        if (!PlanTaskExeStatusEnum.PRODUCE.getValue().equals(workProduceTask.getExeStatus())) {
            throw new ILSBootException("P-OW-0014");
        }
        QueryWrapper<WorkProduceDefectClassification> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("ng_item_id", workProduceDefectClassification.getNgItemId());
        queryWrapper.eq("produce_task_id", workProduceDefectClassification.getProduceTaskId());
        WorkProduceDefectClassification workProduceDefectClassificationOld = baseMapper.selectOne(queryWrapper);
        if (workProduceDefectClassificationOld != null) {
            WorkProduceDefectClassification updateWorkProduceDefectClassification = new WorkProduceDefectClassification();
            updateWorkProduceDefectClassification.setQty(workProduceDefectClassification.getQty());
            updateWorkProduceDefectClassification.setId(workProduceDefectClassificationOld.getId());
            baseMapper.updateById(updateWorkProduceDefectClassification);
        } else {
            baseMapper.insert(workProduceDefectClassification);
        }
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void saveBatchWorkProduceDefectClassification(List<WorkProduceDefectClassificationVO> workProduceDefectClassificationVOList) {
        String taskId = workProduceDefectClassificationVOList.get(0).getTaskId();
        WorkProduceTask workProduceTask = workProduceTaskService.getById(taskId);
        List<ProcessNgItem> processNgItems = this.selectNgItemByProcessFromTaskId(taskId);

        BigDecimal sumQty = BigDecimal.ZERO;
        for (WorkProduceDefectClassificationVO workProduceDefectClassification : workProduceDefectClassificationVOList) {
            sumQty = sumQty.add(workProduceDefectClassification.getQty());
        }
        if (workProduceTask.getBadQty().compareTo(sumQty) < 0) {
            throw new ILSBootException("缺陷项总数不能超过不合格品数量");
        }
        String[] ignoreProperty = IGNORE_PROPERTY.split(",");

        for (ProcessNgItem processNgItem : processNgItems) {
            for (WorkProduceDefectClassificationVO workProduceDefectClassificationVO : workProduceDefectClassificationVOList) {
                if (workProduceDefectClassificationVO.getQty().compareTo(BigDecimal.ZERO) != 0) {
                    if (processNgItem.getNgItemId().equals(workProduceDefectClassificationVO.getNgItemId())) {
                        WorkProduceDefectClassification workProduceDefectClassification = new WorkProduceDefectClassification();
                        BeanUtils.copyProperties(workProduceTask, workProduceDefectClassification, ignoreProperty);
                        workProduceDefectClassification.setNgItemName(processNgItem.getNgItemName());
                        workProduceDefectClassification.setNgTypeName(processNgItem.getNgItemTypeName());
                        workProduceDefectClassification.setQty(workProduceDefectClassificationVO.getQty());
                        workProduceDefectClassification.setNgItemId(workProduceDefectClassificationVO.getNgItemId());
                        workProduceDefectClassification.setNgTypeId(workProduceDefectClassificationVO.getNgTypeId());
                        workProduceDefectClassification.setProduceTaskId(workProduceDefectClassificationVO.getTaskId());
                        this.saveWorkProduceDefectClassification(workProduceDefectClassification);
                    }
                }
            }
        }
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void updateWorkProduceDefectClassification(WorkProduceDefectClassification workProduceDefectClassification) {
        baseMapper.updateById(workProduceDefectClassification);
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void delWorkProduceDefectClassification(String id) {
        baseMapper.deleteById(id);
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void delBatchWorkProduceDefectClassification(List<String> idList) {
        baseMapper.deleteBatchIds(idList);
    }

    @Override
    public List<ProcessNgItem> selectNgItemByProcessFromTaskId(String taskId) {
        WorkProduceTask workProduceTask = produceTaskService.getById(taskId);
        List<ProcessNgItem> processNgItemList = processNgItemService.selectByMainId(workProduceTask.getProcessId());
        return processNgItemList;
    }

    @Override
    public List<WorkProduceDefectClassification> selectDefectClassificationByTaskId(String taskId) {
        QueryWrapper<WorkProduceDefectClassification> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("produce_task_id", taskId);
        return baseMapper.selectList(queryWrapper);
    }

    @Override
    public JSONArray statisticsDefectClassificationByProcess() {
        JSONArray data = new JSONArray();
        List<String> maxProcessList = baseMapper.statisticsDefectProcessMax();
        maxProcessList.forEach(processName -> {
            JSONObject keyValue = new JSONObject();
            keyValue.put("name", processName);
            List<StatisticsDefectVO> value = baseMapper.statisticsDefectByProcess(processName);
            JSONArray processValue = new JSONArray();
            value.forEach(statisticsDefectVO -> {
                JSONObject defectValue = new JSONObject();
                defectValue.put("name",statisticsDefectVO.getNgItemName());
                defectValue.put("value", statisticsDefectVO.getDefectQty());
                processValue.add(defectValue);
            });
            keyValue.put("value", processValue);
            data.add(keyValue);
        });
        return data;
    }

    @Override
    public JSONArray statisticsDefectClassificationByStation() {
        JSONArray data = new JSONArray();
        List<String> maxStationList = baseMapper.statisticsDefectStationMax();

        maxStationList.forEach(stationName -> {
            JSONObject keyValue = new JSONObject();
            keyValue.put("name", stationName);
            List<StatisticsDefectVO> value = baseMapper.statisticsDefectByStation(stationName);
            JSONArray stationValue = new JSONArray();
            value.forEach(statisticsDefectVO -> {
                JSONObject defectValue = new JSONObject();
                defectValue.put("name", statisticsDefectVO.getNgItemName());
                defectValue.put("value", statisticsDefectVO.getDefectQty());
                stationValue.add(defectValue);
            });
            keyValue.put("value", stationValue);
            data.add(keyValue);
        });
        return data;
    }

    @Override
    public JSONArray selectQualityPlato(){
        List<StatisticsDefectVO> statisticsDefectVOList = baseMapper.selectQualityPlato(CommonUtil.getTenantId());
        JSONArray data=new JSONArray();
        BigDecimal sumPercentage=BigDecimal.ZERO;
        for (StatisticsDefectVO statisticsDefectVO : statisticsDefectVOList) {
            JSONObject jsonObject=new JSONObject();
            jsonObject.put("ngItemName",statisticsDefectVO.getNgItemName());
            jsonObject.put("totalQty",statisticsDefectVO.getDefectQty());
            sumPercentage=sumPercentage.add(statisticsDefectVO.getPercentage());
            if(data.size()==statisticsDefectVOList.size()-1){
                jsonObject.put("percentage",new BigDecimal(100));
            }else {
                jsonObject.put("percentage",sumPercentage.doubleValue());
            }
            data.add(jsonObject);
        }
        return data;
    }
}
