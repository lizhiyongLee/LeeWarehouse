package com.ils.modules.mes.base.factory.vo;

import java.util.List;

import com.ils.modules.mes.base.factory.entity.Factory;
import com.ils.modules.mes.config.vo.DefineFieldValueVO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * @Description: 描述这个类的作用
 * @author: fengyi
 * @date: 2020年10月13日 下午5:46:33
 * @param: 
 */
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
public class FactoryVO extends Factory {

    /**
     * 自定义字段
     */
    List<DefineFieldValueVO> lstDefineFields;
}
