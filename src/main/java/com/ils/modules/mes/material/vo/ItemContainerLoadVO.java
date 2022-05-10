package com.ils.modules.mes.material.vo;

import com.ils.modules.mes.material.entity.ItemCell;
import com.ils.modules.mes.material.entity.ItemContainerManage;
import com.ils.modules.mes.material.entity.ItemContainerManageDetail;
import lombok.Data;

import java.util.List;

/**
 * @author lishaojie
 * @description
 * @date 2021/10/25 13:28
 */
@Data
public class ItemContainerLoadVO extends ItemContainerManage {
    private List<ItemContainerManageDetail> itemCellList;
}
