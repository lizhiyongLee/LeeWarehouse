package com.ils.modules.mes.base.ware.vo;

import com.ils.common.aspect.annotation.Dict;
import com.ils.modules.mes.base.factory.vo.NodeVO;
import lombok.*;

/**
 * TODO 类描述
 *
 * @author Anna.
 * @date 2020/12/4 13:13
 */
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
public class WareRelateAreaVO extends NodeVO {

    private static final long serialVersionUID = 1L;
    /**
     * 投料仓位集合
     */
    private String feedingStorageRelateAreaId;
    /**
     *  完工仓位
     */
    @Dict(dictTable = "mes_ware_storage",dicText = "storage_name",dicCode = "id")
    private String finishedStorageRelateAreaId;
    /**
     *  是否有关联的线边仓
     */
    private Boolean hasStorage;
    /**
     * 投料仓位名字字符串
     */
    private String feedingStorageRelateAreaNames;
    /**
     * 完工仓位名字字符串
     */
    private String finishedStorageRelateAreaName;

    /**
     * 车间ID
     */
    private String workShopId;
}
