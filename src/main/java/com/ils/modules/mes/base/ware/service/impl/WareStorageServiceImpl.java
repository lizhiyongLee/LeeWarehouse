package com.ils.modules.mes.base.ware.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ils.modules.mes.base.factory.vo.NodeVO;
import com.ils.modules.mes.base.ware.entity.WareHouse;
import com.ils.modules.mes.base.ware.entity.WareStorage;
import com.ils.modules.mes.base.ware.mapper.WareHouseMapper;
import com.ils.modules.mes.base.ware.mapper.WareStorageMapper;
import com.ils.modules.mes.base.ware.service.WareHouseService;
import com.ils.modules.mes.base.ware.service.WareStorageService;
import com.ils.modules.mes.base.ware.vo.*;
import com.ils.modules.mes.config.service.DefineFieldValueService;
import com.ils.modules.mes.constants.MesCommonConstant;
import com.ils.modules.mes.constants.TableCodeConstants;
import com.ils.modules.mes.enums.NodeTypeEnum;
import com.ils.modules.mes.enums.StorageTypeEnum;
import com.ils.modules.mes.enums.ZeroOrOneEnum;
import com.ils.modules.mes.util.CommonUtil;
import com.ils.modules.mes.util.ConvertTreeUtil;
import com.ils.modules.mes.util.TreeNode;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @Description: 仓位定义
 * @Author: Conner
 * @Date: 2020-11-06
 * @Version: V1.0
 */
@Service
public class WareStorageServiceImpl extends ServiceImpl<WareStorageMapper, WareStorage> implements WareStorageService {

