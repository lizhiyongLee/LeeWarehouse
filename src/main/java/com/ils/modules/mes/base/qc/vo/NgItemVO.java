package com.ils.modules.mes.base.qc.vo;

import com.ils.modules.mes.base.qc.entity.NgItem;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * @Description: 不良项
 * @author: fengyi
 * @date: 2020年10月19日 下午2:05:35
 */
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
public class NgItemVO extends NgItem {

    private String ngTypeName;

}
