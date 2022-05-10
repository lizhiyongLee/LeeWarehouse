package com.ils.modules.mes.base.qc.handler;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ils.common.constant.CommonConstant;
import com.ils.common.exception.ILSBootException;
import com.ils.common.system.vo.LoginUser;
import com.ils.modules.mes.base.qc.entity.QcItem;
import com.ils.modules.mes.base.qc.entity.QcItemType;
import com.ils.modules.mes.base.qc.service.QcItemService;
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
 * 质检项导入类
 *
 * @author Anna.
 * @date 2021/6/21 16:31
 */
@Component
public class QcItemImportHandler extends AbstractExcelHandler {
    @Autowired
    private QcItemService qcItemService;
    @Autowired
    private QcItemTypeService qcItemTypeService;

    @Override
    public void importExcel(List<Map<String, Object>> dataList) {
        // 当前登录用户
        LoginUser loginUser = CommonUtil.getLoginUser();
        // 当前时间
        Date currentDate = new Date();

        List<QcItem> qcItemList = new ArrayList<>(16);

        //查询当前质检项编码
        QueryWrapper<QcItem> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("is_deleted", ZeroOrOneEnum.ZERO.getIcode());
        List<QcItem> existQcItemList = qcItemService.list(queryWrapper);
        List<String> itemCodeList = new ArrayList<>(16);
        if (CollectionUtil.isNotEmpty(existQcItemList)) {
            itemCodeList = existQcItemList.stream().map(QcItem::getQcItemCode).collect(Collectors.toList());
        }

        List<QcItemType> qcItemTypeList = getQcItemTypeList();

        for (Map<String, Object> data : dataList) {

            StringBuffer res = new StringBuffer();

            QcItem qcItem = BeanUtil.toBeanIgnoreError(data, QcItem.class);
            // 校验编码是否存在 唯一性
            if (itemCodeList.contains(qcItem.getQcItemCode())) {
                res.append("质检项编码[" + qcItem.getQcItemCode() + "]已存在，不能继续导入");
            }
            List<QcItemType> exsitQcItemTypeList = qcItemTypeList.stream().filter(item -> item.getQcTypeName().equals(qcItem.getQcTypeId())).collect(Collectors.toList());
            if (CollectionUtil.isEmpty(exsitQcItemTypeList)) {
                res.append("不存在[" + qcItem.getQcItemName() + "]质检项分类，不能继续导入");
            }
            qcItem.setQcTypeId(exsitQcItemTypeList.get(0).getId());
            qcItem.setTenantId(CommonUtil.getTenantId());
            qcItem.setCreateBy(loginUser.getUsername());
            qcItem.setCreateTime(currentDate);
            qcItem.setUpdateBy(loginUser.getUsername());
            qcItem.setUpdateTime(currentDate);
            qcItem.setDeleted(CommonConstant.DEL_FLAG_0);
            qcItemList.add(qcItem);
            if (res.length() > 0) {
                throw new ILSBootException(res.toString());
            }
        }
        qcItemService.saveBatch(qcItemList);
    }

    /**
     * 构建质检项分类名字的集合
     *
     * @return
     */
    private List<QcItemType> getQcItemTypeList() {
        QueryWrapper<QcItemType> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("is_deleted", ZeroOrOneEnum.ZERO.getIcode());
        List<QcItemType> qcItemTypeList = qcItemTypeService.list(queryWrapper);
        return qcItemTypeList;
    }
}
