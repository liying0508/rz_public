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
@TableName("f_repayment_actual")
public class FRepaymentActual extends BaseEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    //主键
    private Long id;

    //融资种类（1直融，2间融，3政府债券）
    private Integer financingType;
    //融资单id
    private Long financingId;

    //计划类型（1实际还本，2实际付息）
    private Integer actualType;

    //还款日期
    private Date repaymentDate;

    //实际还本金额
    private BigDecimal actualRepayPrincipal;

    //实际付息金额
    private BigDecimal actualRepayInterest;

    //利率
    private Double rate;

    //备注
    private String remark;
}
