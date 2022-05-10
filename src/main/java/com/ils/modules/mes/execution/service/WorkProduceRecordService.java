package com.ils.modules.mes.execution.service;

import java.util.List;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ils.modules.mes.execution.entity.WorkProduceRecord;
import com.ils.modules.mes.execution.vo.QrItemCellInfo;
import com.ils.modules.mes.execution.vo.WorkProduceRecordInfoVO;
import com.ils.modules.mes.execution.vo.WorkProduceRecordReportVO;
import com.ils.modules.mes.material.entity.ItemCell;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

/**
 * @Description: 产出记录
 * @Author: fengyi
 */
public interface WorkProduceRecordService extends IService<WorkProduceRecord> {

    /**
     * 无码、批次码合格添加
     *
     * @param workProduceRecord
     */
    public void qualifiedAdd(WorkProduceRecord workProduceRecord);


    /**
     * 无码、批次码不合格添加
     *
     * @param workProduceRecord
     */
    public void unQualifiedAdd(WorkProduceRecord workProduceRecord);

    /**
     * 无码、批次码产出物料前校验
     *
     * @param produceTaskId
     */
    public void checkPreProduceRecord(String produceTaskId);

    /**
     * 修改
     *
     * @param workProduceRecord
     */
    public void updateWorkProduceRecord(WorkProduceRecord workProduceRecord);

    /**
     * 删除
     *
     * @param id
     */
    public void delWorkProduceRecord(String id);

    /**
     * 批量删除
     *
     * @param idList
     */
    public void delBatchWorkProduceRecord(List<String> idList);

    /**
     * 标签码合格添加
     *
     * @param workProduceRecordInfoVO
     */
    public void qualifiedQrcodeAdd(WorkProduceRecordInfoVO workProduceRecordInfoVO);

    /**
     * 标签码不合格添加
     *
     * @param workProduceRecordInfoVO
     */
    public void unQualifiedQrcodeAdd(WorkProduceRecordInfoVO workProduceRecordInfoVO);

    /**
     * 标签码投产时查询符合条件物料单元
     *
     * @param produceTaskId
     * @param qrcode
     * @param qcStatus
     * @return
     */
    public QrItemCellInfo queryQrcodeItemCell(String produceTaskId, String qrcode, String qcStatus);


    /**
     * 联产出无码、批次码合格添加
     *
     * @param workProduceRecord
     */
    void jointProductNoCodeAdd(WorkProduceRecord workProduceRecord);

    /**
     * 联产出标签码合格添加
     *
     * @param workProduceRecord
     */
    void jointProductQrcodeAdd(WorkProduceRecord workProduceRecord);

    /**
     * 二维码检查
     *
     * @param qrcode
     * @param produceTaskId
     * @return
     */
    ItemCell checkJointProductQrcode(String qrcode, String produceTaskId);

    /**
     * 报表查询
     *
     * @param workProduceRecord
     * @param page
     * @param req
     * @return
     */
    Page<WorkProduceRecordReportVO> getReportData(WorkProduceRecord workProduceRecord, Page<WorkProduceRecord> page, HttpServletRequest req);

    /**
     * 导出报表
     *
     * @param workProduceRecord
     * @param req
     * @return
     */
    ModelAndView exportReportXls(WorkProduceRecord workProduceRecord, HttpServletRequest req);
}
