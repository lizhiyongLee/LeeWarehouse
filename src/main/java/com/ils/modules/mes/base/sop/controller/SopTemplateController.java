package com.ils.modules.mes.base.sop.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ils.common.api.vo.Result;
import com.ils.common.constant.CommonConstant;
import com.ils.common.system.base.controller.ILSController;
import com.ils.common.system.query.QueryGenerator;
import com.ils.common.system.vo.DictModel;
import com.ils.modules.mes.base.craft.mapper.RouteLineMethodMapper;
import com.ils.modules.mes.base.craft.service.RouteLineService;
import com.ils.modules.mes.base.craft.vo.RouteLineMethodVO;
import com.ils.modules.mes.base.craft.vo.RouteLineVO;
import com.ils.modules.mes.base.factory.service.ReportTemplateService;
import com.ils.modules.mes.base.material.entity.Item;
import com.ils.modules.mes.base.material.service.ItemService;
import com.ils.modules.mes.base.product.entity.ProductBom;
import com.ils.modules.mes.base.product.mapper.ProductRouteMethodMapper;
import com.ils.modules.mes.base.product.service.ProductBomService;
import com.ils.modules.mes.base.product.service.ProductLineService;
import com.ils.modules.mes.base.product.service.ProductService;
import com.ils.modules.mes.base.product.vo.ProductLineVO;
import com.ils.modules.mes.base.qc.entity.QcMethod;
import com.ils.modules.mes.base.sop.entity.SopTemplate;
import com.ils.modules.mes.base.sop.service.SopTemplateService;
import com.ils.modules.mes.base.sop.vo.SopTemplateVO;
import com.ils.modules.mes.enums.SopControlTypeEnum;
import com.ils.modules.mes.enums.TemplateTypeEnum;
import com.ils.modules.mes.enums.ZeroOrOneEnum;
import com.ils.modules.mes.util.CommonUtil;
import com.ils.modules.system.entity.Role;
import com.ils.modules.system.entity.User;
import com.ils.modules.system.service.RoleService;
import com.ils.modules.system.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

/**
 * @Description: sop????????????
 * @Author: Tian
 * @Date: 2021-07-15
 * @Version: V1.0
 */
@Slf4j
@Api(tags = "sop????????????")
@RestController
@RequestMapping("/base/sop/sopTemplate")
public class SopTemplateController extends ILSController<SopTemplate, SopTemplateService> {
    /**
     * 1???????????????????????????
     */
    public static final String EXECUTE_AUTHORITY_USER = "1";
    /**
     * 2???????????????????????????
     */
    public static final String EXECUTE_AUTHORITY_ROLE = "2";
    /**
     * ??????????????????????????????????????????
     */
    private static final String IN_CONTROLS = "1";
    /**
     * ?????????????????????????????????????????????
     */
    private static final String NOT_IN_CONTROLS = "2";
    @Autowired
    private SopTemplateService sopTemplateService;
    @Autowired
    private UserService userService;
    @Autowired
    private RoleService roleService;
    @Autowired
    private RouteLineService routeLineService;
    @Autowired
    private ProductLineService productLineService;
    @Autowired
    private ItemService itemService;
    @Autowired
    private ProductService productService;
    @Autowired
    private ProductRouteMethodMapper productRouteMethodMapper;
    @Autowired
    private RouteLineMethodMapper routeLineMethodMapper;
    @Autowired
    private ReportTemplateService reportTemplateService;
    @Autowired
    private ProductBomService productBomService;

    /**
     * ??????????????????
     *
     * @param sopTemplate
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @ApiOperation(value = "sop????????????-??????????????????", notes = "sop????????????-??????????????????")
    @GetMapping(value = "/list")
    public Result<?> queryPageList(SopTemplate sopTemplate,
                                   @RequestParam(name = PAGE_NO, defaultValue = DEFAULT_PAGE_NO) Integer pageNo,
                                   @RequestParam(name = PAGE_SIZE, defaultValue = DEFAULT_PAGE_SIZE) Integer pageSize,
                                   HttpServletRequest req) {
        QueryWrapper<SopTemplate> queryWrapper = QueryGenerator.initQueryWrapper(sopTemplate, req.getParameterMap());
        Page<SopTemplate> page = new Page<SopTemplate>(pageNo, pageSize);
        IPage<SopTemplate> pageList = sopTemplateService.page(page, queryWrapper);
        return Result.ok(pageList);
    }

    /**
     * ??????
     *
     * @param sopTemplate
     * @return
     */
    @ApiOperation(value = "sop????????????-??????", notes = "sop????????????-??????")
    @PostMapping(value = "/add")
    public Result<?> add(@RequestBody SopTemplate sopTemplate) {
        sopTemplateService.saveSopTemplate(sopTemplate);
        return Result.ok(sopTemplate);
    }

