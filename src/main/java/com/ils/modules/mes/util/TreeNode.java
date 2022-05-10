package com.ils.modules.mes.util;

import java.util.List;

/**
 * @Description: 树形结构node
 * @Author: fengyi
 * @Date:   2020-06-18
 * @Version: V1.0
 */
public interface TreeNode<T> {
    
      
    /**
     * 
     * 前端数据树中的key
     * 
     * @return key
     * @date 2020年11月5日
     */
    public abstract String getKey() ;
 
    /**
     * 
     * 前端数据树中的value
     * 
     * @return value
     * @date 2020年11月5日
     */
    public abstract String getValue();

    /**
     * 
     * 前端数据树中的
     * 
     * @return title
     * @date 2020 年11月5日
     */
    public abstract String getTitle();
    
    /**
     * 
     * nodeID
     * 
     * @return ID
     * @date 2020年11月5日
     */
    public abstract String getId();
    
    /**
     * 
     * node的父节点
     * 
     * @return parentId
     * @date 2020年11月5日
     */
    public abstract String getParentId();
    
    /**
     * 
     * node的是否是叶子节点*
     * 
     * @return 1 是 0 否
     * @date 2020年11月5日
     */
    public abstract String getLeaf();
    
    /**
     * 获取子节点
     * 
     * @return List<T>
     * @date 2020年11月5日
     */
    public List<T> getChildren();
    

}
