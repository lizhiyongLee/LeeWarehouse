package com.ils.modules.mes.base.factory.service;

import java.util.List;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ils.modules.mes.base.factory.entity.WorkShop;
import com.ils.modules.mes.base.factory.entity.WorkShopEmployee;
import com.ils.modules.mes.base.factory.vo.NodeVO;
import com.ils.modules.mes.base.factory.vo.WorkShopVO;
import com.ils.modules.mes.util.TreeNode;


/**
 * @Description: 车间
 * @Author: fengyi
 * @Date: 2020-10-14
 * @Version: V1.0
 */
public interface WorkShopService extends IService<WorkShop> {

	/**
	 * 添加一对多
	 * @param workShopVO
	 */
	public void saveMain(WorkShopVO workShopVO) ;
	
	/**
	 * 修改一对多
	 * @param workShopVO
	 */
	public void updateMain(WorkShopVO workShopVO);
	
	/**
	 * 删除一对多
	 * @param id
	 */
	public void delMain (String id);
	
	/**
	 * 批量删除一对多
	 * @param idList
	 */
	public void delBatchMain (List<String> idList);
	
    /**
     * 
     * 查询机构定义：工厂、车间、产品、工位
     * 
     * @param queryWrapper
     * @return List<NodeVO>
     * @date 2020年10月16日
     */
    public List<NodeVO> queryInstitutionList(QueryWrapper queryWrapper);


    /**
     * 
     * 查询树形结构机构定义：工厂、车间、产品、工位
     * 
     * @param status
     * @param name
     * @return List<TreeNode>
     * @date 2020年10月22日
     */
    public List<TreeNode> queryInstitutionTreeList(String status, String name);

    /**
     * 
     * 查询车间、产线、工位
     * 
     * @param queryWrapper
     * @return List<TreeNode>
     * @date 2020年10月22日
     */
    List<TreeNode> queryStationTreeList();

    /**
     * 
     * 查询车间、产线、工位
     * 
     * @param stationIds
     * @return
     * @date 2020年11月24日
     */
    List<TreeNode> queryAssignStationTreeList(List<String> stationIds);

}
