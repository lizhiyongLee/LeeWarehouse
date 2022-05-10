package com.ils.modules.mes.execution.service;

import java.util.List;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ils.modules.mes.execution.entity.WorkProduceMaterialRecord;
import com.ils.modules.mes.execution.vo.GroupMaterialRecordVO;
import com.ils.modules.mes.execution.vo.ItemCellExtendInfo;
import com.ils.modules.mes.execution.vo.MaterialRecordReportVO;

/**
 * @Description: 投料记录
 * @Author: fengyi
 * @Date: 2020-12-10
 * @Version: V1.0
 */
public interface WorkProduceMaterialRecordService extends IService<WorkProduceMaterialRecord> {

    /**
     * 添加
     *
     * @param workProduceMaterialRecord
     */
    public void saveWorkProduceMaterialRecord(WorkProduceMaterialRecord workProduceMaterialRecord);

    /**
     * 修改
     *
     * @param workProduceMaterialRecord
     */
    public void updateWorkProduceMaterialRecord(WorkProduceMaterialRecord workProduceMaterialRecord);

    /**
     * 删除
     *
     * @param id
     */
    public void delWorkProduceMaterialRecord(String id);

    /**
     * 批量删除
     *
     * @param idList
     */
    public void delBatchWorkProduceMaterialRecord(List<String> idList);


    /**
     * 查询待投入物料-分页
     *
     * @param produceTaskId
     * @param keyWord
     * @param page
     * @param item
     * @param mainItem
     * @return
     * @date 2020年12月15日
     */
    public IPage<GroupMaterialRecordVO> queryMaterialPageList(String produceTaskId, String keyWord,
                                                              Page<GroupMaterialRecordVO> page, String item, String mainItem);

    /**
     * 标签码查询物料
     *
     * @param produceTaskId
     * @param qrcode
     * @param stationId
     * @param itemId
     * @return
     * @date 2020年12月24日
     */
    public ItemCellExtendInfo queryItemCell(String produceTaskId, String qrcode, String stationId, String itemId);

    /**
     * 有码投料-添加
     *
     * @param workProduceMaterialRecord
     */
    public void qrCodeAdd(WorkProduceMaterialRecord workProduceMaterialRecord);

    /**
     * 有码撤料-撤料
     *
     * @param workProduceMaterialRecord
     */
    public void qrCodeUndo(WorkProduceMaterialRecord workProduceMaterialRecord);

    /**
     * 无码投料-添加
     *
     * @param lstWorkProduceMaterialRecord
     */
    public void noCodeAdd(List<WorkProduceMaterialRecord> lstWorkProduceMaterialRecord);

    /**
     * 查询待投入物料-分页
     *
     * @param produceTaskId
     * @param controlId
     * @param page
     * @return
     * @date 2020年12月15日
     */
    public IPage<GroupMaterialRecordVO> queryGroupRecordPageList(String produceTaskId, String controlId,
                                                                 Page<GroupMaterialRecordVO> page);

    /**
     * 消耗报表
     *
     * @param page
     * @param materialRecordReportVO
     * @return
     */
    Page<MaterialRecordReportVO> getMaterialRecord(Page<MaterialRecordReportVO> page, MaterialRecordReportVO materialRecordReportVO);
}
