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
 * @Description: ??????BOM
 * @Author: fengyi
 * @Date: 2020-10-26
 * @Version: V1.0
 */
@RestController
@RequestMapping("/base/product/itemBom")
@Api(tags="??????BOM")
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

    /** bomId ????????? */
    private static final String BOM_ID = "bomId";

    /** NOT_ITEM_IDS ????????? */
    private static final String NOT_ITEM_IDS = "notItemIds";

	/**
	 * ??????????????????
	 *
	 * @param itemBom
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
    @ApiOperation(value="??????BOM-??????????????????", notes="??????BOM-??????????????????")
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
     * ??????????????????
     *
     * @param itemBom
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @ApiOperation(value = "????????????-??????????????????", notes = "????????????-??????????????????")
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
	 * ??????
	 *
	 * @param itemBomPage
	 * @return
	 */
    @AutoLog("??????BOM-??????")
    @ApiOperation(value="??????BOM-??????", notes="??????BOM-??????")
	@PostMapping(value = "/add")
    public Result<?> add(@RequestBody ItemBomVO itemBomVO) {
        itemBomService.saveMain(itemBomVO);
        return commonSuccessResult(CommonConstant.OPERATE_TYPE_ADD);
	}
	
	/**
	 * ??????
	 *
	 * @param itemBomPage
	 * @return
	 */
    @AutoLog("??????BOM-??????")
    @ApiOperation(value="??????BOM-??????", notes="??????BOM-??????")
	@PostMapping(value = "/edit")
    public Result<?> edit(@RequestBody ItemBomVO itemBomVO) {
        itemBomService.updateMain(itemBomVO);
		return commonSuccessResult(CommonConstant.OPERATE_TYPE_EDIT);
	}
	

	
	/**
	 * ??????id??????
     *
	 * @param id
	 * @return
	 */
    @ApiOperation(value="??????BOM-??????id??????", notes="??????BOM-??????id??????")
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
     * ??????id?????? BOM ??????,?????????????????????????????????
     *
     * @param id
     * @return
     */
    @ApiOperation(value = "??????BOM??????-??????id??????", notes = "??????BOM??????-??????id??????")
    @GetMapping(value = "/queryBomDetailById")
    public Result<?> queryBomDetailById(@RequestParam(name = "id", required = true) String id) {
        ResultDataVO resultDataVO = new ResultDataVO();
        List<ItemBomUnitVO> itemBomDetailList = itemBomDetailService.selectDetailInfoByMainId(id);
        resultDataVO.setData(itemBomDetailList);
        return Result.ok(resultDataVO);
    }
	


    /**
     * ??????BOM????????????
     *
     * @param id
     * @return
     */
    @ApiOperation(value = "??????BOM????????????-??????id??????", notes = "??????BOM????????????-??????id??????")
    @GetMapping(value = "/queryMainInfoById")
    public Result<?> queryMainInfoById(@RequestParam(name = "id", required = true) String id) {
        ItemBom itemBom = itemBomService.getById(id);
        return Result.ok(itemBom);
    }


    /**
     * ??????excel
     *
     * @param request
     * @param item
     */
    @GetMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, ItemBom itemBom) {
        // Step.1 ??????????????????
        QueryWrapper<ItemBom> queryWrapper = QueryGenerator.initQueryWrapper(itemBom, request.getParameterMap());
        String[] selections = request.getParameterMap().get("selections");
        if (selections != null && selections.length > 0) {
            List<String> idList = Arrays.asList(selections[0].split(","));
            queryWrapper.in("id", idList);
        }
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();

        //Step.2 ??????????????????
        List<ItemBomVO> pageList = new ArrayList<ItemBomVO>();
        List<ItemBom> itemBomList = itemBomService.list(queryWrapper);

        List<String> idList = new ArrayList<>();
        for (ItemBom temp : itemBomList) {
            idList.add(temp.getId());
        }
        //2.1 ?????????????????????????????????
        putList(pageList, itemBomList, idList);

        //Step.3 ??????AutoPoi??????Excel
        ModelAndView mv = new ModelAndView(new ILSEntityExcelView());
        mv.addObject(NormalExcelConstants.FILE_NAME, "????????????");
        mv.addObject(NormalExcelConstants.CLASS, ItemBomVO.class);
        mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("??????????????????", "?????????:" + sysUser.getRealname(), "????????????"));
        mv.addObject(NormalExcelConstants.DATA_LIST, pageList);
        return mv;
    }

    private void putList(List<ItemBomVO> pageList, List<ItemBom> itemBomList, List<String> idList) {
        QueryWrapper<ItemBomDetail> itemBomDetailQueryWrapper = new QueryWrapper<>();
        itemBomDetailQueryWrapper.in("bom_id", idList);
        List<ItemBomDetail> itemBomDetailList = itemBomDetailService.list(itemBomDetailQueryWrapper);

        //2.2 ????????????
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
