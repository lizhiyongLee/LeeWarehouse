package com.ils.modules.mes.base.product.controller;

import java.util.*;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import com.ils.common.aspect.annotation.AutoLog;
import com.ils.common.system.vo.LoginUser;
import com.ils.framework.poi.excel.entity.ExportParams;
import com.ils.framework.poi.excel.entity.vo.NormalExcelConstants;
import com.ils.framework.poi.excel.view.ILSEntityExcelView;
import com.ils.modules.mes.base.material.entity.*;
import com.ils.modules.mes.base.material.vo.ItemQualityVO;
import com.ils.modules.mes.base.material.vo.ItemSupplierVO;
import com.ils.modules.mes.base.material.vo.ItemVO;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.BeanUtils;
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
import com.ils.common.constant.CommonConstant;
import com.ils.common.system.base.controller.ILSController;
import com.ils.common.system.query.QueryGenerator;
import com.ils.modules.mes.base.material.service.ItemService;
import com.ils.modules.mes.base.product.entity.ItemBom;
import com.ils.modules.mes.base.product.entity.ItemBomDetail;
import com.ils.modules.mes.base.product.entity.ItemBomSubstitute;
import com.ils.modules.mes.base.product.service.ItemBomDetailService;
import com.ils.modules.mes.base.product.service.ItemBomService;
import com.ils.modules.mes.base.product.service.ItemBomSubstituteService;
import com.ils.modules.mes.base.product.vo.ItemBomDetailVO;
import com.ils.modules.mes.base.product.vo.ItemBomSubstituteVO;
import com.ils.modules.mes.base.product.vo.ItemBomUnitVO;
import com.ils.modules.mes.base.product.vo.ItemBomVO;
import com.ils.modules.mes.base.product.vo.ResultDataVO;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.ModelAndView;

/**
 * @Description: 物料BOM
 * @Author: fengyi
 * @Date: 2020-10-26
 * @Version: V1.0
 */
@RestController
@RequestMapping("/base/product/itemBom")
@Api(tags="物料BOM")
@Slf4j
public class ItemBomController extends ILSController<ItemBom, ItemBomService> {
	@Autowired
	private ItemBomService itemBomService;

	@Autowired
    private ItemService itemService;

    @Autowired
	private ItemBomDetailService itemBomDetailService;
	
    @Autowired
    private ItemBomSubstituteService itemBomSubstituteService;

    /** bomId 参数名 */
    private static final String BOM_ID = "bomId";

    /** NOT_ITEM_IDS 参数名 */
    private static final String NOT_ITEM_IDS = "notItemIds";

	/**
	 * 分页列表查询
	 *
	 * @param itemBom
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
    @ApiOperation(value="物料BOM-分页列表查询", notes="物料BOM-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(ItemBom itemBom,
								   @RequestParam(name = PAGE_NO, defaultValue = DEFAULT_PAGE_NO) Integer pageNo,
            @RequestParam(name = PAGE_SIZE, defaultValue = DEFAULT_PAGE_SIZE) Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<ItemBom> queryWrapper = QueryGenerator.initQueryWrapper(itemBom, req.getParameterMap());
		Page<ItemBom> page = new Page<ItemBom>(pageNo, pageSize);
		IPage<ItemBom> pageList = itemBomService.page(page, queryWrapper);
		return Result.ok(pageList);
	}

    /**
     * 分页列表查询
     *
     * @param itemBom
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @ApiOperation(value = "选择物料-分页列表查询", notes = "选择物料-分页列表查询")
    @GetMapping(value = "/selectMaterialList")
    public Result<?> selectMaterialList(Item item,
        @RequestParam(name = PAGE_NO, defaultValue = DEFAULT_PAGE_NO) Integer pageNo,
        @RequestParam(name = PAGE_SIZE, defaultValue = DEFAULT_PAGE_SIZE) Integer pageSize, HttpServletRequest req) {
        String bomId = req.getParameter(BOM_ID);
        String notItemId = req.getParameter(NOT_ITEM_IDS);

        if (StringUtils.isNotBlank(bomId)) {
            ItemBomDetail itemBomDetail = new ItemBomDetail();
            QueryWrapper<ItemBomDetail> queryWrapper =
                QueryGenerator.initQueryWrapper(itemBomDetail, req.getParameterMap());
            queryWrapper.eq("bom_id", bomId);
            if (StringUtils.isNotBlank(notItemId)) {
                queryWrapper.notIn("item_id", Arrays.asList(notItemId.split(",")));
            }
            Page<ItemBomDetail> page = new Page<ItemBomDetail>(pageNo, pageSize);
            IPage<ItemBomDetail> pageList = itemBomDetailService.page(page, queryWrapper);
            return Result.ok(pageList);
        } else {
            QueryWrapper<Item> queryWrapper = QueryGenerator.initQueryWrapper(item, req.getParameterMap());

            if (StringUtils.isNotBlank(notItemId)) {
                queryWrapper.notIn("id", Arrays.asList(notItemId.split(",")));
            }
            Page<Item> page = new Page<Item>(pageNo, pageSize);
            IPage<Item> pageList = itemService.page(page, queryWrapper);
            return Result.ok(pageList);
        }

    }
	
	/**
	 * 添加
	 *
	 * @param itemBomPage
	 * @return
	 */
    @AutoLog("物料BOM-添加")
    @ApiOperation(value="物料BOM-添加", notes="物料BOM-添加")
	@PostMapping(value = "/add")
    public Result<?> add(@RequestBody ItemBomVO itemBomVO) {
        itemBomService.saveMain(itemBomVO);
        return commonSuccessResult(CommonConstant.OPERATE_TYPE_ADD);
	}
	
