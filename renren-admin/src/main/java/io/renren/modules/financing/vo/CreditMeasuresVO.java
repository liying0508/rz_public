package io.renren.modules.financing.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @Author：LiaoZX
 * @Date：2022/6/7
 * @Description:
 */
@Data
@ApiModel(value = "增信措施详情")
public class CreditMeasuresVO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "增信措施详情ID")
    private Long id;
    @ApiModelProperty(value = "保证")
    private List<GuaranteeVO> guarantee;
    @ApiModelProperty(value = "抵押")
    private List<CollateralVO> collateral;
    @ApiModelProperty(value = "质押")
    private List<PledgeVO> pledge;


}
