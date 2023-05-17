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
@TableName("f_repayment_info_table_history")
public class FRepaymentInfoTableHistoryEntity extends BaseEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    //主键
    private Long id;
    //融资单id
    private Long infoId;
    //融资种类
    private Integer financingType;
    //融资单id
    private Long financingId;
    //信息种类(1为还本信息，2为还息信息)
    private Integer infoType;
    //还款日期
    private Date repayDate;
    //已还本金
    private BigDecimal alreadyRepayPrincipal;
    //已还利息
    private BigDecimal alreadyRepayInterest;
    //利率
    private Double rate;
}
