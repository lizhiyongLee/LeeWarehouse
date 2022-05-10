package com.ils.modules.mes.execution.vo;

import java.io.Serializable;

import com.ils.modules.mes.material.entity.ItemCell;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @Description: 标签码报工查询物料单元VO
 * @author: fengyi
 * @date: 2021年1月4日 上午9:03:27
 */
@Setter
@Getter
@ToString(callSuper = true)
public class QrItemCellInfo implements Serializable {
    /** 物料单元信息 */
    private ItemCell itemCell;
    /** 是否存在物料单元1:存在，0 不存在 */
    private String existItemCell;
    /** 是否编辑页面 1: 编辑 ，0 不编辑 */
    private String editPage;
}
