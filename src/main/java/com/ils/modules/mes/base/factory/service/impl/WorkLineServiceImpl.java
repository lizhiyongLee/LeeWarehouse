package com.ils.modules.mes.base.factory.service.impl;

import java.io.Serializable;
import java.util.List;

import com.ils.modules.mes.base.factory.vo.WorkLineVO;
import com.ils.modules.mes.config.service.DefineFieldValueService;
import com.ils.modules.mes.constants.TableCodeConstants;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ils.common.exception.ILSBootException;
import com.ils.modules.mes.base.factory.entity.WorkLine;
import com.ils.modules.mes.base.factory.entity.WorkLineEmployee;
import com.ils.modules.mes.base.factory.entity.Workstation;
import com.ils.modules.mes.base.factory.mapper.WorkLineEmployeeMapper;
import com.ils.modules.mes.base.factory.mapper.WorkLineMapper;
import com.ils.modules.mes.base.factory.service.WorkLineService;
import com.ils.modules.mes.base.factory.service.WorkstationService;
import com.ils.modules.mes.enums.ZeroOrOneEnum;


/**
 * @Description: 产线
 * @Author: fengyi
 * @Date: 2020-10-14
 * @Version: V1.0
 */
@Service
public class WorkLineServiceImpl extends ServiceImpl<WorkLineMapper, WorkLine> implements WorkLineService {

	@Autowired
	private WorkLineMapper workLineMapper;
	@Autowired
	private WorkLineEmployeeMapper workLineEmployeeMapper;
	@Autowired
	private DefineFieldValueService defineFieldValueService;
	
    @Autowired
    @Lazy
    private WorkstationService workstationService;

	@Override
	@Transactional(rollbackFor = RuntimeException.class)
	public void saveMain(WorkLineVO workLineVO) {
        WorkLine workLine = new WorkLine();
        BeanUtils.copyProperties(workLineVO, workLine);
        this.checkCondition(workLine);

        workLineMapper.insert(workLine);
		for(WorkLineEmployee entity:workLineVO.getWorkLineEmployeeList()) {
			//外键设置
			entity.setWorkLineId(workLine.getId());
			workLineEmployeeMapper.insert(entity);
		}
        workLineVO.setId(workLine.getId());
        defineFieldValueService.saveDefineFieldValue(workLineVO.getLstDefineFields(),
                TableCodeConstants.WORK_LINE_TABLE_CODE, workLine.getId());
	}

	@Override
	@Transactional(rollbackFor = RuntimeException.class)
	public void updateMain(WorkLineVO workLineVO) {
        WorkLine workLine = new WorkLine();
        BeanUtils.copyProperties(workLineVO, workLine);
        this.checkCondition(workLine);

        workLineMapper.updateById(workLine);
		
		//1.先删除子表数据
		workLineEmployeeMapper.deleteByMainId(workLine.getId());
		
		//2.子表数据重新插入
		for(WorkLineEmployee entity:workLineVO.getWorkLineEmployeeList()) {
			//外键设置
			entity.setWorkLineId(workLine.getId());
			workLineEmployeeMapper.insert(entity);
		}
        workLineVO.setId(workLine.getId());
        defineFieldValueService.saveDefineFieldValue(workLineVO.getLstDefineFields(),
                TableCodeConstants.WORK_LINE_TABLE_CODE, workLine.getId());
	}

    /**
     * 
     * 条件校验
     * 
     * @param workLine
     * @date 2020年12月4日
     */
    private void checkCondition(WorkLine workLine) {
        // 编码不重复
        QueryWrapper<WorkLine> queryWrapper = new QueryWrapper();
        queryWrapper.eq("line_code", workLine.getLineCode());
        if (StringUtils.isNoneBlank(workLine.getId())) {
            queryWrapper.ne("id", workLine.getId());
        }
        WorkLine obj = baseMapper.selectOne(queryWrapper);
        if (obj != null) {
            throw new ILSBootException("B-FCT-0010");
        }

        if (StringUtils.isNoneBlank(workLine.getId())) {
            if (!this.checkUpdateStatusCondition(workLine)) {
                throw new ILSBootException("B-FCT-0003");
            }
        }

    }

    /**
     * 
     * 检查停用产线时是否满足条件,false是下面存在没有停用的工位
     * 
     * @return true 满足，false 不满足
     * @date 2020年10月16日
     */
    private boolean checkUpdateStatusCondition(WorkLine workLine) {
        if (ZeroOrOneEnum.ZERO.getStrCode().equals(workLine.getStatus())) {
            QueryWrapper<Workstation> queryStationWrapper = new QueryWrapper<Workstation>();
            queryStationWrapper.eq("up_area", workLine.getId());
            queryStationWrapper.eq("status", ZeroOrOneEnum.ONE.getStrCode());
            queryStationWrapper.eq("tenant_id", workLine.getTenantId());
            int iCount = workstationService.count(queryStationWrapper);
            return iCount == 0 ? true : false;
        }
        return true;
    }

	@Override
	@Transactional(rollbackFor = RuntimeException.class)
	public void delMain(String id) {
		workLineEmployeeMapper.deleteByMainId(id);
		workLineMapper.deleteById(id);
	}

	@Override
	@Transactional(rollbackFor = RuntimeException.class)
	public void delBatchMain(List<String> idList) {
		for(Serializable id:idList) {
			workLineEmployeeMapper.deleteByMainId(id.toString());
			workLineMapper.deleteById(id);
		}
	}
	
}
