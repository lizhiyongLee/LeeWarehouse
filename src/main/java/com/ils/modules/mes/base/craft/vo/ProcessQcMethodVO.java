package com.ils.modules.mes.base.craft.vo;

import com.ils.modules.mes.base.craft.entity.ProcessQcMethod;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * @Description: 工序跟质检方案关联VO
 * @author: fengyi
 * @date: 2020年10月28日 下午2:15:55
 */
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
public class ProcessQcMethodVO extends ProcessQcMethod {

    /** 质检类型:1,入厂检；2，出厂检；3，首检；4，生产检，5，巡检，6，普通检 */
    private String qcType;
    /** 质检方案名称 */
    private String methodName;

}
