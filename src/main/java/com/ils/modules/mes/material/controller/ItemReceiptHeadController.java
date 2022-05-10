package com.ils.modules.mes.material.controller;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ils.common.exception.ILSBootException;
import com.ils.modules.mes.util.CommonUtil;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
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
import com.ils.common.system.vo.LoginUser;
import com.ils.framework.poi.excel.ExcelImportUtil;
import com.ils.framework.poi.excel.entity.ExportParams;
import com.ils.framework.poi.excel.entity.ImportParams;
import com.ils.framework.poi.excel.entity.vo.NormalExcelConstants;
import com.ils.framework.poi.excel.view.ILSEntityExcelView;
import com.ils.modules.mes.material.entity.ItemReceiptHead;
import com.ils.modules.mes.material.entity.ItemReceiptLine;
import com.ils.modules.mes.material.service.ItemReceiptHeadService;
import com.ils.modules.mes.material.service.ItemReceiptLineService;
import com.ils.modules.mes.material.vo.ItemReceiptHeadVO;
import com.ils.modules.system.service.CodeGeneratorService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
 /**
 * @Description: 收货单头
 * @Author: wyssss
 * @Date:   2020-11-18
 * @Version: V1.0
 */
@RestController
@RequestMapping("/material/itemReceiptHead")
@Api(tags="收货单头")
@Slf4j
public class ItemReceiptHeadController extends ILSController<ItemReceiptHead, ItemReceiptHeadService> {
	@Autowired
	private ItemReceiptHeadService itemReceiptHeadService;
	@Autowired
	private ItemReceiptLineService itemReceiptLineService;
	@Autowired
    private CodeGeneratorService codeGeneratorService;
	
	/**
	 * 分页列表查询
	 *
	 * @param itemReceiptHead
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
    @ApiOperation(value="收货单头-分页列表查询", notes="收货单头-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(ItemReceiptHead itemReceiptHead,
								   @RequestParam(name = PAGE_NO, defaultValue = DEFAULT_PAGE_NO) Integer pageNo,
            @RequestParam(name = PAGE_SIZE, defaultValue = DEFAULT_PAGE_SIZE) Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<ItemReceiptHead> queryWrapper = QueryGenerator.initQueryWrapper(itemReceiptHead, req.getParameterMap());
		Page<ItemReceiptHead> page = new Page<ItemReceiptHead>(pageNo, pageSize);
		IPage<ItemReceiptHead> pageList = itemReceiptHeadService.page(page, queryWrapper);
		return Result.ok(pageList);
	}
	
	/**
	 * 添加
	 *
	 * @param itemReceiptHeadPage
	 * @return
	 */
	@AutoLog("收货单头-添加")
    @ApiOperation(value="收货单头-添加", notes="收货单头-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody ItemReceiptHeadVO itemReceiptHeadPage) {
		ItemReceiptHead itemReceiptHead = new ItemReceiptHead();
		BeanUtils.copyProperties(itemReceiptHeadPage, itemReceiptHead);
		itemReceiptHead.setReceiptCode(codeGeneratorService.getNextCode(CommonUtil.getTenantId(), "mesReceiptCode", itemReceiptHead));
		itemReceiptHeadService.saveMain(itemReceiptHead, itemReceiptHeadPage.getItemReceiptLineList());
		return commonSuccessResult(CommonConstant.OPERATE_TYPE_ADD);
	}
	
	/**
	 * 编辑
	 *
	 * @param itemReceiptHeadPage
	 * @return
	 */
	@AutoLog("收货单头-编辑")
    @ApiOperation(value="收货单头-编辑", notes="收货单头-编辑")
	@PostMapping(value = "/edit")
	public Result<?> edit(@RequestBody ItemReceiptHeadVO itemReceiptHeadPage) {
		ItemReceiptHead itemReceiptHead = new ItemReceiptHead();
		BeanUtils.copyProperties(itemReceiptHeadPage, itemReceiptHead);
		itemReceiptHeadService.updateMain(itemReceiptHead, itemReceiptHeadPage.getItemReceiptLineList());
		return commonSuccessResult(CommonConstant.OPERATE_TYPE_EDIT);
	}
	
