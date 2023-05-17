package io.renren.modules.financing.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @Author：LiaoZX
 * @Date：2022/6/7
 * @Description:
 */
@Data
@ApiModel(value = "间接融资信息(新)")
public class FIndirectFinancingVO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "编号")
    private Long id;

    @ApiModelProperty(value = "融资单位ID")
    private Long deptId;
    @ApiModelProperty(value = "融资单位")
    private String deptName;

    @ApiModelProperty(value = "金融机构：1银行机构 2非银机构")
    private Integer institutionType;
    @ApiModelProperty(value = "金融机构")
    private String institution;

    @ApiModelProperty(value = "融资品种")
    private Integer varieties;
    @ApiModelProperty(value = "保证金比例")
    private Integer marginRatio;
    @ApiModelProperty(value = "贴现率")
    private Integer discountRate;

    @ApiModelProperty(value = "利率类型")
    private Integer interestRatesWay;

    @ApiModelProperty(value = "浮动利率周期")
    private Integer lprCycle;
    @ApiModelProperty(value = "利率")
    private Integer rate;


    @ApiModelProperty(value = "合同编号")
    private String contractNo;
    @ApiModelProperty(value = "合同金额")
    private Integer contractAmount;
    @ApiModelProperty(value = "开票金额")
    private Integer invoiceAmount;
    @ApiModelProperty(value = "提款金额")
    private Integer withdrawalAmount;

    @ApiModelProperty(value = "付息方式")
    private String interestMethod;
    @ApiModelProperty(value = "放款日期")
    private Date loanDate;
    @ApiModelProperty(value = "还款日期")
    private Date repaymentDate;

    @ApiModelProperty(value = "可用余额")
    private Integer availableBalance;

    @ApiModelProperty(value = "其他费用")
    private String otherExpenses;
    @ApiModelProperty(value = "其他费用描述")
    private String otherExpensesDesc;
    @ApiModelProperty(value = "用途")
    private String purpose;

    @ApiModelProperty(value = "增信措施")
    private List<String> creditMeasures;
    @ApiModelProperty(value = "增信措施详情")
    private CreditMeasuresVO guaranteeInfo;

    @ApiModelProperty(value = "决策依据")
    private String decisionBasis;

    @ApiModelProperty(value = "附件")
    private List<FileInfoVO> fileList;

    @ApiModelProperty(value = "备注")
    private String remarks;
    @ApiModelProperty(value = "创建者")
    private Long creator;
    @ApiModelProperty(value = "创建时间")
    private Date createDate;

}