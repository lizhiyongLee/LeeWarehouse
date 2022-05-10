package com.ils.modules.mes.base.craft.service.impl;

import java.io.Serializable;
import java.util.List;

import com.ils.modules.mes.base.craft.mapper.*;
import com.ils.modules.mes.base.craft.service.ProcessParaHeadService;
import com.ils.modules.mes.base.craft.vo.ProcessParaVO;
import com.ils.modules.mes.base.qc.mapper.NgItemMapper;
import com.ils.modules.mes.base.qc.mapper.NgItemTypeMapper;
import com.ils.modules.mes.config.service.DefineFieldValueService;
import com.ils.modules.mes.constants.TableCodeConstants;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ils.common.exception.ILSBootException;
import com.ils.modules.mes.base.craft.entity.Process;
import com.ils.modules.mes.base.craft.entity.ProcessNgItem;
import com.ils.modules.mes.base.craft.entity.ProcessQcMethod;
import com.ils.modules.mes.base.craft.entity.ProcessStation;
import com.ils.modules.mes.base.craft.service.ProcessService;
import com.ils.modules.mes.base.craft.vo.ProcessQcMethodVO;
import com.ils.modules.mes.base.craft.vo.ProcessVO;
import com.ils.modules.mes.base.factory.entity.Workstation;
import com.ils.modules.mes.base.factory.service.WorkstationService;
import org.springframework.util.CollectionUtils;

/**
 * @Description: 工序
 * @Author: fengyi
 * @Date: 2020-10-28
 * @Version: V1.0
 */
@Service
public class ProcessServiceImpl extends ServiceImpl<ProcessMapper, Process> implements ProcessService {

