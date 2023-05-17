package io.renren.modules.financing.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.renren.common.utils.DateUtils;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class FRepaymentActualDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    private Long id;
    @ApiModelProperty(value = "融资类型")
    private Integer financingType;
    @ApiModelProperty(value = "融资单id")
    private Long financingId;
    @ApiModelProperty(value = "实际还款种类 1实际还本，2实际付息")
    private Integer actualType;
    @ApiModelProperty(value = "还款日期")
    @JsonFormat(pattern = DateUtils.DATE_PATTERN)
    private Date repaymentDate;
    @ApiModelProperty(value = "偿还利息")
    private BigDecimal actualRepayInterest;
    @ApiModelProperty(value = "实际还本")
    private BigDecimal actualRepayPrincipal;
    @ApiModelProperty(value = "利率")
    private Double rate;
    @ApiModelProperty(value = "说明")
    private String remark;

}
