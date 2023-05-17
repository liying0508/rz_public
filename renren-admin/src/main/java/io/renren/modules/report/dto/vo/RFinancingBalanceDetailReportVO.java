package io.renren.modules.report.dto.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.renren.common.utils.DateUtils;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

@Data
@ApiModel(value = "融资余额明细表VO")
@AllArgsConstructor
@NoArgsConstructor
public class RFinancingBalanceDetailReportVO {
    @ApiModelProperty(value = "融资种类名")
    private String financingTypeName;
    @ApiModelProperty(value = "融资主体名")
    private String deptName;
    @ApiModelProperty(value = "金融机构名")
    private String institutionName;
    @ApiModelProperty(value = "融资产品")
    private String financeProduct;
    @ApiModelProperty(value = "执行利率")
    private Double strikeRate;
    @ApiModelProperty(value = "期限/年")
    private Integer deadLine;

    @ApiModelProperty(value = "放款日")
    @JsonFormat(pattern = DateUtils.DATE_PATTERN)
    private Date loanDate;
    @ApiModelProperty(value = "到期日")
    @JsonFormat(pattern = DateUtils.DATE_PATTERN)
    private Date endDate;

    @ApiModelProperty(value = "贷款金额")
    private BigDecimal financingAmount;
    @ApiModelProperty(value = "贷款余额")
    private BigDecimal financingBalance;
    @ApiModelProperty(value = "增信措施")
    private String creditMeasures;
    @ApiModelProperty(value = "担保主体")
    private String guarantor;
    @ApiModelProperty(value = "担保金额")
    private BigDecimal contractAmount;
    @ApiModelProperty(value = "是否与被担保方存在产权关系")
    private Integer propertyRelations;
    @ApiModelProperty(value = "是否为被担保方超股比担保")
    private Integer shareRatio;
    @ApiModelProperty(value = "是否有反担保，注明反担保方式")
    private Integer counterGuarantee;
    @ApiModelProperty(value = "决策依据")
    private String decisionBasis;
    @ApiModelProperty(value = "备注")
    private String remarks;
}
