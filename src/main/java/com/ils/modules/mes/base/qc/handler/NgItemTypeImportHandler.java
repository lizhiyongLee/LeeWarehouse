package com.ils.modules.mes.base.qc.handler;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ils.common.constant.CommonConstant;
import com.ils.common.exception.ILSBootException;
import com.ils.common.system.vo.LoginUser;
import com.ils.modules.mes.base.qc.entity.NgItemType;
import com.ils.modules.mes.base.qc.service.NgItemTypeService;
import com.ils.modules.mes.enums.ZeroOrOneEnum;
import com.ils.modules.mes.util.CommonUtil;
import com.ils.modules.system.service.AbstractExcelHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 不良项分类导入类
 *
 * @author Anna.
 * @date 2021/6/21 17:04
 */
@Component
public class NgItemTypeImportHandler extends AbstractExcelHandler {

    @Autowired
    private NgItemTypeService ngItemTypeService;

    @Override
    public void importExcel(List<Map<String, Object>> dataList) {
        //获取登录用户
        LoginUser loginUser = CommonUtil.getLoginUser();
        //获取当前时间
        Date currentDate = new Date();

        List<NgItemType> ngItemTypeList = new ArrayList<>();

        //查询已存在不良项分类名称集合
        QueryWrapper<NgItemType> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("is_deleted", ZeroOrOneEnum.ZERO.getIcode());
        List<NgItemType> existNgItemTypeList = ngItemTypeService.list(queryWrapper);

        List<String> existTypeNameList = new ArrayList<>(16);
        if (CollectionUtil.isNotEmpty(existNgItemTypeList)) {
            existTypeNameList = existNgItemTypeList.stream().map(NgItemType::getNgTypeName).collect(Collectors.toList());
        }

        for (Map<String, Object> data : dataList) {
            NgItemType ngItemType = BeanUtil.toBeanIgnoreError(data, NgItemType.class);
            if (existTypeNameList.contains(ngItemType.getNgTypeName())) {
                throw new ILSBootException("不良项分类[" + ngItemType.getNgTypeName() + "]已存在，不能继续导入");
            }
            ngItemType.setTenantId(CommonUtil.getTenantId());
            ngItemType.setCreateBy(loginUser.getUsername());
            ngItemType.setCreateTime(currentDate);
            ngItemType.setUpdateBy(loginUser.getUsername());
            ngItemType.setUpdateTime(currentDate);
            ngItemType.setDeleted(CommonConstant.DEL_FLAG_0);
            ngItemTypeList.add(ngItemType);
        }
        ngItemTypeService.saveBatch(ngItemTypeList);
    }
}
