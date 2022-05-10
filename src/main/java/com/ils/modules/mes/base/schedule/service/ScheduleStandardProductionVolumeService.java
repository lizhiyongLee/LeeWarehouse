package com.ils.modules.mes.base.schedule.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ils.modules.mes.base.schedule.entity.ScheduleStandardProductionVolume;
import com.ils.modules.mes.util.TreeNode;

import java.util.List;

/**
 * @Description: 标准产能
 * @Author: fengyi
 * @Date: 2021-02-01
 * @Version: V1.0
 */
public interface ScheduleStandardProductionVolumeService extends IService<ScheduleStandardProductionVolume> {

    /**
     * 
     * 返回批量设置失败的标准产能
     * 
     * @param scheduleStandardProductionVolume
     * @return
     * @date 2021年2月20日
     */
    public List<ScheduleStandardProductionVolume>
        saveScheduleStandardProductionVolume(ScheduleStandardProductionVolume scheduleStandardProductionVolume);
    
    /**
     * 
     * 返回成功
     * 
     * @param scheduleStandardProductionVolume
     * @return
     * @date 2021年2月20日
     */
    public boolean
        updateScheduleStandardProductionVolume(ScheduleStandardProductionVolume scheduleStandardProductionVolume);
    
    /**
     * 
     * 更新状态
     * 
     * @param id
     * @param status
     */
    public void updateStatus(String id, String status);

    /**
     * 删除
     * 
     * @param id
     */
    public void delScheduleStandardProductionVolume (String id);
    
    /**
     * 批量删除
     * @param idList
     */
    public void delBatchScheduleStandardProductionVolume (List<String> idList);

    /**
     * 
     * 查询工位
     * 
     * @param id
     * @param standardType
     * @return
     * @date 2021年2月22日
     */
    public List<TreeNode> queryStationById(String id, String standardType);
}
