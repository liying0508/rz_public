package io.renren.modules.financing.vo;

import io.renren.modules.financing.dto.FRepayInterestPlanDTO;
import io.renren.modules.financing.dto.FRepayPrincipalPlanDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
@ApiModel(value = "还款计划对象")
public class PayPlanDataVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "中途还本时是否付息")
    private Integer isPayPrincipalAndInterest;
    @ApiModelProperty(value = "测算周期")
    private String interestMeasurementCycle;
    @ApiModelProperty(value = "是否结息日算息")
    private Integer endNeedCount;
    @ApiModelProperty(value = "还本信息列表")
    private List<FRepayPrincipalPlanDTO> repayPrincipalPlanList;
    @ApiModelProperty(value = "还息信息列表")
    private List<FRepayInterestPlanDTO> repayInterestPlanList;

}
