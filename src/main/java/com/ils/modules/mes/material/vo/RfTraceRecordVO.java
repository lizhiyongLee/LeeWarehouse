package com.ils.modules.mes.material.vo;

import com.ils.common.aspect.annotation.Dict;
import com.ils.modules.mes.material.entity.ItemCell;
import lombok.*;

import java.util.Date;
import java.util.List;

/**
 * 记录
 *
 * @author Anna.
 * @date 2021/6/9 17:23
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString(callSuper = true)
public class RfTraceRecordVO extends ItemCell {


    /**
     * 工艺路线
     */
    @Dict(dictTable = "mes_route",dicText = "route_name",dicCode = "id")
    private String routeId;
    /**
     * 物料清单
     */
    @Dict(dictTable = "mes_item_bom" , dicCode = "id", dicText = "version")
    private String bomId;
    /**
     * 生产bom
     */
    @Dict(dictTable = "mes_product" , dicCode = "id", dicText = "version")
    private String productId;
    /**
     * 产出人
     */
    private String workEmployee;
    /**
     * 产出工位
     */
    private String workStation;
    /**
     * 产出时间
     */
    private Date workTime;
    /**
     * 质检详情
     */
    private List<QcInfoDetailVO> qcInfoDetailVOList;
    /**
     * 转移记录追溯
     */
    private List<TransferRecordVO> transferRecordVOList;

}
