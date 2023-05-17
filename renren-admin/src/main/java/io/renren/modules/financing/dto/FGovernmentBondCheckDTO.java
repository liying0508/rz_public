package io.renren.modules.financing.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "政府债券审核")
public class FGovernmentBondCheckDTO {
    private Long id;

    private Long bondId;

    private Integer checked;

    private Integer groupChecked;

    private String groupCheckedDes;
    /**
     * 审核意见
     */
    private String checkedDes;
}
