package io.renren.modules.financing.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @Author：LiaoZX
 * @Date：2022/6/7
 * @Description:
 */
@Data
@ApiModel(value = "保证信息")
public class GuaranteeVO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "保证人ID")
    private Long id;
    @ApiModelProperty(value = "保证人")
    private String guarantor;
    @ApiModelProperty(value = "合同比例")
    private String ratio;
    @ApiModelProperty(value = "金额")
    private Integer contractAmount;
    @ApiModelProperty(value = "是否与被担保方存在产权关系")
    private Integer propertyRelations;
    @ApiModelProperty(value = "是否为被担保方超股比担保")
    private Integer shareRatio;
    @ApiModelProperty(value = "是否有反担保，注明反担保方式")
    private Integer counterGuarantee;
}
