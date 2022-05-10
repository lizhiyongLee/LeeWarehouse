package com.ils.modules.mes.produce.vo;

import java.io.Serializable;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * @Description: 派工任务下发时错误信息
 * @author: fengyi
 * @date: 2020年11月27日 上午10:08:47
 */
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
public class PlanTaskErrorMsgVO implements Serializable {

    private static final long serialVersionUID = 1L;
    
    /** 错误编码 */
    private String errorCode;
    
    /** 错误提示行 */
    private List<PlanTaskErrorVO> lstPlanTaskErrorVO;

}
