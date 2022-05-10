package com.ils.modules.mes.base.ware.vo;

import lombok.*;

import java.util.List;

/**
 * 线边仓关联仓库
 *
 * @author Anna.
 * @date 2021/8/5 14:36
 */
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
public class RelatedStorageVO {
    /**
     * 租户
     */
    private String tenantId;
    /**
     * id
     */
    private String id;
    /**
     * 车间id
     */
    private String workshopId;
    /**
     * 类型
     */
    private String type;
    /**
     * 状态
     */
    private String status;
    /**
     * 编码
     */
    private String code;
    /**
     * 父节点id
     */
    private String upArea;
    /**
     * 名字
     */
    private String name;
    /**
     * 是否有线边仓
     */
    private Boolean hasWarehouse;
    /**
     * 完工仓集合
     */
    private List<RelatedVO> finishedStorageList;
    /**
     * 投料仓集合
     */
    private List<RelatedVO> feedingStorageList;
    /**
     * 是否有子节点
     */
    private Boolean child;

}
