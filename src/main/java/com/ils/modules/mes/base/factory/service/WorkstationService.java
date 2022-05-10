package com.ils.modules.mes.base.factory.service;

import java.util.Collection;
import java.util.List;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ils.modules.mes.base.factory.entity.Workstation;
import com.ils.modules.mes.base.factory.entity.WorkstationEmployee;
import com.ils.modules.mes.base.factory.vo.NodeTreeVO;
import com.ils.modules.mes.base.factory.vo.WorkstationVO;


/**
 * @Description: 工位
 * @Author: fengyi
 * @Date: 2020-10-14
 * @Version: V1.0
 */
public interface WorkstationService extends IService<Workstation> {

	/**
	 * 添加一对多
	 * @param workstationVO
	 */
	public void saveMain(WorkstationVO workstationVO) ;
	
	/**
	 * 修改一对多
	 * @param workstationVO
	 */
	public void updateMain(WorkstationVO workstationVO);
	
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
     * 查询数据车间，产线以树形结构展示
     * 
     * @return
     */
    public List<NodeTreeVO> queryShopLineTreeList();

    /**
     * 
     * 根据IDs 查询
     * 
     * @param ids
     * @return
     */
    public List<Workstation> queryWorkStationByIds(Collection<String> ids);

}
