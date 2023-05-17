package io.renren.modules.financing.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PayActualInfoVo {
    @ApiModelProperty(value = "总实际付息")
    private BigDecimal totalRepayInterestActual;
    @ApiModelProperty(value = "总实际还本")
    private BigDecimal totalRepayPrincipalActual;
    @ApiModelProperty(value = "总实际还款")
    private BigDecimal totalRepaymentActual;
}
