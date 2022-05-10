package com.ils.modules.mes.produce.rule;

import com.ils.modules.mes.produce.util.AutoScheContextEnv;
import com.ils.modules.mes.produce.vo.AutoScheWorkstationVO;

import java.util.List;

/**
 * 查找工位接口
 *
 * @author niushuai
 * @date 2021/5/7 10:31:47
 */
public interface FindStationRuleService {

    /**
     * 初始化工位 赋值 workstationIds workstationMap processWorkstationMap
     * @param autoScheContextEnv 所有可用工位
     * @author niushuai
     * @date: 2021/5/7 10:32:40
     * @return: {@link AutoScheWorkstationVO}
     */
    public void initWorkstation(AutoScheContextEnv autoScheContextEnv);

    /**
     * 获取所有工位上在基准时间之后的任务 赋值 autoScheProcessTaskTimeMap
     * @param autoScheContextEnv 所有可用工位
     * @author niushuai
     * @date: 2021/5/7 10:32:40
     * @return: {@link AutoScheWorkstationVO}
     */
    public void initWorkPlanTask(AutoScheContextEnv autoScheContextEnv);
}
