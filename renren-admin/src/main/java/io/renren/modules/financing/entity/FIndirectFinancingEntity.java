package io.renren.modules.financing.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import com.baomidou.mybatisplus.annotation.*;

import java.math.BigDecimal;
import java.util.Date;
import io.renren.common.entity.BaseEntity;

/**
 * 间接融资信息
 *
 * @author liuwenyong 
 * @since 3.0 2022-05-14
 */
@Data
@EqualsAndHashCode(callSuper=false)
@TableName("f_indirect_financing")
public class FIndirectFinancingEntity extends BaseEntity {
	private static final long serialVersionUID = 1L;

//	/**
//	* 授信单ID
//	*/
//	private Long creditId;
	/**
	 * 主键
	 */
	private Long id;
	/**
	* 被授信单位ID
	*/
	private Long deptId;
	/**
	* 金融机构ID
	*/
	private Long institution;
	/**
	* 融资品种
	*/
	private Integer varieties;
	/**
	* 保证金比例
	*/
	private Integer marginRatio;
	/**
	* 贴现率
	*/
	private Integer discountRate;
    /**
     * 利率类型
     */
	private String interestRatesWay;
    /**
     * 浮动利率周期
     */
    private Integer lprCycle;
    /**
     * 利率
     */
    private String rate;
	/**
	 * 上浮利率
	 */
	private String floatRate;
	/**
	* 合同编号
	*/
	private String contractNo;
	/**
	* 合同金额
	*/
	private BigDecimal contractAmount;
    /**
     * 开票金额
     */
    private BigDecimal invoiceAmount;
	/**
	* 提款金额
	*/
	private BigDecimal withdrawalAmount;
	/**
	* 付息方式
	*/
	private String interestMethod;
	/**
	 * 付本方式
	 */
	private String principalMethod;
	/**
	* 放款日期
	*/
	private Date loanDate;
	/**
	* 还款日期
	*/
	private Date repaymentDate;
	/**
	* 融资余额
	*/
	private BigDecimal financingBalance;
	/**
	* 其他费用
	*/
	private BigDecimal otherExpenses;
    /**
     * 其他费用备注
     */
    private String otherExpensesDesc;
	/**
	* 用途
	*/
	private String purpose;
	/**
	* 增信措施
	*/
	private String creditMeasures;
    /**
     * 决策依据
     */
    private String decisionBasis;
	/**
	* 还款计划
	*/
	private String repaymentPlan;
	/**
	 * 是否为还本付息
	 */
	private Integer isPayPrincipalAndInterest;
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
	/**
	 * 中介机构信息
	 */
	private String intermediaryStructure;
	/**
	 * 投资人信息
	 */
	private String investor;
	/**
	 * 项目描述
	 */
	private String project;
	private String files;
	/**
	 * 一年期还是五年期利率(1为1年期利率，5为5年期)
	 */
	private Integer lprInterestCycle;

	//实际还款
	private BigDecimal actualRepayment;

	//实际还款信息
	private String repaymentActual;
}