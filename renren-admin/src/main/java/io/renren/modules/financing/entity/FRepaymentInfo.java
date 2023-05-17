package io.renren.modules.financing.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.renren.common.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@EqualsAndHashCode(callSuper=false)
@TableName("f_repayment_info")
public class FRepaymentInfo extends BaseEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    //主键
    private Long id;
    //本金
    private BigDecimal principal;
    //利息
    private BigDecimal interest;

    /**
     * 还款信息列表
     */
    private BigDecimal repaymentMoney;
    //实际还款金额
    private BigDecimal actualRepayment;
    //融资单id
    private Long financingId;
    //融资类型：0政府债券，1直接融资，2间接融资
    private Integer financingType;

    private Integer isChecked;

    private String checkedDes;

    private Integer groupChecked;

    private String groupCheckedDes;
}
