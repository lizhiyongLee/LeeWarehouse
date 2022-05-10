package com.ils.modules.mes.base.qc.service;

import java.util.List;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ils.modules.mes.base.qc.entity.QcMethodItem;

/**
 * @Description: 质检方案关联物料
 * @Author: fengyi
 * @Date: 2020-10-20
 * @Version: V1.0
 */
public interface QcMethodItemService extends IService<QcMethodItem> {

    /**
     * 通过父ID查询列表
     * @param mainId
     * @return
     */
	public List<QcMethodItem> selectByMainId(String mainId);
}
