package com.ils.modules.mes.base.ware.vo;

import lombok.*;

/**
 * TODO 类描述
 *
 * @author Anna.
 * @date 2020/12/14 9:38
 */
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
public class ItemCellFinishedStorageVO {

    private static final long serialVersionUID = 1L;
    /**
     * 二级仓位id
     */
    private String secondStorageId;
    /**
     * 二级仓位code
     */
    private String secondStorageCode;
    /**
     * 二级仓位名称
     */
    private String secondStorageName;
    /**
     * 二级仓位质量状态
     */
    private String secondQcStatus;
    /**
     * 二级仓位质量状态控制
     */
    private String secondQcControl;
    /**
     * 一级仓位id
     */
    private String firstStorageId;
    /**
     * 一级仓位code
     */
    private String firstStorageCode;
    /**
     * 一级仓位名称
     */
    private String firstStorageName;
    /**
     * 一级仓位质量状态
     */
    private String firstQcStatus;
    /**
     * 一级仓位质量状态控制
     */
    private String firstQcControl;
    /**
     * 仓库id
     */
    private String wareHouseId;
    /**
     * 仓库code
     */
    private String wareHouseCode;
    /**
     * 仓库名称
     */
    private String wareHouseName;
    /**
     * 仓库质量状态
     */
    private String wareHouseQcStatus;
    /**
     * 仓库质量状态控制
     */
    private String wareHouseQcControl;
}
