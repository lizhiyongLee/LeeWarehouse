package com.ils.modules.mes.base.factory.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.ils.common.aspect.annotation.AutoLog;
import com.ils.modules.mes.base.factory.entity.WorkLineEmployee;
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
import com.ils.modules.mes.base.factory.entity.WorkShop;
import com.ils.modules.mes.base.factory.entity.WorkShopEmployee;
import com.ils.modules.mes.base.factory.service.WorkShopEmployeeService;
import com.ils.modules.mes.base.factory.service.WorkShopService;
import com.ils.modules.mes.base.factory.vo.WorkShopVO;

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
@RequestMapping("/base/factory/workShop")
@Api(tags="??????")
@Slf4j
public class WorkShopController extends ILSController<WorkShop, WorkShopService> {
	@Autowired
	private WorkShopService workShopService;
	@Autowired
	private WorkShopEmployeeService workShopEmployeeService;
	@Autowired
	private DefineFieldValueService defineFieldValueService;
	
	/**
	 * ??????????????????
	 *
	 * @param workShop
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
    @ApiOperation(value="??????-??????????????????", notes="??????-??????????????????")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(WorkShop workShop,
								   @RequestParam(name = PAGE_NO, defaultValue = DEFAULT_PAGE_NO) Integer pageNo,
            @RequestParam(name = PAGE_SIZE, defaultValue = DEFAULT_PAGE_SIZE) Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<WorkShop> queryWrapper = QueryGenerator.initQueryWrapper(workShop, req.getParameterMap());

		Page<WorkShop> page = new Page<WorkShop>(pageNo, pageSize);
		IPage<WorkShop> pageList = workShopService.page(page, queryWrapper);
		return Result.ok(pageList);
	}
	
	/**
	 * ??????
	 *
	 * @param workShopPage
	 * @return
	 */
    @ApiOperation(value="??????-??????", notes="??????-??????")
	@PostMapping(value = "/add")
	@AutoLog("??????-??????")
	public Result<?> add(@RequestBody WorkShopVO workShopPage) {
		workShopPage.setTenantId(CommonUtil.getTenantId());
		workShopService.saveMain(workShopPage);
		return commonSuccessResult(CommonConstant.OPERATE_TYPE_ADD);
	}
	
	/**
	 * ??????
	 *
	 * @param workShopPage
	 * @return
	 */
    @ApiOperation(value="??????-??????", notes="??????-??????")
	@PostMapping(value = "/edit")
	@AutoLog("??????-??????")
	public Result<?> edit(@RequestBody WorkShopVO workShopPage) {
		workShopService.updateMain(workShopPage);
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
        WorkShopVO workShopVO = new WorkShopVO();
        WorkShop workShop = workShopService.getById(id);
        BeanUtils.copyProperties(workShop, workShopVO);
        List<WorkShopEmployee> workShopEmployeeList = workShopEmployeeService.selectByMainId(id);
        workShopVO.setWorkShopEmployeeList(workShopEmployeeList);
		List<DefineFieldValueVO> lstDefineFields =
				defineFieldValueService.queryDefineFieldValue(TableCodeConstants.WORK_SHOP_TABLE_CODE, id);
		workShopVO.setLstDefineFields(lstDefineFields);
        return Result.ok(workShopVO);
	}
	
	/**
	 * ??????id??????
     *
	 * @param id
	 * @return
	 */
    @ApiOperation(value="??????-????????????id??????????????????", notes="??????-????????????id??????????????????")
	@GetMapping(value = "/queryWorkShopEmployeeByMainId")
	public Result<?> queryWorkShopEmployeeListByMainId(@RequestParam(name="id",required=true) String id) {
		List<WorkShopEmployee> workShopEmployeeList = workShopEmployeeService.selectByMainId(id);
		return Result.ok(workShopEmployeeList);
	}

  /**
   * ??????excel
   *
   * @param request
   * @param workShop
   */
  @GetMapping(value = "/exportXls")
  public ModelAndView exportXls(HttpServletRequest request, WorkShop workShop) {
      // Step.1 ??????????????????
      QueryWrapper<WorkShop> queryWrapper = QueryGenerator.initQueryWrapper(workShop, request.getParameterMap());
	  String[] selections = request.getParameterMap().get("selections");
	  if (selections != null && selections.length > 0) {
		  List<String> idList = Arrays.asList(selections[0].split(","));
		  queryWrapper.in("id", idList);
	  }
      LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();

      //Step.2 ??????????????????
      List<WorkShopVO> pageList = new ArrayList<WorkShopVO>();
      List<WorkShop> workShopList = workShopService.list(queryWrapper);

	  List<String> idList=new ArrayList<>();
	  workShopList.forEach(temp->idList.add(temp.getId()));

	  QueryWrapper<WorkShopEmployee> workShopEmployeeQueryWrapper=new QueryWrapper<>();
	  workShopEmployeeQueryWrapper.in("work_shop_id",idList);
	  List<WorkShopEmployee> workShopEmployeeList = workShopEmployeeService.list(workShopEmployeeQueryWrapper);

      for (WorkShop temp : workShopList) {
          WorkShopVO vo = new WorkShopVO();
          BeanUtils.copyProperties(temp, vo);

		  List<WorkShopEmployee> tempWorkShopEmployeeList = new ArrayList<>();
		  workShopEmployeeList.forEach(workShopEmployee -> {
			  if (temp.getId().equals(workShopEmployee.getWorkShopId())) {
				  tempWorkShopEmployeeList.add(workShopEmployee);
			  }
		  });
          vo.setWorkShopEmployeeList(tempWorkShopEmployeeList);

          pageList.add(vo);
      }
      //Step.3 ??????AutoPoi??????Excel
      ModelAndView mv = new ModelAndView(new ILSEntityExcelView());
      mv.addObject(NormalExcelConstants.FILE_NAME, "??????");
      mv.addObject(NormalExcelConstants.CLASS, WorkShopVO.class);
      mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("????????????", "?????????:"+sysUser.getRealname(), "??????"));
      mv.addObject(NormalExcelConstants.DATA_LIST, pageList);
      return mv;
  }



}
