package com.ils.modules.mes.qc.vo;
import com.ils.framework.poi.excel.annotation.ExcelCollection;
import com.ils.modules.mes.config.vo.DefineFieldValueVO;
import com.ils.modules.mes.qc.entity.QcTask;
import com.ils.modules.mes.qc.entity.QcTaskRelateTotal;
import lombok.*;

import java.util.List;

/**
 * 质检任务添加时用来接收所需的参数
 *
 * @author Anna.
 * @date 2021/3/4 10:28
 */
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class QcTaskSaveVO extends QcTask{
    /**
     * @Fields serialVersionUID
     */
    private static final long serialVersionUID = 1L;
    /**
     * 计划执行人id集合或者角色id
     */
    private String taskEmployeeIds;
    /**
     * 质检任务关联物料整体的集合
     */
    private List<QcTaskRelateTotal> qcTaskRelateTotalList;

    @ExcelCollection(name="自定义字段")
    private List<DefineFieldValueVO> lstDefineFields;

}
