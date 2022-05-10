package com.ils.modules.mes.base.ware.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ils.common.system.vo.DictModel;
import com.ils.modules.mes.base.ware.entity.WareHouse;
import com.ils.modules.mes.base.ware.mapper.WareHouseMapper;
import com.ils.modules.mes.base.ware.mapper.WareStorageMapper;
import com.ils.modules.mes.base.ware.service.WareHouseService;
import com.ils.modules.mes.base.ware.vo.ItemCellFinishedStorageVO;
import com.ils.modules.mes.base.ware.vo.WareHouseVO;
import com.ils.modules.mes.base.ware.vo.WareStorageVO;
import com.ils.modules.mes.config.service.DefineFieldValueService;
import com.ils.modules.mes.constants.TableCodeConstants;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @Description: 仓库定义
 * @Author: Conner
 * @Date: 2020-11-05
 * @Version: V1.0
 */
@Service
public class WareHouseServiceImpl extends ServiceImpl<WareHouseMapper, WareHouse> implements WareHouseService {

    @Autowired
    private WareStorageMapper wareStorageMapper;
    @Autowired
    private DefineFieldValueService defineFieldValueService;

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void saveWareHouse(WareHouseVO wareHouseVO) {
        WareHouse wareHouse = new WareHouse();
        BeanUtils.copyProperties(wareHouseVO, wareHouse);
        baseMapper.insert(wareHouse);
        wareHouseVO.setId(wareHouse.getId());
        defineFieldValueService.saveDefineFieldValue(wareHouseVO.getLstDefineFields(),
                TableCodeConstants.WARE_HOUSE_TABLE_CODE, wareHouse.getId());
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void updateWareHouse(WareHouseVO wareHouseVO) {
        WareHouse wareHouse = new WareHouse();
        BeanUtils.copyProperties(wareHouseVO, wareHouse);
        baseMapper.updateById(wareHouse);
        wareHouseVO.setId(wareHouse.getId());
        defineFieldValueService.saveDefineFieldValue(wareHouseVO.getLstDefineFields(),
                TableCodeConstants.WARE_HOUSE_TABLE_CODE, wareHouse.getId());
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void delWareHouse(String id) {
        baseMapper.deleteById(id);
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void delBatchWareHouse(List<String> idList) {
        baseMapper.deleteBatchIds(idList);
    }

    @Override
    public ItemCellFinishedStorageVO findFinishedStorageByStationId(String stationId) {
        return baseMapper.selectStorageByStationId(stationId);
    }

    @Override
    public IPage<WareStorageVO> queryWareHouseList(Page<WareStorageVO> page) {
        IPage<WareStorageVO> queryWareHouseList = wareStorageMapper.queryWareHouseList(page);
        List<WareStorageVO> records = queryWareHouseList.getRecords();
        for (WareStorageVO wareStorageVO : records) {
            wareStorageVO.setStorageType("3");
        }
        return queryWareHouseList;
    }

    @Override
    public ItemCellFinishedStorageVO findFinishedStorageByStorageCode(String storageCode) {
        return baseMapper.selectStorageByStorageCode(storageCode);
    }

    @Override
    public List<DictModel> queryWareHouseList() {
        return baseMapper.queryWareHouseList();
    }

    @Override
    public List<WareHouse> queryWareHouse() {
        return baseMapper.selectAll();
    }

    @Override
    public WareHouse queryByStorageCode(String storageCode) {
        return baseMapper.queryByStorageCode(storageCode);
    }
}
