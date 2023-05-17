package io.renren.modules.financing.dto;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 直融信息审核
 */
@Data
@ApiModel(value = "直融信息审核")
public class FDirectFinancingCheckDTO {
    private Long id;

    private Long financingId;

    private Integer checked;

    private Integer groupChecked;

    private String groupCheckedDes;

    /**
     * 审核意见
     */
    private String checkedDes;

}
