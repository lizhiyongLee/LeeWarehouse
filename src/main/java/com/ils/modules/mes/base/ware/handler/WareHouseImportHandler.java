package com.ils.modules.mes.base.ware.handler;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ils.common.constant.CommonConstant;
import com.ils.common.exception.ILSBootException;
import com.ils.common.system.vo.LoginUser;
import com.ils.modules.mes.base.ware.entity.WareHouse;
import com.ils.modules.mes.base.ware.service.WareHouseService;
import com.ils.modules.mes.enums.ZeroOrOneEnum;
import com.ils.modules.mes.util.CommonUtil;
import com.ils.modules.system.service.AbstractExcelHandler;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 仓库导入类
 *
 * @author Anna.
 * @date 2021/6/23 15:17
 */
public class WareHouseImportHandler extends AbstractExcelHandler {

    @Autowired
    private WareHouseService wareHouseService;

    @Override
    public void importExcel(List<Map<String, Object>> dataList) {

        //获取登录用户
        LoginUser loginUser = CommonUtil.getLoginUser();
        //获取当前时间
        Date currentDate = new Date();
        //待导入的数据
        List<WareHouse> wareHouseList = new ArrayList<>(16);

        //查询已存在的仓库
        QueryWrapper<WareHouse> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("is_deleted", ZeroOrOneEnum.ZERO.getIcode());
        List<WareHouse> existWareHouseList = wareHouseService.list(queryWrapper);

        //构建数据
        for (Map<String, Object> data : dataList) {

            //从excel中获取成实体类
            WareHouse wareHouse = BeanUtil.toBeanIgnoreError(data, WareHouse.class);

            //保存信息，如果为空则不需要返回
            StringBuffer res = new StringBuffer();

            //判断编码是否已存在
            List<String> codeList = existWareHouseList.stream().map(WareHouse::getHouseCode).collect(Collectors.toList());
            if (codeList.contains(wareHouse.getHouseCode())) {
                res.append("仓库编码[" + wareHouse.getHouseCode() + "]已存在，不能继续导入");
            }

            //判断该仓库名字是否存在
            List<String> nameList = existWareHouseList.stream().map(WareHouse::getHouseName).collect(Collectors.toList());
            if (nameList.contains(wareHouse.getHouseName())) {
                res.append("仓库名称[" + wareHouse.getHouseName() + "]已存在,不能继续导入");
            }

            //设置基础属性
            wareHouse.setTenantId(CommonUtil.getTenantId());
            wareHouse.setCreateBy(loginUser.getUsername());
            wareHouse.setCreateTime(currentDate);
            wareHouse.setUpdateBy(loginUser.getUsername());
            wareHouse.setUpdateTime(currentDate);
            wareHouse.setDeleted(CommonConstant.DEL_FLAG_0);
            wareHouseList.add(wareHouse);
            if (res.length() > 0) {
                throw new ILSBootException(res.toString());
            }
        }
        wareHouseService.saveBatch(wareHouseList);
    }
}
