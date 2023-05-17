package io.renren.modules.financing.vo;

import io.renren.modules.financing.dto.FRepayInterestPlanDTO;
import io.renren.modules.financing.dto.FRepayPrincipalPlanDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Data
@ApiModel(value = "间融利息测算VO")
public class IndirectInterestVO {
    @ApiModelProperty(value = "固定利率")
    private double rate;
    @ApiModelProperty(value = "利率类型 0固定，1浮动")
    private Integer interestRatesWay;
    @ApiModelProperty(value = "调息周期")
    private Integer lprCycle;
    @ApiModelProperty(value = "上浮利率")
    private double floatRate;
    @ApiModelProperty(value = "lpr利息周期")
    private int lprInterestCycle;
    @ApiModelProperty(value = "贷款金额")
    private BigDecimal withdrawalAmount;
    @ApiModelProperty(value = "还本计划")
    private List<FRepayPrincipalPlanDTO> principalList;
    @ApiModelProperty(value = "还息计划")
    private List<FRepayInterestPlanDTO> interestList;
    @ApiModelProperty(value = "测算周期")
    private Integer interestMeasurementCycle;
    @ApiModelProperty(value = "是否是还本付息（0否，1是）")
    private Integer isPayPrincipalAndInterest;
    @ApiModelProperty(value = "单位")
    private Integer unit;
}
