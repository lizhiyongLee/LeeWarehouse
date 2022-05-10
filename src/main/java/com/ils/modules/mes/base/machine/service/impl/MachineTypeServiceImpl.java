package com.ils.modules.mes.base.machine.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ils.common.exception.ILSBootException;
import com.ils.modules.mes.base.machine.entity.*;
import com.ils.modules.mes.base.machine.mapper.*;
import com.ils.modules.mes.base.machine.service.MachineTypeParaHeadService;
import com.ils.modules.mes.base.machine.service.MachineTypeService;
import com.ils.modules.mes.base.machine.vo.MachineTypeParaVO;
import com.ils.modules.mes.base.machine.vo.MachineTypePolicyVO;
import com.ils.modules.mes.base.machine.vo.MachineTypeVO;
import com.ils.modules.mes.enums.ZeroOrOneEnum;
import com.ils.modules.mes.machine.entity.MachineParaHead;
import com.ils.modules.mes.machine.mapper.MachinePolicyMapper;
import com.ils.modules.mes.util.CommonUtil;
import com.ils.modules.system.entity.User;
import com.ils.modules.system.service.UserService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @Description: 设备类型关联策略组
 * @Author: Conner
 * @Date: 2020-10-30
 * @Version: V1.0
 */
@Service
public class MachineTypeServiceImpl extends ServiceImpl<MachineTypeMapper, MachineType> implements MachineTypeService {

    @Autowired
    private MachineTypeDataMapper machineTypeDataMapper;

    @Autowired
    private MachineTypePolicyMapper machineTypePolicyMapper;

    @Autowired
    private MachinePolicyMapper machinePolicyMapper;

    @Autowired
    private UserService sysUserService;

