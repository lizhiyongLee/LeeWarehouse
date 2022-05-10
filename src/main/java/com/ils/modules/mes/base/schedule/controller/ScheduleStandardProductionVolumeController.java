package com.ils.modules.mes.base.schedule.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
import com.ils.common.aspect.annotation.AutoLog;
import com.ils.common.constant.CommonConstant;
import com.ils.common.system.base.controller.ILSController;
import com.ils.common.system.query.QueryGenerator;
import com.ils.modules.mes.base.craft.entity.Process;
import com.ils.modules.mes.base.craft.service.ProcessService;
import com.ils.modules.mes.base.craft.service.RouteLineService;
import com.ils.modules.mes.base.craft.vo.RouteLineVO;
import com.ils.modules.mes.base.product.service.ProductLineService;
import com.ils.modules.mes.base.product.vo.ProductLineVO;
import com.ils.modules.mes.base.schedule.entity.ScheduleStandardProductionVolume;
import com.ils.modules.mes.base.schedule.service.ScheduleStandardProductionVolumeService;
import com.ils.modules.mes.enums.StandardTypeEnum;
import com.ils.modules.mes.util.TreeNode;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

 /**
 * @Description: 标准产能
 * @Author: fengyi
 * @Date: 2021-02-01
 * @Version: V1.0
 */
@Slf4j
@Api(tags="标准产能")
@RestController
@RequestMapping("/base/schedule/scheduleStandardProductionVolume")
public class ScheduleStandardProductionVolumeController extends ILSController<ScheduleStandardProductionVolume, ScheduleStandardProductionVolumeService> {
	@Autowired
	private ScheduleStandardProductionVolumeService scheduleStandardProductionVolumeService;

    @Autowired
    private ProcessService processService;

    @Autowired
    private RouteLineService routeLineService;

    @Autowired
    private ProductLineService productLineService;

	/**
	 * 分页列表查询
	 *
	 * @param scheduleStandardProductionVolume
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@ApiOperation(value="标准产能-分页列表查询", notes="标准产能-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(ScheduleStandardProductionVolume scheduleStandardProductionVolume,
								   @RequestParam(name = PAGE_NO, defaultValue = DEFAULT_PAGE_NO) Integer pageNo,
                                    @RequestParam(name = PAGE_SIZE, defaultValue = DEFAULT_PAGE_SIZE) Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<ScheduleStandardProductionVolume> queryWrapper = QueryGenerator.initQueryWrapper(scheduleStandardProductionVolume, req.getParameterMap());
		Page<ScheduleStandardProductionVolume> page = new Page<ScheduleStandardProductionVolume>(pageNo, pageSize);
		IPage<ScheduleStandardProductionVolume> pageList = scheduleStandardProductionVolumeService.page(page, queryWrapper);
		return Result.ok(pageList);
	}
	
	/**
	 * 添加
	 *
	 * @param scheduleStandardProductionVolume
	 * @return
	 */
	@AutoLog("标准产能-添加")
	@ApiOperation(value="标准产能-添加", notes="标准产能-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody ScheduleStandardProductionVolume scheduleStandardProductionVolume) {
        List<ScheduleStandardProductionVolume> lstScheduleStandardProductionVolume =
            scheduleStandardProductionVolumeService
                .saveScheduleStandardProductionVolume(scheduleStandardProductionVolume);
		return commonSuccessResult(CommonConstant.OPERATE_TYPE_ADD);
	}
	
	/**
	 * 编辑
	 *
	 * @param scheduleStandardProductionVolume
	 * @return
	 */
	@AutoLog("标准产能-编辑")
	@ApiOperation(value="标准产能-编辑", notes="标准产能-编辑")
	@PostMapping(value = "/edit")
	public Result<?> edit(@RequestBody ScheduleStandardProductionVolume scheduleStandardProductionVolume) {
		boolean bResult = scheduleStandardProductionVolumeService.updateScheduleStandardProductionVolume(scheduleStandardProductionVolume);
		if(!bResult) {
            List<ScheduleStandardProductionVolume> lstFail = new ArrayList(1);
            lstFail.add(scheduleStandardProductionVolume);
            return Result.ok(lstFail);
        } else {
            return commonSuccessResult(CommonConstant.OPERATE_TYPE_EDIT);
        }
	}
	
