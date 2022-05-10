package com.ils.modules.mes.qc.vo;

import com.ils.common.aspect.annotation.Dict;
import com.ils.framework.poi.excel.annotation.ExcelCollection;
import com.ils.modules.mes.base.qc.entity.QcMethod;
import com.ils.modules.mes.config.vo.DefineFieldValueVO;
import com.ils.modules.mes.qc.entity.QcTask;
import com.ils.modules.mes.qc.entity.QcTaskItemStandard;
import com.ils.modules.mes.qc.entity.QcTaskReport;
import com.ils.modules.mes.qc.entity.QcTaskReportItemValue;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;

/**
 * 质检任务详情
 *
 * @author Anna.
 * @date 2021/3/8 10:32
 */
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
public class QcTaskDetailVO extends QcTask {

    private static final long serialVersionUID = 1L;
    /**
     * 质检方案
     */
    private QcMethod qcMethod;
    /**
     * 质检任务报告
     */
    private QcTaskReport qcTaskReport;
    /**
     * 样本合格数
     */
    private Integer sampleGoodQty;
    /**
     * 计划抽样数
     */
    private BigDecimal planQty;
    /**
     * 质检任务标准，质检项
     */
    private List<QcTaskItemStandard> qcTaskItemStandardList;
    /**
     * 质检任务样本总数集合
     */
    private List<QcTaskRelateTotalVO> qcTaskRelateTotalVOList;
    /**
     * 质检报告记录值
     */
    private List<List<QcTaskReportItemValue>> taskValueLine;
    /**
     * 单位名称
     */
    @Dict(dicCode = "id",dictTable = "mes_unit",dicText = "unit_name")
    private String itemMainUnit;
    /**
     * 管理方式
     */
    private String manageWay;
    /**
     * 质检任务人员
     */
    private String taskEmployees;
    /**
     * 合格数
     */
    private Integer goodQty;
    /**
     * 不合格数
     */
    private Integer badQty;

    private List<DefineFieldValueVO> lstDefineFields;

}
