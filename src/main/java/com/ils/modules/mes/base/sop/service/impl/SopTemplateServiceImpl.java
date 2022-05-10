package com.ils.modules.mes.base.sop.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ils.common.exception.ILSBootException;
import com.ils.modules.mes.base.sop.entity.SopTemplate;
import com.ils.modules.mes.base.sop.entity.SopTemplateControl;
import com.ils.modules.mes.base.sop.entity.SopTemplateStep;
import com.ils.modules.mes.base.sop.mapper.SopTemplateControlMapper;
import com.ils.modules.mes.base.sop.mapper.SopTemplateMapper;
import com.ils.modules.mes.base.sop.mapper.SopTemplateStepMapper;
import com.ils.modules.mes.base.sop.service.SopTemplateService;
import com.ils.modules.mes.base.sop.vo.SopTemplateStepVO;
import com.ils.modules.mes.base.sop.vo.SopTemplateVO;
import com.ils.modules.mes.enums.ZeroOrOneEnum;
import com.ils.modules.system.entity.Role;
import com.ils.modules.system.entity.User;
import com.ils.modules.system.service.RoleService;
import com.ils.modules.system.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.ils.modules.mes.base.sop.controller.SopTemplateController.EXECUTE_AUTHORITY_ROLE;
import static com.ils.modules.mes.base.sop.controller.SopTemplateController.EXECUTE_AUTHORITY_USER;

/**
 * @Description: sop模板表头
 * @Author: Conner
 * @Date: 2021-07-15
 * @Version: V1.0
 */
