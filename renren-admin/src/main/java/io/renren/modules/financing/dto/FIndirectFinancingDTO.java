package io.renren.modules.financing.dto;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.renren.common.utils.DateUtils;
import io.renren.modules.financing.vo.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
* 间接融资信息
*
* @author liuwenyong 
* @since 3.0 2022-05-14
*/
@Data
@ApiModel(value = "间接融资信息")
public class FIndirectFinancingDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    private Long id;
//    @ApiModelProperty(value = "授信单ID")
//    private Long creditId;

    @ApiModelProperty(value = "被授信单位ID")
    private Long deptId;
    @ApiModelProperty(value = "被授信单位名")
    private String deptName;
    @ApiModelProperty(value = "金融机构类型")
    private Integer institutionType;
    @ApiModelProperty(value = "金融机构主键")
    private Long institution;
    @ApiModelProperty(value = "金融机构名")
    private String institutionName;

    @ApiModelProperty(value = "融资品种")
    private Integer varieties;

    @ApiModelProperty(value = "保证金比例")
    private Integer marginRatio;
    @ApiModelProperty(value = "贴现率")
    private Integer discountRate;
    @ApiModelProperty(value = "利率类型")
    private String interestRatesWay;
    @ApiModelProperty(value = "浮动利率周期")
    private Integer lprCycle;
    @ApiModelProperty(value = "利率")
    private String rate;
    @ApiModelProperty(value = "利率")
    private String floatRate;
    @ApiModelProperty(value = "合同编号")
    private String contractNo;
    @ApiModelProperty(value = "合同金额")
    private BigDecimal contractAmount;
    @ApiModelProperty(value = "开票金额")
    private BigDecimal invoiceAmount;
    @ApiModelProperty(value = "提款金额")
    private BigDecimal withdrawalAmount;

    @ApiModelProperty(value = "付息方式")
    private String interestMethod;
    @ApiModelProperty(value = "付本方式")
    private String principalMethod;
    @ApiModelProperty(value = "放款日期")
    @JsonFormat(pattern = DateUtils.DATE_PATTERN)
    private Date loanDate;
    @ApiModelProperty(value = "还款日期")
    @JsonFormat(pattern = DateUtils.DATE_PATTERN)
    private Date repaymentDate;
    @ApiModelProperty(value = "期限")
    private Long deadLine;
    @ApiModelProperty(value = "实际还款金额")
    private BigDecimal actualRepayment;
    @ApiModelProperty(value = "融资余额")
    private BigDecimal financingBalance;
    @ApiModelProperty(value = "未提金额")
    private BigDecimal availableBalance;
    @ApiModelProperty(value = "其他费用")
    private BigDecimal otherExpenses;
    @ApiModelProperty(value = "其他费用备注")
    private String otherExpensesDesc;
    @ApiModelProperty(value = "用途")
    private String purpose;
    @ApiModelProperty(value = "是否为还本付息")
    private Integer isPayPrincipalAndInterest;

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

    @ApiModelProperty(value = "决策依据")
    private String decisionBasis;

    @ApiModelProperty(value = "还款计划")
    private String repaymentPlan;
    @ApiModelProperty(value = "还款计划列表")
    private PayPlanDataVO payPlanData;

    @ApiModelProperty(value = "实际还款")
    private String repaymentActual;
    @ApiModelProperty(value = "实际还款列表")
    private PayActualDataVO payActualData;

    @ApiModelProperty(value = "备注")
    private String remarks;

    @ApiModelProperty(value = "创建者")
    private Long creator;
    @ApiModelProperty(value = "创建时间")
    private Date createDate;

    @ApiModelProperty(value = "审核状态")
    private Integer isChecked;
    @ApiModelProperty(value = "审核意见")
    private String checkedDes;
    @ApiModelProperty(value = "集团审核状态")
    private Integer groupChecked;
    @ApiModelProperty(value = "集团审核意见")
    private String groupCheckedDes;
    @ApiModelProperty(value = "中介机构列表")
    private List<IntermediaryStructureVo> intermediaryStructureList;
    @ApiModelProperty(value = "中介机构")
    private String intermediaryStructure;
    @ApiModelProperty(value = "投资人列表")
    private List<InvestorVO> investorList;
    @ApiModelProperty(value = "投资人")
    private String investor;

    @ApiModelProperty(value = "项目描述")
    private String project;

    @ApiModelProperty(value = "授信额度")
    private BigDecimal quota;
    @ApiModelProperty(value = "同机构和融资id的单位已使用的额度")
    private BigDecimal usedQuota;
    @ApiModelProperty(value = "附件上传")
    private String files;
    @ApiModelProperty(value = "附件上传")
    private List<FileListVO> fileList;
    @ApiModelProperty(value = "一年期还是五年期利率")
    private Integer lprInterestCycle;
    @ApiModelProperty(value = "单位，1为元，2为万元")
    private Integer unit;
//    private
    @ApiModelProperty(value = "还款计划的id")
    private Long repaymentPlanId;
}