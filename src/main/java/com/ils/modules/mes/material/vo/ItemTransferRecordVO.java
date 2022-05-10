package com.ils.modules.mes.material.vo;

import com.ils.modules.mes.material.entity.ItemTransferRecord;
import lombok.*;

import java.util.List;

/**
 * 库存入库vo类
 *
 * @author Anna.
 * @date 2021/6/7 9:28
 */
@Data
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class ItemTransferRecordVO {

    /**
     * 物料记录集合
     */
    private List<ItemTransferRecord> itemTransferRecordList;

    /**
     * 事务id
     */
    private String eventId;
    /**
     * 事务名称
     */
    private String eventName;
    /**
     * 事务对象
     */
    private String eventObject;
    /**
     * 期望入库名称
     */
    private String hopeInStorageName;
    /**
     * 单据号
     */
    private String billCode;
    /**
     * 期望入库编码
     */
    private String hopeInStorageCode;

    /**
     * sop控件id
     */
    private String controlId;
}
