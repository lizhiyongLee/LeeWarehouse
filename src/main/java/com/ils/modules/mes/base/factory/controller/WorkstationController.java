package com.ils.modules.mes.base.factory.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ils.common.aspect.annotation.AutoLog;
import com.ils.framework.poi.excel.ExcelImportUtil;
import com.ils.framework.poi.excel.entity.ImportParams;
import com.ils.modules.mes.base.factory.entity.WorkShopEmployee;
import com.ils.modules.mes.base.machine.entity.MachineFaultAppearance;
import com.ils.modules.mes.config.service.DefineFieldValueService;
import com.ils.modules.mes.config.vo.DefineFieldValueVO;
import com.ils.modules.mes.constants.TableCodeConstants;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
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
import com.ils.modules.mes.base.factory.entity.Workstation;
import com.ils.modules.mes.base.factory.entity.WorkstationEmployee;
import com.ils.modules.mes.base.factory.service.WorkShopService;
import com.ils.modules.mes.base.factory.service.WorkstationEmployeeService;
import com.ils.modules.mes.base.factory.service.WorkstationService;
import com.ils.modules.mes.base.factory.vo.NodeTreeVO;
import com.ils.modules.mes.base.factory.vo.WorkstationVO;
import com.ils.modules.mes.util.TreeNode;

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
@RequestMapping("/base/factory/workstation")
@Api(tags="??????")
@Slf4j
public class WorkstationController extends ILSController<Workstation, WorkstationService> {
	@Autowired
	private WorkstationService workstationService;

    @Autowired
    private WorkShopService workShopService;

	@Autowired
	private WorkstationEmployeeService workstationEmployeeService;

	@Autowired
	private DefineFieldValueService defineFieldValueService;
	
	/**
	 * ??????????????????
	 *
	 * @param workstation
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
    @ApiOperation(value="??????-??????????????????", notes="??????-??????????????????")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(Workstation workstation,
								   @RequestParam(name = PAGE_NO, defaultValue = DEFAULT_PAGE_NO) Integer pageNo,
            @RequestParam(name = PAGE_SIZE, defaultValue = DEFAULT_PAGE_SIZE) Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<Workstation> queryWrapper = QueryGenerator.initQueryWrapper(workstation, req.getParameterMap());

		Page<Workstation> page = new Page<Workstation>(pageNo, pageSize);
		IPage<Workstation> pageList = workstationService.page(page, queryWrapper);
		return Result.ok(pageList);
	}
	
	/**
	 * ??????
	 *
	 * @param workstationPage
	 * @return
	 */
    @ApiOperation(value="??????-??????", notes="??????-??????")
	@PostMapping(value = "/add")
	@AutoLog("??????-??????")
	public Result<?> add(@RequestBody WorkstationVO workstationPage) {
		workstationService.saveMain(workstationPage);
		return commonSuccessResult(CommonConstant.OPERATE_TYPE_ADD);
	}
	
	/**
	 * ??????
	 *
	 * @param workstationPage
	 * @return
	 */
    @ApiOperation(value="??????-??????", notes="??????-??????")
	@PostMapping(value = "/edit")
	@AutoLog("??????-??????")
	public Result<?> edit(@RequestBody WorkstationVO workstationPage) {
		workstationService.updateMain(workstationPage);
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
        WorkstationVO workstationVO = new WorkstationVO();
        Workstation workstation = workstationService.getById(id);
        BeanUtils.copyProperties(workstation, workstationVO);
        List<WorkstationEmployee> workstationEmployeeList = workstationEmployeeService.selectByMainId(id);
        workstationVO.setWorkstationEmployeeList(workstationEmployeeList);
		List<DefineFieldValueVO> lstDefineFields =
				defineFieldValueService.queryDefineFieldValue(TableCodeConstants.WORKSTATION_TABLE_CODE, id);
		workstationVO.setLstDefineFields(lstDefineFields);
        return Result.ok(workstationVO);
	}
	
