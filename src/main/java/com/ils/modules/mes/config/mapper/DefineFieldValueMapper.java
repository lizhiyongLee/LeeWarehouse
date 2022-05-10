package com.ils.modules.mes.config.mapper;

import java.util.List;

import com.ils.common.system.base.mapper.ILSMapper;
import com.ils.modules.mes.config.entity.DefineFieldValue;
import com.ils.modules.mes.config.vo.DefineFieldValueVO;

/**
 * @Description: 自定义字段值存储
 * @Author: fengyi
 * @Date: 2020-10-13
 * @Version: V1.0
 */
public interface DefineFieldValueMapper extends ILSMapper<DefineFieldValue> {

    /**
     * 查询业务表单自定义字段
     * 
     * @param tableCode
     * @param mainId
     * @param tenantId
     * @ @return
     */
    List<DefineFieldValueVO> queryDefineFieldValue(String tableCode, String mainId, String tenantId);

    /**
     * 
     * 根据表单主键删除自定义值
     * 
     * @param tableCode
     * @param mainId
     * @param tenantId
     * @return
     * @date 2020年10月13日
     */
    int deleteByMainId(String tableCode, String mainId, String tenantId);

}
