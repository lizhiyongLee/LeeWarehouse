package com.ils.modules.mes.produce.rule.impl;

import com.ils.common.exception.ILSBootException;
import com.ils.modules.mes.produce.rule.FindStationRuleService;
import com.ils.modules.mes.produce.util.AutoScheContextEnv;
import com.ils.modules.mes.produce.vo.AutoScheWorkstationVO;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Z型搜索排位
 *
 * @author niushuai
 * @date 2021/5/7 10:35:43
 */
@Service
public class FindStationRuleZtypeServiceImpl implements FindStationRuleService {

    /**
     * 初始化工位 赋值 workstationIds workstationMap processWorkstationMap
     *
     * @param autoScheContextEnv 所有可用工位
     * @author niushuai
     * @date: 2021/5/7 10:32:40
     * @return: {@link AutoScheWorkstationVO}
     */
    @Override
    public void initWorkstation(AutoScheContextEnv autoScheContextEnv) {
        throw new ILSBootException("P-AU-0088");
    }

    /**
     * 获取所有工位上在基准时间之后的任务 赋值 autoScheProcessTaskTimeMap
     *
     * @param autoScheContextEnv 所有可用工位
     * @author niushuai
     * @date: 2021/5/7 10:32:40
     * @return: {@link AutoScheWorkstationVO}
     */
    @Override
    public void initWorkPlanTask(AutoScheContextEnv autoScheContextEnv) {
        throw new ILSBootException("P-AU-0088");
    }
}
