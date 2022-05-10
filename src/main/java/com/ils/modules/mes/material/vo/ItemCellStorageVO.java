package com.ils.modules.mes.material.vo;

import lombok.*;

import java.util.List;

/**
 * 库存出入库时接收的参数
 *
 * @author Anna.
 * @date 2021/6/4 10:50
 */
@Data
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class ItemCellStorageVO {
    /**
     * 物料集合
     */
    private List<ItemCellOutStorageVO> itemCellList;
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
