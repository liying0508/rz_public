package io.renren.modules.financing.vo;

import io.renren.modules.financing.dto.FRepayInterestActualDTO;
import io.renren.modules.financing.dto.FRepayPrincipalActualDTO;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PayActualDataVO {

    @ApiModelProperty(value = "总实际付息")
    private BigDecimal totalRepayInterestActual;
    @ApiModelProperty(value = "总实际还本")
    private BigDecimal totalRepayPrincipalActual;
    @ApiModelProperty(value = "总实际还款")
    private BigDecimal totalRepaymentActual;

    @ApiModelProperty(value = "实际还本列表")
    private List<FRepayPrincipalActualDTO> repayPrincipalActualList;
    @ApiModelProperty(value = "实际付息列表")
    private List<FRepayInterestActualDTO> repayInterestActualList;
}
