package io.renren.modules.financing.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "间融信息审核")
public class FIndirectFinancingCheckDTO {
    private Long id;

    private Long indirectId;

    private Integer checked;

    private Integer groupChecked;

    private String groupCheckedDes;

    /**
     * 审核意见
     */
    private String checkedDes;
}
