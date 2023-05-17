package io.renren.modules.financing.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.renren.common.utils.DateUtils;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class FRepaymentPlanDTO {

    //主键
    private Long id;
    //融资种类
    private Integer financingType;
    //融资单id
    private Long financingId;
    //计划种类（1还本，2还息）
    private Integer planType;
    //开始日期
    @JsonFormat(pattern = DateUtils.DATE_PATTERN)
    private Date startDate;
    //还款日期
    @JsonFormat(pattern = DateUtils.DATE_PATTERN)
    private Date endDate;
    //偿还本金
    private BigDecimal repaymentOfPrincipal;
    //偿还利息
    private BigDecimal interest;
    //剩余本金
    private BigDecimal oddCorpus;
    //说明
    private String remark;

}
