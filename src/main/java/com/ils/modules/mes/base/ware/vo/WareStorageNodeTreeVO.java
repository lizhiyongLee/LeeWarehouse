package com.ils.modules.mes.base.ware.vo;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import com.ils.modules.mes.base.ware.entity.WareStorage;
import com.ils.modules.mes.enums.ZeroOrOneEnum;
import com.ils.modules.mes.util.TreeNode;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

/**
 * 仓库类树形结构类
 *
 * @author Anna.
 * @date 2020/11/16 11:14
 */
@ToString(callSuper = true)
public class WareStorageNodeTreeVO extends WareStorage implements TreeNode<WareStorageNodeTreeVO> {

    private static final long serialVersionUID = 1L;

    private List<WareStorageNodeTreeVO> lstChildNodeTreeVO;


    public void setLstChildNodeTreeVO(List<WareStorageNodeTreeVO> lstChildNodeTreeVO) {
        this.lstChildNodeTreeVO = lstChildNodeTreeVO;
    }

    public WareStorageNodeTreeVO() {

    }

    /**
     * 根据提供的VO包装成TreeVO
     *
     * @param wareStorage
     */
    public WareStorageNodeTreeVO(WareStorage wareStorage) {
        BeanUtil.copyProperties(wareStorage, this);
    }

    @Override
    public String getKey() {
        return this.getId();
    }

    @Override
    public String getValue() {
        return this.getId();
    }

    @Override
    public String getTitle() {
        return this.getStorageName();
    }

    @Override
    public String getParentId() {
        return this.getUpStorageId();
    }

    @Override
    public String getLeaf() {
        if (lstChildNodeTreeVO == null || lstChildNodeTreeVO.size() == 0) {
            return ZeroOrOneEnum.ONE.getStrCode();
        } else {
            return ZeroOrOneEnum.ZERO.getStrCode();
        }
    }

    @Override
    public List<WareStorageNodeTreeVO> getChildren() {
        if (CollectionUtil.isEmpty(lstChildNodeTreeVO)) {
            lstChildNodeTreeVO = new ArrayList<>();
        }
        return lstChildNodeTreeVO;
    }


}