@Service
public class SopTemplateServiceImpl extends ServiceImpl<SopTemplateMapper, SopTemplate> implements SopTemplateService {
    @Autowired
    private SopTemplateStepMapper sopTemplateStepMapper;
    @Autowired
    private RoleService roleService;
    @Autowired
    private UserService userService;
    @Autowired
    private SopTemplateControlMapper sopTemplateControlMapper;

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void saveSopTemplate(SopTemplate sopTemplate) {
        //判断是否存在有相同工序的版本
        verifySopTemplate(sopTemplate);
        baseMapper.insert(sopTemplate);
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void updateSopTemplate(SopTemplateVO sopTemplateVO) {
        //更新主表
        baseMapper.updateById(sopTemplateVO);

        //更新子表前删除原来的子表
        sopTemplateControlMapper.deletedByMainId(sopTemplateVO.getId());
        sopTemplateStepMapper.deleteByMainId(sopTemplateVO.getId());

        //保存sop模板步骤
        List<SopTemplateStepVO> sopTemplateStepVOList = sopTemplateVO.getSopTemplateStepVOList();

        //排序,然后倒序
        CollectionUtil.sortByProperty(sopTemplateStepVOList, "stepSeq");
        //设置模板步骤中的首模板和末模板
        sopTemplateStepVOList.forEach(item -> item.setFirstStep(ZeroOrOneEnum.ZERO.getStrCode()));
        sopTemplateStepVOList.forEach(item -> item.setLastStep(ZeroOrOneEnum.ZERO.getStrCode()));
        sopTemplateStepVOList.get(0).setFirstStep(ZeroOrOneEnum.ONE.getStrCode());
        sopTemplateStepVOList.get(sopTemplateStepVOList.size() - 1).setLastStep(ZeroOrOneEnum.ONE.getStrCode());
        Collections.reverse(sopTemplateStepVOList);

        //定义下一个步骤的id，如果是最后一个则为end
        String nextStepId = "end";
        //保存sop模板控件
        for (SopTemplateStepVO sopTemplateStepVO : sopTemplateStepVOList) {
            sopTemplateStepVO.setTemplateId(sopTemplateVO.getId());
            //设置下个步骤的id
            sopTemplateStepVO.setNextStepId(nextStepId);
            sopTemplateStepMapper.insert(sopTemplateStepVO);
            nextStepId = sopTemplateStepVO.getId();
            if (CollectionUtil.isEmpty(sopTemplateStepVO.getSopTemplateControlList())) {
                continue;
            }
            List<SopTemplateControl> sopTemplateControlList = sopTemplateStepVO.getSopTemplateControlList();
            for (SopTemplateControl sopTemplateControl : sopTemplateControlList) {
                sopTemplateControl.setTemplateStepId(sopTemplateStepVO.getId());
                sopTemplateControlMapper.insert(sopTemplateControl);
            }
        }
    }

    @Override
    public SopTemplateVO queryById(String id) {
        SopTemplateVO sopTemplateVO = new SopTemplateVO();

        //查询并设置模板主表数据
        SopTemplate sopTemplate = baseMapper.selectById(id);
        BeanUtil.copyProperties(sopTemplate, sopTemplateVO);

        //查询并设置模板步骤数据
        QueryWrapper<SopTemplateStep> stepQueryWrapper = new QueryWrapper<>();
        stepQueryWrapper.eq("template_id", id);
        stepQueryWrapper.orderByAsc("step_seq");
        stepQueryWrapper.eq("is_deleted", ZeroOrOneEnum.ZERO.getIcode());
        List<SopTemplateStep> sopTemplateStepList =
                sopTemplateStepMapper.selectList(stepQueryWrapper);
        //如果步骤数量为空
        if (CollectionUtil.isEmpty(sopTemplateStepList)) {
            return sopTemplateVO;
        }

        List<SopTemplateStepVO> sopTemplateStepVOList = new ArrayList<>(16);
        //遍历查询模板控件
        for (SopTemplateStep sopTemplateStep : sopTemplateStepList) {
            SopTemplateStepVO sopTemplateStepVO = getSopTemplateStepVO(sopTemplateStep.getId());
            ////查询并设置执行者的名字
            if (EXECUTE_AUTHORITY_ROLE.equals(sopTemplateStepVO.getExecuteAuthority())) {
                Role role = roleService.getById(sopTemplateStepVO.getExecuter());
                if (null != role) {
                    sopTemplateStepVO.setExecuterName(role.getRoleName());
                }else {
                    sopTemplateStepVO.setExecuterName("角色异常，请重新配置");
                }
            } else {
                User user = userService.getById(sopTemplateStepVO.getExecuter());
                if (null != user) {
                    sopTemplateStepVO.setExecuterName(user.getUsername());
                }else {
                    sopTemplateStepVO.setExecuterName("用户异常，请重新配置");
                }
            }

            sopTemplateStepVOList.add(sopTemplateStepVO);
        }
        sopTemplateVO.setSopTemplateStepVOList(sopTemplateStepVOList);

        return sopTemplateVO;
    }

    @Override
    public void queryByTemplateControlId(String controlId) {

    }

    @Override
    public void stopTemplate(String id) {
        SopTemplate sopTemplate = new SopTemplate();
        sopTemplate.setStatus(ZeroOrOneEnum.ZERO.getStrCode());
        sopTemplate.setId(id);
        baseMapper.updateById(sopTemplate);
    }

    @Override
    public void startTemplate(String id) {
        SopTemplate sopTemplate = baseMapper.selectById(id);
        QueryWrapper<SopTemplate> queryWrapper = new QueryWrapper<>();

        queryWrapper.eq("template_type", sopTemplate.getTemplateType());
        queryWrapper.eq("entity_code", sopTemplate.getEntityCode());
        queryWrapper.eq("entity_name", sopTemplate.getEntityName());
        queryWrapper.eq("entity_line_id", sopTemplate.getEntityLineId());
        List<SopTemplate> sopTemplates = baseMapper.selectList(queryWrapper);

        for (SopTemplate template : sopTemplates) {
            if (ZeroOrOneEnum.ONE.getStrCode().equals(template.getStatus())) {
                throw new ILSBootException("P-SOP-0097");
            }
        }

        sopTemplate.setStatus(ZeroOrOneEnum.ONE.getStrCode());
        baseMapper.updateById(sopTemplate);
    }


    /**
     * 查询模板步骤及其步骤下面的控件
     *
     * @param id
     * @return
     */
    private SopTemplateStepVO getSopTemplateStepVO(String id) {
        SopTemplateStepVO sopTemplateStepVO = new SopTemplateStepVO();

        //查询模板步骤
        SopTemplateStep sopTemplateStep = sopTemplateStepMapper.selectById(id);
        BeanUtil.copyProperties(sopTemplateStep, sopTemplateStepVO);

        //查询模板控件
        QueryWrapper<SopTemplateControl> controlQueryWrapper = new QueryWrapper<>();
        controlQueryWrapper.eq("template_step_id", id);
        controlQueryWrapper.orderByAsc("control_seq");
        controlQueryWrapper.eq("is_deleted", ZeroOrOneEnum.ZERO.getIcode());
        List<SopTemplateControl> sopTemplateControlList = sopTemplateControlMapper.selectList(controlQueryWrapper);
        sopTemplateStepVO.setSopTemplateControlList(sopTemplateControlList);

        return sopTemplateStepVO;
    }


    private void verifySopTemplate(SopTemplate sopTemplate) {
        QueryWrapper<SopTemplate> sopTemplateQueryWrapper = new QueryWrapper<>();
        sopTemplateQueryWrapper.eq("entity_line_id", sopTemplate.getEntityLineId());
        List<SopTemplate> sopTemplates = baseMapper.selectList(sopTemplateQueryWrapper);
        for (SopTemplate template : sopTemplates) {
            if (template.getVersion().equals(sopTemplate.getVersion())) {
                throw new ILSBootException("P-SOP-0092");
            }
        }
    }
}
