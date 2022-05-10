package com.ils.modules.mes.base.factory.service.impl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.ils.modules.mes.base.factory.vo.WorkstationVO;
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
import com.ils.modules.mes.base.factory.entity.WorkLine;
import com.ils.modules.mes.base.factory.entity.WorkShop;
import com.ils.modules.mes.base.factory.entity.Workstation;
import com.ils.modules.mes.base.factory.entity.WorkstationEmployee;
import com.ils.modules.mes.base.factory.mapper.WorkstationEmployeeMapper;
import com.ils.modules.mes.base.factory.mapper.WorkstationMapper;
import com.ils.modules.mes.base.factory.service.WorkLineService;
import com.ils.modules.mes.base.factory.service.WorkShopService;
import com.ils.modules.mes.base.factory.service.WorkstationService;
import com.ils.modules.mes.base.factory.vo.NodeTreeVO;
import com.ils.modules.mes.enums.FactoryAreaTypeEnum;
import com.ils.modules.mes.enums.ZeroOrOneEnum;
import com.ils.modules.mes.util.CommonUtil;


/**
 * @Description: 工位
 * @Author: fengyi
 * @Date: 2020-10-14
 * @Version: V1.0
 */
@Service
public class WorkstationServiceImpl extends ServiceImpl<WorkstationMapper, Workstation> implements WorkstationService {

	@Autowired
	private WorkstationMapper workstationMapper;
	@Autowired
	private WorkstationEmployeeMapper workstationEmployeeMapper;
	
    @Autowired
    private WorkShopService workShopService;

    @Autowired
    private WorkLineService workLineService;
    @Autowired
    private DefineFieldValueService defineFieldValueService;

	@Override
	@Transactional(rollbackFor = RuntimeException.class)
	public void saveMain(WorkstationVO workstationVO) {
        Workstation workstation = new Workstation();
        BeanUtils.copyProperties(workstationVO, workstation);
        this.checkCondition(workstation);
        this.initUpArea(workstation);
        workstationMapper.insert(workstation);
		for(WorkstationEmployee entity:workstationVO.getWorkstationEmployeeList()) {
			//外键设置
			entity.setWorkstationId(workstation.getId());
			workstationEmployeeMapper.insert(entity);
		}
        workstationVO.setId(workstation.getId());
        defineFieldValueService.saveDefineFieldValue(workstationVO.getLstDefineFields(),
                TableCodeConstants.WORKSTATION_TABLE_CODE, workstation.getId());
	}



	@Override
	@Transactional(rollbackFor = RuntimeException.class)
	public void updateMain(WorkstationVO workstationVO) {
        Workstation workstation = new Workstation();
        BeanUtils.copyProperties(workstationVO, workstation);
        this.checkCondition(workstation);
        this.initUpArea(workstation);
        workstationMapper.updateById(workstation);

		//1.先删除子表数据
		workstationEmployeeMapper.deleteByMainId(workstation.getId());
		
		//2.子表数据重新插入
		for(WorkstationEmployee entity:workstationVO.getWorkstationEmployeeList()) {
			//外键设置
			entity.setWorkstationId(workstation.getId());
			workstationEmployeeMapper.insert(entity);
		}
        workstationVO.setId(workstation.getId());
        defineFieldValueService.saveDefineFieldValue(workstationVO.getLstDefineFields(),
                TableCodeConstants.WORKSTATION_TABLE_CODE, workstation.getId());
	}

    /**
     * 
     * 初始化上级区域
     * 
     * @param workstation
     * @date 2021年1月18日
     */
    private void initUpArea(Workstation workstation) {

        WorkLine workLine = workLineService.getById(workstation.getUpArea());
        if (workLine == null) {
            WorkShop workShop = workShopService.getById(workstation.getUpArea());
            if (workShop != null) {
                workstation.setUpAreaType(FactoryAreaTypeEnum.WORK_SHOP.getValue());
                workstation.setUpAreaName(workShop.getShopName());
            }
        } else {
            workstation.setUpAreaType(FactoryAreaTypeEnum.WORK_LINE.getValue());
            workstation.setUpAreaName(workLine.getLineName());
        }

    }

