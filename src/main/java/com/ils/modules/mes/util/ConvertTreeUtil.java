package com.ils.modules.mes.util;

import java.util.List;

import com.ils.common.util.ConvertUtils;


/**
  * 从对象列表转换成树结构
 * 
 * @author fengyi
 * @date 2020/04/02
 */
public class ConvertTreeUtil {

    private static final String ROOT_ONE_FLAG = "-1";
        
    /**
     * @Description: 根据metaList里面对象构建树形结构TreeList
     * @param  treeList  接收树形结构的参数
     * @param  metaList 传入待构建的对象列表
     * @param  temp   递归用到的临时对象，调用处直接传NULL
     */
    public static void getTreeModelList(List<TreeNode> treeList, List<TreeNode> metaList, TreeNode temp) {
        for (TreeNode metaNode : metaList) {
            String tempPid = metaNode.getParentId();
            if (isRoot(tempPid)&&temp==null) {
                treeList.add(metaNode);
                getTreeModelList(treeList, metaList, metaNode);
            } else if (temp != null && tempPid != null && tempPid.equals(temp.getKey())) {
                temp.getChildren().add(metaNode);
                getTreeModelList(treeList, metaList, metaNode);
            }

        }
    }
    
     /**
      * @Description: 判断是否是根节点
      * @return true 是根节点, false不是根节点
      * @param  parentId 父节点ID
      */
    public static Boolean isRoot(String parentId) {
        if (ConvertUtils.isEmpty(parentId) || ROOT_ONE_FLAG.equals(parentId)) {
            return true;
        }else {
            return false;
        }
    }

}
