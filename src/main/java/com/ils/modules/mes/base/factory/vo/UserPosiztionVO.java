package com.ils.modules.mes.base.factory.vo;

import java.io.Serializable;

import lombok.Data;

/**
 * @Description: 用户职位
 * @author: fengyi
 */
@Data
public class UserPosiztionVO implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 用户ID
     */
    private String id;
    /**
     * 用户名称
     */
    private String realname;
    /**
     * 用户工号
     */
    private String workNo;
    /**
     * 用户登录账号
     */
    private String username;
    /**
     * 岗位ID
     */
    private String positionId;
    /**
     * 岗位编码
     */
    private String positionCode;
    /**
     * 岗位名称
     */
    private String positionName;
    /**
     * 租户名称
     */
    private String relTenantIds;

}