    private void checkCondition(Workstation workstation) {
        // 编码不重复
        QueryWrapper<Workstation> queryWrapper = new QueryWrapper();
        queryWrapper.eq("station_code", workstation.getStationCode());
        if (StringUtils.isNoneBlank(workstation.getId())) {
            queryWrapper.ne("id", workstation.getId());
        }
        Workstation obj = baseMapper.selectOne(queryWrapper);
        if (obj != null) {
            throw new ILSBootException("B-FCT-0010");
        }
    }

	@Override
	@Transactional(rollbackFor = RuntimeException.class)
	public void delMain(String id) {
		workstationEmployeeMapper.deleteByMainId(id);
		workstationMapper.deleteById(id);
	}

	@Override
	@Transactional(rollbackFor = RuntimeException.class)
	public void delBatchMain(List<String> idList) {
		for(Serializable id:idList) {
			workstationEmployeeMapper.deleteByMainId(id.toString());
			workstationMapper.deleteById(id);
		}
	}

    @Override
    public List<NodeTreeVO> queryShopLineTreeList() {
        // 查询车间
        QueryWrapper<WorkShop> queryWorkShopWrapper = new QueryWrapper<WorkShop>();
        queryWorkShopWrapper.eq("tenant_id", CommonUtil.getTenantId());
        queryWorkShopWrapper.eq("status", ZeroOrOneEnum.ONE.getStrCode());
        queryWorkShopWrapper.eq("is_deleted", ZeroOrOneEnum.ZERO.getIcode());
        List<WorkShop> lstWorkShop = workShopService.list(queryWorkShopWrapper);
        if (lstWorkShop == null || lstWorkShop.size() == 0) {
            return new ArrayList<NodeTreeVO>(0);
        }
        // 转换为树形节点
        List<NodeTreeVO> lstWorkShopNode = new ArrayList<NodeTreeVO>(lstWorkShop.size());
        for (WorkShop workShop : lstWorkShop) {
            NodeTreeVO nodeVO = new NodeTreeVO();
            nodeVO.setId(workShop.getId());
            nodeVO.setName(workShop.getShopName());
            nodeVO.setCode(workShop.getShopCode());
            nodeVO.setNodeType(FactoryAreaTypeEnum.WORK_SHOP.getValue());
            lstWorkShopNode.add(nodeVO);
        }
        // 查询产线
        QueryWrapper<WorkLine> queryWorkLineWrapper = new QueryWrapper<WorkLine>();
        queryWorkLineWrapper.eq("tenant_id", CommonUtil.getTenantId());
        queryWorkLineWrapper.eq("status", ZeroOrOneEnum.ONE.getStrCode());
        queryWorkLineWrapper.eq("is_deleted", ZeroOrOneEnum.ZERO.getIcode());
        List<WorkLine> lstWorkLine = workLineService.list(queryWorkLineWrapper);
        if (lstWorkLine != null || lstWorkLine.size() > 0) {
            // 转换为树形节点
            List<NodeTreeVO> lstWorkLineNode = new ArrayList<NodeTreeVO>(lstWorkShop.size());
            for (WorkLine workLine : lstWorkLine) {
                NodeTreeVO nodeVO = new NodeTreeVO();
                nodeVO.setId(workLine.getId());
                nodeVO.setName(workLine.getLineName());
                nodeVO.setCode(workLine.getLineCode());
                nodeVO.setUpArea(workLine.getUpArea());
                nodeVO.setNodeType(FactoryAreaTypeEnum.WORK_LINE.getValue());
                lstWorkLineNode.add(nodeVO);
            }
            Map<String, List<NodeTreeVO>> workLineGroupMap =
                lstWorkLineNode.stream().collect(Collectors.groupingBy(NodeTreeVO::getParentId));

            for (NodeTreeVO node : lstWorkShopNode) {
                List<NodeTreeVO> lstChildVO = workLineGroupMap.get(node.getId());
                if (lstChildVO != null && lstChildVO.size() > 0) {
                    node.setLstChildNodeTreeVO(lstChildVO);
                }
            }
        }
        return lstWorkShopNode;
    }

    @Override
    public List<Workstation> queryWorkStationByIds(Collection<String> ids) {
        QueryWrapper<Workstation> queryWrapper = new QueryWrapper();
        queryWrapper.in("id", ids);
        List lstWorkStation = workstationMapper.selectList(queryWrapper);
        return lstWorkStation;
    }
	
}
