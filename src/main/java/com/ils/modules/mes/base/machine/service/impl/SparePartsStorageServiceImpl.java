package com.ils.modules.mes.base.machine.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ils.modules.mes.base.machine.entity.SparePartsHouse;
import com.ils.modules.mes.base.machine.entity.SparePartsStorage;
import com.ils.modules.mes.base.machine.mapper.SparePartsHouseMapper;
import com.ils.modules.mes.base.machine.mapper.SparePartsStorageMapper;
import com.ils.modules.mes.base.machine.service.SparePartsHouseService;
import com.ils.modules.mes.base.machine.service.SparePartsStorageService;
import com.ils.modules.mes.base.machine.vo.SparePartsStoargeNodeTreeVO;
import com.ils.modules.mes.base.machine.vo.SparePartsStorageVO;
import com.ils.modules.mes.constants.MesCommonConstant;
import com.ils.modules.mes.enums.ZeroOrOneEnum;
import com.ils.modules.mes.util.CommonUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Description: 备件库位
 * @Author: Conner
 * @Date: 2021-02-24
 * @Version: V1.0
 */
@Service
public class SparePartsStorageServiceImpl extends ServiceImpl<SparePartsStorageMapper, SparePartsStorage> implements SparePartsStorageService {

    @Autowired
    private SparePartsHouseMapper sparePartsHouseMapper;

    @Override
    public void saveSparePartsStorage(SparePartsStorage sparePartsStorage) {
        SparePartsStorage sparePartsStorageNew = setSparePartsStorageProperty(sparePartsStorage);
        baseMapper.insert(sparePartsStorageNew);
    }

    @Override
    public void updateSparePartsStorage(SparePartsStorage sparePartsStorage) {
        SparePartsStorage sparePartsStorageNew = setSparePartsStorageProperty(sparePartsStorage);
        baseMapper.updateById(sparePartsStorageNew);
    }
    @Override
    public IPage<SparePartsStorageVO> querySparePartsHouse(Page<SparePartsStorageVO> page) {
        IPage<SparePartsStorageVO> querySparePartsHouse = baseMapper.querySparePartsHouse(page);
        List<SparePartsStorageVO> records = querySparePartsHouse.getRecords();
        for (SparePartsStorageVO sparePartsStorageVO : records) {
            sparePartsStorageVO.setStorageType("3");
        }
        return querySparePartsHouse;
    }

    @Override
    public List<SparePartsStorageVO> selcetChildList(String upStorageId) {
        List<SparePartsStorageVO> sparePartsStorageVOList = new ArrayList<>(16);
        QueryWrapper<SparePartsStorage> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("up_storage_id", upStorageId);
        List<SparePartsStorage> sparePartsStorageList = baseMapper.selectList(queryWrapper);
        for (SparePartsStorage sparePartsStorage : sparePartsStorageList) {
            SparePartsStorageVO sparePartsStorageVO = new SparePartsStorageVO();
            BeanUtils.copyProperties(sparePartsStorage, sparePartsStorageVO);
            QueryWrapper<SparePartsStorage> sparePartsStorageQueryWrapper = new QueryWrapper<>();
            sparePartsStorageQueryWrapper.eq("up_storage_id", sparePartsStorage.getId());
            sparePartsStorageQueryWrapper.eq("is_deleted", ZeroOrOneEnum.ZERO.getIcode());
            List<SparePartsStorage> sparePartsStorageOtherList = baseMapper.selectList(sparePartsStorageQueryWrapper);
            if (CommonUtil.isEmptyOrNull(sparePartsStorageOtherList)) {
                sparePartsStorageVO.setHasChild(0);
            } else {
                sparePartsStorageVO.setHasChild(1);
            }
            sparePartsStorageVOList.add(sparePartsStorageVO);
        }
        return sparePartsStorageVOList;
    }

