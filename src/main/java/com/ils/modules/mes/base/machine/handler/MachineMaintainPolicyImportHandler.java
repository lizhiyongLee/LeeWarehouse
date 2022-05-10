package com.ils.modules.mes.base.machine.handler;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ils.common.constant.CommonConstant;
import com.ils.common.exception.ILSBootException;
import com.ils.common.system.vo.LoginUser;
import com.ils.modules.mes.base.machine.entity.MachineMaintainPolicy;
import com.ils.modules.mes.base.machine.service.MachineMaintainPolicyService;
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
 * 设备维保策略组导入处理类
 *
 * @author Anna.
 * @date 2021/8/9 14:02
 */
@Service
@Slf4j
public class MachineMaintainPolicyImportHandler extends AbstractExcelHandler {
    @Autowired
    private MachineMaintainPolicyService machineMaintainPolicyService;

    @Override
    public void importExcel(List<Map<String, Object>> dataList) {
        //获取登录用户
        LoginUser loginUser = CommonUtil.getLoginUser();
        //获取当前时间
        Date currentDate = new Date();
        //查询已存在策略组分类名称集合
        QueryWrapper<MachineMaintainPolicy> queryWrapper = new QueryWrapper<>();
        List<MachineMaintainPolicy> list = machineMaintainPolicyService.list();

        //将list以名字分组
        List<String> dataNames = list.stream().map(MachineMaintainPolicy::getDataName).collect(Collectors.toList());

        //导入集合
        List<MachineMaintainPolicy> importList = new ArrayList<>(16);
        for (Map<String, Object> data : dataList) {
            MachineMaintainPolicy machineMaintainPolicy = BeanUtil.toBeanIgnoreError(data, MachineMaintainPolicy.class);

            if (dataNames.contains(machineMaintainPolicy.getDataName())) {
                throw new ILSBootException("P-MC-0104", machineMaintainPolicy.getDataName());
            }

            machineMaintainPolicy.setTenantId(CommonUtil.getTenantId());
            machineMaintainPolicy.setCreateBy(loginUser.getUsername());
            machineMaintainPolicy.setCreateTime(currentDate);
            machineMaintainPolicy.setUpdateBy(loginUser.getUsername());
            machineMaintainPolicy.setUpdateTime(currentDate);
            machineMaintainPolicy.setDeleted(CommonConstant.DEL_FLAG_0);
            importList.add(machineMaintainPolicy);
        }

        //保存
        machineMaintainPolicyService.saveBatch(importList);
    }
}
