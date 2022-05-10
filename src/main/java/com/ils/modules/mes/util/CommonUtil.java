/**
 * @Title: MesUtil.java
 * @Package: com.ils.modules.mes.util
 * @author: fengyi
 * @date: 2020年10月13日 下午2:56:25
 */
package com.ils.modules.mes.util;

import java.util.Collection;
import java.util.Date;

import com.ils.common.system.util.TenantContext;
import org.apache.shiro.SecurityUtils;

import com.ils.common.constant.CommonConstant;
import com.ils.common.system.base.entity.ILSEntity;
import com.ils.common.system.vo.LoginUser;
import com.ils.common.util.SpringContextUtils;
import com.ils.modules.mes.base.material.entity.Item;
import com.ils.modules.mes.enums.ItemManageWayEnum;
import com.ils.modules.mes.enums.ZeroOrOneEnum;
import com.ils.modules.system.entity.BizConfig;
import com.ils.modules.system.service.BizConfigService;

/**
 * @Description: 通用工具类
 * @author: fengyi
 * @date: 2020年10月13日 下午2:56:25
 * @param:
 */

public class CommonUtil {

    /**
     * 获取当前登录租户
     * 
     * @return 返回当前登录租户
     * @date 2020年10月13日
     */
    public static String getTenantId() {
        return TenantContext.getTenant();
    }

    public static LoginUser getLoginUser() {
        LoginUser user = null;
        try {
            user = (LoginUser)SecurityUtils.getSubject().getPrincipal();
        } catch (Exception e) {
            user = new LoginUser();
        }
        return user;
    }

    /**
     * 设置系统参数
     * 
     * @param target
     * @param source
     */
    public static void setSysParam(ILSEntity target) {
        target.setTenantId(getTenantId());
        LoginUser sysUser = null;
        if (SecurityUtils.getSubject().isAuthenticated()) {
            sysUser = (LoginUser)SecurityUtils.getSubject().getPrincipal();
            Date nowDate = new Date();
            target.setCreateBy(sysUser.getUsername());
            target.setCreateTime(nowDate);
            target.setUpdateBy(sysUser.getUsername());
            target.setUpdateTime(nowDate);
        }
        target.setDeleted(CommonConstant.DEL_FLAG_0);
    }

    /**
     * 设置系统参数
     * 
     * @param target
     * @param source
     */
    public static void setSysParam(ILSEntity target, ILSEntity source) {
        target.setTenantId(source.getTenantId());
        target.setCreateBy(source.getCreateBy() == null ? "ils" : source.getCreateBy());
        target.setCreateTime(source.getCreateTime() == null ? new Date() : source.getCreateTime());
        target.setUpdateBy(source.getUpdateBy() == null ? "ils" : source.getUpdateBy());
        target.setUpdateTime(source.getUpdateTime() == null ? new Date() : source.getUpdateTime());
        target.setDeleted(CommonConstant.DEL_FLAG_0);
    }

    /**
     * 
     * 判断集合是否为空
     * 
     * @return 为null 或者 没有元素返回ture,否则为false
     */
    public static boolean isEmptyOrNull(Collection con) {
        if (null == con || con.isEmpty()) {
            return true;
        }
        return false;
    }

    /**
     * 
     * 获取业务配置工具类
     * 
     * @param configCode
     * @return
     */
    public static BizConfig getBizConfig(String configCode) {

        BizConfigService bizConfigService = SpringContextUtils.getBean(BizConfigService.class);
        BizConfig bizConfig = bizConfigService.getBizConfigByCode(configCode);
        if (bizConfig == null) {
            bizConfig = new BizConfig();
        }
        return bizConfig;
    }

    /**
     * 
     * 获取物料管理方式
     * 
     * @param item
     * @return
     */
    public static String getItemManageWay(Item item) {
        if (ZeroOrOneEnum.ONE.getStrCode().equals(item.getQrcode())) {
            return ItemManageWayEnum.QRCODE_MANAGE.getValue();
        } else if (ZeroOrOneEnum.ONE.getStrCode().equals(item.getBatch())) {
            return ItemManageWayEnum.BATCH_MANAGE.getValue();
        } else {
            return ItemManageWayEnum.NONE_MANAGE.getValue();
        }
    }
}
