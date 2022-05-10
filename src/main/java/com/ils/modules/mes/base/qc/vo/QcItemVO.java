package com.ils.modules.mes.base.qc.vo;

import com.ils.framework.poi.excel.annotation.Excel;
import com.ils.modules.mes.base.qc.entity.QcItem;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * @Description: 描述这个类的作用
 * @author: fengyi
 * @date: 2020年10月20日 下午1:26:24
 */
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
public class QcItemVO extends QcItem {
    /** 不良类型名称 */
    @Excel(name = "不良类型名称", width = 15)
    private String qcTypeName;
}
