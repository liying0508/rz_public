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
@ApiModel(value = "直接融资信息(新)")
public class FDirectFinancingVO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    private Long Long;

    @ApiModelProperty(value = "批文号")
    private String approveNo;

    @ApiModelProperty(value = "已发行期次")
    private Integer issue;

    @ApiModelProperty(value = "融资单位ID")
    private Long deptId;
    @ApiModelProperty(value = "融资单位")
    private String deptName;
    @ApiModelProperty(value = "融资品种")
    private Integer varieties;

    @ApiModelProperty(value = "付息周期")
    private Integer serviceCycle;
    @ApiModelProperty(value = "付息方式")
    private String interestMethod;

    @ApiModelProperty(value = "利率")
    private Float rate;

    @ApiModelProperty(value = "币种")
    private String currency;
    @ApiModelProperty(value = "发行金额")
    private Integer foreignCurrencyAmount;
    @ApiModelProperty(value = "汇率")
    private Float exchangeRate;
    @ApiModelProperty(value = "折合人民币")
    private Integer issueQuota;


    @ApiModelProperty(value = "发行日")
    private Date issueDate;
    @ApiModelProperty(value = "起息日")
    private Date valueDate;
    @ApiModelProperty(value = "到期日")
    private Date dueDate;

    @ApiModelProperty(value = "融资余额")
    private Integer financingBalance;

    @ApiModelProperty(value = "用途")
    private String purpose;

    @ApiModelProperty(value = "其它费用")
    private Integer otherExpenses;
    @ApiModelProperty(value = "其它费用备注")
    private String otherExpensesDesc;

    @ApiModelProperty(value = "增信措施")
    private List<String> creditMeasures;
    @ApiModelProperty(value = "增信措施详情")
    private CreditMeasuresVO guaranteeInfo;

    @ApiModelProperty(value = "中介机构集合")
    private List<IntermediaryStructureVo> intermediaryStructure;
    @ApiModelProperty(value = "投资人集合")
    private List<InvestorVO> investor;
    @ApiModelProperty(value = "附件集合")
    private List<FileInfoVO> fileList;

}
