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
 * @Description: sop模板表头
 * @Author: Tian
 * @Date: 2021-07-15
 * @Version: V1.0
 */
@Slf4j
@Api(tags = "sop模板表头")
@RestController
@RequestMapping("/base/sop/sopTemplate")
public class SopTemplateController extends ILSController<SopTemplate, SopTemplateService> {
    /**
     * 1代表执行权限为用户
     */
    public static final String EXECUTE_AUTHORITY_USER = "1";
    /**
     * 2代表执行权限为角色
     */
    public static final String EXECUTE_AUTHORITY_ROLE = "2";
    /**
     * 模板控件逻辑中受控的代表字段
     */
    private static final String IN_CONTROLS = "1";
    /**
     * 模板控件逻辑中不受控的代表字段
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
     * 分页列表查询
     *
     * @param sopTemplate
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @ApiOperation(value = "sop模板表头-分页列表查询", notes = "sop模板表头-分页列表查询")
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
     * 添加
     *
     * @param sopTemplate
     * @return
     */
    @ApiOperation(value = "sop模板表头-添加", notes = "sop模板表头-添加")
    @PostMapping(value = "/add")
    public Result<?> add(@RequestBody SopTemplate sopTemplate) {
        sopTemplateService.saveSopTemplate(sopTemplate);
        return Result.ok(sopTemplate);
    }

    /**
     * 编辑
     *
     * @param sopTemplateVO
     * @return
     */
    @ApiOperation(value = "sop模板表头-编辑", notes = "sop模板表头-编辑")
    @PostMapping(value = "/edit")
    public Result<?> edit(@RequestBody SopTemplateVO sopTemplateVO) {
        sopTemplateService.updateSopTemplate(sopTemplateVO);
        return commonSuccessResult(CommonConstant.OPERATE_TYPE_EDIT);
    }

    /**
     * 通过sop权限类型查询执行人或角色列表
     *
     * @param type
     * @return
     */
    @ApiOperation(value = "sop模板表头-通过sop权限类型查询执行人或角色列表", notes = "sop模板表头-通过sop权限类型查询执行人或角色列表")
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
     * 通过id查询
     *
     * @param id
     * @return
     */
    @ApiOperation(value = "sop模板表头-通过id查询", notes = "sop模板表头-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<?> queryById(@RequestParam(name = "id", required = true) String id) {
        SopTemplateVO sopTemplateVO = sopTemplateService.queryById(id);
        return Result.ok(sopTemplateVO);
    }

    /**
     * 通过控件类型获取模板控件控制逻辑
     *
     * @param type
     * @return
     */
    @GetMapping(value = "/getControlLogic")
    @ApiOperation(value = "sop模板表头-通过控件类型获取模板控件控制逻辑", notes = "sop模板表头-通过控件类型获取模板控件控制逻辑")
    public Result<?> getControlLogic(@RequestParam(name = "type", required = true) String type) {
        SopControlTypeEnum sopControlTypeEnum = SopControlTypeEnum.match(type);

        if (sopControlTypeEnum == null) {
            return Result.ok(null);
        }
        return Result.ok(sopControlTypeEnum.getControlLogicMap());
    }

    @GetMapping(value = "getSopEntity")
    @ApiOperation(value = "sop模板表头-通过实体类型获取工序", notes = "sop模板表头-通过实体类型获取工序")
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
    @ApiOperation(value = "sop模板表头-通过控件类型和控件实体获取sop控件中的管控实体", notes = "sop模板表头-通过sop控件中的管控实体")
    public Result<?> getEntityItem(@RequestParam(name = "type", required = true) String type,
                                   @RequestParam(name = "logic", required = false) String logic,
                                   @RequestParam(name = "id", required = false) String id) {
        SopTemplate sopTemplate = sopTemplateService.getById(id);
        List<DictModel> dictModels = new ArrayList<>(16);
        //如果控制逻辑为不管控，则直接返回
        if (NOT_IN_CONTROLS.equals(logic)) {
            return Result.ok();
        }
        if (SopControlTypeEnum.LABEL_IN.getValue().equals(type) || SopControlTypeEnum.STORAGE_IN.getValue().equals(type)||SopControlTypeEnum.FEED.getValue().equals(type)) {
            //如果控制逻辑为管控，则确定具体的物料
            if (TemplateTypeEnum.PRODUCT_BOM.getValue().equals(sopTemplate.getTemplateType())) {
                return Result.ok(getItemByProductLineId(sopTemplate));
            }
            //如果模板的工序类型为工艺路线，则直接返回所以物料类型
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
    @ApiOperation(value = "sop模板表头-停用sop模板", notes = "sop模板表头-停用sop模板")
    public Result sopTemplateStop(@RequestParam(name = "id", required = true) String id) {
        sopTemplateService.stopTemplate(id);
        return Result.ok();
    }

    @GetMapping(value = "sopTemplateStart")
    @ApiOperation(value = "sop模板表头-启用sop模板", notes = "sop模板表头-启用sop模板")
    public Result sopTemplateStart(@RequestParam(name = "id", required = true) String id) {
        sopTemplateService.startTemplate(id);
        return Result.ok();
    }

    /**
     * 导出excel
     *
     * @param request
     * @param sopTemplate
     */
    @GetMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, SopTemplate sopTemplate) {
        return super.exportXls(request, sopTemplate, SopTemplate.class, "sop模板表头");
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
