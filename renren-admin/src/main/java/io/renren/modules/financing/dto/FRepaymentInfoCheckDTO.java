package io.renren.modules.financing.dto;

import io.swagger.annotations.ApiModel;
import lombok.Data;

/**
 * 直融信息审核
 */
@Data
@ApiModel(value = "还款信息审核")
public class FRepaymentInfoCheckDTO {
    private Long id;

    private Long repaymentId;

    private Integer checked;

    /**
     * 审核意见
     */
    private String checkedDes;

    private Integer groupChecked;

    private String groupCheckedDes;
}
