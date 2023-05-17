package io.renren.modules.financing.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@ApiModel(value = "还款信息分页")
public class FRepaymentInfoPageDTO implements Serializable {
    private static final long serialVersionUID = 1L;
    @ApiModelProperty(value = "id")
    private Long id;
    @ApiModelProperty(value = "融资被授信单位id")
    private Long deptId;
    @ApiModelProperty(value = "融资种类")
    private Integer varieties;
    @ApiModelProperty(value = "批文号")
    private String approveNo;
    @ApiModelProperty(value = "融资/被授信单位名称")
    private String deptName;
    @ApiModelProperty(value = "融资总金额")
    private BigDecimal totalAmount;
    @ApiModelProperty(value = "金融机构名称")
    private String institutionName;
    @ApiModelProperty(value = "已还本金")
    private String principalRepay;
    @ApiModelProperty(value = "未还本金")
    private String outstandingPrincipal;
    @ApiModelProperty(value = "已付利息")
    private String interestRepay;
    @ApiModelProperty(value = "融资单id")
    private Long financingId;
    @ApiModelProperty(value = "融资类型：0政府债券，1直接融资，2间接融资")
    private Integer financingType;
    @ApiModelProperty(value = "审核状态")
    private Integer isChecked;
    @ApiModelProperty(value = "集团审核状态")
    private Integer groupChecked;
}
