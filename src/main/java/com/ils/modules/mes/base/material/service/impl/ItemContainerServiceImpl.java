package com.ils.modules.mes.base.material.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ils.common.api.vo.Result;
import com.ils.common.exception.ILSBootException;
import com.ils.modules.mes.base.material.entity.ItemContainer;
import com.ils.modules.mes.base.material.entity.ItemContainerQty;
import com.ils.modules.mes.base.material.mapper.ItemContainerMapper;
import com.ils.modules.mes.base.material.mapper.ItemContainerQtyMapper;
import com.ils.modules.mes.base.material.service.ItemContainerService;
import com.ils.modules.mes.base.material.vo.ItemContainerQtyVO;
import com.ils.modules.mes.base.material.vo.ItemContainerVO;
import com.ils.modules.mes.enums.ItemCellPositionStatusEnum;
import com.ils.modules.mes.enums.ItemCellQrcodeStatusEnum;
import com.ils.modules.mes.enums.ZeroOrOneEnum;
import com.ils.modules.mes.material.entity.ItemCell;
import com.ils.modules.mes.material.mapper.ItemCellMapper;
import com.ils.modules.mes.util.CommonUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @Description: 物料类型
 * @Author: fengyi
 * @Date: 2020-10-22
 * @Version: V1.0
 */
@Service
public class ItemContainerServiceImpl extends ServiceImpl<ItemContainerMapper, ItemContainer> implements ItemContainerService {

    @Autowired
    private ItemContainerQtyMapper itemContainerQtyMapper;
    @Autowired
    private ItemCellMapper itemCellMapper;

    @Override
    public ItemContainerVO saveMain(ItemContainerVO itemContainerVO) {
        Integer qrCodeCount = itemCellMapper.queryCountQrcodeInAllPlace(itemContainerVO.getQrcode());
        if (qrCodeCount > 0) {
            throw new ILSBootException("P-OW-0054");
        }
        itemContainerVO.setStatus(ZeroOrOneEnum.ONE.getStrCode());
        baseMapper.insert(itemContainerVO);
        List<ItemContainerQtyVO> itemContainerQtyList = itemContainerVO.getItemContainerQtyList();
        if (!CommonUtil.isEmptyOrNull(itemContainerQtyList)) {
            itemContainerQtyList.forEach(itemContainerQty -> {
                itemContainerQty.setContainerId(itemContainerVO.getId());
                itemContainerQty.setContainerCode(itemContainerVO.getContainerCode());
                itemContainerQty.setContainerName(itemContainerVO.getContainerName());
                itemContainerQtyMapper.insert(itemContainerQty);
            });
        }
        return itemContainerVO;
    }

    @Override
    public ItemContainerVO selectById(String id) {
        ItemContainer itemContainer = baseMapper.selectById(id);
        ItemContainerVO itemContainerVO = new ItemContainerVO();
        BeanUtils.copyProperties(itemContainer, itemContainerVO);
        List<ItemContainerQty> itemContainerQtyList = itemContainerQtyMapper.selectByMainId(id);
        List<ItemContainerQtyVO> itemContainerQtyVOList = new ArrayList<>();
        itemContainerQtyList.forEach(itemContainerQty -> {
            ItemContainerQtyVO itemContainerQtyVO = new ItemContainerQtyVO();
            BeanUtils.copyProperties(itemContainerQty,itemContainerQtyVO);
            itemContainerQtyVOList.add(itemContainerQtyVO);
        });
        itemContainerVO.setItemContainerQtyList(itemContainerQtyVOList);
        return itemContainerVO;
    }

    @Override
    public ItemContainerVO updateMain(ItemContainerVO itemContainerVO) {
        baseMapper.updateById(itemContainerVO);
        //删除子表
        itemContainerQtyMapper.deleteByMainId(itemContainerVO.getId());
        //重新插入
        List<ItemContainerQtyVO> itemContainerQtyList = itemContainerVO.getItemContainerQtyList();
        if (!CommonUtil.isEmptyOrNull(itemContainerQtyList)) {
            itemContainerQtyList.forEach(itemContainerQty -> {
                itemContainerQty.setId(null);
                itemContainerQty.setContainerId(itemContainerVO.getId());
                itemContainerQty.setContainerCode(itemContainerVO.getContainerCode());
                itemContainerQty.setContainerName(itemContainerVO.getContainerName());
                itemContainerQtyMapper.insert(itemContainerQty);
            });
        }
        return itemContainerVO;
    }

    @Override
    public ItemCell checkItemCellByQrcode(String containerQrcode, String itemCellQrcode) {
        QueryWrapper<ItemContainer> itemContainerQueryWrapper = new QueryWrapper<>();
        itemContainerQueryWrapper.eq("qrcode", containerQrcode);
        itemContainerQueryWrapper.eq("status", ZeroOrOneEnum.ONE.getStrCode());
        ItemContainer itemContainer = baseMapper.selectOne(itemContainerQueryWrapper);
        if (itemContainer == null) {
            throw new ILSBootException("容器标签码错误！");
        }
        ItemContainerVO itemContainerVO = selectById(itemContainer.getId());
        List<ItemContainerQtyVO> itemContainerQtyList = itemContainerVO.getItemContainerQtyList();

        List<String> itemIdList = new ArrayList<>();
        itemContainerQtyList.forEach(itemContainerQty -> itemIdList.add(itemContainerQty.getItemId()));
        QueryWrapper<ItemCell> itemCellQueryWrapper = new QueryWrapper<>();
        if (!CommonUtil.isEmptyOrNull(itemContainerQtyList)) {
            itemCellQueryWrapper.in("item_id", itemIdList);
        }
        itemCellQueryWrapper.eq("qrcode", itemCellQrcode);
        itemCellQueryWrapper.gt("qty", ZeroOrOneEnum.ZERO.getIcode());
        itemCellQueryWrapper.eq("position_status", ItemCellPositionStatusEnum.STORAGE.getValue());
        itemCellQueryWrapper.eq("qrcode_status", ItemCellQrcodeStatusEnum.FACTORY.getValue());
        return itemCellMapper.selectOne(itemCellQueryWrapper);
    }

}
