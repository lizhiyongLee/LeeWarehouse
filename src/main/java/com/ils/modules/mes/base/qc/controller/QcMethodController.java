package com.ils.modules.mes.base.qc.controller;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ils.common.api.vo.Result;
import com.ils.common.aspect.annotation.AutoLog;
import com.ils.common.constant.CommonConstant;
import com.ils.common.system.base.controller.ILSController;
import com.ils.common.system.query.QueryGenerator;
import com.ils.common.system.vo.LoginUser;
import com.ils.common.util.PmsUtil;
import com.ils.framework.poi.excel.ExcelImportUtil;
import com.ils.framework.poi.excel.entity.ExportParams;
import com.ils.framework.poi.excel.entity.ImportParams;
import com.ils.framework.poi.excel.entity.vo.NormalExcelConstants;
import com.ils.framework.poi.excel.view.ILSEntityExcelView;
import com.ils.modules.mes.base.qc.entity.QcMethod;
import com.ils.modules.mes.base.qc.entity.QcMethodDetail;
import com.ils.modules.mes.base.qc.entity.QcMethodItem;
import com.ils.modules.mes.base.qc.service.QcMethodDetailService;
import com.ils.modules.mes.base.qc.service.QcMethodItemService;
import com.ils.modules.mes.base.qc.service.QcMethodService;
import com.ils.modules.mes.base.qc.vo.QcMethodVO;
import com.ils.modules.mes.util.CommonUtil;
import com.ils.modules.system.vo.SysDictPage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.poifs.filesystem.FileMagic;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

/**
 * @Description: 质检方案
 * @Author: fengyi
 * @Date: 2020-10-20
 * @Version: V1.0
 */
@RestController
@RequestMapping("/base/qc/qcMethod")
@Api(tags = "质检方案")
@Slf4j
public class QcMethodController extends ILSController<QcMethod, QcMethodService> {
    @Autowired
    private QcMethodService qcMethodService;
    @Autowired
    private QcMethodDetailService qcMethodDetailService;
    @Autowired
    private QcMethodItemService qcMethodItemService;

    /**
     * 分页列表查询
     *
     * @param qcMethod
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @ApiOperation(value = "质检方案-分页列表查询", notes = "质检方案-分页列表查询")
    @GetMapping(value = "/list")
    public Result<?> queryPageList(QcMethod qcMethod,
                                   @RequestParam(name = PAGE_NO, defaultValue = DEFAULT_PAGE_NO) Integer pageNo,
                                   @RequestParam(name = PAGE_SIZE, defaultValue = DEFAULT_PAGE_SIZE) Integer pageSize,
                                   HttpServletRequest req) {
        QueryWrapper<QcMethod> queryWrapper = QueryGenerator.initQueryWrapper(qcMethod, req.getParameterMap());

        Page<QcMethod> page = new Page<QcMethod>(pageNo, pageSize);
        IPage<QcMethod> pageList = qcMethodService.page(page, queryWrapper);
        return Result.ok(pageList);
    }

    /**
     * 添加
     *
     * @param qcMethodPage
     * @return
     */
    @AutoLog(value = "质检方案-添加",operateType = CommonConstant.OPERATE_TYPE_ADD)
    @ApiOperation(value = "质检方案-添加", notes = "质检方案-添加")
    @PostMapping(value = "/add")
    public Result<?> add(@RequestBody QcMethodVO qcMethodPage) {
        QcMethod qcMethod = new QcMethod();
        BeanUtils.copyProperties(qcMethodPage, qcMethod);
        qcMethod.setTenantId(CommonUtil.getTenantId());
        qcMethodService.saveMain(qcMethod, qcMethodPage.getQcMethodDetailList());
        return Result.ok(qcMethod);
    }

    /**
     * 编辑
     *
     * @param qcMethodPage
     * @return
     */
    @AutoLog("质检方案-编辑")
    @ApiOperation(value = "质检方案-编辑", notes = "质检方案-编辑")
    @PostMapping(value = "/edit")
    public Result<?> edit(@RequestBody QcMethodVO qcMethodPage) {
        QcMethod qcMethod = new QcMethod();
        BeanUtils.copyProperties(qcMethodPage, qcMethod);
        qcMethodService.updateMain(qcMethod, qcMethodPage.getQcMethodDetailList());
        return commonSuccessResult(CommonConstant.OPERATE_TYPE_EDIT);
    }

