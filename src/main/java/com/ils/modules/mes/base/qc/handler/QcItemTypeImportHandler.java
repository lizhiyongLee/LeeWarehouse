package com.ils.modules.mes.base.qc.handler;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ils.common.constant.CommonConstant;
import com.ils.common.exception.ILSBootException;
import com.ils.common.system.vo.LoginUser;
import com.ils.modules.mes.base.qc.entity.QcItemType;
import com.ils.modules.mes.base.qc.service.QcItemTypeService;
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
 * 质检项分类的导入类
 *
 * @author Anna.
 * @date 2021/6/21 19:06
 */
@Component
public class QcItemTypeImportHandler extends AbstractExcelHandler {

    @Autowired
    private QcItemTypeService qcItemTypeService;

    @Override
    public void importExcel(List<Map<String, Object>> dataList) {
        // 当前登录用户
        LoginUser loginUser = CommonUtil.getLoginUser();
        // 当前时间
        Date currentDate = new Date();

        List<QcItemType> qcItemTypeList = new ArrayList<>();

        List<String> qcTypeNameList = new ArrayList<>();

        //查询已存在的质检项集合
        QueryWrapper<QcItemType> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("is_deleted", ZeroOrOneEnum.ZERO.getIcode());
        List<QcItemType> exsitQcItemTypeList = qcItemTypeService.list(queryWrapper);

        if (CollectionUtil.isNotEmpty(exsitQcItemTypeList)) {
            qcTypeNameList = exsitQcItemTypeList.stream().map(QcItemType::getQcTypeName).collect(Collectors.toList());
        }

        for (Map<String, Object> data : dataList) {
            QcItemType qcItemType = BeanUtil.toBeanIgnoreError(data, QcItemType.class);

            //判断该质检项是否已存在
            if (qcTypeNameList.contains(qcItemType.getQcTypeName())) {
                throw new ILSBootException("质检项分类[" + qcItemType.getQcTypeName() + "]已存在，不能继续导入");
            }
            //设置基础属性
            qcItemType.setTenantId(CommonUtil.getTenantId());
            qcItemType.setCreateBy(loginUser.getUsername());
            qcItemType.setCreateTime(currentDate);
            qcItemType.setUpdateBy(loginUser.getUsername());
            qcItemType.setUpdateTime(currentDate);
            qcItemType.setDeleted(CommonConstant.DEL_FLAG_0);
            qcItemTypeList.add(qcItemType);
        }
        qcItemTypeService.saveBatch(qcItemTypeList);
    }
}
