package com.ils.modules.mes.machine.handler;

import cn.hutool.core.bean.BeanUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ils.common.api.vo.Result;
import com.ils.common.constant.CommonConstant;
import com.ils.common.exception.ILSBootException;
import com.ils.common.system.vo.LoginUser;
import com.ils.common.util.PmsUtil;
import com.ils.modules.mes.base.factory.entity.WorkShop;
import com.ils.modules.mes.base.factory.service.WorkShopService;
import com.ils.modules.mes.base.machine.entity.MachineLabel;
import com.ils.modules.mes.base.machine.entity.MachineManufacturer;
import com.ils.modules.mes.base.machine.entity.MachineType;
import com.ils.modules.mes.base.machine.service.MachineLabelService;
import com.ils.modules.mes.base.machine.service.MachineManufacturerService;
import com.ils.modules.mes.base.machine.service.MachineTypeService;
import com.ils.modules.mes.enums.ZeroOrOneEnum;
import com.ils.modules.mes.machine.entity.Machine;
import com.ils.modules.mes.machine.service.MachineService;
import com.ils.modules.mes.util.CommonUtil;
import com.ils.modules.system.service.AbstractExcelHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 设备管理导入类
 *
 * @author Anna.
 * @date 2021/7/1 15:56
 */
@Slf4j
@Service
public class MachineExcelImportHandler extends AbstractExcelHandler {
    /**
     * 设备分类
     */
    private static final String LABEL_TYPE = "1";
    /**
     * 设备等级
     */
    private static final String LEVEL_TYPE = "2";
    @Autowired
    private MachineTypeService machineTypeService;
    @Autowired
    private MachineLabelService machineLabelService;
    @Autowired
    private WorkShopService workShopService;
    @Autowired
    private MachineService machineService;
    @Autowired
    private MachineManufacturerService machineManufacturerService;


    @Override
    public void importExcel(List<Map<String, Object>> dataList) {
        //获取登录用户
        LoginUser loginUser = CommonUtil.getLoginUser();
        //获取当前时间
        Date currentDate = new Date();

        Integer successLines = 0;
        Integer errCount = 0;

        int lineNumber = 0;
        //错误信息
        StringBuffer errorMessage = new StringBuffer() ;


        for (Map<String, Object> date : dataList) {
            try {
                Machine machine = BeanUtil.toBeanIgnoreError(date, Machine.class);
                //验证并设置设备类型
                String machineType = validateMachineType(machine.getMachineTypeId());
                if (StringUtils.isEmpty(machineType)) {
                    errorMessage.append("共导入"+(lineNumber-1)+"行，第 " + lineNumber + "行数据导入失败，请检查该行设备类型数据是否合法。");
                    lineNumber++;
                    errCount++;
                    continue;
                }
                machine.setMachineTypeId(machineType);

                //判断设备标注是否存在
                String labelType = validateLabelType(machine.getLabelId());
                if (StringUtils.isEmpty(labelType)) {
                    errorMessage.append("共导入"+(lineNumber-1)+"行，第 " + lineNumber + "行数据导入失败，请检查该行设备标注数据是否合法。");
                    lineNumber++;
                    errCount++;
                    continue;
                }
                machine.setLabelId(labelType);

                //判断设备等级是否存在
                String levelId = validatelevelId(machine.getLevelId());
                if (StringUtils.isEmpty(levelId)) {
                    errorMessage.append("共导入"+(lineNumber-1)+"行，第 " + lineNumber + "行数据导入失败，请检查该行设备等级数据是否合法。");
                    lineNumber++;
                    errCount++;
                    continue;
                }
                machine.setLevelId(levelId);

                //校验车间
                String shopId = validateWorkShopId(machine.getWorkShopId());
                if (StringUtils.isEmpty(shopId)) {
                    errorMessage.append("共导入"+(lineNumber-1)+"行，第 "+ lineNumber + "行数据导入失败，请检查该行车间数据是否合法。");
                    lineNumber++;
                    errCount++;
                    continue;
                }
                machine.setWorkShopId(shopId);

                //校验供应商
                String manufacturerId = validateManufacturerId(machine.getManufacturerId());
                if (StringUtils.isEmpty(manufacturerId)) {
                    errorMessage.append("共导入"+(lineNumber-1)+"行，第 " + lineNumber + "行数据导入失败，请检查该行供应商数据是否合法。");
                    lineNumber++;
                    errCount++;
                    continue;
                }
                machine.setManufacturerId(manufacturerId);

                //设置基础属性
                machine.setTenantId(CommonUtil.getTenantId());
                machine.setCreateBy(loginUser.getUsername());
                machine.setCreateTime(currentDate);
                machine.setUpdateBy(loginUser.getUsername());
                machine.setUpdateTime(currentDate);
                machine.setDeleted(CommonConstant.DEL_FLAG_0);
                machineService.saveMachine(machine);

                successLines++;
                lineNumber++;
            } catch (Exception e) {
                errCount++;
                lineNumber++;
                errorMessage.append("共导入"+(lineNumber-1)+"行，第 " + lineNumber + "行数据导入失败，未知错误，请检查数据是否合法。");
                throw new ILSBootException(errorMessage.toString());
            }
            if (StringUtils.isNotEmpty(errorMessage)){
                throw new ILSBootException(errorMessage.toString());
            }
        }
    }

    /**
     * 校验是否存在该设备类型
     *
     * @param machineType
     * @return
     */
    public String validateMachineType(String machineType) {
        QueryWrapper<MachineType> machineTypeQueryWrapper = new QueryWrapper<>();
        machineTypeQueryWrapper.eq("type_name", machineType);
        machineTypeQueryWrapper.eq("is_deleted", ZeroOrOneEnum.ZERO.getIcode());
        List<MachineType> list = machineTypeService.list(machineTypeQueryWrapper);
        return list.get(0).getId();
    }

    /**
     * 查询并校验设备标注是否存在
     *
     * @param labelId
     * @return
     */
    public String validateLabelType(String labelId) {

        QueryWrapper<MachineLabel> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("machine_label_name", labelId);
        queryWrapper.eq("label_type", LABEL_TYPE);
        queryWrapper.eq("is_deleted", ZeroOrOneEnum.ZERO.getIcode());
        List<MachineLabel> list = machineLabelService.list(queryWrapper);
        return list.get(0).getId();
    }

    /**
     * 设置设备等级
     *
     * @param levelId
     * @return
     */
    private String validatelevelId(String levelId) {

        QueryWrapper<MachineLabel> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("machine_label_name", levelId);
        queryWrapper.eq("label_type", LEVEL_TYPE);
        queryWrapper.eq("is_deleted", ZeroOrOneEnum.ZERO.getIcode());
        List<MachineLabel> list = machineLabelService.list(queryWrapper);
        return list.get(0).getId();
    }

    /**
     * 查询和校验车间是否存在
     *
     * @param shopId
     * @return
     */
    private String validateWorkShopId(String shopId) {
        QueryWrapper<WorkShop> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("shop_name", shopId);
        queryWrapper.eq("is_deleted", ZeroOrOneEnum.ZERO.getIcode());
        List<WorkShop> list = workShopService.list(queryWrapper);

        return list.get(0).getId();
    }

    private String validateManufacturerId(String manufacturerId) {

        QueryWrapper<MachineManufacturer> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("manufacturer_name", manufacturerId);
        queryWrapper.eq("is_deleted", ZeroOrOneEnum.ZERO.getIcode());
        List<MachineManufacturer> list = machineManufacturerService.list(queryWrapper);

        return list.get(0).getId();
    }

}
