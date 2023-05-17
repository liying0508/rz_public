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
@ApiModel(value = "中介机构(新)")
public class IntermediaryStructureVo implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "机构类型值")
    private String intermediaryValue;
    @ApiModelProperty(value = "机构类型文本")
    private String intermediaryLabel;
    @ApiModelProperty(value = "机构名称")
    private String orgName;
    @ApiModelProperty(value = "中介费用")
    private BigDecimal amount;

}
