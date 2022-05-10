package com.ils.modules.mes.material.service.impl;

import java.io.Serializable;
import java.util.List;

import com.ils.common.exception.ILSBootException;
import com.ils.modules.mes.base.product.vo.ItemBomDetailVO;
import com.ils.modules.mes.material.vo.ItemReceiptHeadVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ils.modules.mes.material.entity.ItemReceiptHead;
import com.ils.modules.mes.material.entity.ItemReceiptLine;
import com.ils.modules.mes.material.mapper.ItemReceiptHeadMapper;
import com.ils.modules.mes.material.mapper.ItemReceiptLineMapper;
import com.ils.modules.mes.material.service.ItemReceiptHeadService;

/**
 * @Description: 收货单头
 * @Author: wyssss
 * @Date:   2020-11-18
 * @Version: V1.0
 */
@Service
public class ItemReceiptHeadServiceImpl extends ServiceImpl<ItemReceiptHeadMapper, ItemReceiptHead> implements ItemReceiptHeadService {

	@Autowired
	private ItemReceiptHeadMapper itemReceiptHeadMapper;
	@Autowired
	private ItemReceiptLineMapper itemReceiptLineMapper;
	
	@Override
	@Transactional(rollbackFor = RuntimeException.class)
	public void saveMain(ItemReceiptHead itemReceiptHead, List<ItemReceiptLine> itemReceiptLineList) {
		//明细行必须至少添加一条
		if (itemReceiptLineList.isEmpty()) {
			throw new ILSBootException("P-AU-0072");
		}
		itemReceiptHeadMapper.insert(itemReceiptHead);
		for(ItemReceiptLine entity:itemReceiptLineList) {
			//外键设置
			entity.setHeadId(itemReceiptHead.getId());
			itemReceiptLineMapper.insert(entity);
		}
	}

	@Override
	@Transactional(rollbackFor = RuntimeException.class)
	public void saveBatchMain(List<ItemReceiptHeadVO> itemReceiptHeadList) {
		for (ItemReceiptHeadVO page : itemReceiptHeadList) {
			ItemReceiptHead po = new ItemReceiptHead();
			BeanUtils.copyProperties(page, po);
			//明细行必须至少添加一条
			if (page.getItemReceiptLineList().isEmpty()) {
				throw new ILSBootException("P-AU-0072");
			}
			this.saveMain(po, page.getItemReceiptLineList());
		}
	}

	@Override
	@Transactional(rollbackFor = RuntimeException.class)
	public void updateMain(ItemReceiptHead itemReceiptHead,List<ItemReceiptLine> itemReceiptLineList) {
		itemReceiptHeadMapper.updateById(itemReceiptHead);
		
		//1.先删除子表数据
		itemReceiptLineMapper.deleteByMainId(itemReceiptHead.getId());
		
		//2.子表数据重新插入
		for(ItemReceiptLine entity:itemReceiptLineList) {
			//外键设置
			entity.setHeadId(itemReceiptHead.getId());
			itemReceiptLineMapper.insert(entity);
		}
	}

	@Override
	@Transactional(rollbackFor = RuntimeException.class)
	public void delMain(String id) {
		itemReceiptLineMapper.deleteByMainId(id);
		itemReceiptHeadMapper.deleteById(id);
	}

	@Override
	@Transactional(rollbackFor = RuntimeException.class)
	public void delBatchMain(List<String> idList) {
		for(Serializable id:idList) {
			itemReceiptLineMapper.deleteByMainId(id.toString());
			itemReceiptHeadMapper.deleteById(id);
		}
	}

    @Override
    public void updateStatus(String status, List<String> idList) {
        for(String id : idList) {
            ItemReceiptHead updateHead = new ItemReceiptHead();
            updateHead.setId(id);
            updateHead.setReceiptStatus(status);
            itemReceiptHeadMapper.updateById(updateHead);
        }
    }
	
}
