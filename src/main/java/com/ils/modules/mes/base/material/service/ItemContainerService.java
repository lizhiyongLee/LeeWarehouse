package com.ils.modules.mes.base.material.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ils.modules.mes.base.material.entity.Item;
import com.ils.modules.mes.base.material.entity.ItemContainer;
import com.ils.modules.mes.base.material.vo.ItemContainerVO;
import com.ils.modules.mes.base.material.vo.ItemVO;
import com.ils.modules.mes.material.entity.ItemCell;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @author lishaojie
 * @description
 * @date 2021/10/25 9:47
 */
public interface ItemContainerService extends IService<ItemContainer> {

    /**
     * 添加一对多
     *
     * @param itemContainerVO
     * @return ItemContainerVO
     */
    ItemContainerVO saveMain(ItemContainerVO itemContainerVO);

    /**
     * 根据主id查询
     *
     * @param id
     * @return
     */
    ItemContainerVO selectById(String id);

    /**
     * 修改一对多
     * @param itemContainerVO
     * @return
     */
    ItemContainerVO updateMain(ItemContainerVO itemContainerVO);

    /**
     * 根据标签码查询物料单元
     * @param containerQrcode
     * @param itemCellQrcode
     * @return
     */
    ItemCell checkItemCellByQrcode(String containerQrcode,String itemCellQrcode);
}
