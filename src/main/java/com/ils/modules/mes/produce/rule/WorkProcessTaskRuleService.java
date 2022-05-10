package com.ils.modules.mes.produce.rule;

import java.util.List;

import com.ils.modules.mes.produce.vo.AutoScheParam;
import com.ils.modules.mes.produce.vo.AutoScheWorkProcessVO;

/**
 * @Description: 获取有序工序服务接口
 * @author: fengyi
 * @date: 2021年2月25日 上午11:02:02
 */
public interface WorkProcessTaskRuleService {
    /**
     * 
     * 查询排序的工序任务
     * 
     * @param autoScheParam
     * @return
     * @date 2021年2月25日
     */
    List<AutoScheWorkProcessVO> getSortWorkProcessTaskList(AutoScheParam autoScheParam);
}
