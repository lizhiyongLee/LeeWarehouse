package com.ils.modules.mes.base.ware.vo;

import com.ils.framework.poi.excel.annotation.ExcelCollection;
import com.ils.modules.mes.base.ware.entity.WareStorage;
import com.ils.modules.mes.config.vo.DefineFieldValueVO;
import lombok.*;

import java.util.List;

/**
 * TODO 类描述
 *
 * @author Anna.
 * @date 2020/11/12 9:01
 */
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
public class WareStorageVO extends WareStorage {
    private static final long serialVersionUID = 1L;
    @ExcelCollection(name="子节点个数")
    private Integer hasChild;

    @ExcelCollection(name="自定义字段")
    private List<DefineFieldValueVO> lstDefineFields;
}
