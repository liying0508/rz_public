package io.renren.modules.report.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @Author: Eddy
 * @Date: 2022/11/15 0015
 * @Time: 11:35
 * @Description: 银行授信月度报表
 */
@Data
@ApiModel(value = "银行授信月度报表")
public class RCreditMonthDTO {
    @ApiModelProperty(value = "银行名称")
    private String institutionName;
    @ApiModelProperty(value = "授信总额")
    private BigDecimal creditTotalAmount;
    @ApiModelProperty(value = "已使用额度")
    private BigDecimal usedAmount;
    @ApiModelProperty(value = "剩余额度")
    private BigDecimal surplusAmount;
}