    /**
     * ??????
     *
     * @param sopTemplateVO
     * @return
     */
    @ApiOperation(value = "sop????????????-??????", notes = "sop????????????-??????")
    @PostMapping(value = "/edit")
    public Result<?> edit(@RequestBody SopTemplateVO sopTemplateVO) {
        sopTemplateService.updateSopTemplate(sopTemplateVO);
        return commonSuccessResult(CommonConstant.OPERATE_TYPE_EDIT);
    }

    /**
     * ??????sop??????????????????????????????????????????
     *
     * @param type
     * @return
     */
    @ApiOperation(value = "sop????????????-??????sop??????????????????????????????????????????", notes = "sop????????????-??????sop??????????????????????????????????????????")
    @GetMapping(value = "/getExecuter")
    public Result getExecuterByExecuteAuthorityType(@RequestParam(name = "type", required = true) String type) {
        List<DictModel> dictModels = new ArrayList<>(16);
        if (EXECUTE_AUTHORITY_USER.equals(type)) {
            QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
            userQueryWrapper.eq("rel_tenant_ids", CommonUtil.getTenantId());
            userQueryWrapper.select("id,username");
            List<User> list = userService.list(userQueryWrapper);
            for (User user : list) {
                DictModel dictModel = new DictModel();
                dictModel.setText(user.getUsername());
                dictModel.setValue(user.getId());
                dictModels.add(dictModel);
            }
        } else if (EXECUTE_AUTHORITY_ROLE.equals(type)) {
            List<Role> roles = roleService.list();
            for (Role role : roles) {
                DictModel dictModel = new DictModel();
                dictModel.setValue(role.getId());
                dictModel.setText(role.getRoleName());
                dictModels.add(dictModel);
            }
        } else {
            return Result.ok(null);
        }

        return Result.ok(dictModels);
    }

    /**
     * ??????id??????
     *
     * @param id
     * @return
     */
    @ApiOperation(value = "sop????????????-??????id??????", notes = "sop????????????-??????id??????")
    @GetMapping(value = "/queryById")
    public Result<?> queryById(@RequestParam(name = "id", required = true) String id) {
        SopTemplateVO sopTemplateVO = sopTemplateService.queryById(id);
        return Result.ok(sopTemplateVO);
    }

    /**
     * ????????????????????????????????????????????????
     *
     * @param type
     * @return
     */
    @GetMapping(value = "/getControlLogic")
    @ApiOperation(value = "sop????????????-????????????????????????????????????????????????", notes = "sop????????????-????????????????????????????????????????????????")
    public Result<?> getControlLogic(@RequestParam(name = "type", required = true) String type) {
        SopControlTypeEnum sopControlTypeEnum = SopControlTypeEnum.match(type);

        if (sopControlTypeEnum == null) {
            return Result.ok(null);
        }
        return Result.ok(sopControlTypeEnum.getControlLogicMap());
    }

    @GetMapping(value = "getSopEntity")
    @ApiOperation(value = "sop????????????-??????????????????????????????", notes = "sop????????????-??????????????????????????????")
    public Result<?> getSopEntity(
            @RequestParam(name = "type", required = true) String type
            , @RequestParam(name = "id", required = true) String id) {
        if (TemplateTypeEnum.ROUTE.getValue().equals(type)) {
            List<RouteLineVO> routeLineVOList = routeLineService.selectByMainId(id);
            return Result.ok(routeLineVOList);
        } else if (TemplateTypeEnum.PRODUCT_BOM.getValue().equals(type)) {
            List<ProductLineVO> productLineVOList = productLineService.selectByProductId(id);
            return Result.ok(productLineVOList);
        }
        return Result.ok();
    }

