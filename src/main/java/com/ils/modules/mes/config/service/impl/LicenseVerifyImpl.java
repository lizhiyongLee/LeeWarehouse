package com.ils.modules.mes.config.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ils.license.verify.service.LicenseVerifyManager;
import com.ils.modules.system.service.LicenseVerifyService;

import lombok.extern.slf4j.Slf4j;

/**
 * @Description: license检验
 * @author: fengyi
 */
@Service
@Slf4j
public class LicenseVerifyImpl implements LicenseVerifyService {

    @Autowired
    private LicenseVerifyManager licenseVerifyManager;
    @Override
    public boolean verifyLicense() {
        System.err.println("自己实现");
        return true;
    }

}
