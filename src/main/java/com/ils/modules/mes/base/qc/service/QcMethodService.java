package com.ils.modules.mes.base.qc.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ils.modules.mes.base.qc.entity.QcMethod;
import com.ils.modules.mes.base.qc.entity.QcMethodDetail;
import com.ils.modules.mes.base.qc.vo.QcMethodVO;

import java.util.List;

/**
 * @Description: 质检方案
 * @Author: fengyi
 * @Date: 2020-10-20
 * @Version: V1.0
 */
public interface QcMethodService extends IService<QcMethod> {

	/**
	 * 添加一对多
	 * @param qcMethod
	 * @param qcMethodDetailList
	 * @return
	 */
	public Integer saveMain(QcMethod qcMethod,List<QcMethodDetail> qcMethodDetailList) ;


	/**
	 * 添加一对多List
	 * @param qcMethodVOList
	 */
	public void saveMainList(List<QcMethodVO> qcMethodVOList) ;

	/**
	 * 修改一对多
	 * @param qcMethod
    * @param qcMethodDetailList
    * @param qcMethodItemList
	 */
	public void updateMain(QcMethod qcMethod,List<QcMethodDetail> qcMethodDetailList);
	
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
	 * 查询质检方案
	 * @param qcType
	 * @param id
	 * @return
	 */
	public List<QcMethod> queryQcMeThodByItemIdAndQcType(String qcType,String id);
	
}
