package com.ils.modules.mes.sop.controller;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ils.common.api.vo.Result;
import com.ils.common.constant.CommonConstant;
import com.ils.common.exception.ILSBootException;
import com.ils.common.system.base.controller.ILSController;
import com.ils.common.system.query.QueryGenerator;
import com.ils.common.system.vo.LoginUser;
import com.ils.modules.mes.constants.MesCommonConstant;
import com.ils.modules.mes.enums.ZeroOrOneEnum;
import com.ils.modules.mes.qc.entity.QcTaskEmployee;
import com.ils.modules.mes.qc.mapper.QcTaskEmployeeMapper;
import com.ils.modules.mes.sop.entity.SopStep;
import com.ils.modules.mes.sop.service.SopStepService;
import com.ils.modules.mes.sop.vo.SopStepVO;
import com.ils.modules.mes.util.CommonUtil;
import com.ils.modules.system.entity.UserRole;
import com.ils.modules.system.service.UserRoleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @Description: 标准作业任务步骤
 * @Author: Tian
 * @Date: 2021-07-16
 * @Version: V1.0
 */
@Slf4j
@Api(tags = "标准作业任务步骤")
@RestController
@RequestMapping("/sop/sopStep")
public class SopStepController extends ILSController<SopStep, SopStepService> {
    /**
     * 1代表执行权限为用户
     */
    private static final String EXECUTE_AUTHORITY_USER = "1";
    /**
     * 2代表执行权限为角色
     */
    private static final String EXECUTE_AUTHORITY_ROLE = "2";
    @Autowired
    private SopStepService sopStepService;
    @Autowired
    private UserRoleService userRoleService;
    @Autowired
    private QcTaskEmployeeMapper qcTaskEmployeeMapper;

    /**
     * 分页列表查询
     *
     * @param sopStep
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @ApiOperation(value = "标准作业任务步骤-分页列表查询", notes = "标准作业任务步骤-分页列表查询")
    @GetMapping(value = "/list")
    public Result<?> queryPageList(SopStep sopStep,
                                   @RequestParam(name = PAGE_NO, defaultValue = DEFAULT_PAGE_NO) Integer pageNo,
                                   @RequestParam(name = PAGE_SIZE, defaultValue = DEFAULT_PAGE_SIZE) Integer pageSize,
                                   HttpServletRequest req) {
        QueryWrapper<SopStep> queryWrapper = QueryGenerator.initQueryWrapper(sopStep, req.getParameterMap());
        Page<SopStep> page = new Page<SopStep>(pageNo, pageSize);
        IPage<SopStep> pageList = sopStepService.page(page, queryWrapper);
        return Result.ok(pageList);
    }

    @ApiOperation(value = "标准作业任务步骤-查询质检任务是否被人领取过了", notes = "标准作业任务步骤-查询质检任务是否被人领取过了")
    @GetMapping(value = "/queryQcTaskExecutePermissions")
    public Result<?> queryQcTaskExecutePermissions(@RequestParam(name = "id", required = true) String id) {

        //2.查询该任务是否是被领取过
        QueryWrapper<QcTaskEmployee> queryWrapper = new QueryWrapper<>();
        queryWrapper.in("using_type", "1", "2");
        queryWrapper.eq("excute_task_id", id);
        queryWrapper.eq("is_deleted", ZeroOrOneEnum.ZERO.getStrCode());
        List<QcTaskEmployee> qcTaskEmployees = qcTaskEmployeeMapper.selectList(queryWrapper);

        if (CollectionUtil.isEmpty(qcTaskEmployees)) {
            return Result.ok(MesCommonConstant.COULD_RECEIVED_QC_TASK);
        }
        //1.当前用户是该执行人 2.但钱
        LoginUser loginUser = CommonUtil.getLoginUser();
        for (QcTaskEmployee qcTaskEmployee : qcTaskEmployees) {
            if (loginUser.getId().equals(qcTaskEmployee.getEmployeeId())) {
                return Result.ok(MesCommonConstant.HAS_RECEIVED_QC_TASK);
            }
        }
        //3代表没有权限，只能查看
        return Result.ok(MesCommonConstant.COULD_NOT_RECEIVED_QC_TASK);
    }

    /**
     * 添加
     *
     * @param sopStep
     * @return
     */
    @ApiOperation(value = "标准作业任务步骤-添加", notes = "标准作业任务步骤-添加")
    @PostMapping(value = "/add")
    public Result<?> add(@RequestBody SopStep sopStep) {
        sopStepService.saveSopStep(sopStep);
        return Result.ok(sopStep);
    }

    /**
     * 编辑
     *
     * @param sopStep
     * @return
     */
    @ApiOperation(value = "标准作业任务步骤-编辑", notes = "标准作业任务步骤-编辑")
    @PostMapping(value = "/edit")
    public Result<?> edit(@RequestBody SopStep sopStep) {
        sopStepService.updateSopStep(sopStep);
        return commonSuccessResult(CommonConstant.OPERATE_TYPE_EDIT);
    }

    @ApiOperation(value = "标准作业任务-完成当前任务", notes = "标准作业任务步骤控件-编辑")
    @GetMapping(value = "/completeTask")
    public Result<?> completeTask(@RequestParam(value = "id", required = true) String id) {
        //判断是否有有权限执行该任务
        SopStep sopStep = sopStepService.getById(id);
        LoginUser loginUser = CommonUtil.getLoginUser();
        String executeAuthority = sopStep.getExecuteAuthority();
        if (EXECUTE_AUTHORITY_USER.equals(executeAuthority)) {
            if (!loginUser.getId().equals(sopStep.getExecuter())) {
                throw new ILSBootException("P-SOP-0090");
            }
        } else if (EXECUTE_AUTHORITY_ROLE.equals(executeAuthority)) {
            QueryWrapper<UserRole> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("user_id", loginUser.getId());
            List<UserRole> list = userRoleService.list(queryWrapper);
            if (!sopStep.getExecuter().equals(list.get(0).getRoleId())) {
                throw new ILSBootException("P-SOP-0090");
            }
        }
        //步骤完成，更新下个步骤状态，生成对应的质检任务
        sopStepService.completeTask(id);
        return Result.ok();
    }

    /**
     * 通过任务id查询标准作业流程
     *
     * @param id
     * @return
     */
    @ApiOperation(value = "标准作业任务-通过任务id查询标准作业流程", notes = "标准作业任务-通过任务id查询标准作业流程")
    @GetMapping(value = "/queryByTaskId")
    public Result<?> queryByTaskId(@RequestParam(name = "id", required = true) String id) {
        List<SopStepVO> sopStepVOList = sopStepService.queryByTaskId(id);
        return Result.ok(sopStepVOList);
    }


    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    @ApiOperation(value = "标准作业任务步骤-通过id查询", notes = "标准作业任务步骤-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<?> queryById(@RequestParam(name = "id", required = true) String id) {
        QueryWrapper<SopStep> sopStepQueryWrapper = new QueryWrapper<>();
        sopStepQueryWrapper.eq("related_task_id", id);
        sopStepQueryWrapper.eq("is_deleted", ZeroOrOneEnum.ZERO.getIcode());
        sopStepQueryWrapper.orderByAsc("step_seq");
        List<SopStep> list = sopStepService.list(sopStepQueryWrapper);
        return Result.ok(list);
    }


    /**
     * 导出excel
     *
     * @param request
     * @param sopStep
     */
    @GetMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, SopStep sopStep) {
        return super.exportXls(request, sopStep, SopStep.class, "标准作业任务步骤");
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
        return super.importExcel(request, response, SopStep.class);
    }
}
