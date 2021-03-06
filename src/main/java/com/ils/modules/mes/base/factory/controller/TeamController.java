package com.ils.modules.mes.base.factory.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.SecurityUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ils.common.api.vo.Result;
import com.ils.common.aspect.annotation.AutoLog;
import com.ils.common.constant.CommonConstant;
import com.ils.common.system.base.controller.ILSController;
import com.ils.common.system.query.QueryGenerator;
import com.ils.common.system.util.TenantContext;
import com.ils.common.system.vo.LoginUser;
import com.ils.framework.poi.excel.ExcelImportUtil;
import com.ils.framework.poi.excel.entity.ExportParams;
import com.ils.framework.poi.excel.entity.ImportParams;
import com.ils.framework.poi.excel.entity.vo.NormalExcelConstants;
import com.ils.framework.poi.excel.view.ILSEntityExcelView;
import com.ils.modules.mes.base.factory.entity.Team;
import com.ils.modules.mes.base.factory.entity.TeamEmployee;
import com.ils.modules.mes.base.factory.service.TeamEmployeeService;
import com.ils.modules.mes.base.factory.service.TeamService;
import com.ils.modules.mes.base.factory.vo.TeamVO;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
 
/**
 * @Description: ??????
 * @Author: fengyi
 * @Date: 2020-10-15
 * @Version: V1.0
 */
@RestController
@RequestMapping("/base/factory/team")
@Api(tags="??????")
@Slf4j
public class TeamController extends ILSController<Team, TeamService> {
	@Autowired
	private TeamService teamService;
	@Autowired
	private TeamEmployeeService teamEmployeeService;
	
	/**
	 * ??????????????????
	 *
	 * @param team
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
    @ApiOperation(value="??????-??????????????????", notes="??????-??????????????????")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(Team team,
								   @RequestParam(name = PAGE_NO, defaultValue = DEFAULT_PAGE_NO) Integer pageNo,
            @RequestParam(name = PAGE_SIZE, defaultValue = DEFAULT_PAGE_SIZE) Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<Team> queryWrapper = QueryGenerator.initQueryWrapper(team, req.getParameterMap());
		Page<Team> page = new Page<Team>(pageNo, pageSize);
		IPage<Team> pageList = teamService.page(page, queryWrapper);
		return Result.ok(pageList);
	}
	
	/**
	 * ??????
	 *
	 * @param teamPage
	 * @return
	 */
    @ApiOperation(value="??????-??????", notes="??????-??????")
	@PostMapping(value = "/add")
	@AutoLog("??????-??????")
	public Result<?> add(@RequestBody TeamVO teamPage) {
		Team team = new Team();
		BeanUtils.copyProperties(teamPage, team);
        team.setTenantId(TenantContext.getTenant());
		teamService.saveMain(team, teamPage.getTeamEmployeeList());
		return commonSuccessResult(CommonConstant.OPERATE_TYPE_ADD);
	}
	
	/**
	 * ??????
	 *
	 * @param teamPage
	 * @return
	 */
    @ApiOperation(value="??????-??????", notes="??????-??????")
	@PostMapping(value = "/edit")
	@AutoLog("??????-??????")
	public Result<?> edit(@RequestBody TeamVO teamPage) {
		Team team = new Team();
		BeanUtils.copyProperties(teamPage, team);
		teamService.updateMain(team, teamPage.getTeamEmployeeList());
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
        TeamVO teamVO = new TeamVO();
		Team team = teamService.getById(id);
        BeanUtils.copyProperties(team, teamVO);
        List<TeamEmployee> teamEmployeeList = teamEmployeeService.selectByMainId(id);
        teamVO.setTeamEmployeeList(teamEmployeeList);
        return Result.ok(teamVO);
	}
	
	/**
	 * ??????id??????
     *
	 * @param id
	 * @return
	 */
    @ApiOperation(value="??????-????????????id??????????????????", notes="??????-????????????id??????????????????")
	@GetMapping(value = "/queryTeamEmployeeByMainId")
	public Result<?> queryTeamEmployeeListByMainId(@RequestParam(name="id",required=true) String id) {
		List<TeamEmployee> teamEmployeeList = teamEmployeeService.selectByMainId(id);
		return Result.ok(teamEmployeeList);
	}

  /**
   * ??????excel
   *
   * @param request
   * @param team
   */
  @GetMapping(value = "/exportXls")
  public ModelAndView exportXls(HttpServletRequest request, Team team) {
      // Step.1 ??????????????????
      QueryWrapper<Team> queryWrapper = QueryGenerator.initQueryWrapper(team, request.getParameterMap());
	  String[] selections = request.getParameterMap().get("selections");
	  if (selections != null && selections.length > 0) {
		  List<String> idList = Arrays.asList(selections[0].split(","));
		  queryWrapper.in("id", idList);
	  }
      LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();

      //Step.2 ??????????????????
      List<TeamVO> pageList = new ArrayList<TeamVO>();
      List<Team> teamList = teamService.list(queryWrapper);
      for (Team temp : teamList) {
          TeamVO vo = new TeamVO();
          BeanUtils.copyProperties(temp, vo);
          List<TeamEmployee> teamEmployeeList = teamEmployeeService.selectByMainId(temp.getId());
          vo.setTeamEmployeeList(teamEmployeeList);
          pageList.add(vo);
      }
      //Step.3 ??????AutoPoi??????Excel
      ModelAndView mv = new ModelAndView(new ILSEntityExcelView());
      mv.addObject(NormalExcelConstants.FILE_NAME, "??????");
      mv.addObject(NormalExcelConstants.CLASS, TeamVO.class);
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
  @PostMapping(value = "/importExcel")
  public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
      MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
      Map<String, MultipartFile> fileMap = multipartRequest.getFileMap();
      for (Map.Entry<String, MultipartFile> entity : fileMap.entrySet()) {
          MultipartFile file = entity.getValue();
          ImportParams params = new ImportParams();
          params.setTitleRows(2);
          params.setHeadRows(1);
          params.setNeedSave(true);
          try {
              List<TeamVO> list = ExcelImportUtil.importExcel(file.getInputStream(), TeamVO.class, params);
              for (TeamVO page : list) {
                  Team po = new Team();
                  BeanUtils.copyProperties(page, po);
                  teamService.saveMain(po, page.getTeamEmployeeList());
              }
              return Result.ok("?????????????????????????????????:" + list.size());
          } catch (Exception e) {
              log.error(e.getMessage(),e);
              return Result.error("??????????????????:"+e.getMessage());
          } finally {
              try {
                  file.getInputStream().close();
              } catch (IOException e) {
                  e.printStackTrace();
              }
          }
      }
      return Result.error("?????????????????????");
  }

}
