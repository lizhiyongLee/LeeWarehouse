package com.ils.modules.mes.base.machine.vo;

import cn.hutool.core.bean.BeanUtil;
import com.ils.modules.mes.base.machine.entity.SparePartsStorage;
import com.ils.modules.mes.base.ware.entity.WareStorage;
import com.ils.modules.mes.enums.ZeroOrOneEnum;
import com.ils.modules.mes.util.TreeNode;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

/**
 * TODO 类描述
 *
 * @author Anna.
 * @date 2021/2/24 13:48
 */
@ToString(callSuper = true)
public class SparePartsStoargeNodeTreeVO extends WareStorage implements TreeNode<SparePartsStoargeNodeTreeVO> {
    private static final long serialVersionUID = 1L;

    private List<SparePartsStoargeNodeTreeVO> lstChildNodeTreeVO;


    public void setLstChildNodeTreeVO(List<SparePartsStoargeNodeTreeVO> lstChildNodeTreeVO) {
        this.lstChildNodeTreeVO = lstChildNodeTreeVO;
    }

    public SparePartsStoargeNodeTreeVO() {

    }

    /**
     * 根据提供的VO包装成TreeVO
     *
     * @param sparePartsStorage
     */
    public SparePartsStoargeNodeTreeVO(SparePartsStorage sparePartsStorage) {
        BeanUtil.copyProperties(sparePartsStorage, this);
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
    public List<SparePartsStoargeNodeTreeVO> getChildren() {
        if (lstChildNodeTreeVO == null) {
            lstChildNodeTreeVO = new ArrayList<SparePartsStoargeNodeTreeVO>();
        }
        return lstChildNodeTreeVO;
    }
}
