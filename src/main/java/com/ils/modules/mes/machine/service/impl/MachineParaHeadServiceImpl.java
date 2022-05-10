package com.ils.modules.mes.machine.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ils.modules.mes.base.machine.entity.MachineTypeParaDetail;
import com.ils.modules.mes.base.machine.vo.MachineTypeParaVO;
import com.ils.modules.mes.machine.entity.MachineParaDetail;
import com.ils.modules.mes.machine.entity.MachineParaHead;
import com.ils.modules.mes.machine.mapper.MachineParaDetailMapper;
import com.ils.modules.mes.machine.mapper.MachineParaHeadMapper;
import com.ils.modules.mes.machine.service.MachineParaHeadService;
import com.ils.modules.mes.machine.vo.MachineParaVO;
import com.ils.modules.mes.util.CommonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * @Description: 设备关联参数主表
 * @Author: Conner
 * @Date: 2021-10-19
 * @Version: V1.0
 */
@Service
public class MachineParaHeadServiceImpl extends ServiceImpl<MachineParaHeadMapper, MachineParaHead> implements MachineParaHeadService {

    @Autowired
    private MachineParaDetailMapper machineParaDetailMapper;

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void saveMachineParaHeadVO(List<MachineTypeParaVO> machineTypeParaVOList, String machineId) {
        for (MachineTypeParaVO machineTypeParaVO : machineTypeParaVOList) {
            MachineParaHead machineParaHead = new MachineParaHead();
            BeanUtil.copyProperties(machineTypeParaVO, machineParaHead);
            machineParaHead.setMachineId(machineId);
            baseMapper.insert(machineParaHead);
            //保存详情表
            List<MachineTypeParaDetail> machineTypeParaDetailList = machineTypeParaVO.getMachineTypeParaDetailList();
            for (MachineTypeParaDetail machineTypeParaDetail : machineTypeParaDetailList) {
                MachineParaDetail machineParaDetail = new MachineParaDetail();
                BeanUtil.copyProperties(machineTypeParaDetail, machineParaDetail);
                machineParaDetail.setMachineId(machineId);
                machineParaDetailMapper.insert(machineParaDetail);
            }
        }
    }

    @Override
    public List<MachineParaVO> queryParaDataByMachineId(String machineId) {
        //step.1:查询主表
        List<MachineParaVO> machineParaVOList = new ArrayList<>(16);
        QueryWrapper<MachineParaHead> headQueryWrapper = new QueryWrapper<>();
        headQueryWrapper.eq("machine_id", machineId);
        List<MachineParaHead> machineParaHeadList = baseMapper.selectList(headQueryWrapper);
        //step.2:查询从表
        for (MachineParaHead machineParaHead : machineParaHeadList) {
            MachineParaVO machineParaVO = new MachineParaVO();
            BeanUtil.copyProperties(machineParaHead, machineParaVO);
            QueryWrapper<MachineParaDetail> detailQueryWrapper = new QueryWrapper<>();
            detailQueryWrapper.eq("machine_id", machineId);
            detailQueryWrapper.eq("para_head_id", machineParaHead.getId());
            List<MachineParaDetail> machineParaDetailList = machineParaDetailMapper.selectList(detailQueryWrapper);
            machineParaVO.setMachineParaDetailList(machineParaDetailList);
            machineParaVOList.add(machineParaVO);
        }
        return machineParaVOList;
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void deleteByMachineId(String machineId) {
        QueryWrapper<MachineParaHead> headQueryWrapper = new QueryWrapper<>();
        headQueryWrapper.eq("machine_id", machineId);
        List<MachineParaHead> machineParaHeadList = baseMapper.selectList(headQueryWrapper);
        machineParaHeadList.forEach(machineParaHead -> machineParaDetailMapper.deleteByMainId(machineParaHead.getId()));
        baseMapper.deleteByMainId(machineId);
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void deleteById(String id){
        machineParaDetailMapper.deleteByMainId(id);
        baseMapper.deleteById(id);
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void saveBatch(List<MachineParaVO> machineParaVOList, String machineId) {
        machineParaVOList.forEach(machineParaVO -> {
            machineParaVO.setMachineId(machineId);
            machineParaVO.setId(null);
            machineParaVO.setCreateBy(null);
            machineParaVO.setCreateTime(null);
            baseMapper.insert(machineParaVO);
            List<MachineParaDetail> machineParaDetailList = machineParaVO.getMachineParaDetailList();
            if (!CommonUtil.isEmptyOrNull(machineParaDetailList)) {
                machineParaDetailList.forEach(machineParaDetail -> {
                    machineParaDetail.setMachineId(machineId);
                    machineParaDetail.setParaHeadId(machineParaVO.getId());
                    machineParaDetail.setId(null);
                    machineParaDetail.setCreateBy(null);
                    machineParaDetail.setCreateTime(null);
                    machineParaDetailMapper.insert(machineParaDetail);
                });
            }
        });
    }
}
