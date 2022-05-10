package com.ils.modules.mes.base.factory.vo;

import cn.hutool.core.bean.BeanUtil;
import com.ils.modules.mes.enums.ZeroOrOneEnum;
import com.ils.modules.mes.util.TreeNode;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

/**
 * @Description: 树形节点VO
 * @author: fengyi
 * @date: 2020年10月16日 上午9:50:31
 */
@ToString(callSuper = true)
public class NodeTreeVO extends NodeVO implements TreeNode<NodeTreeVO> {

    private static final long serialVersionUID = 1L;

    private List<NodeTreeVO> lstChildNodeTreeVO;

    public NodeTreeVO() {

    }

    /**
     * 根据提供的VO包装成TreeVO
     *
     * @param nodeVO
     */
    public NodeTreeVO(NodeVO nodeVO) {
        BeanUtil.copyProperties(nodeVO, this);
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
    public String getLeaf() {
        if (lstChildNodeTreeVO == null || lstChildNodeTreeVO.isEmpty()) {
            return ZeroOrOneEnum.ONE.getStrCode();
        } else {
            return ZeroOrOneEnum.ZERO.getStrCode();
        }

    }

    @Override
    public List<NodeTreeVO> getChildren() {
        if (lstChildNodeTreeVO == null) {
            lstChildNodeTreeVO = new ArrayList<NodeTreeVO>();
        }
        return lstChildNodeTreeVO;
    }


    public void setLstChildNodeTreeVO(List<NodeTreeVO> lstChildNodeTreeVO) {
        this.lstChildNodeTreeVO = lstChildNodeTreeVO;
    }

    @Override
    public String getParentId() {
        return this.getUpArea();
    }
}
