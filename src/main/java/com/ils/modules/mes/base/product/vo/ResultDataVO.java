package com.ils.modules.mes.base.product.vo;

import java.util.List;

import com.ils.common.system.base.entity.ILSEntity;

import lombok.Data;

/**
 * @Description: 数据返回接收
 * @author: fengyi
 * @date: 2021年1月6日 下午3:01:47
 */
@Data
public class ResultDataVO extends ILSEntity {
    /**
     * 返回数据对象 data
     */
    private List<?> data;
}
