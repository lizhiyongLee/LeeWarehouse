package com.ils.modules.mes.execution.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ils.modules.mes.base.craft.entity.ProcessNgItem;
import com.ils.modules.mes.execution.entity.WorkProduceDefectClassification;
import com.ils.modules.mes.execution.vo.WorkProduceDefectClassificationVO;

import java.util.List;

/**
 * @author lishaojie
 * @description
 * @date 2021/6/3 10:58
 */
public interface WorkProduceDefectClassificationService extends IService<WorkProduceDefectClassification> {
    /**
     * 添加
     *
     * @param workProduceDefectClassification
     */
    public void saveWorkProduceDefectClassification(WorkProduceDefectClassification workProduceDefectClassification);

    /**
     * 批量新增
     *
     * @param workProduceDefectClassificationVOList
     */
    public void saveBatchWorkProduceDefectClassification(List<WorkProduceDefectClassificationVO> workProduceDefectClassificationVOList);

    /**
     * 修改
     *
     * @param workProduceDefectClassification
     */
    public void updateWorkProduceDefectClassification(WorkProduceDefectClassification workProduceDefectClassification);

    /**
     * 删除
     *
     * @param id
     */
    public void delWorkProduceDefectClassification(String id);

    /**
     * 批量删除
     *
     * @param idList
     */
    public void delBatchWorkProduceDefectClassification(List<String> idList);

    /**
     * 根据任务id查询工序对应缺陷详情
     *
     * @param taskId
     * @return 工序对应缺陷项
     */
    public List<ProcessNgItem> selectNgItemByProcessFromTaskId(String taskId);

    /**
     * 根据任务id缺陷分类记录
     *
     * @param taskId
     * @return 缺陷记录
     */
    public List<WorkProduceDefectClassification> selectDefectClassificationByTaskId(String taskId);

    /**
     * 工序缺陷排序
     *
     * @return
     */
    public JSONArray statisticsDefectClassificationByProcess();

    /**
     * 工位缺陷排序
     *
     * @return
     */
    public JSONArray statisticsDefectClassificationByStation();

    /**
     * 质量柏拉图
     *
     * @return
     */
    public JSONArray selectQualityPlato();

}
