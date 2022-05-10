package com.ils.modules.mes.base.craft.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ils.modules.mes.base.craft.entity.ParaTemplateDetail;
import com.ils.modules.mes.base.craft.entity.ParaTemplateHead;
import com.ils.modules.mes.base.craft.entity.ProcessParaDetail;
import com.ils.modules.mes.base.craft.entity.ProcessParaHead;
import com.ils.modules.mes.base.craft.mapper.ParaTemplateDetailMapper;
import com.ils.modules.mes.base.craft.mapper.ParaTemplateHeadMapper;
import com.ils.modules.mes.base.craft.mapper.ProcessParaDetailMapper;
import com.ils.modules.mes.base.craft.mapper.ProcessParaHeadMapper;
import com.ils.modules.mes.base.craft.service.ParaTemplateHeadService;
import com.ils.modules.mes.base.craft.service.ProcessParaHeadService;
import com.ils.modules.mes.base.craft.vo.ParameterTemplateVO;
import com.ils.modules.mes.base.craft.vo.ProcessParaVO;
import com.ils.modules.mes.util.CommonUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * @Description: 参数模板主表
 * @Author: Conner
 * @Date: 2021-10-15
 * @Version: V1.0
 */
@Service
public class ProcessParaHeadServiceImpl extends ServiceImpl<ProcessParaHeadMapper, ProcessParaHead> implements ProcessParaHeadService {

    @Autowired
    private ProcessParaDetailMapper processParaDetailMapper;

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void saveProcessPara(ProcessParaVO processParaVO) {
        //保存主表
        baseMapper.insert(processParaVO);
        List<ProcessParaDetail> processParaDetailList = processParaVO.getProcessParaDetailList();
        //保存从表
        if (!CommonUtil.isEmptyOrNull(processParaDetailList)) {
            processParaDetailList.forEach(processParaDetail -> {
                processParaDetail.setProcessId(processParaVO.getProcessId());
                processParaDetail.setParaHeadId(processParaVO.getId());
                processParaDetail.setId(null);
                processParaDetail.setCreateBy(null);
                processParaDetail.setCreateTime(null);
                processParaDetailMapper.insert(processParaDetail);
            });
        }

    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void updateProcessPara(ProcessParaVO processParaVO) {
        baseMapper.updateById(processParaVO);
        //删除从表
        processParaDetailMapper.deleteByMainId(processParaVO.getId());

        //重新插入从表
        List<ProcessParaDetail> processParaDetailList = processParaVO.getProcessParaDetailList();
        processParaDetailList.forEach(processParaDetail -> processParaDetailMapper.insert(processParaDetail));
    }

    @Override
    public ProcessParaVO queryById(String id) {
        ProcessParaVO processParaVO = new ProcessParaVO();
        //查询主表
        ProcessParaHead processParaHead = baseMapper.selectById(id);
        BeanUtils.copyProperties(processParaHead, processParaVO);
        //查询从表
        QueryWrapper<ProcessParaDetail> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("para_head_id", id);
        List<ProcessParaDetail> processParaDetailList = processParaDetailMapper.selectList(queryWrapper);

        processParaVO.setProcessParaDetailList(processParaDetailList);
        return processParaVO;
    }

    @Override
    public List<ProcessParaVO> queryByProcessId(String processId) {
        List<ProcessParaVO> processParaVOList = new ArrayList<>();
        //查询主表
        QueryWrapper<ProcessParaHead> headQueryWrapper = new QueryWrapper<>();
        headQueryWrapper.eq("process_id", processId);
        List<ProcessParaHead> processParaHeadList = baseMapper.selectList(headQueryWrapper);
        processParaHeadList.forEach(processParaHead -> {
            ProcessParaVO processParaVO = new ProcessParaVO();
            BeanUtils.copyProperties(processParaHead, processParaVO);
            //查询从表
            QueryWrapper<ProcessParaDetail> detailQueryWrapper = new QueryWrapper<>();
            detailQueryWrapper.eq("para_head_id", processParaHead.getId());
            List<ProcessParaDetail> processParaDetailList = processParaDetailMapper.selectList(detailQueryWrapper);

            processParaVO.setProcessParaDetailList(processParaDetailList);
            processParaVOList.add(processParaVO);
        });
        return processParaVOList;
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void deleteByProcessId(String processId) {
        List<String> ids = new ArrayList<>();
        //删除主从表
        QueryWrapper<ProcessParaHead> headQueryWrapper = new QueryWrapper<>();
        headQueryWrapper.eq("process_id", processId);
        List<ProcessParaHead> processParaHeadList = baseMapper.selectList(headQueryWrapper);
        processParaHeadList.forEach(processParaHead -> ids.add(processParaHead.getId()));
        baseMapper.deleteByMainId(processId);
        if (!CommonUtil.isEmptyOrNull(ids)) {
            ids.forEach(id->processParaDetailMapper.deleteByMainId(id));
        }
    }
}
