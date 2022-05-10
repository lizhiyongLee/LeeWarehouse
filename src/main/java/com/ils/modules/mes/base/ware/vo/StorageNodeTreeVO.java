package com.ils.modules.mes.base.ware.vo;

import cn.hutool.core.bean.BeanUtil;
import com.ils.modules.mes.enums.ZeroOrOneEnum;
import com.ils.modules.mes.util.TreeNode;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

/**
 * 用来构造树形结构的仓位VO
 *
 * @author Anna.
 * @date 2020/11/16 11:14
 */
@ToString(callSuper = true)
public class StorageNodeTreeVO extends WareStorageTreeVO implements TreeNode<StorageNodeTreeVO> {

    private static final long serialVersionUID = 1L;

    private List<StorageNodeTreeVO> lstChildNodeTreeVO;


    public void setLstChildNodeTreeVO(List<StorageNodeTreeVO> lstChildNodeTreeVO) {
        this.lstChildNodeTreeVO = lstChildNodeTreeVO;
    }

    public StorageNodeTreeVO() {

    }

    /**
     * 根据提供的VO包装成TreeVO
     *
     * @param wareStorageTreeVO
     */
    public StorageNodeTreeVO(WareStorageTreeVO wareStorageTreeVO) {
        BeanUtil.copyProperties(wareStorageTreeVO, this);
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
        return this.getName();
    }


    @Override
    public String getParentId() {
        return this.getUpArea();
    }

    @Override
    public String getLeaf() {
        if (lstChildNodeTreeVO == null || lstChildNodeTreeVO.isEmpty() ) {
            return ZeroOrOneEnum.ONE.getStrCode();
        } else {
            return ZeroOrOneEnum.ZERO.getStrCode();
        }
    }

    @Override
    public List<StorageNodeTreeVO> getChildren() {
        if (lstChildNodeTreeVO == null) {
            lstChildNodeTreeVO = new ArrayList<StorageNodeTreeVO>();
        }
        return lstChildNodeTreeVO;
    }
}
