package com.ils.modules.mes.base.ware.service;

import com.ils.modules.mes.base.ware.vo.RelatedStorageTreeNodeVO;
import com.ils.modules.mes.base.ware.vo.RelatedStorageVO;
import com.ils.modules.mes.base.ware.vo.WareRelateAreaVO;
import com.ils.modules.mes.util.TreeNode;

import java.util.List;

/**
 * TODO 类描述
 *
 * @author Anna.
 * @date 2020/12/4 11:14
 */
public interface WareStorageRelateAreaService {

    /**
     * 查询树形结构机构定义：工厂、车间、产品、工位
     *
     * @param status
     * @param name
     * @return List<TreeNode>
     * @date 2020年10月22日
     */
    List<TreeNode> queryInstitutionTreeList(String name, String status);

    /**
     * 添加关联仓位
     *
     * @param feedingStoreageIds
     * @param areaId
     */
    void addFeedingStorageRelateArea(String feedingStoreageIds, String areaId);

    /**
     * 添加完工仓位
     *
     * @param finishedStoreageIds
     * @param areaId
     */
    void addFinishedStorageRelateArea(String finishedStoreageIds, String areaId);

    List<TreeNode> queryRelateHouse(String parentId);


    List<TreeNode> queryRelateStorageByParameter(String name,String status);

}
