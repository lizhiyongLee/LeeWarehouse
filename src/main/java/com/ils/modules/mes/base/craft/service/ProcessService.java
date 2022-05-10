package com.ils.modules.mes.base.craft.service;

import java.util.List;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ils.modules.mes.base.craft.entity.Process;
import com.ils.modules.mes.base.craft.vo.ProcessVO;

/**
 * @Description: 工序
 * @Author: fengyi
 * @Date: 2020-10-28
 * @Version: V1.0
 */
public interface ProcessService extends IService<Process> {

	/**
     * 
     * 保存
     * 
     * @param processVO
     * @return Process
     * @date 2020年10月28日
     */
    public Process saveMain(ProcessVO processVO);
	
    /**
     * 
     * 修改
     * 
     * @param processVO
     * @date 2020年10月28日
     */
    public void updateMain(ProcessVO processVO);;
	
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
