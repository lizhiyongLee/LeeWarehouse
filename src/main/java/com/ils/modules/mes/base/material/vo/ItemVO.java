package com.ils.modules.mes.base.material.vo;

import java.util.List;

import com.ils.framework.poi.excel.annotation.ExcelCollection;
import com.ils.framework.poi.excel.annotation.ExcelEntity;
import com.ils.modules.mes.base.material.entity.Item;
import com.ils.modules.mes.base.material.entity.ItemQualityEmployee;
import com.ils.modules.mes.base.material.entity.ItemStock;
import com.ils.modules.mes.base.material.entity.ItemUnit;

import com.ils.modules.mes.config.vo.DefineFieldValueVO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * @Description: 物料定义
 * @Author: hezhigang
 * @Date: 2020-10-23
 * @Version: V1.0
 */
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
public class ItemVO extends Item {

    private static final long serialVersionUID = 1L;

    @ExcelCollection(name = "物料转换单位", orderNum = "18")
    private List<ItemUnit> itemUnitList;

    @ExcelEntity(id = "", name = "物料关联库存", exportName = "物料关联库存")
    private ItemStock itemStock;

    @ExcelCollection(name = "物料关联质检人员", orderNum = "19")
    private List<ItemQualityEmployee> itemQualityEmployeeList;

    /**
     * 质检人员，多个用；隔开
     */
    private String qualityEmployeeIds;

    @ExcelCollection(name = "物料关联质检方案", orderNum = "20")
    private List<ItemQualityVO> itemQualityList;

    @ExcelCollection(name = "物料关联供应商", orderNum = "21")
    private List<ItemSupplierVO> itemSupplierList;

    private List<DefineFieldValueVO> lstDefineFields;

}
