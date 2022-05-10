package com.ils.modules.mes.config.service;

import java.util.List;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ils.modules.mes.config.entity.DefineFieldValue;
import com.ils.modules.mes.config.vo.DefineFieldValueVO;

/**
 * @Description: 自定义字段值存储
 * @Author: fengyi
 * @Date: 2020-10-13
 * @Version: V1.0
 */
public interface DefineFieldValueService extends IService<DefineFieldValue> {

    /**
     * 添加
     * @param defineFieldValue
     */
    public void saveDefineFieldValue(DefineFieldValue defineFieldValue) ;
    
    /**
     * 修改
     * @param defineFieldValue
     */
    public void updateDefineFieldValue(DefineFieldValue defineFieldValue);
    
    /**
     * 删除
     * @param id
     */
    public void delDefineFieldValue (String id);
    
    /**
     * 批量删除
     * @param idList
     */
    public void delBatchDefineFieldValue (List<String> idList);

    /**
     * 查询业务表单自定义字段
     * 
     * @param tableCode
     * @param mainId
     * @return List<DefineFieldValueVO>
     */
    List<DefineFieldValueVO> queryDefineFieldValue(String tableCode, String mainId);

    /**
     * 保存业务表单自定义字段
     * 
     * @param userId
     * @param tableCode
     * @ @return
     */
    /**
     * 
     * 保存业务表单自定义字段
     * 
     * @param lstDefineFieldValueVO
     * @param tableCode
     * @param mainId
     * @date 2020年10月22日
     */
    void saveDefineFieldValue(List<DefineFieldValueVO> lstDefineFieldValueVO, String tableCode, String mainId);

}
