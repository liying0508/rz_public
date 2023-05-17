package io.renren.modules.financing.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.renren.common.entity.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper=false)
@TableName("f_repayment_plan")
public class FRepaymentPlan extends BaseEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    //主键
    private Long id;
    //融资种类
    private Integer financingType;
    //融资单id
    private Long financingId;
    //计划种类
    private Integer planType;
    //开始日期
    private Date startDate;
    //还款日期
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
