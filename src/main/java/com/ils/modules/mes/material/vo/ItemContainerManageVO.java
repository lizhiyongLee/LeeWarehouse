package com.ils.modules.mes.material.vo;

import com.ils.modules.mes.base.material.entity.ItemContainerQty;
import com.ils.modules.mes.material.entity.ItemContainerLoadItemRecord;
import com.ils.modules.mes.material.entity.ItemContainerManage;
import com.ils.modules.mes.material.entity.ItemContainerManageDetail;
import com.ils.modules.mes.material.entity.ItemContainerTransferRecord;
import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * @author lishaojie
 * @description
 * @date 2021/10/26 13:55
 */
@Data
public class ItemContainerManageVO extends ItemContainerManage {
    private List<List<ItemContainerManageDetail>>  itemDetailList;
    private List<ItemContainerQty>  itemContainerQtyList;
    private List<ItemContainerTransferRecord> itemContainerTransferRecordList;
    private List<ItemContainerLoadItemRecord> itemContainerLoadItemRecordList;
}
