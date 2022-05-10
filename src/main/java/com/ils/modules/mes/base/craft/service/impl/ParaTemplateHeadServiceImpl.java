package com.ils.modules.mes.base.craft.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ils.modules.mes.base.craft.entity.ParaTemplateDetail;
import com.ils.modules.mes.base.craft.entity.ParaTemplateHead;
import com.ils.modules.mes.base.craft.mapper.ParaTemplateDetailMapper;
import com.ils.modules.mes.base.craft.mapper.ParaTemplateHeadMapper;
import com.ils.modules.mes.base.craft.service.ParaTemplateHeadService;
import com.ils.modules.mes.base.craft.vo.ParameterTemplateVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @Description: 参数模板主表
 * @Author: Conner
 * @Date: 2021-10-15
 * @Version: V1.0
 */
@Service
public class ParaTemplateHeadServiceImpl extends ServiceImpl<ParaTemplateHeadMapper, ParaTemplateHead> implements ParaTemplateHeadService {

    @Autowired
    private ParaTemplateDetailMapper paraTemplateDetailMapper;
    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void saveParaTemplateHead(ParameterTemplateVO parameterTemplateVO) {
        //保存主表
        baseMapper.insert(parameterTemplateVO);
        List<ParaTemplateDetail> paraTemplateDetailList = parameterTemplateVO.getParaTemplateDetailList();
        //保存从表
        for (ParaTemplateDetail paraTemplateDetail : paraTemplateDetailList) {
            paraTemplateDetail.setId(null);
            paraTemplateDetail.setCreateBy(null);
            paraTemplateDetail.setCreateTime(null);
            paraTemplateDetail.setParaTempId(parameterTemplateVO.getId());
            paraTemplateDetailMapper.insert(paraTemplateDetail);
        }
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void updateParaTemplateHead(ParameterTemplateVO parameterTemplateVO) {
        baseMapper.updateById(parameterTemplateVO);
        //删除从表
        paraTemplateDetailMapper.delByMainId(parameterTemplateVO.getId());

        //重新插入从表
        List<ParaTemplateDetail> paraTemplateDetailList = parameterTemplateVO.getParaTemplateDetailList();
        for (ParaTemplateDetail paraTemplateDetail : paraTemplateDetailList) {
            paraTemplateDetail.setParaTempId(parameterTemplateVO.getId());
            paraTemplateDetailMapper.insert(paraTemplateDetail);
        }
    }

    @Override
    public ParameterTemplateVO queryById(String id) {
        ParameterTemplateVO parameterTemplateVO = new ParameterTemplateVO();
        //查询主表
        ParaTemplateHead paraTemplateHead = baseMapper.selectById(id);
        BeanUtils.copyProperties(paraTemplateHead,parameterTemplateVO);
        //查询从表
        QueryWrapper<ParaTemplateDetail> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("para_temp_id",id);
        List<ParaTemplateDetail> paraTemplateDetailList = paraTemplateDetailMapper.selectList(queryWrapper);

        parameterTemplateVO.setParaTemplateDetailList(paraTemplateDetailList);

        return parameterTemplateVO;
    }
}
