package com.ils.modules.mes.base.factory.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ils.common.exception.ILSBootException;
import com.ils.modules.mes.base.factory.entity.WorkLine;
import com.ils.modules.mes.base.factory.entity.WorkShop;
import com.ils.modules.mes.base.factory.entity.WorkShopEmployee;
import com.ils.modules.mes.base.factory.entity.Workstation;
import com.ils.modules.mes.base.factory.mapper.WorkShopEmployeeMapper;
import com.ils.modules.mes.base.factory.mapper.WorkShopMapper;
import com.ils.modules.mes.base.factory.service.WorkLineService;
import com.ils.modules.mes.base.factory.service.WorkShopService;
import com.ils.modules.mes.base.factory.service.WorkstationService;
import com.ils.modules.mes.base.factory.vo.NodeTreeVO;
import com.ils.modules.mes.base.factory.vo.NodeVO;
import com.ils.modules.mes.base.factory.vo.WorkShopVO;
import com.ils.modules.mes.config.service.DefineFieldValueService;
import com.ils.modules.mes.constants.TableCodeConstants;
import com.ils.modules.mes.enums.FactoryAreaTypeEnum;
import com.ils.modules.mes.enums.ZeroOrOneEnum;
import com.ils.modules.mes.util.CommonUtil;
import com.ils.modules.mes.util.ConvertTreeUtil;
import com.ils.modules.mes.util.TreeNode;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.*;


/**
 * @Description: 车间
 * @Author: fengyi
 * @Date: 2020-10-14
 * @Version: V1.0
 */
@Service
public class WorkShopServiceImpl extends ServiceImpl<WorkShopMapper, WorkShop> implements WorkShopService {

    @Autowired
    private WorkShopMapper workShopMapper;
    @Autowired
    private WorkShopEmployeeMapper workShopEmployeeMapper;

    @Autowired
    private DefineFieldValueService defineFieldValueService;

    @Autowired
    @Lazy
    private WorkLineService workLineService;

