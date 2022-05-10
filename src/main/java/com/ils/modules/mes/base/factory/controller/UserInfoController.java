/**
 * @Title: UserInfoController.java
 * @Package: com.ils.modules.mes.base.controller
 * @author: fengyi
 * @date: 2020年10月14日 下午2:48:35
 */
package com.ils.modules.mes.base.factory.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.ils.common.system.query.QueryGenerator;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ils.common.api.vo.Result;
import com.ils.common.system.base.controller.ILSController;
import com.ils.common.system.vo.DictModel;
import com.ils.modules.mes.base.factory.entity.WorkShopEmployee;
import com.ils.modules.mes.base.factory.service.WorkShopEmployeeService;
import com.ils.modules.mes.base.factory.vo.UserPosiztionVO;
import com.ils.modules.mes.machine.service.MachineRepairTaskService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

/**
 * @Description: 查询用户信息
 * @author: fengyi
 * @date: 2020年10月14日 下午2:48:35
 * @param:
 */
@RestController
@RequestMapping("/base/factory/userInfo")
@Api(tags = "用户信息")
@Slf4j
public class UserInfoController extends ILSController<WorkShopEmployee, WorkShopEmployeeService> {
    @Autowired
    private WorkShopEmployeeService workShopEmployeeService;
    @Autowired
    private MachineRepairTaskService machineRepairTaskService;

    /**
     * 分页列表查询
     *
     * @param user
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @ApiOperation(value = "用户-分页列表查询", notes = "用户-分页列表查询")
    @GetMapping(value = "/list")
    public Result<?> queryPageList(UserPosiztionVO userPosiztionVO,
                                   @RequestParam(name = PAGE_NO, defaultValue = DEFAULT_PAGE_NO) Integer pageNo,
                                   @RequestParam(name = PAGE_SIZE, defaultValue = DEFAULT_PAGE_SIZE) Integer pageSize, HttpServletRequest req) {

        QueryWrapper<UserPosiztionVO> queryWrapper = new QueryWrapper<>();
        String keyWord = req.getParameter("keyWord");
        if (StringUtils.isNoneBlank(keyWord)) {
            queryWrapper.like("concat(ifnull(u.realname,''),ifnull(u.username,''))", keyWord);
        }
        queryWrapper.eq("u.rel_tenant_ids", userPosiztionVO.getRelTenantIds());
        queryWrapper.eq("u.status", "1");
        queryWrapper.eq("u.is_deleted", "0");
        Page<UserPosiztionVO> page = new Page<UserPosiztionVO>(pageNo, pageSize);
        IPage<UserPosiztionVO> pageList = workShopEmployeeService.queryUserPositionInfo(page, queryWrapper);
        return Result.ok(pageList);
    }

    /**
     * 用户-id和名字查询
     *
     * @return
     */
    @ApiOperation(value = "用户-id和名字查询", notes = "用户-id和名字查询")
    @GetMapping(value = "/mesUserList")
    public Result<?> queryUserList() {
        List<DictModel> userList = machineRepairTaskService.queryUserInfoWithNameAndId();
        return Result.ok(userList);
    }
}
