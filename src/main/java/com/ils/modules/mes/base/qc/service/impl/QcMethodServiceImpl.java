package com.ils.modules.mes.base.qc.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ils.common.exception.ILSBootException;
import com.ils.modules.mes.base.craft.entity.RouteLine;
import com.ils.modules.mes.base.craft.mapper.RouteLineMapper;
import com.ils.modules.mes.base.product.entity.ProductLine;
import com.ils.modules.mes.base.product.mapper.ProductLineMapper;
import com.ils.modules.mes.base.qc.entity.QcMethod;
import com.ils.modules.mes.base.qc.entity.QcMethodDetail;
import com.ils.modules.mes.base.qc.mapper.QcMethodDetailMapper;
import com.ils.modules.mes.base.qc.mapper.QcMethodItemMapper;
import com.ils.modules.mes.base.qc.mapper.QcMethodMapper;
import com.ils.modules.mes.base.qc.service.QcItemService;
import com.ils.modules.mes.base.qc.service.QcMethodService;
import com.ils.modules.mes.base.qc.vo.QcMethodVO;
import com.ils.modules.mes.enums.QcTaskQcTypeEnum;
import com.ils.modules.mes.enums.WorkflowTypeEnum;
import com.ils.modules.mes.enums.ZeroOrOneEnum;
import com.ils.modules.mes.execution.entity.WorkProduceTask;
import com.ils.modules.mes.execution.mapper.WorkProduceTaskMapper;
import com.ils.modules.mes.produce.entity.WorkOrder;
import com.ils.modules.mes.produce.mapper.WorkOrderMapper;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @Description: 质检方案
 * @Author: fengyi
 * @Date: 2020-10-20
 * @Version: V1.0
 */
@Service
public class QcMethodServiceImpl extends ServiceImpl<QcMethodMapper, QcMethod> implements QcMethodService {

    @Autowired
    private QcMethodMapper qcMethodMapper;
    @Autowired
    private QcMethodDetailMapper qcMethodDetailMapper;
    @Autowired
    private QcMethodItemMapper qcMethodItemMapper;
    @Autowired
    private WorkProduceTaskMapper workProduceTaskMapper;
    @Autowired
    private WorkOrderMapper workOrderMapper;
    @Autowired
    private RouteLineMapper routeLineMapper;
    @Autowired
    private QcItemService qcItemService;
    @Autowired
    private ProductLineMapper productLineMapper;

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public Integer saveMain(QcMethod qcMethod, List<QcMethodDetail> qcMethodDetailList) {
        QueryWrapper<QcMethod> qcMethodQueryWrapper = new QueryWrapper<>();
        qcMethodQueryWrapper.eq("method_name", qcMethod.getMethodName());
        QcMethod check = baseMapper.selectOne(qcMethodQueryWrapper);

        if (null != check) {
            throw new ILSBootException("已存在该名称的方案");
        }

        int insert = 0;

        try {
            insert = qcMethodMapper.insert(qcMethod);
            if (qcMethodDetailList != null) {
                for (QcMethodDetail entity : qcMethodDetailList) {

                    //判断，如果质检项名字为空，则设置质检项名字，如果不为空，则不做其他操作
                    if (StringUtils.isEmpty(entity.getItemName())) {
                        entity.setItemName(qcItemService.getById(entity.getItemId()).getQcItemName());
                    }

                    //外键设置
                    entity.setMehtodId(qcMethod.getId());
                    insert = qcMethodDetailMapper.insert(entity);
                }
            }

        } catch (Exception e) {
            return insert;
        }
        return insert;
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void saveMainList(List<QcMethodVO> qcMethodVOList) {
        for (QcMethodVO qcMethodVO : qcMethodVOList) {
            QcMethod qcMethod = new QcMethod();
            BeanUtil.copyProperties(qcMethodVO, qcMethod);
            saveMain(qcMethod, qcMethodVO.getQcMethodDetailList());
        }
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void updateMain(QcMethod qcMethod, List<QcMethodDetail> qcMethodDetailList) {
        qcMethodMapper.updateById(qcMethod);

        //1.先删除子表数据
        qcMethodDetailMapper.deleteByMainId(qcMethod.getId());
        qcMethodItemMapper.deleteByMainId(qcMethod.getId());

        //2.子表数据重新插入
        for (QcMethodDetail entity : qcMethodDetailList) {
            //外键设置
            entity.setMehtodId(qcMethod.getId());
            qcMethodDetailMapper.insert(entity);
        }
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void delMain(String id) {
        qcMethodDetailMapper.deleteByMainId(id);
        qcMethodItemMapper.deleteByMainId(id);
        qcMethodMapper.deleteById(id);
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void delBatchMain(List<String> idList) {
        for (Serializable id : idList) {
            qcMethodDetailMapper.deleteByMainId(id.toString());
            qcMethodItemMapper.deleteByMainId(id.toString());
            qcMethodMapper.deleteById(id);
        }
    }

    @Override
    public List<QcMethod> queryQcMeThodByItemIdAndQcType(String qcType, String id) {
        if (QcTaskQcTypeEnum.FIRST_CHECK.getValue().equals(qcType)
                || QcTaskQcTypeEnum.PRODUCTION_CHECK.getValue().equals(qcType)
                || QcTaskQcTypeEnum.TOUR_CHECK.getValue().equals(qcType)) {
            return baseMapper.queryByTaskId(qcType, id);
        } else if (QcTaskQcTypeEnum.ADMISSION_CHECK.getValue().equals(qcType)
                || QcTaskQcTypeEnum.LEAVE_FACTORY_CHECK.getValue().equals(qcType)) {
            List<QcMethod> qcMethodList = qcMethodMapper.queryQcMeThodByItemIdAndQcType(qcType, id);
            return qcMethodList;
        } else {
            QueryWrapper<QcMethod> qcMethodQueryWrapper = new QueryWrapper<>();
            qcMethodQueryWrapper.eq("is_deleted", ZeroOrOneEnum.ZERO.getIcode());
            qcMethodQueryWrapper.eq(ZeroOrOneEnum.ONE.getStrCode(), ZeroOrOneEnum.ONE.getStrCode());
            List<QcMethod> qcMethodList = qcMethodMapper.selectList(qcMethodQueryWrapper);
            return qcMethodList;
        }
    }
}