    /**
     * 一级仓位
     */
    private static final String STORAGE_TYPE_ONE = "1";
    /**
     * 二级仓位
     */
    private static final String STORAGE_TYPE_TWO = "2";
    /**
     * 仓库
     */
    private static final String WARE_HOUSE_TYPE = "3";
    private final String STORAGE_STATUS = "1";
    @Autowired
    private WareHouseMapper wareHouseMapper;
    @Autowired
    private WareStorageMapper wareStorageMapper;
    @Autowired
    private DefineFieldValueService defineFieldValueService;

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void saveWareStorage(WareStorageVO wareStorageVO) {
        WareStorage wareStorage = new WareStorage();
        BeanUtils.copyProperties(wareStorageVO, wareStorage);
        String qccontrol = wareStorage.getQccontrol();
        String upStorageId = wareStorage.getUpStorageId();
        //判断当前仓位的类型
        if (STORAGE_TYPE_ONE.equals(wareStorage.getStorageType())) {
            WareHouse wareHouse = wareHouseMapper.selectById(wareStorage.getUpStorageId());
            wareStorage.setUpStorageName(wareHouse.getHouseName());
            wareStorage.setUpStorageCode(wareHouse.getHouseCode());
            wareStorage.setUpStorageType(wareHouse.getType());
            //判断质量管理字段
            if (MesCommonConstant.STORAGE_RELARE_AREA_ADD_ONE.equals(qccontrol)) {
                wareStorage.setQcStatus(wareHouse.getQcStatus());
            }
        } else if (STORAGE_TYPE_TWO.equals(wareStorage.getStorageType())) {
            WareStorage upAreaWareStorage = wareStorageMapper.selectById(upStorageId);
            wareStorage.setUpStorageName(upAreaWareStorage.getStorageName());
            wareStorage.setUpStorageCode(upAreaWareStorage.getStorageCode());
            wareStorage.setUpStorageType(upAreaWareStorage.getStorageType());
            //判断质量管理字段
            if (MesCommonConstant.STORAGE_RELARE_AREA_ADD_ONE.equals(qccontrol)) {
                wareStorage.setQcStatus(upAreaWareStorage.getQcStatus());
            }
        }

        baseMapper.insert(wareStorage);

        //设置自定义字段
        wareStorageVO.setId(wareStorage.getId());
        defineFieldValueService.saveDefineFieldValue(wareStorageVO.getLstDefineFields(),
                TableCodeConstants.WARE_STORAGE_TABLE_CODE, wareStorage.getId());
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void updateWareStorage(WareStorageVO wareStorageVO) {
        WareStorage wareStorage = new WareStorage();
        BeanUtils.copyProperties(wareStorageVO, wareStorage);
        baseMapper.updateById(wareStorage);
        //设置自定义字段
        wareStorageVO.setId(wareStorage.getId());
        defineFieldValueService.saveDefineFieldValue(wareStorageVO.getLstDefineFields(),
                TableCodeConstants.WARE_STORAGE_TABLE_CODE, wareStorage.getId());
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public List<WareStorageVO> selcetChildList(String upStorageId) {
        List<WareStorageVO> wareStorageVOList = wareStorageMapper.selectByUpStorageId(upStorageId);
        return wareStorageVOList;
    }

    @Override
    public List<TreeNode> queryWareStorageTreeList() {
        List<WareStorage> wareStorageList = baseMapper.selectStorageTreeList();

        List<TreeNode> lstTreeModel = new ArrayList<>();
        List<TreeNode> lstMetaModel = new ArrayList<>();

        for (WareStorage wareStorage : wareStorageList) {
            WareStorageNodeTreeVO wareStorageNodeTreeVO = new WareStorageNodeTreeVO(wareStorage);
            lstMetaModel.add(wareStorageNodeTreeVO);
        }
        ConvertTreeUtil.getTreeModelList(lstTreeModel, lstMetaModel, null);
        return lstTreeModel;
    }

    @Override
    public List<TreeNode> queryTreeStorage(String workShopId) {
        List<WareStorageTreeVO> wareStorageTreeVOList = wareStorageMapper.selectStorageByWorkShopIdTreeVO(workShopId);
        if (wareStorageTreeVOList.isEmpty()) {
            return new ArrayList<TreeNode>(16);
        }
        List<TreeNode> lstTreeModel = new ArrayList<>();
        List<TreeNode> lstMetaModel = new ArrayList<>();

        for (WareStorageTreeVO wareStorageTreeVO : wareStorageTreeVOList) {
            StorageNodeTreeVO storageNodeTreeVO = new StorageNodeTreeVO(wareStorageTreeVO);
            lstMetaModel.add(storageNodeTreeVO);
        }
        ConvertTreeUtil.getTreeModelList(lstTreeModel, lstMetaModel, null);
        return lstTreeModel;
    }

    @Override
    public IPage<ProduceWareStorageVO> selectStorageByStationId(String areaId, String itemId,
                                                                Page<ProduceWareStorageVO> page) {
        return baseMapper.selectStorageByStationId(areaId, itemId, page);
    }

    @Override
    public List<TreeNode> queryReceivingGoodsTreeStorage(String name, String status) {
        QueryWrapper<WareStorageTreeVO> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("b.tenant_id", CommonUtil.getTenantId());
        if (StringUtils.isNotBlank(status)) {
            String[] split = status.split(",");
            queryWrapper.in("b.status", Arrays.asList(split));
        }
        List<WareStorageTreeVO> wareStorageTreeVOList = baseMapper.queryReceivingGoodsTreeStorage(queryWrapper);

        if (wareStorageTreeVOList.isEmpty()) {
            return new ArrayList<TreeNode>(16);
        }

        if (StringUtils.isNotBlank(name)) {
            List<WareStorageTreeVO> lstExistNameNode = new ArrayList<WareStorageTreeVO>(16);
            Map<String, WareStorageTreeVO> map = new HashMap<String, WareStorageTreeVO>(64);
            for (WareStorageTreeVO node : wareStorageTreeVOList) {
                if (node.getName().contains(name)) {
                    lstExistNameNode.add(node);
                }
                map.put(node.getId(), node);
            }

            if (lstExistNameNode.size() == 0) {
                return new ArrayList<TreeNode>(0);
            }

            // 保存树形结构的id
            Set<String> idSet = new HashSet<String>();
            for (WareStorageTreeVO nameNode : lstExistNameNode) {
                this.recursionParentNode(nameNode, idSet, map);
                this.recursionSubNode(nameNode, idSet, map);
            }

            List<WareStorageTreeVO> lstNewNodeVO = new ArrayList<WareStorageTreeVO>(idSet.size());
            for (String id : idSet) {
                lstNewNodeVO.add(map.get(id));
            }
            wareStorageTreeVOList = lstNewNodeVO;
        }

        List<TreeNode> lstTreeModel = new ArrayList<>();
        List<TreeNode> lstMetaModel = new ArrayList<>();

        for (WareStorageTreeVO wareStorageTreeVO : wareStorageTreeVOList) {
            StorageNodeTreeVO storageNodeTreeVO = new StorageNodeTreeVO(wareStorageTreeVO);
            lstMetaModel.add(storageNodeTreeVO);
        }

        ConvertTreeUtil.getTreeModelList(lstTreeModel, lstMetaModel, null);
        return lstTreeModel;
    }

    @Override
    public List<String> getStorageNameList(List<String> positionNameList, List<WareStorageTreeVO> wareStorageTreeVOList) {
        List<String> subLocationNameList = new ArrayList<>();

        for (String positionName : positionNameList) {
            for (WareStorageTreeVO nodeVO : wareStorageTreeVOList) {
                if (nodeVO.getName().equals(positionName)) {
                    //二级仓，直接返回
                    if (StorageTypeEnum.WARE_STORAGE_TWO.getValue().equals(nodeVO.getNodeType())) {
                        return positionNameList;
                    } else {
                        //非二级仓，找到下级所有二级仓
                        wareStorageTreeVOList.forEach(checkNodeVO -> {
                            if (checkNodeVO.getUpArea().equals(nodeVO.getId())) {
                                subLocationNameList.add(checkNodeVO.getName());
                            }
                        });
                    }
                }
            }
        }
        if (CommonUtil.isEmptyOrNull(subLocationNameList)) {
            return subLocationNameList;
        }
        return getStorageNameList(subLocationNameList, wareStorageTreeVOList);
    }


    /**
     * 递归获取父节点
     *
     * @param nodeVO
     * @param idSet
     * @param map
     * @date 2020年10月16日
     */
    private void recursionParentNode(WareStorageTreeVO nodeVO, Set<String> idSet, Map<String, WareStorageTreeVO> map) {

        boolean bFalg = idSet.add(nodeVO.getId());
        if (!bFalg) {
            return;
        }

        if (StringUtils.isNoneBlank(nodeVO.getUpArea())) {
            WareStorageTreeVO tempVO = map.get(nodeVO.getUpArea());
            recursionParentNode(tempVO, idSet, map);
        }
    }

    /**
     * 递归获取子节点
     *
     * @param nodeVO
     * @param idSet
     * @param map
     * @date 2020年10月16日
     */
    private void recursionSubNode(WareStorageTreeVO nodeVO, Set<String> idSet, Map<String, WareStorageTreeVO> map) {
        map.keySet().forEach(key -> {
            if (map.get(key).getUpArea().equals(nodeVO.getId())) {
                idSet.add(map.get(key).getId());
                this.recursionSubNode(map.get(key), idSet, map);
            }
        });
    }

    @Override
    public int queryWareHouseCount(String areaId, String storageId) {
        return baseMapper.queryWareHouseCount(areaId, storageId);
    }

    @Override
    public List<WareStorageListVO> queryWareStorageByWareHouseId(String wareHouseId) {
        QueryWrapper<WareStorage> storageQueryWrapper = new QueryWrapper<>();
        storageQueryWrapper.eq("is_deleted", ZeroOrOneEnum.ZERO.getIcode());
        storageQueryWrapper.eq("up_storage_id", wareHouseId);
        storageQueryWrapper.eq("status", STORAGE_STATUS);
        storageQueryWrapper.eq("tenant_id", CommonUtil.getTenantId());
        List<WareStorage> wareStorageList = baseMapper.selectList(storageQueryWrapper);
        List<WareStorageListVO> wareStorageListVOList = new ArrayList<>(16);
        for (WareStorage wareStorage : wareStorageList) {
            QueryWrapper<WareStorage> storageQueryWrapper1 = new QueryWrapper<>();
            storageQueryWrapper1.eq("is_deleted", ZeroOrOneEnum.ZERO.getIcode());
            storageQueryWrapper1.eq("up_storage_id", wareStorage.getId());
            storageQueryWrapper1.eq("status", STORAGE_STATUS);
            storageQueryWrapper1.eq("tenant_id", CommonUtil.getTenantId());
            List<WareStorage> wareStorageList1 = baseMapper.selectList(storageQueryWrapper1);
            WareStorageListVO wareStorageListVO = new WareStorageListVO();
            BeanUtils.copyProperties(wareStorage, wareStorageListVO);
            wareStorageListVO.setWareStorageList(wareStorageList1);
            wareStorageListVOList.add(wareStorageListVO);
        }
        return wareStorageListVOList;
    }
}
