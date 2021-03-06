package com.ils.modules.mes.base.factory.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.ils.common.aspect.annotation.AutoLog;
import com.ils.modules.mes.config.service.DefineFieldValueService;
import com.ils.modules.mes.config.vo.DefineFieldValueVO;
import com.ils.modules.mes.constants.TableCodeConstants;
import com.ils.modules.mes.util.CommonUtil;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ils.common.api.vo.Result;
import com.ils.common.constant.CommonConstant;
import com.ils.common.system.base.controller.ILSController;
import com.ils.common.system.query.QueryGenerator;
import com.ils.common.system.vo.LoginUser;
import com.ils.framework.poi.excel.entity.ExportParams;
import com.ils.framework.poi.excel.entity.vo.NormalExcelConstants;
import com.ils.framework.poi.excel.view.ILSEntityExcelView;
import com.ils.modules.mes.base.factory.entity.WorkLine;
import com.ils.modules.mes.base.factory.entity.WorkLineEmployee;
import com.ils.modules.mes.base.factory.service.WorkLineEmployeeService;
import com.ils.modules.mes.base.factory.service.WorkLineService;
import com.ils.modules.mes.base.factory.vo.WorkLineVO;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
 
/**
 * @Description: ??????
 * @Author: fengyi
 * @Date: 2020-10-14
 * @Version: V1.0
 */
@RestController
@RequestMapping("/base/factory/workLine")
@Api(tags="??????")
@Slf4j
public class WorkLineController extends ILSController<WorkLine, WorkLineService> {
	@Autowired
	private WorkLineService workLineService;
	@Autowired
	private WorkLineEmployeeService workLineEmployeeService;
	@Autowired
	private DefineFieldValueService defineFieldValueService;
	
	/**
	 * ??????????????????
	 *
	 * @param workLine
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
    @ApiOperation(value="??????-??????????????????", notes="??????-??????????????????")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(WorkLine workLine,
								   @RequestParam(name = PAGE_NO, defaultValue = DEFAULT_PAGE_NO) Integer pageNo,
            @RequestParam(name = PAGE_SIZE, defaultValue = DEFAULT_PAGE_SIZE) Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<WorkLine> queryWrapper = QueryGenerator.initQueryWrapper(workLine, req.getParameterMap());
		Page<WorkLine> page = new Page<WorkLine>(pageNo, pageSize);
		IPage<WorkLine> pageList = workLineService.page(page, queryWrapper);
		return Result.ok(pageList);
	}
	
	/**
	 * ??????
	 *
	 * @param workLinePage
	 * @return
	 */
    @ApiOperation(value="??????-??????", notes="??????-??????")
	@PostMapping(value = "/add")
	@AutoLog("??????-??????")
	public Result<?> add(@RequestBody WorkLineVO workLinePage) {
		workLinePage.setTenantId(CommonUtil.getTenantId());
		workLineService.saveMain(workLinePage);
		return commonSuccessResult(CommonConstant.OPERATE_TYPE_ADD);
	}
	
	/**
	 * ??????
	 *
	 * @param workLinePage
	 * @return
	 */
    @ApiOperation(value="??????-??????", notes="??????-??????")
	@PostMapping(value = "/edit")
	@AutoLog("??????-??????")
	public Result<?> edit(@RequestBody WorkLineVO workLinePage) {
		workLineService.updateMain(workLinePage);
		return commonSuccessResult(CommonConstant.OPERATE_TYPE_EDIT);
	}
	
	
	
	/**
	 * ??????id??????
     *
	 * @param id
	 * @return
	 */
    @ApiOperation(value="??????-??????id??????", notes="??????-??????id??????")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
        WorkLineVO workLineVO = new WorkLineVO();
        WorkLine workLine = workLineService.getById(id);
        BeanUtils.copyProperties(workLine, workLineVO);
        List<WorkLineEmployee> workLineEmployeeList = workLineEmployeeService.selectByMainId(id);
        workLineVO.setWorkLineEmployeeList(workLineEmployeeList);
		List<DefineFieldValueVO> lstDefineFields =
				defineFieldValueService.queryDefineFieldValue(TableCodeConstants.WORK_LINE_TABLE_CODE, id);
		workLineVO.setLstDefineFields(lstDefineFields);
        return Result.ok(workLineVO);

	}
	
	/**
	 * ??????id??????
     *
	 * @param id
	 * @return
	 */
    @ApiOperation(value="??????-????????????id??????????????????", notes="??????-????????????id??????????????????")
	@GetMapping(value = "/queryWorkLineEmployeeByMainId")
	public Result<?> queryWorkLineEmployeeListByMainId(@RequestParam(name="id",required=true) String id) {
		List<WorkLineEmployee> workLineEmployeeList = workLineEmployeeService.selectByMainId(id);
		return Result.ok(workLineEmployeeList);
	}

  /**
   * ??????excel
   *
   * @param request
   * @param workLine
   */
  @GetMapping(value = "/exportXls")
  public ModelAndView exportXls(HttpServletRequest request, WorkLine workLine) {
      // Step.1 ??????????????????
      QueryWrapper<WorkLine> queryWrapper = QueryGenerator.initQueryWrapper(workLine, request.getParameterMap());
	  String[] selections = request.getParameterMap().get("selections");
	  if (selections != null && selections.length > 0) {
		  List<String> idList = Arrays.asList(selections[0].split(","));
		  queryWrapper.in("id", idList);
	  }
      LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();

      //Step.2 ??????????????????
      List<WorkLineVO> pageList = new ArrayList<WorkLineVO>();
      List<WorkLine> workLineList = workLineService.list(queryWrapper);

      List<String> idList=new ArrayList<>();
	  workLineList.forEach(temp->idList.add(temp.getId()));

	  QueryWrapper<WorkLineEmployee> workLineEmployeeQueryWrapper=new QueryWrapper<>();
	  workLineEmployeeQueryWrapper.in("work_line_id",idList);
	  List<WorkLineEmployee> workLineEmployeeList = workLineEmployeeService.list(workLineEmployeeQueryWrapper);

	  for (WorkLine temp : workLineList) {
          WorkLineVO vo = new WorkLineVO();
		  BeanUtils.copyProperties(temp, vo);
		  List<WorkLineEmployee> tempWorkLineEmployeeList = new ArrayList<>();
		  workLineEmployeeList.forEach(workLineEmployee -> {
			  if (temp.getId().equals(workLineEmployee.getWorkLineId())) {
				  tempWorkLineEmployeeList.add(workLineEmployee);
			  }
		  });
          vo.setWorkLineEmployeeList(tempWorkLineEmployeeList);

          pageList.add(vo);
      }
      //Step.3 ??????AutoPoi??????Excel
      ModelAndView mv = new ModelAndView(new ILSEntityExcelView());
      mv.addObject(NormalExcelConstants.FILE_NAME, "??????");
      mv.addObject(NormalExcelConstants.CLASS, WorkLineVO.class);
      mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("????????????", "?????????:"+sysUser.getRealname(), "??????"));
      mv.addObject(NormalExcelConstants.DATA_LIST, pageList);
      return mv;
  }



}
