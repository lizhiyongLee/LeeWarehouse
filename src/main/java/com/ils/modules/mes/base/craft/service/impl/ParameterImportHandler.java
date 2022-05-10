package com.ils.modules.mes.base.craft.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ils.common.constant.CommonConstant;
import com.ils.common.exception.ILSBootException;
import com.ils.common.system.vo.LoginUser;
import com.ils.modules.mes.base.craft.entity.Parameter;
import com.ils.modules.mes.base.craft.service.ParameterService;
import com.ils.modules.mes.enums.ZeroOrOneEnum;
import com.ils.modules.mes.util.CommonUtil;
import com.ils.modules.system.service.AbstractExcelHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 参数模板导入类
 *
 * @author Anna.
 * @date 2021/10/18 10:59
 */
@Service
@Slf4j
public class ParameterImportHandler extends AbstractExcelHandler {
    @Autowired
    private ParameterService parameterService;

    @Override
    public void importExcel(List<Map<String, Object>> dataList) {

        List<Parameter> parameterList = new ArrayList<>(16);

        //获取登录用户
        LoginUser loginUser = CommonUtil.getLoginUser();
        //获取当前时间
        Date currentDate = new Date();
        //查询参数管理里的所有编码
        List<String> existParameterList = getExistParameterList();

        for (Map<String, Object> data : dataList) {
            Parameter parameter = BeanUtil.toBeanIgnoreError(data, Parameter.class);
            if (existParameterList.contains(parameter.getParaCode())) {
                throw new ILSBootException("参数编码[" + parameter.getParaCode() + "]已存在，不能继续导入");
            }
            //设置基础属性
            parameter.setTenantId(CommonUtil.getTenantId());
            parameter.setCreateBy(loginUser.getUsername());
            parameter.setCreateTime(currentDate);
            parameter.setUpdateBy(loginUser.getUsername());
            parameter.setUpdateTime(currentDate);
            parameter.setDeleted(CommonConstant.DEL_FLAG_0);
            parameterList.add(parameter);
        }
        parameterService.saveBatch(parameterList);
    }


    /**
     * 获取已存在的参数管理编码
     *
     * @return
     */
    private List<String> getExistParameterList() {
        List<String> parameterCodeList = new ArrayList<>();
        QueryWrapper<Parameter> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("is_deleted", ZeroOrOneEnum.ZERO.getIcode());
        List<Parameter> parameterList = parameterService.list(queryWrapper);
        if (CollectionUtil.isNotEmpty(parameterList)) {
            parameterCodeList = parameterList.stream().map(Parameter::getParaCode).collect(Collectors.toList());
        }
        return parameterCodeList;
    }
}
