package com.ils.modules.mes.base.ware.vo;

import com.ils.modules.mes.base.ware.entity.WareStorage;
import lombok.*;

import java.util.List;

/**
 * 执行端出库时选择仓库后，查询其一级仓位和二级仓位的集合
 *
 * @author Anna.
 * @date 2021/1/12 10:45
 */
@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
@ToString
public class WareStorageListVO extends WareStorage {
    private static final long serialVersionUID = 1L;
    /**
     * wareStoragelISt
     */
    private List<WareStorage> wareStorageList;
}
