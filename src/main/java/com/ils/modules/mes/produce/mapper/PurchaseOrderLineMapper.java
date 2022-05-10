package com.ils.modules.mes.produce.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ils.common.system.base.mapper.ILSMapper;
import com.ils.modules.mes.produce.entity.PurchaseOrderLine;
import com.ils.modules.mes.produce.vo.PurchaseOrderLinePageVO;
import org.apache.ibatis.annotations.Param;

/**
 * @Description: 采购清单物料行
 * @Author: Tian
 * @Date: 2021-01-28
 * @Version: V1.0
 */
public interface PurchaseOrderLineMapper extends ILSMapper<PurchaseOrderLine> {
    /**
     * 通过采购清单id删除采购清单物料行
     * @param mainId
     * @return
     */
    public boolean delByMainId(String mainId);

    /**
     * 分页查询采购清单物料行
     * @param page
     * @param queryWrapper
     * @return
     */
    public IPage<PurchaseOrderLinePageVO> listPage(Page<PurchaseOrderLinePageVO> page, @Param("ew") QueryWrapper<PurchaseOrderLinePageVO> queryWrapper);

}
