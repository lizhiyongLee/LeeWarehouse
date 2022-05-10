package com.ils.modules.mes.material.service.impl;

import com.ils.modules.mes.material.entity.ItemReceiptLine;
import com.ils.modules.mes.material.mapper.ItemReceiptLineMapper;
import com.ils.modules.mes.material.service.ItemReceiptLineService;
import org.springframework.stereotype.Service;
import java.util.List;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @Description: 收货单行
 * @Author: wyssss
 * @Date:   2020-11-18
 * @Version: V1.0
 */
@Service
public class ItemReceiptLineServiceImpl extends ServiceImpl<ItemReceiptLineMapper, ItemReceiptLine> implements ItemReceiptLineService {
	
	@Autowired
	private ItemReceiptLineMapper itemReceiptLineMapper;
	
	@Override
	public List<ItemReceiptLine> selectByMainId(String mainId) {
		return itemReceiptLineMapper.selectByMainId(mainId);
	}
}
