package com.ils.modules.mes.material.vo;

import com.ils.modules.mes.material.entity.ItemCell;
import lombok.*;

import java.util.List;

/**
 * TODO 类描述
 *
 * @author Anna.
 * @date 2021/1/12 13:40
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ItemCellVO extends ItemCell {

    private static final long serialVersionUID = 1L;

    private List<QrCodeVO> qrCodeVOList;
}
