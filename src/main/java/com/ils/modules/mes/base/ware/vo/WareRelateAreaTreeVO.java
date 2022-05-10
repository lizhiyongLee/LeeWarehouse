package com.ils.modules.mes.base.ware.vo;

import cn.hutool.core.bean.BeanUtil;
import com.ils.modules.mes.enums.ZeroOrOneEnum;
import com.ils.modules.mes.util.TreeNode;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

/**
 * 用来构造关联仓位的树形结构的工具类
 *
 * @author Anna.
 * @date 2020/12/4 14:34
 */
@ToString(callSuper = true)
public class WareRelateAreaTreeVO extends WareRelateAreaVO implements TreeNode<WareRelateAreaTreeVO> {

    private static final long serialVersionUID = 1L;

    private List<WareRelateAreaTreeVO> wareRelateAreaTreeVOList;


    public WareRelateAreaTreeVO() {

    }

    public WareRelateAreaTreeVO(WareRelateAreaVO wareRelateAreaVO) {
        BeanUtil.copyProperties(wareRelateAreaVO, this);
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
        if (wareRelateAreaTreeVOList == null || wareRelateAreaTreeVOList.isEmpty()) {
            return ZeroOrOneEnum.ONE.getStrCode();
        } else {
            return ZeroOrOneEnum.ZERO.getStrCode();
        }
    }

    @Override
    public List<WareRelateAreaTreeVO> getChildren() {
        if (wareRelateAreaTreeVOList == null) {
            wareRelateAreaTreeVOList = new ArrayList<>();
        }
        return wareRelateAreaTreeVOList;
    }
}
