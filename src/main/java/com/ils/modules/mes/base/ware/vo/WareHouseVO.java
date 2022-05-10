package com.ils.modules.mes.base.ware.vo;

import com.ils.framework.poi.excel.annotation.ExcelCollection;
import com.ils.modules.mes.base.ware.entity.WareHouse;
import com.ils.modules.mes.config.vo.DefineFieldValueVO;
import lombok.*;

import java.util.List;

/**
 * 用来构造和返回仓位和仓库树形结构的集合类
 *
 * @author Anna.
 * @date 2020/11/9 10:23
 */
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
public class WareHouseVO extends WareHouse {
    private static final long serialVersionUID = 1L;

    @ExcelCollection(name="子节点个数")
    private Integer hasChild;

    @ExcelCollection(name="自定义字段")
    private List<DefineFieldValueVO> lstDefineFields;
}
