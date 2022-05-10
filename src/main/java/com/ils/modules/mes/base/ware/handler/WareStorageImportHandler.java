package com.ils.modules.mes.base.ware.handler;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ils.common.constant.CommonConstant;
import com.ils.common.exception.ILSBootException;
import com.ils.common.system.vo.LoginUser;
import com.ils.modules.mes.base.ware.entity.WareHouse;
import com.ils.modules.mes.base.ware.entity.WareStorage;
import com.ils.modules.mes.base.ware.service.WareHouseService;
import com.ils.modules.mes.base.ware.service.WareStorageService;
import com.ils.modules.mes.enums.StorageTypeEnum;
import com.ils.modules.mes.enums.ZeroOrOneEnum;
import com.ils.modules.mes.material.entity.ItemCell;
import com.ils.modules.mes.material.mapper.ItemCellMapper;
import com.ils.modules.mes.util.CommonUtil;
import com.ils.modules.system.service.AbstractExcelHandler;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 仓位导入类
 *
 * @author Anna.
 * @date 2021/6/23 15:40
 */
public class WareStorageImportHandler extends AbstractExcelHandler {

    @Autowired
    private WareStorageService wareStorageService;
    @Autowired
    private WareHouseService wareHouseService;
    @Autowired
    private ItemCellMapper itemCellMapper;

    @Override
    public void importExcel(List<Map<String, Object>> dataList) {

        //获取登录用户
        LoginUser loginUser = CommonUtil.getLoginUser();
        //获取当前时间
        Date currentDate = new Date();

        //待导入的仓位集合
        List<WareStorage> wareStorageList = new ArrayList<>(16);

        //获取已存在仓库集合，
        List<WareHouse> exsitWareHouseList = getWareHouseList();
        List<String> existHouseCodeList = exsitWareHouseList.stream().map(WareHouse::getHouseCode).collect(Collectors.toList());

        //获取已存在的仓位集合
        List<WareStorage> exsitWareStorageList = getWareStorageList(StorageTypeEnum.WARE_STORAGE_ONE.getValue(),true);
        List<String> exsitStorageCodeList = exsitWareStorageList.stream().map(WareStorage::getStorageCode).collect(Collectors.toList());

        //查询已存在仓位,一级和二级都查出来
        List<WareStorage> existStorageListAll = getWareStorageList(null, false);
        List<String> existStorageCodeListAll = existStorageListAll.stream().map(WareStorage::getStorageCode).collect(Collectors.toList());

        //导入数据构建
        for (Map<String, Object> data : dataList) {

            //异常信息描述，如果没有，则不返回
            StringBuffer res = new StringBuffer();

            //仓位构建
            WareStorage wareStorage = BeanUtil.toBeanIgnoreError(data, WareStorage.class);

            //判断上级仓位/仓库是否存在
            if (StorageTypeEnum.WARE_HOUSE.getValue().equals(wareStorage.getUpStorageType())) {
                if (!existHouseCodeList.contains(wareStorage.getUpStorageCode())) {
                    res.append("仓库编码[" + wareStorage.getStorageCode() + "]不存在，不能继续导入");
                }
            } else if (StorageTypeEnum.WARE_HOUSE.getValue().equals(wareStorage.getUpStorageType())) {
                if (!exsitStorageCodeList.contains(wareStorage.getUpStorageCode())) {
                    res.append("\n");
                    res.append("一级仓位[" + wareStorage.getStorageCode() + "]不存在，不能继续导入");
                }
            }else {
                res.append("\n");
                res.append("仓位["+wareStorage.getStorageName()+"]的上级类型不存在，不能继续导入");
            }

            //判断仓位编码是否重复
            if (existStorageCodeListAll.contains(wareStorage.getStorageCode())) {
                res.append("\n");
                res.append("编码["+wareStorage.getStorageCode()+"]已存在，不能继续导入");
            }

            //判断二维码是否重复
            Integer integer = itemCellMapper.queryCountQrcodeInAllPlace(wareStorage.getQrcode());
            if (integer>0){
                res.append("\n");
                res.append("二维码["+wareStorage.getQrcode()+"]已存在，不能继续导入");
            }

            if (res.length()>0){
                throw new ILSBootException(res.toString());
            }

            //设置基础属性
            wareStorage.setTenantId(CommonUtil.getTenantId());
            wareStorage.setCreateBy(loginUser.getUsername());
            wareStorage.setCreateTime(currentDate);
            wareStorage.setUpdateBy(loginUser.getUsername());
            wareStorage.setUpdateTime(currentDate);
            wareStorage.setDeleted(CommonConstant.DEL_FLAG_0);

            wareStorageList.add(wareStorage);
        }

        //判断编码是否重复
        StringBuffer errorMsg = new StringBuffer();
        Map<Object, Long> mapGroup = wareStorageList.stream().collect(Collectors.groupingBy(wareStorage -> wareStorage.getStorageCode(), Collectors.counting()));
        // 筛选Map中value大于1的key
        Stream<Object> stringStream  = mapGroup.entrySet().stream().filter(entry -> entry.getValue() > 1).map(entry -> entry.getKey());
        stringStream.forEach(str -> {
            errorMsg.append("仓库编码["+str+"]重复，不能继续导入");
        });
        if (errorMsg.length()>0){
            throw new ILSBootException(errorMsg.toString());
        }
        wareStorageService.saveBatch(wareStorageList);

    }

    public List<WareStorage> getWareStorageList(String storageType,boolean notAll) {

        QueryWrapper<WareStorage> queryWrapper = new QueryWrapper<>();
        if (notAll){
            queryWrapper.eq("storage_type", storageType);
        }
        queryWrapper.eq("is_deleted", ZeroOrOneEnum.ZERO.getIcode());
        List<WareStorage> wareStorageList = wareStorageService.list(queryWrapper);

        return wareStorageList;

    }

    public List<WareHouse> getWareHouseList() {
        QueryWrapper<WareHouse> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("is_deleted", ZeroOrOneEnum.ZERO.getIcode());
        List<WareHouse> wareStorageList = wareHouseService.list(queryWrapper);
        return wareStorageList;
    }

}
