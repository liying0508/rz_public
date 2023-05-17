package io.renren.modules.financing.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @Author：LiaoZX
 * @Date：2022/6/7
 * @Description:
 */
@Data
@ApiModel(value = "质押信息")
public class PledgeVO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "质押标的")
    private String pledgeType;
    @ApiModelProperty(value = "质押权人")
    private String pledgeName;
    @ApiModelProperty(value = "质押金额")
    private BigDecimal pledgeAmount;
    @ApiModelProperty(value = "质押起止日期")
    private List<Date> releaseTime;
}
