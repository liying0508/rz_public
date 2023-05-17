package io.renren.modules.report.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.renren.common.utils.DateUtils;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
@ApiModel(value = "融资余额表DTO")
public class RFinancingBalanceDTO {

    @ApiModelProperty(value = "融资品种")
    private Integer varieties;
    @ApiModelProperty(value = "用款/融资主体Id")
    private Long deptId;
    @ApiModelProperty(value = "用款/融资主体名")
    private String deptName;
    @ApiModelProperty(value = "金融机构id")
    private Long institution;
    @ApiModelProperty(value = "金融机构名称")
    private String institutionName;
    @ApiModelProperty(value = "授信项目")
    private String project;
    @ApiModelProperty(value = "LPR利率（%）")
    private Double lprRate;
    @ApiModelProperty(value = "上浮利率（%）")
    private Double floatRate;
    @ApiModelProperty(value = "固定利率（%）")
    private Double rate;
    @ApiModelProperty(value = "放款时间")
    @JsonFormat(pattern = DateUtils.DATE_PATTERN)
    private Date loanDate;
    @ApiModelProperty(value = "期限/月")
    private Integer deadLine;
    @ApiModelProperty(value = "到期日")
    @JsonFormat(pattern = DateUtils.DATE_PATTERN)
    private Date expireDate;
    @ApiModelProperty(value = "贷款余额")
    private BigDecimal financingBalance;

    @ApiModelProperty(value = "增信内容")
    private String creditMeasures;
    @ApiModelProperty(value = "保证")
    private String guarantee;
    @ApiModelProperty(value = "抵押")
    private String collateral;
    @ApiModelProperty(value = "质押")
    private String pledge;
    @ApiModelProperty(value = "担保主体")
    private String guaranteeBody;

    @ApiModelProperty(value = "融资类型(1 直融 2 间融 3 政府债券)")
    private Integer financingType;
    @ApiModelProperty(value = "利率类型（0固定利率 1lpr利率）")
    private Integer interestRatesWay;
    @ApiModelProperty(value = "利息周期（1一年期 5五年期）")
    private Integer lprInterestCycle;
    @ApiModelProperty(value = "一年期或五年期")
    private String lprStr;

    @ApiModelProperty(value = "备注")
    private String remarks;

}