    @Autowired
    private ProcessMapper processMapper;
    @Autowired
    private ProcessQcMethodMapper processQcMethodMapper;
    @Autowired
    private ProcessNgItemMapper processNgItemMapper;
    @Autowired
    private ProcessStationMapper processStationMapper;
    @Autowired
    private ProcessParaHeadService processParaHeadService;
    @Autowired
    private WorkstationService workstationService;
    @Autowired
    private NgItemMapper ngItemMapper;
    @Autowired
    private NgItemTypeMapper ngItemTypeMapper;
    @Autowired
    private DefineFieldValueService defineFieldValueService;

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public Process saveMain(ProcessVO processVO) {

        this.checkCondition(processVO);

        Process process = new Process();
        BeanUtils.copyProperties(processVO, process);
        processMapper.insert(process);

        // 质检方案
        List<ProcessQcMethodVO> processQcMethodList = processVO.getProcessQcMethodList();
        if (!CollectionUtils.isEmpty(processQcMethodList)) {
            for (ProcessQcMethod entity : processQcMethodList) {
                //外键设置
                entity.setProcessId(process.getId());
                processQcMethodMapper.insert(entity);
            }
        }

        // 不良项
        List<ProcessNgItem> processNgItemList = processVO.getProcessNgItemList();
        if (!CollectionUtils.isEmpty(processNgItemList)) {
            for (ProcessNgItem entity : processNgItemList) {
                //外键设置
                entity.setProcessId(process.getId());
                entity.setNgItemName(ngItemMapper.selectById(entity.getNgItemId()).getNgName());
                entity.setNgItemTypeName(ngItemTypeMapper.selectById(entity.getNgItemTypeId()).getNgTypeName());
                processNgItemMapper.insert(entity);
            }
        }

        // 工位设置
        if (StringUtils.isNoneBlank(processVO.getProcessStation())) {
            String[] stationIds = processVO.getProcessStation().split(",");
            for (String stationId : stationIds) {

                Workstation workstation = workstationService.getById(stationId);
                if (workstation == null) {
                    continue;
                }
                ProcessStation entity = new ProcessStation();
                entity.setStationId(stationId);
                entity.setStationCode(workstation.getStationCode());
                entity.setStationName(workstation.getStationName());

                // 外键设置
                entity.setProcessId(process.getId());
                processStationMapper.insert(entity);

            }
        }
        //自定义字段
        processVO.setId(process.getId());
        defineFieldValueService.saveDefineFieldValue(processVO.getLstDefineFields(),
                TableCodeConstants.PROCESS_TABLE_CODE, process.getId());
        //参数模板
        List<ProcessParaVO> processParaVOList = processVO.getProcessParaVOList();
        processParaVOList.forEach(processParaVO -> {
            processParaVO.setProcessId(process.getId());
            processParaVO.setId(null);
            processParaVO.setCreateBy(null);
            processParaVO.setCreateTime(null);
            processParaHeadService.saveProcessPara(processParaVO);
        });
        return process;
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void updateMain(ProcessVO processVO) {

        this.checkCondition(processVO);

        Process process = new Process();
        BeanUtils.copyProperties(processVO, process);
        processMapper.updateById(process);

        //1.先删除子表数据
        processQcMethodMapper.deleteByMainId(process.getId());
        processNgItemMapper.deleteByMainId(process.getId());
        processStationMapper.deleteByMainId(process.getId());
        processParaHeadService.deleteByProcessId(process.getId());

        //质检方案
        List<ProcessQcMethodVO> processQcMethodList = processVO.getProcessQcMethodList();
        for (ProcessQcMethod entity : processQcMethodList) {
            // 外键设置
            entity.setProcessId(process.getId());
            processQcMethodMapper.insert(entity);
        }
        List<ProcessNgItem> processNgItemList = processVO.getProcessNgItemList();
        for (ProcessNgItem entity : processNgItemList) {
            // 外键设置
            entity.setProcessId(process.getId());
            processNgItemMapper.insert(entity);
        }

        // 工位设置
        if (StringUtils.isNoneBlank(processVO.getProcessStation())) {
            String[] stationIds = processVO.getProcessStation().split(",");
            for (String stationId : stationIds) {

                Workstation workstation = workstationService.getById(stationId);
                if (workstation == null) {
                    continue;
                }
                ProcessStation entity = new ProcessStation();
                entity.setStationId(stationId);
                entity.setStationCode(workstation.getStationCode());
                entity.setStationName(workstation.getStationName());

                // 外键设置
                entity.setProcessId(process.getId());
                processStationMapper.insert(entity);

            }
        }
        //自定义字段
        processVO.setId(process.getId());
        defineFieldValueService.saveDefineFieldValue(processVO.getLstDefineFields(),
                TableCodeConstants.PROCESS_TABLE_CODE, process.getId());
        //参数模板
        List<ProcessParaVO> processParaVOList = processVO.getProcessParaVOList();
        processParaVOList.forEach(processParaVO -> {
            processParaVO.setProcessId(process.getId());
            processParaVO.setId(null);
            processParaVO.setCreateBy(null);
            processParaVO.setCreateTime(null);
            processParaHeadService.saveProcessPara(processParaVO);
        });
    }

    /**
     * 条件检查
     *
     * @param processVO
     * @date 2020年12月4日
     */
    private void checkCondition(ProcessVO processVO) {
        // 编码不重复
        QueryWrapper<Process> queryWrapper = new QueryWrapper();
        queryWrapper.eq("process_code", processVO.getProcessCode());
        if (StringUtils.isNoneBlank(processVO.getId())) {
            queryWrapper.ne("id", processVO.getId());
        }
        Process obj = baseMapper.selectOne(queryWrapper);
        if (obj != null) {
            throw new ILSBootException("B-FCT-0010");
        }
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void delMain(String id) {
        processQcMethodMapper.deleteByMainId(id);
        processNgItemMapper.deleteByMainId(id);
        processMapper.deleteById(id);
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void delBatchMain(List<String> idList) {
        for (Serializable id : idList) {
            processQcMethodMapper.deleteByMainId(id.toString());
            processNgItemMapper.deleteByMainId(id.toString());
            processMapper.deleteById(id);
        }
    }

}