    /**
     * 通过id删除
     *
     * @param id
     * @return
     */
    @AutoLog(value = "质检方案-通过id删除")
    @ApiOperation(value = "质检方案-通过id删除", notes = "质检方案-通过id删除")
    @GetMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name = "id", required = true) String id) {
        qcMethodService.delMain(id);
        return commonSuccessResult(CommonConstant.OPERATE_TYPE_DELETE);
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @AutoLog(value = "质检方案-批量删除")
    @ApiOperation(value = "质检方案-批量删除", notes = "质检方案-批量删除")
    @GetMapping(value = "/deleteBatch")
    public Result<?> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        this.qcMethodService.delBatchMain(Arrays.asList(ids.split(",")));
        return commonSuccessResult(CommonConstant.OPERATE_TYPE_DELETE);
    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    @ApiOperation(value = "质检方案-通过id查询", notes = "质检方案-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<?> queryById(@RequestParam(name = "id", required = true) String id) {
        QcMethodVO qcMethodVO = new QcMethodVO();
        QcMethod qcMethod = qcMethodService.getById(id);
        BeanUtils.copyProperties(qcMethod, qcMethodVO);
        List<QcMethodDetail> qcMethodDetailList = qcMethodDetailService.selectByMainId(id);
        qcMethodVO.setQcMethodDetailList(qcMethodDetailList);
        return Result.ok(qcMethodVO);
    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    @ApiOperation(value = "质检方案-通过质检方案id查询质检方案关联质检项", notes = "质检方案-通过质检方案id查询质检方案关联质检项")
    @GetMapping(value = "/queryQcMethodDetailByMainId")
    public Result<?> queryQcMethodDetailListByMainId(@RequestParam(name = "id", required = true) String id) {
        List<QcMethodDetail> qcMethodDetailList = qcMethodDetailService.selectByMainId(id);
        return Result.ok(qcMethodDetailList);
    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    @ApiOperation(value = "质检方案-通过质检方案id查询质检方案关联物料", notes = "质检方案-通过质检方案id查询质检方案关联物料")
    @GetMapping(value = "/queryQcMethodItemByMainId")
    public Result<?> queryQcMethodItemListByMainId(@RequestParam(name = "id", required = true) String id) {
        List<QcMethodItem> qcMethodItemList = qcMethodItemService.selectByMainId(id);
        return Result.ok(qcMethodItemList);
    }

    /**
     * 查询质检方案
     *
     * @param qcType
     * @param id
     * @return
     */
    @ApiOperation(value = "质检方案-通过物料id或生产任务id和质检类型查询质检方案", notes = "质检方案-通过物料id或生产任务id和质检类型查询质检方案")
    @GetMapping(value = "/queryQcMethodByItemIdAndQcType")
    public Result<?> queryQcMethodByItemIdAndQcType(@RequestParam(name = "qcType", required = true) String qcType, @RequestParam(name = "id", required = true) String id) {
        List<QcMethod> qcMethods = qcMethodService.queryQcMeThodByItemIdAndQcType(qcType, id);
        return Result.ok(qcMethods);
    }

    /**
     * 导出excel
     *
     * @param request
     * @param qcMethod
     */
    @GetMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, QcMethod qcMethod) {
        // Step.1 组装查询条件
        QueryWrapper<QcMethod> queryWrapper = QueryGenerator.initQueryWrapper(qcMethod, request.getParameterMap());
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();

        //Step.2 获取导出数据
        List<QcMethodVO> pageList = new ArrayList<QcMethodVO>();
        List<QcMethod> qcMethodList = qcMethodService.list(queryWrapper);
        for (QcMethod temp : qcMethodList) {

            List<QcMethodDetail> qcMethodDetailList = qcMethodDetailService.selectByMainId(temp.getId());
            qcMethodDetailList.forEach(qcMethodDetail -> {
                QcMethodVO vo = new QcMethodVO();
                BeanUtils.copyProperties(temp, vo);
                vo.setQcMethodDetailList(Collections.singletonList(qcMethodDetail));
                pageList.add(vo);
            });

        }
        //Step.3 调用AutoPoi导出Excel
        ModelAndView mv = new ModelAndView(new ILSEntityExcelView());
        mv.addObject(NormalExcelConstants.FILE_NAME, "质检方案");
        mv.addObject(NormalExcelConstants.CLASS, QcMethodVO.class);
        mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("质检方案数据", "导出人:" + sysUser.getRealname(), "质检方案"));
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
            // 获取上传文件对象
            ImportParams params = new ImportParams();
            params.setTitleRows(2);
            params.setHeadRows(2);
            params.setNeedSave(true);
            params.setKeyIndex(9);
            try {
                InputStream inputStream = file.getInputStream();
                List<QcMethodVO> list = ExcelImportUtil.importExcel(FileMagic.prepareToCheckMagic(inputStream), QcMethodVO.class, params);
                // 错误信息
                List<String> errorMessage = new ArrayList<>();
                int successLines = 0, errorLines = 0;

                for (int i=0;i< list.size();i++) {
                    QcMethod po = new QcMethod();
                    BeanUtils.copyProperties(list.get(i), po);
                    try {
                        Integer integer = qcMethodService.saveMain(po, list.get(i).getQcMethodDetailList());
                        if(integer>0){
                            successLines++;
                        }else{
                            errorLines++;
                            int lineNumber = i + 1;
                            errorMessage.add("第 " + lineNumber + "行：数据不对，导入失败。");
                        }
                    }  catch (Exception e) {
                        errorLines++;
                        int lineNumber = i + 1;
                        errorMessage.add("第 " + lineNumber + " 行：数据异常，导入失败。");
                    }
                }
                return imporReturnRes(errorLines,successLines,errorMessage);
            } catch (Exception e) {
                log.error(e.getMessage(),e);
                return Result.error("文件导入失败:"+e.getMessage());
            } finally {
                try {
                    file.getInputStream().close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return Result.error("文件导入失败！");
    }

    public Result<?> imporReturnRes(int errorLines,int successLines,List<String> errorMessage) throws IOException {
        if (errorLines == 0) {
            return Result.ok("共" + successLines + "行数据全部导入成功！");
        } else {
            JSONObject result = new JSONObject(5);
            int totalCount = successLines + errorLines;
            result.put("totalCount", totalCount);
            result.put("errorCount", errorLines);
            result.put("successCount", successLines);
            result.put("msg", "总上传行数：" + totalCount + "，已导入行数：" + successLines + "，错误行数：" + errorLines);
            String fileUrl = PmsUtil.saveErrorTxtByList(errorMessage, "userImportExcelErrorLog");
            int lastIndex = fileUrl.lastIndexOf(File.separator);
            String fileName = fileUrl.substring(lastIndex + 1);
            result.put("fileUrl", "/sys/common/static/" + fileUrl);
            result.put("fileName", fileName);
            Result res = Result.ok(result);
            res.setCode(201);
            res.setMessage("文件导入成功，但有错误。");
            return res;
        }
    }
}