	/**
	 * 通过id删除
	 *
	 * @param id
	 * @return
	 */
	 @AutoLog(value = "收货单头-通过id删除")
    @ApiOperation(value="收货单头-通过id删除", notes="收货单头-通过id删除")
	@GetMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
	    itemReceiptHeadService.delMain(id);
		return commonSuccessResult(CommonConstant.OPERATE_TYPE_DELETE);
	}
	
	/**
	 * 批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "收货单头-批量删除")
    @ApiOperation(value="收货单头-批量删除", notes="收货单头-批量删除")
	@GetMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.itemReceiptHeadService.delBatchMain(Arrays.asList(ids.split(",")));
		return commonSuccessResult(CommonConstant.OPERATE_TYPE_DELETE);
	}
	/**
	 * 批量改状态
	 * @param ids
	 * @param status
	 * @return
	 */
	@AutoLog(value = "收货单头-批量改状态")
    @ApiOperation(value="收货单头-批量改状态", notes="收货单头-批量改状态")
    @GetMapping(value = "/updateStatusBatch")
    public Result<?> updateStatusBatch(@RequestParam(name="ids",required=true) String ids,@RequestParam(name="status",required=true) String status) {
        this.itemReceiptHeadService.updateStatus(status, Arrays.asList(ids.split(",")));
        return commonSuccessResult(CommonConstant.OPERATE_TYPE_DELETE);
    }
	
	/**
	 * 通过id查询
     *
	 * @param id
	 * @return
	 */
    @ApiOperation(value="收货单头-通过id查询", notes="收货单头-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		ItemReceiptHead itemReceiptHead = itemReceiptHeadService.getById(id);
		ItemReceiptHeadVO itemReceiptHeadVO = new ItemReceiptHeadVO();
        BeanUtils.copyProperties(itemReceiptHead, itemReceiptHeadVO);
		List<ItemReceiptLine> itemReceiptLineList = itemReceiptLineService.selectByMainId(id);	
		for(ItemReceiptLine line : itemReceiptLineList) {
		    BigDecimal planQty = line.getPlanQty();
		    if(planQty == null || planQty.compareTo(BigDecimal.ZERO) == 0) {
		        continue;
		    }
		    BigDecimal completeQty = line.getCompleteQty() == null ? BigDecimal.ZERO : line.getCompleteQty();
		    BigDecimal refundQty = line.getRefundQty() == null ? BigDecimal.ZERO : line.getRefundQty();
		    BigDecimal completeRatio = completeQty.subtract(refundQty).divide(planQty,4,BigDecimal.ROUND_HALF_UP);
		    line.setCompleteRatio(completeRatio);
		}
		itemReceiptHeadVO.setItemReceiptLineList(itemReceiptLineList);
		return Result.ok(itemReceiptHeadVO);
	}
	
	/**
	 * 通过id查询
     *
	 * @param id
	 * @return
	 */
    @ApiOperation(value="收货单头-通过收货单头id查询收货单行", notes="收货单头-通过收货单头id查询收货单行")
	@GetMapping(value = "/queryItemReceiptLineByMainId")
	public Result<?> queryItemReceiptLineListByMainId(@RequestParam(name="id",required=true) String id) {
		List<ItemReceiptLine> itemReceiptLineList = itemReceiptLineService.selectByMainId(id);
		return Result.ok(itemReceiptLineList);
	}

  /**
   * 导出excel
   *
   * @param request
   * @param itemReceiptHead
   */
  @GetMapping(value = "/exportXls")
  public ModelAndView exportXls(HttpServletRequest request, ItemReceiptHead itemReceiptHead) {
      // Step.1 组装查询条件
      QueryWrapper<ItemReceiptHead> queryWrapper = QueryGenerator.initQueryWrapper(itemReceiptHead, request.getParameterMap());
      LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();

      //Step.2 获取导出数据
      List<ItemReceiptHeadVO> pageList = new ArrayList<ItemReceiptHeadVO>();
      List<ItemReceiptHead> itemReceiptHeadList = itemReceiptHeadService.list(queryWrapper);
      for (ItemReceiptHead temp : itemReceiptHeadList) {
          ItemReceiptHeadVO vo = new ItemReceiptHeadVO();
          BeanUtils.copyProperties(temp, vo);
          List<ItemReceiptLine> itemReceiptLineList = itemReceiptLineService.selectByMainId(temp.getId());
          vo.setItemReceiptLineList(itemReceiptLineList);
          pageList.add(vo);
      }
      //Step.3 调用AutoPoi导出Excel
      ModelAndView mv = new ModelAndView(new ILSEntityExcelView());
      mv.addObject(NormalExcelConstants.FILE_NAME, "收货单头");
      mv.addObject(NormalExcelConstants.CLASS, ItemReceiptHeadVO.class);
      mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("收货单头数据", "导出人:"+sysUser.getRealname(), "收货单头"));
      mv.addObject(NormalExcelConstants.DATA_LIST, pageList);
      return mv;
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
      MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
      Map<String, MultipartFile> fileMap = multipartRequest.getFileMap();
      for (Map.Entry<String, MultipartFile> entity : fileMap.entrySet()) {
          MultipartFile file = entity.getValue();
          ImportParams params = new ImportParams();
          params.setTitleRows(2);
          params.setHeadRows(1);
          params.setNeedSave(true);
          try {
              List<ItemReceiptHeadVO> list = ExcelImportUtil.importExcel(file.getInputStream(), ItemReceiptHeadVO.class, params);
			  itemReceiptHeadService.saveBatchMain(list);
              return Result.ok("文件导入成功！数据行数:" + list.size());
          } catch (Exception e) {
              log.error(e.getMessage(),e);
              return Result.error("文件导入失败:"+e.getMessage());
          } finally {
              try {
                  file.getInputStream().close();
              } catch (IOException e) {
                  e.printStackTrace();
              }
          }
      }
      return Result.error("文件导入失败！");
  }

}