    @Autowired
    private MachineTypeTaskEmployeeMapper machineTypeTaskEmployeeMapper;
    @Autowired
    private MachineTypeParaHeadService machineTypeParaHeadService;
    @Autowired
    private MachineTypeParaDetailMapper machineTypeParaDetailMapper;

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void saveMachineType(MachineType machineType) {
        baseMapper.insert(machineType);
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void updateMachineType(MachineType machineType) {
        baseMapper.updateById(machineType);
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void updateMachineTypeMain(MachineTypeVO machineTypeVO) {
        //删除子表数据
        if (machineTypeVO.getMachineTypeDataList().size() != 0) {
            machineTypeDataMapper.deleteByMainId(machineTypeVO.getId());
        }
        if (machineTypeVO.getMachineTypePolicyVOList().size() != 0) {
            machineTypePolicyMapper.deleteByMainId(machineTypeVO.getId());
        }
        if (machineTypeVO.getMachineTypeParaVOList().size() != 0) {
            machineTypeParaHeadService.deleteByMachineTypeId(machineTypeVO.getId());
        }
        //更新主表
        MachineType machineType = new MachineType();
        BeanUtils.copyProperties(machineTypeVO, machineType);
        baseMapper.updateById(machineType);
        String machineTypeId = machineType.getId();
        // 更新子表数据
        // 拿到设备类型关联读数和设备类型关联策略
        List<MachineTypeData> machineTypeDataList = machineTypeVO.getMachineTypeDataList();
        List<MachineTypePolicyVO> machineTypePolicyVOList = machineTypeVO.getMachineTypePolicyVOList();
        // 保存备类型关联读数
        for (MachineTypeData machineTypeData : machineTypeDataList) {
            machineTypeData.setMachineTypeId(machineTypeId);
            machineTypeDataMapper.insert(machineTypeData);
        }
        //保存设备类型关联策略
        for (MachineTypePolicyVO machineTypePolicyVO : machineTypePolicyVOList) {
            //删除MachineTypeTaskEmployees表By machinetypepolicy
            machineTypeTaskEmployeeMapper.deleteByPolicyId(machineTypePolicyVO.getId());
            MachineTypePolicy machineTypePolicy = new MachineTypePolicy();
            String employeeIds = machineTypePolicyVO.getMachineTypeTaskEmployees();
            BeanUtils.copyProperties(machineTypePolicyVO, machineTypePolicy);
            //更新设备类型策略
            machineTypePolicy.setMachineTypeId(machineTypeId);
            machineTypePolicyMapper.insert(machineTypePolicy);
            String[] employeeIdList = employeeIds.split(",");
            for (String employeeId : employeeIdList) {
                MachineTypeTaskEmployee employee = new MachineTypeTaskEmployee();
                employee.setEmployeeId(employeeId);
                User user = sysUserService.getById(employeeId);
                employee.setEmployeeName(user.getRealname());
                employee.setEmployeeCode(user.getOrgCode());
                employee.setPolicyId(machineTypePolicy.getId());
                machineTypeTaskEmployeeMapper.insert(employee);
            }
            if (ZeroOrOneEnum.ONE.getStrCode().equals(machineType.getStatus())) {
                throw new ILSBootException("B-FCT-0006");
            }

            //更新设备策略
        }

        //保存参数相关的数据
        List<MachineTypeParaVO> machineTypeParaVOList = machineTypeVO.getMachineTypeParaVOList();
        for (MachineTypeParaVO machineTypeParaVO : machineTypeParaVOList) {
            //保存主表
            machineTypeParaVO.setMachineTypeId(machineTypeId);
            machineTypeParaHeadService.save(machineTypeParaVO);
            //保存子表
            List<MachineTypeParaDetail> machineTypeParaDetailList = machineTypeParaVO.getMachineTypeParaDetailList();
            if (!CommonUtil.isEmptyOrNull(machineTypeParaDetailList)) {
                for (MachineTypeParaDetail machineTypeParaDetail : machineTypeParaDetailList) {
                    machineTypeParaDetail.setParaHeadId(machineTypeParaVO.getId());
                    machineTypeParaDetail.setMachineTypeId(machineTypeId);
                    machineTypeParaDetailMapper.insert(machineTypeParaDetail);
                }
            }
        }
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void saveMain(MachineTypeVO machineTypeVO) {
        MachineType machineType = new MachineType();
        BeanUtils.copyProperties(machineTypeVO, machineType);
        baseMapper.insert(machineType);
        String machineTypeId = machineType.getId();
        // 拿到设备类型关联读数和设备类型关联策略
        List<MachineTypeData> machineTypeDataList = machineTypeVO.getMachineTypeDataList();
        List<MachineTypePolicyVO> machineTypePolicyVOList = machineTypeVO.getMachineTypePolicyVOList();
        // 保存备类型关联读数
        for (MachineTypeData machineTypeData : machineTypeDataList) {
            machineTypeData.setMachineTypeId(machineTypeId);
            machineTypeDataMapper.insert(machineTypeData);
        }
        //保存设备类型关联策略
        for (MachineTypePolicyVO machineTypePolicyVO : machineTypePolicyVOList) {
            MachineTypePolicy machineTypePolicy = new MachineTypePolicy();
            String employeeIds = machineTypePolicyVO.getMachineTypeTaskEmployees();
            BeanUtils.copyProperties(machineTypePolicyVO, machineTypePolicy);
            String[] employeeIdList = employeeIds.split(",");
            machineTypePolicy.setMachineTypeId(machineTypeId);
            machineTypePolicy.setStatus(ZeroOrOneEnum.ZERO.getStrCode());
            if (ZeroOrOneEnum.ZERO.getStrCode().equals(machineTypePolicy.getStatus())
                    && ZeroOrOneEnum.ONE.getStrCode().equals(machineType.getStatus())) {
                throw new ILSBootException("B-FCT-0006");
            }
            machineTypePolicyMapper.insert(machineTypePolicy);
            for (String employeeId : employeeIdList) {
                MachineTypeTaskEmployee employee = new MachineTypeTaskEmployee();
                employee.setEmployeeId(employeeId);
                User user = sysUserService.getById(employeeId);
                employee.setEmployeeName(user.getRealname());
                employee.setEmployeeCode(user.getOrgCode());
                employee.setPolicyId(machineTypePolicy.getId());
                machineTypeTaskEmployeeMapper.insert(employee);
            }
        }

        //保存参数相关的数据
        List<MachineTypeParaVO> machineTypeParaVOList = machineTypeVO.getMachineTypeParaVOList();
        for (MachineTypeParaVO machineTypeParaVO : machineTypeParaVOList) {
            //保存主表
            machineTypeParaVO.setMachineTypeId(machineTypeId);
            machineTypeParaHeadService.save(machineTypeParaVO);
            //保存子表
            List<MachineTypeParaDetail> machineTypeParaDetailList = machineTypeParaVO.getMachineTypeParaDetailList();
            if (!CommonUtil.isEmptyOrNull(machineTypeParaDetailList)) {
                for (MachineTypeParaDetail machineTypeParaDetail : machineTypeParaDetailList) {
                    machineTypeParaDetail.setParaHeadId(machineTypeParaVO.getId());
                    machineTypeParaDetail.setMachineTypeId(machineTypeId);
                    machineTypeParaDetailMapper.insert(machineTypeParaDetail);
                }
            }
        }
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public MachineTypeVO selectByMainId(String id) {
        MachineTypeVO machineTypeVO = new MachineTypeVO();
        MachineType machineType = baseMapper.selectById(id);
        String machineTypeId = machineType.getId();
        //通过主表id查询出维保策略关联表的集合
        List<MachineTypePolicy> machineTypePolicies = machineTypePolicyMapper.selectAllByMachineTypeId(machineTypeId);

        //通过主表id查询设备类型关联读数的集合
        List<MachineTypeData> machineTypeDatas = machineTypeDataMapper.selectAllByMachineTypeId(machineTypeId);

        //查询参数相关数据
        List<MachineTypeParaVO> machineTypeParaVOList = queryMachineTypeParaByMainId(id);
        machineTypeVO.setMachineTypeParaVOList(machineTypeParaVOList);
        //把查出来的值赋到VO中去
        BeanUtils.copyProperties(machineType, machineTypeVO);
        machineTypeVO.setMachineTypeDataList(machineTypeDatas);
        //MachineTypePolicyVO machineTypePolicyVO = new MachineTypePolicyVO();
        //查询关联策略的计划执行人员
        List<MachineTypePolicyVO> machineTypePolicyVOList = new ArrayList<>();
        for (MachineTypePolicy machineTypePolicy : machineTypePolicies) {
            MachineTypePolicyVO machineTypePolicyVO = new MachineTypePolicyVO();
            StringBuilder employeeIds = new StringBuilder();
            String policyId = machineTypePolicy.getId();
            List<MachineTypeTaskEmployee> machineTypeTaskEmployees = machineTypeTaskEmployeeMapper.selectByPolicyId(policyId);
            for (MachineTypeTaskEmployee machineTypeTaskEmployee : machineTypeTaskEmployees) {
                employeeIds.append(machineTypeTaskEmployee.getEmployeeId()).append(",");
            }
            String employeeId = "";
            if (machineTypeTaskEmployees.size() != 0) {
                employeeId = employeeIds.substring(0, employeeIds.length() - 1);
            }

            machineTypePolicyVO.setMachineTypeTaskEmployees(employeeId);
            BeanUtils.copyProperties(machineTypePolicy, machineTypePolicyVO);
            machineTypePolicyVOList.add(machineTypePolicyVO);
        }
        machineTypeVO.setMachineTypePolicyVOList(machineTypePolicyVOList);
        return machineTypeVO;
    }

    @Override
    public List<MachineTypeParaVO> queryMachineTypeParaByMainId(String id) {
        //查询参数相关数据
        List<MachineTypeParaVO> machineTypeParaVOList = new ArrayList<>(16);
        QueryWrapper<MachineTypeParaHead> headQueryWrapper = new QueryWrapper<>();
        headQueryWrapper.eq("machine_type_id", id);
        List<MachineTypeParaHead> machineTypeParaHeadList = machineTypeParaHeadService.list(headQueryWrapper);
        for (MachineTypeParaHead machineTypeParaHead : machineTypeParaHeadList) {
            MachineTypeParaVO machineTypeParaVO = new MachineTypeParaVO();
            BeanUtils.copyProperties(machineTypeParaHead, machineTypeParaVO);
            QueryWrapper<MachineTypeParaDetail> detailQueryWrapper = new QueryWrapper<>();
            detailQueryWrapper.eq("machine_type_id", id);
            detailQueryWrapper.eq("para_head_id", machineTypeParaVO.getId());
            List<MachineTypeParaDetail> machineTypeParaDetailList = machineTypeParaDetailMapper.selectList(detailQueryWrapper);
            machineTypeParaVO.setMachineTypeParaDetailList(machineTypeParaDetailList);
            machineTypeParaVOList.add(machineTypeParaVO);
        }
        return machineTypeParaVOList;
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void deleteMachinePara(String ids) {
        List<String> idList = Arrays.asList(ids.split(","));
        idList.forEach(id -> {
            MachineTypeParaHead machineTypeParaHead = machineTypeParaHeadService.getById(id);
            if (machineTypeParaHead.getStatus().equals(ZeroOrOneEnum.ONE.getStrCode())) {
                throw new ILSBootException("启用中的数据不能删除！");
            } else {
                machineTypeParaHeadService.deleteById(id);
            }
        });

    }
}
