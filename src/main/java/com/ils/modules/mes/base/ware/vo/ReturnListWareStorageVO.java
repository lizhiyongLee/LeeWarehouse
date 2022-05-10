package com.ils.modules.mes.base.ware.vo;

import com.ils.common.system.base.entity.ILSEntity;
import lombok.*;

import java.util.List;

/**
 *
 * @Description: 用来返回仓位集合（直接返回list对象不会走数据字典的代理类）
 * @author Anna.
 * @date 2020/12/18 15:18
 */
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString(callSuper = true)
public class ReturnListWareStorageVO extends ILSEntity {
    private static final long serialVersionUID = 1L;
    /**
     * 用来返回wareStorageVOlist的，直接放回lsit不会走数据字典切面
     */
    private List<WareStorageVO> wareStorageVOList;
}
