package io.renren.modules.financing.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author gaby
 * @Date 2022/6/27 18:21
 * @Description
 * @Version 1.0
 * @ClassName FDirectFinancingApprovalDTO
 **/
@Data
@ApiModel(value = "直融批文审核")
public class FDirectFinancingApprovalCheckDTO {

    private Long id;

    private Long approvalId;

    private Integer checked;

    private Integer groupChecked;

    private String groupCheckedDes;

    /**
     * 审核意见
     */
    private String checkedDes;
}
