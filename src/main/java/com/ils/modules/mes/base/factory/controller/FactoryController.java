package com.ils.modules.mes.base.factory.controller;

import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.BeanUtils;
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
import com.ils.modules.mes.base.factory.entity.Factory;
import com.ils.modules.mes.base.factory.service.FactoryService;
import com.ils.modules.mes.base.factory.vo.FactoryVO;
import com.ils.modules.mes.config.service.DefineFieldValueService;
import com.ils.modules.mes.config.vo.DefineFieldValueVO;
import com.ils.modules.mes.constants.TableCodeConstants;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

 /**
 * @Description: 工厂
 * @Author: fengyi
 * @Date: 2020-10-13
 * @Version: V1.0
 */
@Slf4j
@Api(tags="工厂")
@RestController
@RequestMapping("/base/factory/factory")
public class FactoryController extends ILSController<Factory, FactoryService> {
	@Autowired
	private FactoryService factoryService;
	
    @Autowired
    private DefineFieldValueService defineFieldValueService;

	/**
	 * 分页列表查询
	 *
	 * @param factory
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@ApiOperation(value="工厂-分页列表查询", notes="工厂-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(Factory factory,
								   @RequestParam(name = PAGE_NO, defaultValue = DEFAULT_PAGE_NO) Integer pageNo,
                                    @RequestParam(name = PAGE_SIZE, defaultValue = DEFAULT_PAGE_SIZE) Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<Factory> queryWrapper = QueryGenerator.initQueryWrapper(factory, req.getParameterMap());

		Page<Factory> page = new Page<Factory>(pageNo, pageSize);
		IPage<Factory> pageList = factoryService.page(page, queryWrapper);
		return Result.ok(pageList);
	}
	
	/**
	 * 添加
	 *
	 * @param factory
	 * @return
	 */
	@ApiOperation(value="工厂-添加", notes="工厂-添加")
	@PostMapping(value = "/add")
	@AutoLog("工厂-添加")
    public Result<?> add(@RequestBody FactoryVO factoryVO) {
        factoryService.saveFactory(factoryVO);
		return commonSuccessResult(CommonConstant.OPERATE_TYPE_ADD);
	}
	
	/**
	 * 编辑
	 *
	 * @param factory
	 * @return
	 */
	@ApiOperation(value="工厂-编辑", notes="工厂-编辑")
	@PostMapping(value = "/edit")
	@AutoLog("工厂-编辑")
    public Result<?> edit(@RequestBody FactoryVO factoryVO) {
        factoryService.updateFactory(factoryVO);
		return commonSuccessResult(CommonConstant.OPERATE_TYPE_EDIT);
	}
	
	/**
	 * 通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "工厂-通过id删除")
	@ApiOperation(value="工厂-通过id删除", notes="工厂-通过id删除")
	@GetMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		factoryService.delFactory(id);
		return commonSuccessResult(CommonConstant.OPERATE_TYPE_DELETE);
	}
	
	/**
	 * 批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "工厂-批量删除")
	@ApiOperation(value="工厂-批量删除", notes="工厂-批量删除")
	@GetMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		List<String> idList = Arrays.asList(ids.split(","));
		this.factoryService.delBatchFactory(idList);
		return commonSuccessResult(CommonConstant.OPERATE_TYPE_DELETE);
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@ApiOperation(value="工厂-通过id查询", notes="工厂-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
        FactoryVO factoryVO = new FactoryVO();
		Factory factory = factoryService.getById(id);
        BeanUtils.copyProperties(factory, factoryVO);
        List<DefineFieldValueVO> lstDefineFields =
            defineFieldValueService.queryDefineFieldValue(TableCodeConstants.FACTORY_TABLE_CODE, id);
        factoryVO.setLstDefineFields(lstDefineFields);
        return Result.ok(factoryVO);
	}

  /**
   * 导出excel
   *
   * @param request
   * @param factory
   */
  @GetMapping(value = "/exportXls")
  public ModelAndView exportXls(HttpServletRequest request, Factory factory) {
      return super.exportXls(request, factory, Factory.class, "工厂");
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
      return super.importExcel(request, response, Factory.class);
  }
}
