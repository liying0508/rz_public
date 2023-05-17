package io.renren.modules.financing.dto;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "间融利息测算接口用DTO")
public class IndirectInterestDTO {
    private Date stDate;
    private Date[] payDate;
    private double amount;
    private double[] payAmount;
    private int adjustDays;
}
