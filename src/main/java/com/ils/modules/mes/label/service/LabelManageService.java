package com.ils.modules.mes.label.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ils.modules.mes.label.entity.LabelManage;
import com.ils.modules.mes.label.vo.LabelManageVO;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author lishaojie
 * @description
 * @date 2021/7/13 10:54
 */
public interface LabelManageService extends IService<LabelManage> {
    /**
     * 新增
     * @param labelManage
     */
    void saveMain(LabelManage labelManage);

    /**
     * 查找子表
     * @param id
     * @return
     */
    LabelManageVO getDetailById(String id);

    /**
     * 更新数据，当日期更新时，更新子表日期
     * @param labelManage
     */
    void updateMain(LabelManage labelManage);

    /**
     * 查找VO
     * @param labelManage
     * @param pageNo
     * @param pageSize
     * @param startTime
     * @param endTime
     * @param req
     * @return
     */
    Page<LabelManageVO> queryPageList(LabelManage labelManage, Integer pageNo, Integer pageSize, String startTime, String endTime, HttpServletRequest req);

    /**
     * 批量完结（修改行状态）
     *
     * @param ids
     */
    void completionBatch(List<String> ids);

}
