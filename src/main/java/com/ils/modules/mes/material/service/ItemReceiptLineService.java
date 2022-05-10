package com.ils.modules.mes.material.service;

import com.ils.modules.mes.material.entity.ItemReceiptLine;
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;

/**
 * @Description: 收货单行
 * @Author: wyssss
 * @Date:   2020-11-18
 * @Version: V1.0
 */
public interface ItemReceiptLineService extends IService<ItemReceiptLine> {

    /**
     * 通过父ID查询列表
     * @param mainId
     * @return
     */
	public List<ItemReceiptLine> selectByMainId(String mainId);
}
