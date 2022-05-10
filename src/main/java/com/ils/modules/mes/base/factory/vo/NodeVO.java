package com.ils.modules.mes.base.factory.vo;

import com.ils.common.aspect.annotation.Dict;
import lombok.ToString;

import java.io.Serializable;

/**
 * @Description: 工厂定义nodeVO
 * @author: fengyi
 * @date: 2020年10月15日 上午11:28:32
 */
@ToString(callSuper = true)
public class NodeVO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 租户ID
     */
    private String tenantId;

    /**
     * 节点ID
     */
    private String id;
    /**
     * 父节点ID
     */
    private String upArea;
    /**
     * 节点code
     */
    private String code;
    /**
     * 节点名称
     */
    private String name;
    /**
     * 节点类型
     */
    @Dict(dicCode = "mesNodeType")
    private String nodeType;

    /**
     * 责任人
     */
    private String dutyPersonName;

    /**
     * 状态
     */
    @Dict(dicCode = "mesStatus")
    private String status;

    /**
     * 二维码
     */
    private String qrcode;

    /**
     * 备注
     */
    private String note;

    /**
     * 全路径
     */
    private String fullPath;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNodeType() {
        return nodeType;
    }

    public void setNodeType(String nodeType) {
        this.nodeType = nodeType;
    }

    public String getUpArea() {
        return upArea;
    }

    public void setUpArea(String upArea) {
        this.upArea = upArea;
    }

    public String getDutyPersonName() {
        return dutyPersonName;
    }

    public void setDutyPersonName(String dutyPersonName) {
        this.dutyPersonName = dutyPersonName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getQrcode() {
        return qrcode;
    }

    public void setQrcode(String qrcode) {
        this.qrcode = qrcode;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    public String getFullPath() {
        return fullPath;
    }

    public void setFullPath(String fullPath) {
        this.fullPath = fullPath;
    }

}
