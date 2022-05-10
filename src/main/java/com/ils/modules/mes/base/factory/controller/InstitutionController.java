package com.ils.modules.mes.base.factory.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ils.common.api.vo.Result;
import com.ils.common.system.base.controller.ILSController;
import com.ils.modules.mes.base.factory.entity.Customer;
import com.ils.modules.mes.base.factory.service.CustomerService;
import com.ils.modules.mes.base.factory.service.WorkShopService;
import com.ils.modules.mes.util.TreeNode;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

 /**
 * @Description: 机构定义
 * @Author: fengyi
 * @Date: 2020-10-15
 * @Version: V1.0
 */
@Slf4j
@Api(tags = "机构定义")
@RestController
@RequestMapping("/base/factory/institution")
public class InstitutionController extends ILSController<Customer, CustomerService> {
    @Autowired
    private WorkShopService workShopService;

    /**
     * 机构定义查询
     *
     * @param factory
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @ApiOperation(value = "机构定义-查询", notes = "机构定义查询")
    @GetMapping(value = "/queryInstitutionList")
    public Result<?> queryInstitutionList(HttpServletRequest req) {
        String status = req.getParameter("status");
        String name = req.getParameter("name");

        List<TreeNode> lstNodeTreeVO = workShopService.queryInstitutionTreeList(status, name);

        return Result.ok(lstNodeTreeVO);
    }
}
