package com.ils.modules.mes.base.ware.vo;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import com.ils.modules.mes.enums.ZeroOrOneEnum;
import com.ils.modules.mes.util.TreeNode;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

/**
 * TODO 类描述
 *
 * @author Anna.
 * @date 2021/8/5 15:44
 */
public class RelatedStorageTreeNodeVO extends RelatedStorageVO implements TreeNode<RelatedStorageTreeNodeVO> {

    private static final long serialVersionUID = 1L;

    private List<RelatedStorageTreeNodeVO> lstChildNodeTreeVO;


    public RelatedStorageTreeNodeVO() {
    }

    /**
     * 根据提供的VO包装成TreeVO
     *
     * @param relatedStorageVO
     */
    public RelatedStorageTreeNodeVO(RelatedStorageVO relatedStorageVO) {
        BeanUtil.copyProperties(relatedStorageVO, this);
    }

    public void setLstChildNodeTreeVO(List<RelatedStorageTreeNodeVO> lstChildNodeTreeVO) {
        this.lstChildNodeTreeVO = lstChildNodeTreeVO;
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
        if (lstChildNodeTreeVO == null || lstChildNodeTreeVO.size() == 0) {
            return ZeroOrOneEnum.ONE.getStrCode();
        } else {
            return ZeroOrOneEnum.ZERO.getStrCode();
        }
    }

    @Override
    public List<RelatedStorageTreeNodeVO> getChildren() {
        if (CollectionUtil.isEmpty(lstChildNodeTreeVO)) {
            lstChildNodeTreeVO = new ArrayList<>();
        }
        return lstChildNodeTreeVO;
    }
}
