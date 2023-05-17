package io.renren.modules.financing.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
* 担保信息
*
* @author liuwenyong 
* @since 3.0 2022-05-14
*/
@Data
@ApiModel(value = "担保信息")
public class FGuaranteeInfoDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long id;
    @ApiModelProperty(value = "授信单ID")
    private Long creditId;
    @ApiModelProperty(value = "信用")
    private String creditText;
    @ApiModelProperty(value = "保证人")
    private String guarantor;
    @ApiModelProperty(value = "比例")
    private Integer ratio;
    @ApiModelProperty(value = "合同金额")
    private Integer contractAmount;
    @ApiModelProperty(value = "抵押物名称")
    private String collateralName;
    @ApiModelProperty(value = "抵押物评估值")
    private Integer collateralAmount;
    @ApiModelProperty(value = "抵押合同")
    private String collateralContract;
    @ApiModelProperty(value = "解押时间")
    private Date releaseTime;
    @ApiModelProperty(value = "质押标的")
    private String pledgeType;
    @ApiModelProperty(value = "质押人")
    private String pledgeName;
    @ApiModelProperty(value = "质押价值")
    private Integer pledgeAmount;
    @ApiModelProperty(value = "备注")
    private String remarks;
    @ApiModelProperty(value = "创建者")
    private Long creator;
    @ApiModelProperty(value = "创建时间")
    private Date createDate;

}