	/**
	 * 通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "标准产能-通过id删除")
	@ApiOperation(value="标准产能-通过id删除", notes="标准产能-通过id删除")
	@GetMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		scheduleStandardProductionVolumeService.delScheduleStandardProductionVolume(id);
		return commonSuccessResult(CommonConstant.OPERATE_TYPE_DELETE);
	}
	
	/**
	 * 批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "标准产能-批量删除")
	@ApiOperation(value="标准产能-批量删除", notes="标准产能-批量删除")
	@GetMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		List<String> idList = Arrays.asList(ids.split(","));
		this.scheduleStandardProductionVolumeService.delBatchScheduleStandardProductionVolume(idList);
		return commonSuccessResult(CommonConstant.OPERATE_TYPE_DELETE);
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@ApiOperation(value="标准产能-通过id查询", notes="标准产能-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		ScheduleStandardProductionVolume scheduleStandardProductionVolume = scheduleStandardProductionVolumeService.getById(id);
		return Result.ok(scheduleStandardProductionVolume);
	}

    /**
     * 通过id更新状态
     *
     * @param id
     * @return
     */
    @ApiOperation(value = "标准产能-通过id更新状态", notes = "标准产能-通过id更新状态")
    @GetMapping(value = "/updateStatus")
    public Result<?> updateStatus(@RequestParam(name = "id", required = true) String id,
        @RequestParam(name = "status", required = true) String status) {

        scheduleStandardProductionVolumeService.updateStatus(id, status);
        return Result.ok();
    }

    /**
     * 查询工序下拉列表
     *
     * @param id
     * @return
     */
    @ApiOperation(value = "标准产能-查询工序下拉列表", notes = "标准产能-查询工序下拉列表")
    @GetMapping(value = "/processList")
    public Result<?> processList(@RequestParam(name = "standardType", required = true) String standardType,
        @RequestParam(name = "bussId", required = true) String bussId
        ) {
        if (StandardTypeEnum.PROCESS.getValue().equals(standardType)) {
            QueryWrapper<Process> queryWrapper = new QueryWrapper();
            queryWrapper.eq("status", "1");
            List<Process> lstProcess = processService.list(queryWrapper);
            return Result.ok(lstProcess);
        }

        if (StandardTypeEnum.ROUTE.getValue().equals(standardType)) {
            List<RouteLineVO> routeLineList = routeLineService.selectByMainId(bussId);
            return Result.ok(routeLineList);
        }

        if (StandardTypeEnum.PRODUCT_BOM.getValue().equals(standardType)) {
            List<ProductLineVO> lstProductLine = productLineService.selectByProductId(bussId);
            return Result.ok(lstProductLine);
        }
        return Result.ok();
    }

    /**
     * 根据条件查询工位
     *
     * @param id
     * @return
     */
    @ApiOperation(value = "根据条件查询工位-通过id查询", notes = "根据条件查询工位-通过id查询")
    @GetMapping(value = "/queryStationById")
    public Result<?> queryStationById(@RequestParam(name = "id", required = true) String id,
        @RequestParam(name = "standardType", required = true) String standardType) {
        List<TreeNode> lstTreeNode = scheduleStandardProductionVolumeService.queryStationById(id, standardType);
        return Result.ok(lstTreeNode);
    }

  /**
   * 导出excel
   *
   * @param request
   * @param scheduleStandardProductionVolume
   */
  @GetMapping(value = "/exportXls")
  public ModelAndView exportXls(HttpServletRequest request, ScheduleStandardProductionVolume scheduleStandardProductionVolume) {
      return super.exportXls(request, scheduleStandardProductionVolume, ScheduleStandardProductionVolume.class, "标准产能");
  }

  /**
   * 通过excel导入数据
   *
   * @param request
   * @param response
   * @return
   */
  @PostMapping(value = "/importExcel")
  public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
      return super.importExcel(request, response, ScheduleStandardProductionVolume.class);
  }
}
