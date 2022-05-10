package com.ils.modules.mes.sop.vo;

import lombok.Data;

/**
 * @author lishaojie
 * @description
 * @date 2021/8/9 18:51
 */
@Data
public class SopStepMsgVO {
    private String stepName;
    private String relatedTaskType;
    private String processName;
    private String workstation;
    private String taskCode;
}
