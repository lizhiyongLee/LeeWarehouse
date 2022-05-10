package com.ils.modules.mes.base.craft.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ils.modules.mes.base.craft.entity.ProcessParaHead;
import com.ils.modules.mes.base.craft.vo.ProcessParaVO;

import java.util.List;

/**
 * @author lishaojie
 * @description
 * @date 2021/10/18 11:13
 */
public interface ProcessParaHeadService extends IService<ProcessParaHead> {
    /**
     * 新增数据
     *
     * @param processParaVO
     */
    void saveProcessPara(ProcessParaVO processParaVO);

    /**
     * 更新数据
     *
     * @param processParaVO
     */
    void updateProcessPara(ProcessParaVO processParaVO);

    /**
     * 根据id查询
     *
     * @param id
     * @return
     */
    ProcessParaVO queryById(String id);

    /**
     * 根据工序id查询
     * @param processId
     * @return
     */
    List<ProcessParaVO> queryByProcessId(String processId);

    /**
     * 根据工序id删除
     * @param processId
     * @return
     */
    void deleteByProcessId(String processId);
}
