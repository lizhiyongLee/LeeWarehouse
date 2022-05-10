package com.ils.modules.mes.base.ware.vo;

import lombok.Data;

/**
 * TODO 类描述
 *
 * @author Anna.
 * @date 2021/8/5 14:41
 */
public class RelatedVO {
    private String name;
    private String areaId;
    private String storageId;

    public RelatedVO() {
        super();
    }

    public RelatedVO(String name, String areaId, String storageId) {
        this.name = name;
        this.areaId = areaId;
        this.storageId = storageId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAreaId() {
        return areaId;
    }

    public void setAreaId(String areaId) {
        this.areaId = areaId;
    }

    public String getStorageId() {
        return storageId;
    }

    public void setStorageId(String storageId) {
        this.storageId = storageId;
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    @Override
    public String toString() {
        return super.toString();
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
    }
}
