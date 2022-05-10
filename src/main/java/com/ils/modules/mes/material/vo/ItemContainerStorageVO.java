package com.ils.modules.mes.material.vo;

import lombok.Data;

import java.util.List;

/**
 * @author lishaojie
 * @description
 * @date 2021/10/27 15:03
 */
@Data
public class ItemContainerStorageVO {

    /**
     * 容器管理id
     */
    private List<String> itemContainerManageList;
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