	/**
	 * ??????id??????
     *
	 * @param id
	 * @return
	 */
    @ApiOperation(value="??????-????????????id??????????????????", notes="??????-????????????id??????????????????")
	@GetMapping(value = "/queryWorkstationEmployeeByMainId")
	public Result<?> queryWorkstationEmployeeListByMainId(@RequestParam(name="id",required=true) String id) {
		List<WorkstationEmployee> workstationEmployeeList = workstationEmployeeService.selectByMainId(id);
		return Result.ok(workstationEmployeeList);
	}

    /**
     * ????????????????????????????????????????????????
     * 
     * @return
     */
    @ApiOperation(value = "????????????????????????????????????????????????-????????????", notes = "????????????????????????????????????????????????-??????")
    @RequestMapping(value = "/queryShopLineTreeList", method = RequestMethod.GET)
    public Result<?> queryShopLineTreeList() {
        List<NodeTreeVO> lstNodeTreeVO = workstationService.queryShopLineTreeList();
        return Result.ok(lstNodeTreeVO);
    }

    /**
     * ?????????????????????????????????????????????????????????
     * 
     * @return
     */
    @ApiOperation(value = "?????????????????????????????????????????????????????????-????????????", notes = "?????????????????????????????????????????????????????????-??????")
    @RequestMapping(value = "/queryStationTreeList", method = RequestMethod.GET)
    public Result<?> queryStationTreeList() {
        List<TreeNode> listTreeNode = workShopService.queryStationTreeList();
        return Result.ok(listTreeNode);
    }

  /**
   * ??????excel
   *
   * @param request
   * @param workstation
   */
  @ApiOperation(value = "??????excel", notes = "??????excel")
  @GetMapping(value = "/exportXls")
  public ModelAndView exportXls(HttpServletRequest request, Workstation workstation) {
      // Step.1 ??????????????????
      QueryWrapper<Workstation> queryWrapper = QueryGenerator.initQueryWrapper(workstation, request.getParameterMap());
	  String[] selections = request.getParameterMap().get("selections");
	  if (selections != null && selections.length > 0) {
		  List<String> idList = Arrays.asList(selections[0].split(","));
		  queryWrapper.in("id", idList);
	  }
      LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();

      //Step.2 ??????????????????
      List<WorkstationVO> pageList = new ArrayList<WorkstationVO>();
      List<Workstation> workstationList = workstationService.list(queryWrapper);

	  List<String> idList=new ArrayList<>();
	  workstationList.forEach(temp->idList.add(temp.getId()));

	  QueryWrapper<WorkstationEmployee> workstationEmployeeQueryWrapper=new QueryWrapper<>();
	  workstationEmployeeQueryWrapper.in("workstation_id",idList);
	  List<WorkstationEmployee> workstationEmployeeList = workstationEmployeeService.list(workstationEmployeeQueryWrapper);

      for (Workstation temp : workstationList) {
          WorkstationVO vo = new WorkstationVO();
          BeanUtils.copyProperties(temp, vo);
		  List<WorkstationEmployee> tempWorkstationEmployeeList = new ArrayList<>();
		  workstationEmployeeList.forEach(workstationEmployee -> {
			  if (temp.getId().equals(workstationEmployee.getWorkstationId())) {
				  tempWorkstationEmployeeList.add(workstationEmployee);
			  }
		  });
          vo.setWorkstationEmployeeList(tempWorkstationEmployeeList);
          pageList.add(vo);
      }
      //Step.3 ??????AutoPoi??????Excel
      ModelAndView mv = new ModelAndView(new ILSEntityExcelView());
      mv.addObject(NormalExcelConstants.FILE_NAME, "??????");
      mv.addObject(NormalExcelConstants.CLASS, WorkstationVO.class);
      mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("????????????", "?????????:"+sysUser.getRealname(), "??????"));
      mv.addObject(NormalExcelConstants.DATA_LIST, pageList);
      return mv;
  }

	/**
	 * ??????excel????????????
	 *
	 * @param request
	 * @param response
	 * @return
	 */
	@ApiOperation(value = "??????excel????????????", notes = "??????excel????????????")
	@PostMapping(value = "/importExcel")
	@Transactional(rollbackFor = RuntimeException.class)
	public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
		return super.importExcel(request, response, Workstation.class);
	}
}
