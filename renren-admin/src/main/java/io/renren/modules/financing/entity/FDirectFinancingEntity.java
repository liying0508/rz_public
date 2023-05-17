package io.renren.modules.financing.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.renren.common.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 直接融资信息
 *
 * @author liuwenyong 
 * @since 3.0 2022-05-14
 */
@Data
@EqualsAndHashCode(callSuper=false)
@TableName("f_direct_financing")
public class FDirectFinancingEntity extends BaseEntity {
	private static final long serialVersionUID = 1L;

//	/**
//	* 授信单ID
//	*/
//	private Long creditId;
    /**
     * Id
     */
    private Long id;
    /**
     * 批文号
     */
    private String approveNo;
//    /**
//     * 融资品种
//     */
//    private Integer varieties;
    /**
     * 审批单位
     */
    private String approveOrg;
    /**
     * 证券公司id
     */
    private Long secComId;

    /**
     * 证券公司id字符串
     */
    private String secComIdStr;
    /**
     * 付息周期
     */
//    private String serviceCycle;
    /**
     * 付息方式
     */
    private String interestMethod;
    /**
     * 付本方式
     */
    private String principalMethod;
    /**
     * 币种
     */
    private String currency;
    /**
     * 发行金额
     */
    private BigDecimal foreignCurrencyAmount;
    /**
     * 利率
     */
    private String rate;
    /**
     * 汇率
     */
    private String exchangeRate;
    /**
     * 已发行期次
     */
    private Integer issue;
    /**
     * 被授信单位ID
     */
    private String deptId;
    /**
     * 折合人民币
     */
    private BigDecimal issueQuota;
	/**
	* 发行日
	*/
	private Date issueDate;
	/**
	* 起息日
	*/
	private Date valueDate;
    /**
     * 到期日
     */
    private Date dueDate;
	/**
	* 融资余额
	*/
    private BigDecimal financingBalance;
    /**
     * 用途
     */
    private String purpose;
	/**
     * 其它费用
     */
    private BigDecimal otherExpenses;
    /**
     * 其它费用描述
     */
    private String otherExpensesDesc;
	/**
	* 增信措施
	*/
	private String creditMeasures;
    /**
     * 是否为还本付息
     */
    private Integer isPayPrincipalAndInterest;

    private String guarantee;

    private String collateral;

    private String pledge;

    private Integer isChecked;

    private String checkedDes;

    private Integer groupChecked;

    private String groupCheckedDes;

    private String repaymentPlan;
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
    //实际还款
    private BigDecimal actualRepayment;
    //备注
    private String remarks;
    //决策依据
    private String decisionBasis;
    //承销费率
    private Double underwritingRate;
    //实际还款信息
    private String repaymentActual;
    //是否占用授信(0不占用，1占用)
    private Integer occupancyCreditQuota;
}