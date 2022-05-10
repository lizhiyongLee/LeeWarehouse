package com.ils.modules.mes.base.machine.vo;

import com.ils.framework.poi.excel.annotation.ExcelCollection;
import com.ils.modules.mes.base.machine.entity.MachineTypeData;
import com.ils.modules.mes.base.machine.entity.MachineTypePolicy;
import lombok.*;

import java.util.List;

/**
 * TODO 类描述
 *
 * @author Anna.
 * @date 2020/11/4 16:21
 */
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
public class MachineTypePolicyVO extends MachineTypePolicy {
    private static final long serialVersionUID = 1L;

    private String machineTypeTaskEmployees;
}
