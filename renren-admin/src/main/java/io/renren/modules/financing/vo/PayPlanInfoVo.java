package io.renren.modules.financing.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PayPlanInfoVo {
    @ApiModelProperty(value = "测算周期")
    private String interestMeasurementCycle;
    @ApiModelProperty(value = "是否结息日算息")
    private Integer endNeedCount;
}
