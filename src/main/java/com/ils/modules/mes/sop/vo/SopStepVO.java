package com.ils.modules.mes.sop.vo;

import com.ils.modules.mes.sop.entity.SopControl;
import com.ils.modules.mes.sop.entity.SopStep;
import lombok.Data;

import java.util.List;

/**
 * sopsetp展示类
 *
 * @author Anna.
 * @date 2021/7/23 10:14
 */
@Data
public class SopStepVO extends SopStep {

    /**
     * sop控件集合
     */
    List<SopControlVO> sopControlList;
    /**
     * 是否能执行，1.能执行，0，不能执行
     */
    private String execute;
}
