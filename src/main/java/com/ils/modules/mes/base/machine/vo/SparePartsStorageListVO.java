package com.ils.modules.mes.base.machine.vo;

import com.ils.common.system.base.entity.ILSEntity;
import lombok.*;

import java.util.List;

/**
 * 用来返回备件库位集合VO类
 *
 * @author Anna.
 * @date 2021/2/24 10:33
 */
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString(callSuper = true)
public class SparePartsStorageListVO extends ILSEntity {

    private static final long serialVersionUID = 1L;
    /**
     * 备件库位集合
     */
    private List<SparePartsStorageVO> sparePartsStorageVOList;
}
