package io.renren.modules.financing.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.renren.common.utils.DateUtils;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "还款信息列表")
public class RepaymentInfoDTO implements Serializable {
    private static final long serialVersionUID = 1L;
    @ApiModelProperty(value = "主键")
    private Long id;
    @ApiModelProperty(value = "融资种类")
    private Integer financingType;
    @ApiModelProperty(value = "融资单id")
    private Long financingId;
    @ApiModelProperty(value = "信息种类(1为还本信息，2为还息信息)")
    private Integer infoType;
    @ApiModelProperty(value = "还款时间")
    @JsonFormat(pattern = DateUtils.DATE_PATTERN)
    private Date repayDate;
    @ApiModelProperty(value = "已还本金")
    private BigDecimal alreadyRepayPrincipal;
    @ApiModelProperty(value = "已还利息")
    private BigDecimal alreadyRepayInterest;
    @ApiModelProperty(value = "利率")
    private double rate;
}
