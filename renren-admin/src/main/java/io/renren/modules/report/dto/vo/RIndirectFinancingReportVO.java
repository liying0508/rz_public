package io.renren.modules.report.dto.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.renren.common.utils.DateUtils;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @Author: Eddy
 * @Date: 2022/12/28 0028
 * @Time: 16:54
 * @Description: 间接融资报表的VO
 */
@Data
@ApiModel(value = "银行间接融资明细表")
public class RIndirectFinancingReportVO {

    @ApiModelProperty(value = "银行机构名称")
    private String institutionName;

    @ApiModelProperty(value = "支行名称")
    private String institutionBranchName;

    @ApiModelProperty(value = "融资主体")
    private String deptName;

    @ApiModelProperty(value = "产品名称")
    private Integer productName;

    @ApiModelProperty(value = "执行利率")
    private Double rate;

    @ApiModelProperty(value = "期限（年）")
    private Integer deadLine;

    @ApiModelProperty(value = "放款日期")
    @JsonFormat(pattern = DateUtils.DATE_PATTERN)
    private Date loanDate;

    @ApiModelProperty(value = "还款日期 即是 到期日")
    @JsonFormat(pattern = DateUtils.DATE_PATTERN)
    private Date repaymentDate;

    @ApiModelProperty(value = "融资金额")
    private BigDecimal financingAmount;

    @ApiModelProperty(value = "融资余额")
    private BigDecimal financingBalanceAmount;

    @ApiModelProperty(value = "增信措施")
    private String creditMeasures;

    @ApiModelProperty(value = "备注")
    private String remark;
}