    private SparePartsStorage setSparePartsStorageProperty(SparePartsStorage sparePartsStorage){
        String qccontrol = sparePartsStorage.getQccontrol();
        //如果上级位置是仓库
        String upStorageId = sparePartsStorage.getUpStorageId();
        SparePartsHouse sparePartsHouse = sparePartsHouseMapper.selectById(upStorageId);
        if (sparePartsHouse != null) {
            sparePartsStorage.setStorageType("1");
            sparePartsStorage.setUpStorageName(sparePartsHouse.getHouseName());
            sparePartsStorage.setUpStorageCode(sparePartsHouse.getHouseCode());
            sparePartsStorage.setUpStorageType("1");
            if (MesCommonConstant.STORAGE_RELARE_AREA_ADD_ONE.equals(qccontrol)) {
                sparePartsStorage.setQcStatus(sparePartsHouse.getQcStatus());
            }
        } else {
            SparePartsStorage upAreaWareStorage = baseMapper.selectById(upStorageId);
            if (upAreaWareStorage != null) {
                sparePartsStorage.setStorageType("2");
                sparePartsStorage.setUpStorageName(upAreaWareStorage.getStorageName());
                sparePartsStorage.setUpStorageCode(upAreaWareStorage.getStorageCode());
                sparePartsStorage.setUpStorageType(upAreaWareStorage.getStorageType());
                if (MesCommonConstant.STORAGE_RELARE_AREA_ADD_ONE.equals(qccontrol)) {
                    sparePartsStorage.setQcStatus(upAreaWareStorage.getQcStatus());
                }
            }
        }
        return sparePartsStorage;
    }

    @Autowired
    private SparePartsHouseService sparePartsHouseService;
    @Override
    public List<SparePartsStoargeNodeTreeVO> queryWareStorageTreeList() {
        QueryWrapper<SparePartsHouse> sparePartsHouseQueryWrapper = new QueryWrapper<>();
        sparePartsHouseQueryWrapper.eq("status", ZeroOrOneEnum.ONE.getStrCode());
        sparePartsHouseQueryWrapper.eq("is_deleted", ZeroOrOneEnum.ZERO.getIcode());
        List<SparePartsHouse> listStorage = sparePartsHouseService.list(sparePartsHouseQueryWrapper);
        if (listStorage.isEmpty()) {
            return new ArrayList<SparePartsStoargeNodeTreeVO>(16);
        }
        // 转换为树形节点
        List<SparePartsStoargeNodeTreeVO> lstHouse = new ArrayList<>();
        for (SparePartsHouse sparePartsHouse : listStorage) {
            SparePartsStoargeNodeTreeVO sparePartsStoargeNodeTreeVO = new SparePartsStoargeNodeTreeVO();
            sparePartsStoargeNodeTreeVO.setId(sparePartsHouse.getId());
            sparePartsStoargeNodeTreeVO.setStorageName(sparePartsHouse.getHouseName());
            sparePartsStoargeNodeTreeVO.setStorageCode(sparePartsHouse.getHouseCode());
            lstHouse.add(sparePartsStoargeNodeTreeVO);
        }
        //chaxun仓位
        QueryWrapper<SparePartsStorage> sparePartsStorageQueryWrapper = new QueryWrapper<>();
        sparePartsStorageQueryWrapper.eq("status", ZeroOrOneEnum.ONE.getStrCode());
        sparePartsStorageQueryWrapper.eq("is_deleted", ZeroOrOneEnum.ZERO.getIcode());
        List<SparePartsStorage> lstStorage = this.list(sparePartsStorageQueryWrapper);

        if (!lstStorage.isEmpty()) {
            List<SparePartsStoargeNodeTreeVO> treeVO = new ArrayList<>();
            for (SparePartsStorage sparePartsStorage : lstStorage) {
                SparePartsStoargeNodeTreeVO nodeTreeVO = new SparePartsStoargeNodeTreeVO();
                BeanUtils.copyProperties(sparePartsStorage, nodeTreeVO);
                treeVO.add(nodeTreeVO);
            }
            Map<String, List<SparePartsStoargeNodeTreeVO>> storageTreeMap =
                    treeVO.stream().collect(Collectors.groupingBy(SparePartsStoargeNodeTreeVO::getParentId));

            for (SparePartsStoargeNodeTreeVO sparePartsStoargeNodeTreeVO : lstHouse) {
                List<SparePartsStoargeNodeTreeVO> storageVOList = storageTreeMap.get(sparePartsStoargeNodeTreeVO.getId());
                if (storageVOList!=null&&!storageVOList.isEmpty()) {
                    sparePartsStoargeNodeTreeVO.setLstChildNodeTreeVO(storageVOList);
                }
            }
        }
        return lstHouse;
    }
}