	/**
	 * 编辑
	 *
	 * @param itemBomPage
	 * @return
	 */
    @AutoLog("物料BOM-编辑")
    @ApiOperation(value="物料BOM-编辑", notes="物料BOM-编辑")
	@PostMapping(value = "/edit")
    public Result<?> edit(@RequestBody ItemBomVO itemBomVO) {
        itemBomService.updateMain(itemBomVO);
		return commonSuccessResult(CommonConstant.OPERATE_TYPE_EDIT);
	}
	

	
	/**
	 * 通过id查询
     *
	 * @param id
	 * @return
	 */
    @ApiOperation(value="物料BOM-通过id查询", notes="物料BOM-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		ItemBom itemBom = itemBomService.getById(id);
		ItemBomVO itemBomVO = new ItemBomVO();
        BeanUtils.copyProperties(itemBom, itemBomVO);
        List<ItemBomDetailVO> itemBomDetailList = itemBomDetailService.selectByMainId(id);
        QueryWrapper<ItemBomSubstitute> queryWrapper = new QueryWrapper<ItemBomSubstitute>();
        queryWrapper.eq("bom_id", id);
        List<ItemBomSubstituteVO> lstItemBomSubstitute =
            itemBomSubstituteService.queryBomSubstituteMaterialInfoList(queryWrapper);
        Map<String, List<ItemBomSubstituteVO>> itemGroupMap =
            lstItemBomSubstitute.stream().collect(Collectors.groupingBy(ItemBomSubstitute::getItemId));
        for (ItemBomDetailVO itemBomDetailVO : itemBomDetailList) {
            if (itemGroupMap.containsKey(itemBomDetailVO.getItemId())) {
                itemBomDetailVO.setItemBomSubstituteList(itemGroupMap.get(itemBomDetailVO.getItemId()));
            }
        }
        itemBomVO.setItemBomDetailList(itemBomDetailList);
		return Result.ok(itemBomVO);
	}

    /**
     * 通过id查询 BOM 明细,设置数量按基本单位返回
     *
     * @param id
     * @return
     */
    @ApiOperation(value = "物料BOM明细-通过id查询", notes = "物料BOM明细-通过id查询")
    @GetMapping(value = "/queryBomDetailById")
    public Result<?> queryBomDetailById(@RequestParam(name = "id", required = true) String id) {
        ResultDataVO resultDataVO = new ResultDataVO();
        List<ItemBomUnitVO> itemBomDetailList = itemBomDetailService.selectDetailInfoByMainId(id);
        resultDataVO.setData(itemBomDetailList);
        return Result.ok(resultDataVO);
    }
	


    /**
     * 物料BOM主表信息
     *
     * @param id
     * @return
     */
    @ApiOperation(value = "物料BOM主表信息-通过id查询", notes = "物料BOM主表信息-通过id查询")
    @GetMapping(value = "/queryMainInfoById")
    public Result<?> queryMainInfoById(@RequestParam(name = "id", required = true) String id) {
        ItemBom itemBom = itemBomService.getById(id);
        return Result.ok(itemBom);
    }


    /**
     * 导出excel
     *
     * @param request
     * @param item
     */
    @GetMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, ItemBom itemBom) {
        // Step.1 组装查询条件
        QueryWrapper<ItemBom> queryWrapper = QueryGenerator.initQueryWrapper(itemBom, request.getParameterMap());
        String[] selections = request.getParameterMap().get("selections");
        if (selections != null && selections.length > 0) {
            List<String> idList = Arrays.asList(selections[0].split(","));
            queryWrapper.in("id", idList);
        }
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();

        //Step.2 获取导出数据
        List<ItemBomVO> pageList = new ArrayList<ItemBomVO>();
        List<ItemBom> itemBomList = itemBomService.list(queryWrapper);

        List<String> idList = new ArrayList<>();
        for (ItemBom temp : itemBomList) {
            idList.add(temp.getId());
        }
        //2.1 取出符合条件的所有数据
        putList(pageList, itemBomList, idList);

        //Step.3 调用AutoPoi导出Excel
        ModelAndView mv = new ModelAndView(new ILSEntityExcelView());
        mv.addObject(NormalExcelConstants.FILE_NAME, "物料清单");
        mv.addObject(NormalExcelConstants.CLASS, ItemBomVO.class);
        mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("物料清单数据", "导出人:" + sysUser.getRealname(), "物料清单"));
        mv.addObject(NormalExcelConstants.DATA_LIST, pageList);
        return mv;
    }

    private void putList(List<ItemBomVO> pageList, List<ItemBom> itemBomList, List<String> idList) {
        QueryWrapper<ItemBomDetail> itemBomDetailQueryWrapper = new QueryWrapper<>();
        itemBomDetailQueryWrapper.in("bom_id", idList);
        List<ItemBomDetail> itemBomDetailList = itemBomDetailService.list(itemBomDetailQueryWrapper);

        //2.2 数据分类
        for (ItemBom temp : itemBomList) {
            itemBomDetailList.forEach(itemBomDetail -> {
                if (itemBomDetail.getBomId().equals(temp.getId())) {
                    ItemBomVO vo = new ItemBomVO();
                    BeanUtils.copyProperties(temp, vo);
                    ItemBomDetailVO itemBomDetailVO=new ItemBomDetailVO();
                    BeanUtils.copyProperties(itemBomDetail, itemBomDetailVO);
                    vo.setItemBomDetailList(Collections.singletonList(itemBomDetailVO));
                    pageList.add(vo);
                }
            });

        }
    }
}
