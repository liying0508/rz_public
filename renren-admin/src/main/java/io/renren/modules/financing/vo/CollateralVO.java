package io.renren.modules.financing.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @Author：LiaoZX
 * @Date：2022/6/7
 * @Description:
 */
@Data
@ApiModel(value = "抵押信息")
public class CollateralVO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "抵押物信息ID")
    private Long id;
    @ApiModelProperty(value = "抵押物名")
    private String collateralName;
    @ApiModelProperty(value = "抵押物估值")
    private Integer collateralAmount;
    @ApiModelProperty(value = "抵押物所有人")
    private String mortgager;
    @ApiModelProperty(value = "抵押起止日期")
    private List<Date> releaseTime;
}
