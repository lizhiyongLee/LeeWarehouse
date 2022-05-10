package com.ils.modules.mes.base.factory.service.impl;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ils.common.exception.ILSBootException;
import com.ils.modules.mes.base.factory.entity.Factory;
import com.ils.modules.mes.base.factory.entity.WorkShop;
import com.ils.modules.mes.base.factory.mapper.FactoryMapper;
import com.ils.modules.mes.base.factory.service.FactoryService;
import com.ils.modules.mes.base.factory.service.WorkShopService;
import com.ils.modules.mes.base.factory.vo.FactoryVO;
import com.ils.modules.mes.config.service.DefineFieldValueService;
import com.ils.modules.mes.constants.TableCodeConstants;
import com.ils.modules.mes.enums.ZeroOrOneEnum;

/**
 * @Description: 工厂
 * @Author: fengyi
 * @Date: 2020-10-13
 * @Version: V1.0
 */
@Service
public class FactoryServiceImpl extends ServiceImpl<FactoryMapper, Factory> implements FactoryService {

    @Autowired
    private DefineFieldValueService defineFieldValueService;

    @Autowired
    @Lazy
    private WorkShopService workShopService;

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void saveFactory(FactoryVO factoryVO) {

        this.checkCondition(factoryVO);

        Factory factory = new Factory();
        BeanUtils.copyProperties(factoryVO, factory);
         baseMapper.insert(factory);
        factoryVO.setId(factory.getId());
        defineFieldValueService.saveDefineFieldValue(factoryVO.getLstDefineFields(),
            TableCodeConstants.FACTORY_TABLE_CODE, factory.getId());

    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void updateFactory(FactoryVO factoryVO) {

        this.checkCondition(factoryVO);

        Factory factory = new Factory();
        BeanUtils.copyProperties(factoryVO, factory);
        baseMapper.updateById(factory);
        defineFieldValueService.saveDefineFieldValue(factoryVO.getLstDefineFields(),
            TableCodeConstants.FACTORY_TABLE_CODE, factory.getId());
    }

    /**
     * 
     * 条件校验
     * 
     * @param factoryVO
     * @date 2020年12月4日
     */
    private void checkCondition(FactoryVO factoryVO) {
        // 编码不重复
        QueryWrapper<Factory> queryWrapper = new QueryWrapper();
        queryWrapper.eq("factory_code", factoryVO.getFactoryCode());
        if (StringUtils.isNoneBlank(factoryVO.getId())) {
            queryWrapper.ne("id", factoryVO.getId());
        }
        Factory obj = baseMapper.selectOne(queryWrapper);
        if (obj != null) {
            throw new ILSBootException("B-FCT-0010");
        }

        if (StringUtils.isNoneBlank(factoryVO.getId())) {
            if (!this.checkUpdateStatusCondition(factoryVO)) {
                throw new ILSBootException("B-FCT-0001");
            }
        }

    }

    /**
     * 
     * 检查停用工厂时是否满足条件,false是下面存在没有停用的车间
     * @return true 满足，false 不满足
     * @date 2020年10月16日
     */
    private boolean checkUpdateStatusCondition(FactoryVO factoryVO) {
        if (ZeroOrOneEnum.ZERO.getStrCode().equals(factoryVO.getStatus())) {
            QueryWrapper<WorkShop> queryWrapper = new QueryWrapper<WorkShop>();
            queryWrapper.eq("up_area", factoryVO.getId());
            queryWrapper.eq("status", ZeroOrOneEnum.ONE.getStrCode());
            queryWrapper.eq("tenant_id", factoryVO.getTenantId());
            int iCount =   workShopService.count(queryWrapper);
            return iCount == 0 ? true : false;
        }
        return true;
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void delFactory(String id) {
        baseMapper.deleteById(id);
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void delBatchFactory(List<String> idList) {
        baseMapper.deleteBatchIds(idList);
    }
}
