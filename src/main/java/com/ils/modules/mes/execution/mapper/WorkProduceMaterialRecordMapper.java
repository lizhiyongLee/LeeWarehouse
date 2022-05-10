package com.ils.modules.mes.execution.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ils.common.system.base.mapper.ILSMapper;
import com.ils.modules.mes.execution.entity.WorkProduceMaterialRecord;
import com.ils.modules.mes.execution.vo.GroupMaterialRecordVO;
import com.ils.modules.mes.execution.vo.MaterialRecordQtyVO;
import com.ils.modules.mes.execution.vo.MaterialRecordReportVO;
import com.ils.modules.mes.execution.vo.WorkProduceMaterialRecordVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Description: 投料记录
 * @Author: fengyi
 * @Date: 2020-12-10
 * @Version: V1.0
 */
public interface WorkProduceMaterialRecordMapper extends ILSMapper<WorkProduceMaterialRecord> {


    /**
     * 物料清单物料
     *
     * @param page
     * @param queryWrapper
     * @return
     * @date 2020年12月24日
     */
    public IPage<GroupMaterialRecordVO> queryItemMaterialPageList(Page<GroupMaterialRecordVO> page,
                                                                  @Param("ew") QueryWrapper<GroupMaterialRecordVO> queryWrapper);

    /**
     * 查询物料定义
     *
     * @param page
     * @param queryWrapper
     * @return
     * @date 2020年12月24日
     */
    public IPage<GroupMaterialRecordVO> queryItemPage(Page<GroupMaterialRecordVO> page,
                                                      @Param("ew") QueryWrapper<GroupMaterialRecordVO> queryWrapper);

    /**
     * 查询投料物料数量
     *
     * @param produceTaskId
     * @param itemId
     * @return
     * @date 2020年12月16日
     */
    public MaterialRecordQtyVO queryMaterialRecordQty(@Param("produceTaskId") String produceTaskId,
                                                      @Param("itemId") String itemId);

    /**
     * 查询投料物料数量
     *
     * @param produceTaskId
     * @param itemId
     * @param qrcode
     * @return
     * @date 2020年12月24日
     */
    public MaterialRecordQtyVO queryMaterialRecordQtyByQrcode(@Param("produceTaskId") String produceTaskId,
                                                              @Param("itemId") String itemId, @Param("qrcode") String qrcode);

    /**
     * 投料记录BOM物料
     *
     * @param produceTaskId
     * @param page
     * @param queryWrapper
     * @param controlId
     * @return
     */
    public IPage<GroupMaterialRecordVO> queryGroupProductBomMaterialPageList(@Param("produceTaskId") String produceTaskId, Page<GroupMaterialRecordVO> page,
                                                                             @Param("ew") QueryWrapper<GroupMaterialRecordVO> queryWrapper, @Param("controlId") String controlId);


    /**
     * 工艺路线、清单分组投料记录
     *
     * @param produceTaskId
     * @param controlId
     * @param page
     * @return
     */
    public IPage<GroupMaterialRecordVO> queryGroupMaterialRecordPageList(@Param("produceTaskId") String produceTaskId, @Param("controlId") String controlId,
                                                                         Page<GroupMaterialRecordVO> page);

    /**
     * 标签码查询投料记录
     *
     * @param qrcode
     * @return
     */
    public List<WorkProduceMaterialRecordVO> queryWorkProduceMaterialRecordByQrcode(@Param("qrcode") String qrcode);

    /**
     * 标签码查询物料投向记录
     *
     * @param qrcode
     * @return
     */
    public List<WorkProduceMaterialRecordVO> queryWorkProduceMaterialRecordFlowByQrcode(@Param("qrcode") String qrcode);

    /**
     * 获取物料消耗报表记录
     *
     * @param addColumns
     * @param page
     * @param queryWrapper
     * @return
     */
    Page<MaterialRecordReportVO> getMaterialRecord(String addColumns,
                                                    Page<MaterialRecordReportVO> page,
                                                    @Param("ew") QueryWrapper<MaterialRecordReportVO> queryWrapper);
}
