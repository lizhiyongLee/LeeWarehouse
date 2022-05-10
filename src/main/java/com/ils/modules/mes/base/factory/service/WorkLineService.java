package com.ils.modules.mes.base.factory.service;

import java.util.List;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ils.modules.mes.base.factory.entity.WorkLine;
import com.ils.modules.mes.base.factory.vo.WorkLineVO;

/**
 * @Description: 产线
 * @Author: fengyi
 * @Date: 2020-10-14
 * @Version: V1.0
 */
public interface WorkLineService extends IService<WorkLine> {

	/**
	 * 添加一对多
	 * @param workLineVO
	 */
	public void saveMain(WorkLineVO workLineVO) ;
	
	/**
	 * 修改一对多
	 * @param workLineVO
	 */
	public void updateMain(WorkLineVO workLineVO);
	
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
	
}