    @GetMapping(value = "getEntityItem")
    @ApiOperation(value = "sop????????????-???????????????????????????????????????sop????????????????????????", notes = "sop????????????-??????sop????????????????????????")
    public Result<?> getEntityItem(@RequestParam(name = "type", required = true) String type,
                                   @RequestParam(name = "logic", required = false) String logic,
                                   @RequestParam(name = "id", required = false) String id) {
        SopTemplate sopTemplate = sopTemplateService.getById(id);
        List<DictModel> dictModels = new ArrayList<>(16);
        //????????????????????????????????????????????????
        if (NOT_IN_CONTROLS.equals(logic)) {
            return Result.ok();
        }
        if (SopControlTypeEnum.LABEL_IN.getValue().equals(type) || SopControlTypeEnum.STORAGE_IN.getValue().equals(type)||SopControlTypeEnum.FEED.getValue().equals(type)) {
            //??????????????????????????????????????????????????????
            if (TemplateTypeEnum.PRODUCT_BOM.getValue().equals(sopTemplate.getTemplateType())) {
                return Result.ok(getItemByProductLineId(sopTemplate));
            }
            //??????????????????????????????????????????????????????????????????????????????
            QueryWrapper<Item> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("is_deleted", ZeroOrOneEnum.ZERO.getIcode());
            queryWrapper.eq("is_qrcode", ZeroOrOneEnum.ONE.getStrCode());
            List<Item> list = itemService.list(queryWrapper);
            for (Item item : list) {
                DictModel dictModel = new DictModel();
                dictModel.setValue(item.getId());
                dictModel.setText(item.getItemName());
                dictModels.add(dictModel);
            }
            return Result.ok(dictModels);
        }  else if (SopControlTypeEnum.QC.getValue().equals(type)) {
            if (TemplateTypeEnum.PRODUCT_BOM.getValue().equals(sopTemplate.getTemplateType())) {
                List<QcMethod> qcMethodList = productRouteMethodMapper.selectByProductLineIdAndQcMethodType(sopTemplate.getEntityLineId(), logic);
                for (QcMethod qcMethod : qcMethodList) {
                    DictModel dictModel = new DictModel();
                    dictModel.setText(qcMethod.getMethodName());
                    dictModel.setValue(qcMethod.getId());
                    dictModels.add(dictModel);
                }
            } else if (TemplateTypeEnum.ROUTE.getValue().equals(sopTemplate.getTemplateType())) {
                List<RouteLineMethodVO> routeLineMethodVOList = routeLineMethodMapper.selectByRouteLineId(sopTemplate.getEntityLineId(), logic);
                for (RouteLineMethodVO routeLineMethodVO : routeLineMethodVOList) {
                    DictModel dictModel = new DictModel();
                    dictModel.setValue(routeLineMethodVO.getQcMethodId());
                    dictModel.setText(routeLineMethodVO.getMethodName());
                    dictModels.add(dictModel);
                }
            }
            return Result.ok(dictModels);
        } else if (SopControlTypeEnum.REPORT_TEMPLATE.getValue().equals(type)) {
            List<DictModel> models = reportTemplateService.queryDictTemplate();
            return Result.ok(models);
        }
        return Result.ok();
    }

    @GetMapping(value = "sopTemplateStop")
    @ApiOperation(value = "sop????????????-??????sop??????", notes = "sop????????????-??????sop??????")
    public Result sopTemplateStop(@RequestParam(name = "id", required = true) String id) {
        sopTemplateService.stopTemplate(id);
        return Result.ok();
    }

    @GetMapping(value = "sopTemplateStart")
    @ApiOperation(value = "sop????????????-??????sop??????", notes = "sop????????????-??????sop??????")
    public Result sopTemplateStart(@RequestParam(name = "id", required = true) String id) {
        sopTemplateService.startTemplate(id);
        return Result.ok();
    }

    /**
     * ??????excel
     *
     * @param request
     * @param sopTemplate
     */
    @GetMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, SopTemplate sopTemplate) {
        return super.exportXls(request, sopTemplate, SopTemplate.class, "sop????????????");
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
        return super.importExcel(request, response, SopTemplate.class);
    }


    private List<DictModel> getItemByProductLineId(SopTemplate sopTemplate) {
        List<DictModel> dictModels = new ArrayList<>(16);
        QueryWrapper<ProductBom> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("product_line_id", sopTemplate.getEntityLineId());
        queryWrapper.eq("is_deleted", ZeroOrOneEnum.ZERO.getIcode());
        List<ProductBom> list = productBomService.list(queryWrapper);
        for (ProductBom productBom : list) {
            DictModel dictModel = new DictModel();
            dictModel.setText(productBom.getItemName());
            dictModel.setValue(productBom.getItemId());
            dictModels.add(dictModel);
        }
        return dictModels;
    }
}
