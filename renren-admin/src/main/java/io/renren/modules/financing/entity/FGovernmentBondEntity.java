package io.renren.modules.financing.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import io.renren.common.entity.BaseEntity;

/**
 * 政府债券
 *
 * @author liuwenyong 
 * @since 3.0 2022-05-14
 */
@Data
@EqualsAndHashCode(callSuper=false)
@TableName("f_government_bond")
public class FGovernmentBondEntity extends BaseEntity  implements Serializable{
	private static final long serialVersionUID = 1L;

	/**
	* 主键
	*/
	private Long id;

	/**
	 * 用款主体
	 */
	private String deptName;
	/**
	* 融资品种
	*/
	private Integer varieties;
	/**
	 * 融资主体
	 */
	private Long institution;
	/**
	* 融资金额
	*/
	private BigDecimal financingAmount;
	/**
	* 利率
	*/
	private Double rate;
	/**
	* 放款日期
	*/
	private String loanDate;
	/**
	* 还款日期
	*/
	private String repaymentDate;
	/**
	* 融资余额
	*/
	private BigDecimal financingBalance;
	/**
	* 项目
	*/
	private String project;
	/**
	* 增信措施
	*/
	private String creditMeasures;
	/**
	* 付息方式
	*/
	private String interestMethod;
	/**
	 * 付本方式
	 */
	private String principalMethod;
	/**
	* 还款计划
	*/
	private String repaymentPlan;
	/**
	* 备注
	*/
	private String remarks;
	private String guarantee;

	private String collateral;

	private String pledge;

	private Integer isChecked;

	private String checkedDes;

	private Integer groupChecked;

	private String groupCheckedDes;

	private String files;
	/**
	 * 是否为还本付息
	 */
	private Integer isPayPrincipalAndInterest;

	//实际还款
	private BigDecimal actualRepayment;

	//实际还款信息
	private String repaymentActual;
}