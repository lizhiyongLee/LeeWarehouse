package com.ils.modules.mes.label.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ils.modules.mes.label.entity.LabelManageLine;

import java.util.List;

/**
 * @author lishaojie
 * @description
 * @date 2021/7/13 10:54
 */
public interface LabelManageLineService extends IService<LabelManageLine> {
    /**
     * 批量打印（修改行状态）
     *
     * @param ids
     */
    void printingBatch(List<String> ids);

    /**
     * 批量作废（修改行状态）
     *
     * @param ids
     */
    void cancelBatch(List<String> ids);

    /**
     * 根据QrCode查询
     *
     * @param code
     * @return
     */
    LabelManageLine queryByCode(String code);


}
