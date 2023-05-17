package io.renren.modules.financing.dto;

import io.renren.modules.financing.vo.RepaymentInfoVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "还款信息")
public class FRepaymentInfoDTO implements Serializable {
    private static final long serialVersionUID = 1L;
    @ApiModelProperty(value = "主键")
    private Long id;
    @ApiModelProperty(value = "本金")
    private BigDecimal principal;
    @ApiModelProperty(value = "总利息")
    private BigDecimal interest;
    @ApiModelProperty(value = "实际还款")
    private BigDecimal actualRepayment;
    @ApiModelProperty(value = "总还款额")
    private BigDecimal repaymentMoney;
    @ApiModelProperty(value = "还款信息列表")
    private RepaymentInfoVO repaymentInfo;
    @ApiModelProperty(value = "融资单id")
    private Long financingId;
    @ApiModelProperty(value = "融资类型：0政府债券，1直接融资，2间接融资")
    private Integer financingType;
    @ApiModelProperty(value = "审核状态")
    private Integer isChecked;
    @ApiModelProperty(value = "审核意见")
    private String checkedDes;
    @ApiModelProperty(value = "集团审核状态")
    private Integer groupChecked;
    @ApiModelProperty(value = "集团审核意见")
    private String groupCheckedDes;
    @ApiModelProperty(value = "单位")
    private Integer unit;
}
