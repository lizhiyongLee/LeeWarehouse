package com.ils.modules.mes.produce.service;

import java.util.List;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ils.modules.mes.produce.entity.WorkProcessTask;
import com.ils.modules.mes.produce.vo.AutoScheWorkProcessVO;
import com.ils.modules.mes.produce.vo.WorkProcessTaskVO;
import com.ils.modules.mes.util.TreeNode;

/**
 * @Description: 工单工序任务
 * @Author: fengyi
 * @Date: 2020-11-18
 * @Version: V1.0
 */
public interface WorkProcessTaskService extends IService<WorkProcessTask> {

    /**
     * 添加
     * @param workProcessTask
     */
    public void saveWorkProcessTask(WorkProcessTask workProcessTask) ;
    
    /**
     * 修改
     * @param workProcessTask
     */
    public void updateWorkProcessTask(WorkProcessTask workProcessTask);
    
    /**
     * 删除
     * @param id
     */
    public void delWorkProcessTask (String id);
    
    /**
     * 批量删除
     * @param idList
     */
    public void delBatchWorkProcessTask (List<String> idList);

    /**
     * 
     * 查询工序任务ListVO
     * 
     * @param page
     * @param queryWrapper
     * @return
     * @date 2020年11月6日
     */
    public IPage<WorkProcessTaskVO> listPage(Page<WorkProcessTaskVO> page,
        QueryWrapper<WorkProcessTaskVO> queryWrapper);

    /**
     *
     * 查询工序任务ListVO（不分页）
     *
     * @param queryWrapper
     * @return
     * @date 2020年11月6日
     */
    List<WorkProcessTaskVO> listPageAll(QueryWrapper<WorkProcessTaskVO> queryWrapper);

    /**
     * 
     * 根据工序任务ID查询工位
     * 
     * @param id
     *            工序任务ID
     * @return List<TreeNode>
     * @date 2020年11月23日
     */
    public List<TreeNode> queryStationById(String id);

    /**
     * 
     * 查询排序的工序任务
     * 
     * @param queryWrapper
     * @return
     * @date 2021年2月25日
     */
    public List<AutoScheWorkProcessVO> querySortWorkProcessTask(QueryWrapper<AutoScheWorkProcessVO> queryWrapper);

    /**
     * 查找任务
     * 
     * @param bussId 产品bomId或者工艺路线id
     * @param workflowType 工艺
     * @param processId 工序ID
     * @param seq 工序序号
     * @return
     * @date 2021年2月26日
     */
    public List<String> getWorkstationIds(String bussId, String workflowType, String processId, Integer seq);
}