    @Autowired
    @Lazy
    private WorkstationService workstationService;

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void saveMain(WorkShopVO workShopVO) {
        WorkShop workShop = new WorkShop();
        BeanUtils.copyProperties(workShopVO, workShop);
        this.checkCondition(workShop);

        workShopMapper.insert(workShop);
        for (WorkShopEmployee entity : workShopVO.getWorkShopEmployeeList()) {
            //外键设置
            entity.setWorkShopId(workShop.getId());
            workShopEmployeeMapper.insert(entity);
        }

        workShopVO.setId(workShop.getId());
        defineFieldValueService.saveDefineFieldValue(workShopVO.getLstDefineFields(),
                TableCodeConstants.WORK_SHOP_TABLE_CODE, workShop.getId());
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void updateMain(WorkShopVO workShopVO) {
        WorkShop workShop = new WorkShop();
        BeanUtils.copyProperties(workShopVO, workShop);
        this.checkCondition(workShop);

        workShopMapper.updateById(workShop);

        //1.先删除子表数据
        workShopEmployeeMapper.deleteByMainId(workShop.getId());

        //2.子表数据重新插入
        for (WorkShopEmployee entity : workShopVO.getWorkShopEmployeeList()) {
            //外键设置
            entity.setWorkShopId(workShop.getId());
            workShopEmployeeMapper.insert(entity);
        }

        workShopVO.setId(workShop.getId());
        defineFieldValueService.saveDefineFieldValue(workShopVO.getLstDefineFields(),
                TableCodeConstants.WORK_SHOP_TABLE_CODE, workShop.getId());

    }

    private void checkCondition(WorkShop workShop) {
        // 编码不重复
        QueryWrapper<WorkShop> queryWrapper = new QueryWrapper();
        queryWrapper.eq("shop_code", workShop.getShopCode());
        if (StringUtils.isNoneBlank(workShop.getId())) {
            queryWrapper.ne("id", workShop.getId());
        }
        WorkShop obj = baseMapper.selectOne(queryWrapper);
        if (obj != null) {
            throw new ILSBootException("B-FCT-0010");
        }

        if (StringUtils.isNoneBlank(workShop.getId())) {
            if (!this.checkUpdateStatusCondition(workShop)) {
                throw new ILSBootException("B-FCT-0002");
            }
        }
    }

    /**
     * 检查停用车间时是否满足条件,false是下面存在没有停用的产线或工位
     *
     * @return true 满足，false 不满足
     * @date 2020年10月16日
     */
    private boolean checkUpdateStatusCondition(WorkShop workShop) {
        if (ZeroOrOneEnum.ZERO.getStrCode().equals(workShop.getStatus())) {
            QueryWrapper<WorkLine> queryWrapper = new QueryWrapper<WorkLine>();
            queryWrapper.eq("up_area", workShop.getId());
            queryWrapper.eq("status", ZeroOrOneEnum.ONE.getStrCode());
            queryWrapper.eq("tenant_id", workShop.getTenantId());
            int iCount = workLineService.count(queryWrapper);
            if (iCount == 0) {
                QueryWrapper<Workstation> queryStationWrapper = new QueryWrapper<Workstation>();
                queryStationWrapper.eq("up_area", workShop.getId());
                queryStationWrapper.eq("status", ZeroOrOneEnum.ONE.getStrCode());
                queryStationWrapper.eq("tenant_id", workShop.getTenantId());
                iCount = workstationService.count(queryStationWrapper);
                return iCount == 0 ? true : false;
            } else {
                return false;
            }
        }
        return true;
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void delMain(String id) {
        workShopEmployeeMapper.deleteByMainId(id);
        workShopMapper.deleteById(id);
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void delBatchMain(List<String> idList) {
        for (Serializable id : idList) {
            workShopEmployeeMapper.deleteByMainId(id.toString());
            workShopMapper.deleteById(id);
        }
    }

    @Override
    public List<NodeVO> queryInstitutionList(QueryWrapper queryWrapper) {
        return workShopMapper.queryInstitutionList(queryWrapper);
    }

    @Override
    public List<TreeNode> queryInstitutionTreeList(String status, String name) {

        QueryWrapper<NodeVO> queryWrapper = new QueryWrapper<NodeVO>();
        queryWrapper.eq("a.tenant_id", CommonUtil.getTenantId());
        if (StringUtils.isNotBlank(status)) {
            String[] split = status.split(",");
            queryWrapper.in("a.status", Arrays.asList(split));
        }

        List<NodeVO> lstNodeVO = this.queryInstitutionList(queryWrapper);
        if (lstNodeVO == null || lstNodeVO.size() == 0) {
            return new ArrayList<TreeNode>(0);
        }

        // 如果name不为空，根据name过滤节点
        if (StringUtils.isNotBlank(name)) {
            List<NodeVO> lstExistNameNode = new ArrayList<NodeVO>(16);
            Map<String, NodeVO> map = new HashMap<String, NodeVO>(64);
            for (NodeVO node : lstNodeVO) {
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
            for (NodeVO nameNode : lstExistNameNode) {
                this.recursionParentNode(nameNode, idSet, map);
                this.recursionSubNode(nameNode, idSet, map);
            }

            List<NodeVO> lstNewNodeVO = new ArrayList<NodeVO>(idSet.size());
            for (String id : idSet) {
                lstNewNodeVO.add(map.get(id));
            }
            lstNodeVO = lstNewNodeVO;
        }


        // 按树形组件要求初始化Treemodel
        List<TreeNode> lstTreeModel = new ArrayList();
        List<TreeNode> lstMetaModel = new ArrayList();
        for (NodeVO nodeVO : lstNodeVO) {
            NodeTreeVO nodeTreeVO = new NodeTreeVO(nodeVO);
            lstMetaModel.add(nodeTreeVO);
        }

        // 构造树形结构
        ConvertTreeUtil.getTreeModelList(lstTreeModel, lstMetaModel, null);

        return lstTreeModel;

    }

    /**
     * 递归获取父节点
     *
     * @param nodeVO
     * @param idSet
     * @param map
     * @date 2020年10月16日
     */
    private void recursionParentNode(NodeVO nodeVO, Set<String> idSet, Map<String, NodeVO> map) {

        boolean bFalg = idSet.add(nodeVO.getId());
        if (!bFalg) {
            return;
        }

        if (StringUtils.isNoneBlank(nodeVO.getUpArea())) {
            NodeVO tempVO = map.get(nodeVO.getUpArea());
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
    private void recursionSubNode(NodeVO nodeVO, Set<String> idSet, Map<String, NodeVO> map) {
        map.keySet().forEach(key -> {
            if (map.get(key).getUpArea().equals(nodeVO.getId())) {
                idSet.add(map.get(key).getId());
                this.recursionSubNode(map.get(key), idSet, map);
            }
        });
    }

    @Override
    public List<TreeNode> queryStationTreeList() {
        QueryWrapper<NodeVO> queryWrapper = new QueryWrapper<NodeVO>();
        queryWrapper.eq("a.tenant_id", CommonUtil.getTenantId());
        queryWrapper.eq("a.status", ZeroOrOneEnum.ONE.getStrCode());
        List<NodeVO> lstNodeVO = workShopMapper.queryStationList(queryWrapper);

        List<TreeNode> lstTreeModel = new ArrayList();
        List<TreeNode> lstMetaModel = new ArrayList();
        for (NodeVO nodeVO : lstNodeVO) {
            NodeTreeVO nodeTreeVO = new NodeTreeVO(nodeVO);
            lstMetaModel.add(nodeTreeVO);
        }

        // 构造树形结构
        ConvertTreeUtil.getTreeModelList(lstTreeModel, lstMetaModel, null);

        //排除没有工位的产线
        for (TreeNode treeNode : lstTreeModel) {
            NodeTreeVO nodeTopTreeVO = (NodeTreeVO) treeNode;
            List<NodeTreeVO> lstTwoTreeVO = nodeTopTreeVO.getChildren();
            Iterator<NodeTreeVO> nodeTwoIterator = lstTwoTreeVO.iterator();
            while (nodeTwoIterator.hasNext()) {
                NodeTreeVO nodeTwoTreeVO = nodeTwoIterator.next();
                List<NodeTreeVO> tempList = nodeTwoTreeVO.getChildren();
                boolean isChild = !FactoryAreaTypeEnum.WORKSTATION.getValue().equals(nodeTwoTreeVO.getNodeType())
                        && (tempList != null && tempList.size() == 0);
                if (isChild) {
                    nodeTwoIterator.remove();
                }
            }
        }

        //排除没有工位的车间
        Iterator<TreeNode> nodeTopIterator = lstTreeModel.iterator();
        while (nodeTopIterator.hasNext()) {
            NodeTreeVO nodeTopTreeVO = (NodeTreeVO) nodeTopIterator.next();
            if (nodeTopTreeVO.getChildren() != null && nodeTopTreeVO.getChildren().size() == 0) {
                nodeTopIterator.remove();
            }
        }
        return lstTreeModel;
    }

    @Override
    public List<TreeNode> queryAssignStationTreeList(List<String> stationIds) {
        QueryWrapper<NodeVO> queryWrapper = new QueryWrapper<NodeVO>();
        queryWrapper.eq("status", ZeroOrOneEnum.ONE.getStrCode());
        queryWrapper.in("id", stationIds);
        List<NodeVO> lstNodeVO = workShopMapper.queryAssignStationTreeList(queryWrapper);

        List<TreeNode> lstTreeModel = new ArrayList();
        List<TreeNode> lstMetaModel = new ArrayList();
        for (NodeVO nodeVO : lstNodeVO) {
            NodeTreeVO nodeTreeVO = new NodeTreeVO(nodeVO);
            lstMetaModel.add(nodeTreeVO);
        }

        // 构造树形结构
        ConvertTreeUtil.getTreeModelList(lstTreeModel, lstMetaModel, null);

        // 排除没有工位的产线
        for (TreeNode treeNode : lstTreeModel) {
            NodeTreeVO nodeTopTreeVO = (NodeTreeVO) treeNode;
            List<NodeTreeVO> lstTwoTreeVO = nodeTopTreeVO.getChildren();
            Iterator<NodeTreeVO> nodeTwoIterator = lstTwoTreeVO.iterator();
            while (nodeTwoIterator.hasNext()) {
                NodeTreeVO nodeTwoTreeVO = nodeTwoIterator.next();
                List<NodeTreeVO> tempList = nodeTwoTreeVO.getChildren();
                boolean isChild = !FactoryAreaTypeEnum.WORKSTATION.getValue().equals(nodeTwoTreeVO.getNodeType())
                        && (tempList != null && tempList.size() == 0);
                if (isChild) {
                    nodeTwoIterator.remove();
                }
            }
        }

        // 排除没有工位的车间
        Iterator<TreeNode> nodeTopIterator = lstTreeModel.iterator();
        while (nodeTopIterator.hasNext()) {
            NodeTreeVO nodeTopTreeVO = (NodeTreeVO) nodeTopIterator.next();
            if (nodeTopTreeVO.getChildren() != null && nodeTopTreeVO.getChildren().size() == 0) {
                nodeTopIterator.remove();
            }
        }
        return lstTreeModel;
    }

}
