package io.renren.modules.financing.dto;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.renren.common.utils.DateUtils;
import io.renren.modules.financing.vo.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 *直接融资审核
 */
@Data
@ApiModel(value = "直接融资审核信息")
public class FDirectFinancingAuditDTO {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "单位名称")
    private String deptName;
    @ApiModelProperty(value = "主键")
    private Long id;
    @ApiModelProperty(value = "批文号")
    private String approveNo;

    @ApiModelProperty(value = "证券公司id")
    private Long secComId;
    @ApiModelProperty(value = "证券公司id列表")
    private List<Long> secComIdList;
    @ApiModelProperty(value = "证券公司列表json字符串")
    private String secComIdStr;
    @ApiModelProperty(value = "承销费率")
    private Double underwritingRate;

    @ApiModelProperty(value = "融资品种")
    private String varieties;
    @ApiModelProperty(value = "审批单位")
    private String approveOrg;
    @ApiModelProperty(value = "审批金额")
    private BigDecimal approvalAmount;
    @ApiModelProperty(value = "付息周期")
    private String serviceCycle;
    @ApiModelProperty(value = "付息方式")
    private String interestMethod;
    @ApiModelProperty(value = "付本方式")
    private String principalMethod;
    @ApiModelProperty(value = "是否为还本付息")
    private Integer isPayPrincipalAndInterest;

    @ApiModelProperty(value = "是否占用授信（0不占用，1占用）")
    private Integer occupancyCreditQuota;


    @ApiModelProperty(value = "币种")
    private String currency;
    @ApiModelProperty(value = "发行金额")
    private BigDecimal foreignCurrencyAmount;
    @ApiModelProperty(value = "利率")
    private String rate;
    @ApiModelProperty(value = "汇率")
    private String exchangeRate;
    @ApiModelProperty(value = "已发行期次")
    private Integer issue;
    @ApiModelProperty(value = "被授信单位ID")
    private String deptId;
    @ApiModelProperty(value = "折合人民币")
    private BigDecimal issueQuota;

    @ApiModelProperty(value = "发行日")
    @JsonFormat(pattern = DateUtils.DATE_PATTERN)
    private Date issueDate;
    @ApiModelProperty(value = "起息日")
    @JsonFormat(pattern = DateUtils.DATE_PATTERN)
    private Date valueDate;
    @ApiModelProperty(value = "到期日")
    @JsonFormat(pattern = DateUtils.DATE_PATTERN)
    private Date dueDate;
    @ApiModelProperty(value = "实际还款金额")
    private BigDecimal actualRepayment;
    @ApiModelProperty(value = "融资余额")
    private BigDecimal financingBalance;
    @ApiModelProperty(value = "用途")
    private String purpose;

    @ApiModelProperty(value = "其它费用")
    private BigDecimal otherExpenses;
    @ApiModelProperty(value = "其它费用描述")
    private String otherExpensesDesc;

    @ApiModelProperty(value = "增信措施")
    private String creditMeasures;
    @ApiModelProperty(value = "增信措施集合")
    private List<String> creditMeasuresList;
    @ApiModelProperty(value = "增信措施详情")
    @JSONField(name = "guaranteeInfo")
    private CreditMeasuresVO guaranteeInfo;
    @ApiModelProperty(value = "保证")
    private String guarantee;
    @ApiModelProperty(value = "抵押")
    private String collateral;
    @ApiModelProperty(value = "质押")
    private String pledge;

    @ApiModelProperty(value = "备注")
    private String remarks;
    @ApiModelProperty(value = "决策依据")
    private String decisionBasis;

    @ApiModelProperty(value = "中介机构列表")
    private List<IntermediaryStructureVo> intermediaryStructureList;
    @ApiModelProperty(value = "中介机构")
    private String intermediaryStructure;
    @ApiModelProperty(value = "投资人列表")
    private List<InvestorVO> investorList;
    @ApiModelProperty(value = "投资人")
    private String investor;

    @ApiModelProperty(value = "还款计划")
    private String repaymentPlan;
    @ApiModelProperty(value = "还款计划列表")
    private PayPlanDataVO payPlanData;

    @ApiModelProperty(value = "实际还款")
    private String repaymentActual;
    @ApiModelProperty(value = "实际还款列表")
    private PayActualDataVO payActualData;

    @ApiModelProperty(value = "审核状态")
    private Integer isChecked;

    @ApiModelProperty(value = "审核意见")
    private String checkedDes;

    @ApiModelProperty(value = "集团审核状态")
    private Integer groupChecked;
    @ApiModelProperty(value = "集团审核意见")
    private String groupCheckedDes;

    @ApiModelProperty(value = "项目描述")
    private String project;
    @ApiModelProperty(value = "附件上传")
    private String files;
    @ApiModelProperty(value = "附件上传")
    private List<FileListVO> fileList;
}
