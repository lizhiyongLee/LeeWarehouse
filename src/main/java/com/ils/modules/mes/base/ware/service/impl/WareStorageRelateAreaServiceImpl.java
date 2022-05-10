package com.ils.modules.mes.base.ware.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ils.modules.mes.base.ware.entity.WareFeedingStorageRelateArea;
import com.ils.modules.mes.base.ware.entity.WareFinishedStorageRelateArea;
import com.ils.modules.mes.base.ware.mapper.WareFeedingStorageRelateAreaMapper;
import com.ils.modules.mes.base.ware.mapper.WareFinishedStorageRelateAreaMapper;
import com.ils.modules.mes.base.ware.service.WareStorageRelateAreaService;
import com.ils.modules.mes.base.ware.vo.RelatedStorageTreeNodeVO;
import com.ils.modules.mes.base.ware.vo.RelatedStorageVO;
import com.ils.modules.mes.base.ware.vo.RelatedVO;
import com.ils.modules.mes.enums.ZeroOrOneEnum;
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
 * TODO 类描述
 *
 * @author Anna.
 * @date 2020/12/4 11:17
 */
@Service
public class WareStorageRelateAreaServiceImpl implements WareStorageRelateAreaService {

    private static final String ROOT_ONE_FLAG = "-1";
    @Autowired
    private WareFeedingStorageRelateAreaMapper wareFeedingStorageRelateAreaMapper;
    @Autowired
    private WareFinishedStorageRelateAreaMapper wareFinishedStorageRelateAreaMapper;

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public List<TreeNode> queryInstitutionTreeList(String name, String status) {
        QueryWrapper<RelatedStorageVO> queryWrapper = new QueryWrapper<RelatedStorageVO>();
        if (StringUtils.isNotBlank(status)) {
            queryWrapper.eq("status", status);
        }
        List<RelatedStorageVO> lstNodeVO = wareFeedingStorageRelateAreaMapper.selectRelatedStorageVO(queryWrapper);

        if (lstNodeVO == null || lstNodeVO.size() == 0) {
            return new ArrayList<TreeNode>(0);
        }
        // 如果name不为空，根据name过滤节点
        if (StringUtils.isNotBlank(name)) {
            List<RelatedStorageVO> relatedStorageVOList = new ArrayList<>(16);
            Map<String, RelatedStorageVO> map = new HashMap<String, RelatedStorageVO>(64);
            for (RelatedStorageVO relatedStorageVO : lstNodeVO) {
                if (relatedStorageVO.getName().indexOf(name) >= 0) {
                    RelatedStorageVO relateAreaVO = new RelatedStorageVO();
                    BeanUtils.copyProperties(relatedStorageVO, relateAreaVO);
                    relatedStorageVOList.add(relateAreaVO);
                }
                map.put(relatedStorageVO.getId(), relatedStorageVO);
            }
            if (relatedStorageVOList.size() == 0) {
                return new ArrayList<TreeNode>(0);
            }
            // 保存树形结构的id
            Set<String> idSet = new HashSet<String>();
            for (RelatedStorageVO nameNode : relatedStorageVOList) {
                this.recursionNodeUp(nameNode, idSet, map);
                this.recursionNodeDown(nameNode, idSet, map);
            }
            List<RelatedStorageVO> backedRelatedStorageVOList = new ArrayList<RelatedStorageVO>(idSet.size());
            for (String id : idSet) {
                backedRelatedStorageVOList.add(map.get(id));
            }
            lstNodeVO = backedRelatedStorageVOList;
        }
        // 按树形组件要求初始化Treemodel
        List<TreeNode> lstTreeModel = new ArrayList();
        List<TreeNode> lstMetaModel = new ArrayList<>();

        List<RelatedVO> allFinishedStorageList = wareFinishedStorageRelateAreaMapper.selectFinishStoragesByAreaId();
        List<RelatedVO> allFeedingStorageList = wareFeedingStorageRelateAreaMapper.selectFeedingStoragesByAreaId();
        for (RelatedStorageVO relatedStorageVO : lstNodeVO) {
            List<RelatedVO> finishedStorageList = allFinishedStorageList.stream().filter(t1 -> relatedStorageVO.getId().equals(t1.getAreaId())).collect(Collectors.toList());
            List<RelatedVO> feedingStorageList = allFeedingStorageList.stream().filter(t1 -> relatedStorageVO.getId().equals(t1.getAreaId())).collect(Collectors.toList());
            relatedStorageVO.setFinishedStorageList(finishedStorageList);
            relatedStorageVO.setFeedingStorageList(feedingStorageList);
        }

        for (RelatedStorageVO relatedStorageVO : lstNodeVO) {
            RelatedStorageTreeNodeVO relatedStorageTreeNodeVO = new RelatedStorageTreeNodeVO(relatedStorageVO);
            lstMetaModel.add(relatedStorageTreeNodeVO);
        }
        // 构造树形结构
        ConvertTreeUtil.getTreeModelList(lstTreeModel, lstMetaModel, null);
        findshopId(lstTreeModel, null, false, false);
        return lstTreeModel;
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void addFeedingStorageRelateArea(String feedingStoreageIds, String areaId) {
        wareFeedingStorageRelateAreaMapper.delByAreaId(areaId);
        if (StringUtils.isEmpty(feedingStoreageIds)||feedingStoreageIds.equals("0")){
            return;
        }
        QueryWrapper<WareFeedingStorageRelateArea> feedingStorageRelateAreaQueryWrapper = new QueryWrapper<>();
        List<String> ids = Arrays.asList(feedingStoreageIds.split(","));
        feedingStorageRelateAreaQueryWrapper.eq("area_id", areaId);
        for (String id : ids) {
            WareFeedingStorageRelateArea wareFeedingStorageRelateArea = new WareFeedingStorageRelateArea();
            wareFeedingStorageRelateArea.setAreaId(areaId);
            wareFeedingStorageRelateArea.setFeedingStorage(id);
            wareFeedingStorageRelateAreaMapper.insert(wareFeedingStorageRelateArea);
        }
    }

    @Override
    public void addFinishedStorageRelateArea(String finishedStoreageId, String areaId) {
        QueryWrapper<WareFinishedStorageRelateArea> finishedStorageRelateAreaQueryWrapper = new QueryWrapper<>();
        finishedStorageRelateAreaQueryWrapper.eq("area_id", areaId);
        wareFinishedStorageRelateAreaMapper.delByAreaId(areaId);
        WareFinishedStorageRelateArea wareFinishedStorageRelateArea = new WareFinishedStorageRelateArea();
        wareFinishedStorageRelateArea.setAreaId(areaId);
        wareFinishedStorageRelateArea.setFinishedStorage(finishedStoreageId);
        wareFinishedStorageRelateAreaMapper.insert(wareFinishedStorageRelateArea);
    }


    @Override
    public List<TreeNode> queryRelateHouse(String parentId) {
        QueryWrapper<RelatedStorageVO> relatedQueryWrapper = new QueryWrapper<>();

        relatedQueryWrapper.eq("parent_id",StringUtils.isEmpty(parentId)? "-1":parentId);

        List<RelatedStorageVO> relatedStorageVOList = wareFeedingStorageRelateAreaMapper.selectRelatedStorageVO(relatedQueryWrapper);
        List<TreeNode> treeNodes = relatedStorageVOList.stream().map(relatedStorageVO -> {
            RelatedStorageTreeNodeVO relatedStorageTreeNodeVO = new RelatedStorageTreeNodeVO();
            BeanUtils.copyProperties(relatedStorageVO, relatedStorageTreeNodeVO);
            return relatedStorageTreeNodeVO;
        }).collect(Collectors.toList());
        return treeNodes;
    }

    @Override
    public List<TreeNode> queryRelateStorageByParameter(String name, String status) {
        QueryWrapper<RelatedStorageVO> relatedQueryWrapper = new QueryWrapper<>();
        relatedQueryWrapper.like("name",name);
        relatedQueryWrapper.eq("status",status);
        List<RelatedStorageVO> relatedStorageVOList = wareFeedingStorageRelateAreaMapper.selectRelatedStorageVO(relatedQueryWrapper);
        List<RelatedStorageVO> allStorage = wareFeedingStorageRelateAreaMapper.selectRelatedStorageVO(new QueryWrapper<>());
        Set<String> idSet = new HashSet();
        for (RelatedStorageVO relatedStorageVO : relatedStorageVOList) {
            idSet.add(relatedStorageVO.getId());
            findParent(relatedStorageVO,allStorage,idSet);
        }
        List<RelatedStorageVO> newStorage = allStorage.stream().filter(relatedStorageVO -> idSet.contains(relatedStorageVO.getId())).collect(Collectors.toList());

        List<RelatedVO> allFinishedStorageList = wareFinishedStorageRelateAreaMapper.selectFinishStoragesByAreaId();
        List<RelatedVO> allFeedingStorageList = wareFeedingStorageRelateAreaMapper.selectFeedingStoragesByAreaId();
        for (RelatedStorageVO relatedStorageVO : newStorage) {
            List<RelatedVO> finishedStorageList = allFinishedStorageList.stream().filter(t1 -> relatedStorageVO.getId().equals(t1.getAreaId())).collect(Collectors.toList());
            List<RelatedVO> feedingStorageList = allFeedingStorageList.stream().filter(t1 -> relatedStorageVO.getId().equals(t1.getAreaId())).collect(Collectors.toList());
            relatedStorageVO.setFinishedStorageList(finishedStorageList);
            relatedStorageVO.setFeedingStorageList(feedingStorageList);
        }
        List<TreeNode> treeNodes = newStorage.stream().map(relatedStorageVO -> {
            RelatedStorageTreeNodeVO relatedStorageTreeNodeVO = new RelatedStorageTreeNodeVO();
            BeanUtils.copyProperties(relatedStorageVO, relatedStorageTreeNodeVO);
            return relatedStorageTreeNodeVO;
        }).collect(Collectors.toList());

        List<TreeNode> lstTreeModel = new ArrayList<TreeNode>();

        ConvertTreeUtil.getTreeModelList(lstTreeModel, treeNodes, null);

        return lstTreeModel;
    }

    void findParent(RelatedStorageVO relatedStorageVO,List<RelatedStorageVO> allStorage, Set<String> idSet){
        if (ROOT_ONE_FLAG.equals(relatedStorageVO.getUpArea())){
            return;
        }
        for (RelatedStorageVO storageVO : allStorage) {
            if (relatedStorageVO.getUpArea().equals(storageVO.getId())){
                idSet.add(storageVO.getId());
                findParent(storageVO,allStorage,idSet);
                break;
            }
        }
    }


    private void findshopId(List<TreeNode> lstTreeModel, String shopId, boolean f1, boolean f2) {
        for (TreeNode treeNode : lstTreeModel) {
            if (CollectionUtil.isEmpty(lstTreeModel)) {
                return;
            }
            if (((RelatedStorageTreeNodeVO) treeNode).getType().equals(ZeroOrOneEnum.ONE.getStrCode())) {
                shopId = ((RelatedStorageTreeNodeVO) treeNode).getId();
                f1 = true;
                f2 = ((RelatedStorageTreeNodeVO) treeNode).getHasWarehouse();
            }
            if (f1) {
                ((RelatedStorageTreeNodeVO) treeNode).setWorkshopId(shopId);
            }
            if (CollectionUtil.isNotEmpty(((RelatedStorageTreeNodeVO) treeNode).getChildren())) {
                ((RelatedStorageTreeNodeVO) treeNode).setChild(true);
            }
            if ("2".equals(((RelatedStorageTreeNodeVO) treeNode).getType()) || ("3".equals(((RelatedStorageTreeNodeVO) treeNode).getType()))) {
                ((RelatedStorageTreeNodeVO) treeNode).setHasWarehouse(f2);
            }
            findshopId(treeNode.getChildren(), shopId, f1, f2);
        }
    }

    /**
     * 向下查询关联仓位的子集合
     */
    private void recursionNodeDown(RelatedStorageVO nodeVO, Set<String> idSet, Map<String, RelatedStorageVO> map) {
        for (String mapKey : map.keySet()) {
            if (nodeVO.getId().equals(map.get(mapKey).getUpArea())) {
                idSet.add(map.get(mapKey).getId());
                RelatedStorageVO tempVO = map.get(mapKey);
                recursionNodeDown(tempVO, idSet, map);
            }
        }
    }

    /**
     * 向上查询关联仓位集合
     *
     * @param nodeVO
     * @param idSet
     * @param map
     */
    private void recursionNodeUp(RelatedStorageVO nodeVO, Set<String> idSet, Map<String, RelatedStorageVO> map) {
        boolean bFalg = idSet.add(nodeVO.getId());
        if (!bFalg) {
            return;
        }
        if (StringUtils.isNoneBlank(nodeVO.getUpArea()) && !nodeVO.getUpArea().equals(ROOT_ONE_FLAG)) {
            RelatedStorageVO tempVO = map.get(nodeVO.getUpArea());
            recursionNodeUp(tempVO, idSet, map);
        }
    }

}
