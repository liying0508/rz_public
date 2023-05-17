package io.renren.modules.financing.dto;

import com.alibaba.fastjson.annotation.JSONField;
import io.renren.modules.financing.vo.CreditMeasuresVO;
import io.renren.modules.financing.vo.FileListVO;
import io.renren.modules.financing.vo.PayActualDataVO;
import io.renren.modules.financing.vo.PayPlanDataVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

@Data
@ApiModel(value = "政府债券审核信息")
public class FGovernmentBondAuditDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long id;
    @ApiModelProperty(value = "授信单ID")
    private Long creditId;
    @ApiModelProperty(value = "用款主体名称")
    private String deptName;
    @ApiModelProperty(value = "融资主体id")
    private Long institution;
    @ApiModelProperty(value = "融资主体名字")
    private String institutionName;
    @ApiModelProperty(value = "融资品种")
    private Integer varieties;
    @ApiModelProperty(value = "融资金额")
    private BigDecimal financingAmount;
    @ApiModelProperty(value = "利率")
    private double rate;
    @ApiModelProperty(value = "起息日")
    private String loanDate;
    @ApiModelProperty(value = "到期日")
    private String repaymentDate;
    @ApiModelProperty(value = "实际还款金额")
    private BigDecimal actualRepayment;
    @ApiModelProperty(value = "融资余额")
    private BigDecimal financingBalance;
    @ApiModelProperty(value = "项目")
    private String project;
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
    @ApiModelProperty(value = "付息方式")
    private String interestMethod;
    @ApiModelProperty(value = "付本方式")
    private String principalMethod;
    @ApiModelProperty(value = "是否为还本付息")
    private Integer isPayPrincipalAndInterest;

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
    @ApiModelProperty(value = "审核状态")
    private Integer isChecked;
    @ApiModelProperty(value = "审核意见")
    private String checkedDes;
    @ApiModelProperty(value = "集团审核状态")
    private Integer groupChecked;
    @ApiModelProperty(value = "集团审核意见")
    private String groupCheckedDes;
    @ApiModelProperty(value = "附件上传")
    private String files;
    @ApiModelProperty(value = "附件上传")
    private List<FileListVO> fileList;
}
