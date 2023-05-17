package io.renren.modules.financing.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @Author：LiaoZX
 * @Date：2022/6/7
 * @Description:
 */
@Data
@ApiModel(value = "投资人(新)")
public class InvestorVO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "唯一键")
    private Long id;
    @ApiModelProperty(value = "投资人")
    private String investorName;
    @ApiModelProperty(value = "投资金额")
    private BigDecimal investorAmount;
    @ApiModelProperty(value = "备注")
    private String remarks;
}
