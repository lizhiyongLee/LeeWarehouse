package com.ils.modules.mes.base.material.vo;

import com.ils.modules.mes.base.material.entity.ItemContainer;
import com.ils.modules.mes.base.material.entity.ItemContainerQty;
import lombok.Data;

import java.util.List;

/**
 * @author lishaojie
 * @description
 * @date 2021/10/25 9:51
 */
@Data
public class ItemContainerVO extends ItemContainer {
    private List<ItemContainerQtyVO> itemContainerQtyList;

}
