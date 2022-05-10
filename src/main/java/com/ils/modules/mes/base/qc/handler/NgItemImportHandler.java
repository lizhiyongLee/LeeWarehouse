package com.ils.modules.mes.base.qc.handler;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ils.common.constant.CommonConstant;
import com.ils.common.exception.ILSBootException;
import com.ils.common.system.vo.LoginUser;
import com.ils.modules.mes.base.qc.entity.NgItem;
import com.ils.modules.mes.base.qc.entity.NgItemType;
import com.ils.modules.mes.base.qc.service.NgItemService;
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
 * 不良项分类导入
 *
 * @author Anna.
 * @date 2021/6/21 16:55
 */
@Component
public class NgItemImportHandler extends AbstractExcelHandler {
    @Autowired
    private NgItemService ngItemService;
    @Autowired
    private NgItemTypeService ngItemTypeService;

    @Override
    public void importExcel(List<Map<String, Object>> dataList) {
        //获取登录用户
        LoginUser loginUser = CommonUtil.getLoginUser();
        //获取当前时间
        Date currentDate = new Date();

        List<NgItem> ngItemList = new ArrayList<>();
        //获取已存在的不良项
        List<String> existNgItemList = getExistNgItemList();
        //获取不良项分类集合
        List<NgItemType> ngItemTypeList = getNgItemTypeList();

        StringBuffer res = new StringBuffer();
        //构建实体类
        for (Map<String, Object> data : dataList) {
            NgItem ngItem = BeanUtil.toBeanIgnoreError(data, NgItem.class);
            //判断不良项名字是否已存在
            if (existNgItemList.contains(ngItem.getNgCode())) {
                res.append("不良项编码[" + ngItem.getNgCode() + "]已存在，不能继续导入");
            }
            List<NgItemType> ngItemTypes = ngItemTypeList.stream().filter(item -> item.getNgTypeName().equals(ngItem.getNgTypeId())).collect(Collectors.toList());

            //判断系统中是否有该不良项类型
            if (CollectionUtil.isEmpty(ngItemTypes)) {
                res.append("系统中不存在[" + ngItem.getNgTypeId() + "],不能继续导入");
            } else {
                ngItem.setNgTypeId(ngItemTypes.get(0).getId());
            }
            //设置基础属性
            ngItem.setTenantId(CommonUtil.getTenantId());
            ngItem.setCreateBy(loginUser.getUsername());
            ngItem.setCreateTime(currentDate);
            ngItem.setUpdateBy(loginUser.getUsername());
            ngItem.setUpdateTime(currentDate);
            ngItem.setDeleted(CommonConstant.DEL_FLAG_0);
            ngItemList.add(ngItem);

            if (res.length() > 0) {
                throw new ILSBootException(res.toString());
            }
        }
        ngItemService.saveBatch(ngItemList);

    }

    /**
     * 获取不良项分类集合
     *
     * @return
     */
    private List<NgItemType> getNgItemTypeList() {
        QueryWrapper<NgItemType> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("is_deleted", ZeroOrOneEnum.ZERO.getIcode());
        List<NgItemType> ngItemTypeList = ngItemTypeService.list(queryWrapper);

        return ngItemTypeList;
    }

    /**
     * 获取已存在的不良项
     *
     * @return
     */
    private List<String> getExistNgItemList() {
        List<String> ngCodeList = new ArrayList<>();
        QueryWrapper<NgItem> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("is_deleted", ZeroOrOneEnum.ZERO.getIcode());
        List<NgItem> ngItemList = ngItemService.list(queryWrapper);
        if (CollectionUtil.isNotEmpty(ngItemList)) {
            ngCodeList = ngItemList.stream().map(NgItem::getNgCode).collect(Collectors.toList());
        }
        return ngCodeList;
    }

}
