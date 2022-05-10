package com.ils.modules.mes.base.schedule.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.ils.common.system.util.TenantContext;
import com.ils.modules.system.entity.Tenant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ils.common.api.vo.Result;
import com.ils.common.aspect.annotation.AutoLog;
import com.ils.common.constant.CommonConstant;
import com.ils.common.system.base.controller.ILSController;
import com.ils.common.system.query.QueryGenerator;
import com.ils.modules.mes.base.schedule.entity.ScheduleAutoRuleConfigure;
import com.ils.modules.mes.base.schedule.service.ScheduleAutoRuleConfigureService;
import com.ils.modules.mes.util.CommonUtil;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

 /**
 * @Description: 自动排程规则设置
 * @Author: fengyi
 * @Date: 2021-02-22
 * @Version: V1.0
 */
@Slf4j
@Api(tags="自动排程规则设置")
@RestController
@RequestMapping("/base/schedule/scheduleAutoRuleConfigure")
public class ScheduleAutoRuleConfigureController extends ILSController<ScheduleAutoRuleConfigure, ScheduleAutoRuleConfigureService> {
	@Autowired
	private ScheduleAutoRuleConfigureService scheduleAutoRuleConfigureService;
	
	/**
	 * 分页列表查询
	 *
	 * @param scheduleAutoRuleConfigure
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@ApiOperation(value="自动排程规则设置-分页列表查询", notes="自动排程规则设置-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(ScheduleAutoRuleConfigure scheduleAutoRuleConfigure,
								   @RequestParam(name = PAGE_NO, defaultValue = DEFAULT_PAGE_NO) Integer pageNo,
                                    @RequestParam(name = PAGE_SIZE, defaultValue = DEFAULT_PAGE_SIZE) Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<ScheduleAutoRuleConfigure> queryWrapper = QueryGenerator.initQueryWrapper(scheduleAutoRuleConfigure, req.getParameterMap());
		Page<ScheduleAutoRuleConfigure> page = new Page<ScheduleAutoRuleConfigure>(pageNo, pageSize);
		IPage<ScheduleAutoRuleConfigure> pageList = scheduleAutoRuleConfigureService.page(page, queryWrapper);
		return Result.ok(pageList);
	}
	
	/**
	 * 添加
	 *
	 * @param scheduleAutoRuleConfigure
	 * @return
	 */
	@AutoLog("自动排程规则设置-添加")
	@ApiOperation(value="自动排程规则设置-添加", notes="自动排程规则设置-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody ScheduleAutoRuleConfigure scheduleAutoRuleConfigure) {
		scheduleAutoRuleConfigureService.saveScheduleAutoRuleConfigure(scheduleAutoRuleConfigure);
		return commonSuccessResult(CommonConstant.OPERATE_TYPE_ADD);
	}
	
	/**
	 * 编辑
	 *
	 * @param scheduleAutoRuleConfigure
	 * @return
	 */
	@AutoLog("自动排程规则设置-编辑")
	@ApiOperation(value="自动排程规则设置-编辑", notes="自动排程规则设置-编辑")
	@PostMapping(value = "/edit")
	public Result<?> edit(@RequestBody ScheduleAutoRuleConfigure scheduleAutoRuleConfigure) {
		scheduleAutoRuleConfigureService.updateScheduleAutoRuleConfigure(scheduleAutoRuleConfigure);
		return commonSuccessResult(CommonConstant.OPERATE_TYPE_EDIT);
	}
	
	/**
	 * 通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "自动排程规则设置-通过id删除")
	@ApiOperation(value="自动排程规则设置-通过id删除", notes="自动排程规则设置-通过id删除")
	@GetMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		scheduleAutoRuleConfigureService.delScheduleAutoRuleConfigure(id);
		return commonSuccessResult(CommonConstant.OPERATE_TYPE_DELETE);
	}
	

    /**
     * 查询规则
     *
     * @param id
     * @return
     */
    @AutoLog(value = "自动排程规则设置-查询规则")
    @ApiOperation(value = "自动排程规则设置-查询规则", notes = "自动排程规则设置-查询规则")
    @GetMapping(value = "/queryScheAutoRule")
    public Result<?> queryScheAutoRule() {
		String tenant = TenantContext.getTenant();
		QueryWrapper<ScheduleAutoRuleConfigure> configureQueryWrapper=new QueryWrapper<>();
		configureQueryWrapper.eq("tenant_id",tenant);
		List<ScheduleAutoRuleConfigure> lstRule = scheduleAutoRuleConfigureService.list(configureQueryWrapper);
        if (!CommonUtil.isEmptyOrNull(lstRule)) {
            return Result.ok(lstRule.get(0));
        } else {
            return Result.ok(null);
        }
    }

}
