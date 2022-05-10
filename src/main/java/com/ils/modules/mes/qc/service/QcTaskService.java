package com.ils.modules.mes.qc.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ils.modules.mes.base.factory.vo.NodeVO;
import com.ils.modules.mes.material.entity.ItemCell;
import com.ils.modules.mes.qc.entity.QcTask;
import com.ils.modules.mes.qc.vo.*;
import com.ils.modules.mes.util.TreeNode;

import java.util.List;

/**
 * @Description: 质检任务
 * @Author: Tian
 * @Date: 2021-03-01
 * @Version: V1.0
 */
public interface QcTaskService extends IService<QcTask> {

    /**
     * 添加
     *
     * @param qcTaskSaveVO
     * @return
     */
    QcTask saveQcTask(QcTaskSaveVO qcTaskSaveVO);

    /**
     * 修改
     *
     * @param qcTaskSaveVO
     */
    void updateQcTask(QcTaskSaveVO qcTaskSaveVO);


    /**
     * 分页查询
     *
     * @param page
     * @param queryWrapper
     * @return
     */
    IPage<QcTaskVO> listPage(Page<QcTaskVO> page, QueryWrapper<QcTaskVO> queryWrapper);

    /**
     * 已领取任务列表分页查询
     *
     * @param userId
     * @param qcLocationName
     * @param page
     * @param queryWrapper
     * @return
     */
    IPage<QcTaskVO> receivedQcTaskListPage(Page<QcTask> page, QueryWrapper<QcTask> queryWrapper, String userId, String qcLocationName);

    /**
     * 查询仓位或者工位
     *
     * @param name
     * @param status
     * @return
     */
    List<TreeNode> queryTaskLocation(String name, String status);

    /**
     * 待领取任务列表分页查询
     *
     * @param userId
     * @param page
     * @param queryWrapper
     * @return
     */
    IPage<QcTaskVO> toReceiveQcTaskListPage(Page<QcTask> page, QueryWrapper<QcTask> queryWrapper, String userId);

    /**
     * 查询质检任务详情
     *
     * @param id
     * @return
     */
    QcTaskDetailVO queryQcTaskDetail(String id);

    /**
     * 更新质检任务样本数量
     *
     * @param updataQcTaskSample
     */
    void updataQcTaskSampleQty(UpdataQcTaskSample updataQcTaskSample);

    /**
     * 提交质检任务
     *
     * @param qcTaskExecuteVO
     */
    void executeQcTask(QcTaskExecuteVO qcTaskExecuteVO);

    /**
     * 结束质检任务
     *
     * @param qcTaskEndVO
     */
    void endTask(QcTaskEndVO qcTaskEndVO);

    /**
     * 取消质检任务
     *
     * @param ids
     */
    void cancelTask(List<String> ids);


    /**
     * 领取任务
     *
     * @param id
     */
    void takeTask(String id);

    /**
     * 调整物料质量状态
     *
     * @param itemCellList
     * @param qcStatus
     * @param note
     */
    void adjustItemCellQcStatus(List<ItemCell> itemCellList, String qcStatus, String note);


    /**
     * 通过生产任务id查询物料
     *
     * @param id
     * @return
     */
    List<ItemCell> queryItemCellByExecutionTask(String id);


    /**
     * XR_ControlChart图
     *
     * @param xrControlChartVO
     * @return
     */
    XrControlChartTableVO getXRControlChartData(XrControlChartVO xrControlChartVO);


    /**
     * 获取x图的表数据
     *
     * @param xrControlChartVO
     * @return
     */
    ControlChartTableVO getXCharTableData(XrControlChartVO xrControlChartVO);

    /**
     * 获取r图的表数据
     *
     * @param xrControlChartVO
     * @return
     */
    ControlChartTableVO getRCharTableData(XrControlChartVO xrControlChartVO);
}
