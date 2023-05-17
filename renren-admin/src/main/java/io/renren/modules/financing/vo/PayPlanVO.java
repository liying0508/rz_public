package io.renren.modules.financing.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
@ApiModel(value = "还款计划列表")
public class PayPlanVO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "开始日期")
    private String startDate;

    @ApiModelProperty(value = "结束日期")
    private String endDate;

    @ApiModelProperty(value = "偿还利息")
    private String interest;

    @ApiModelProperty(value = "偿还本金")
    private String repaymentOfPrincipal;

    @ApiModelProperty(value = "剩余本金")
    private String oddCorpus;

    @ApiModelProperty(value = "说明")
    private String desc;
}
