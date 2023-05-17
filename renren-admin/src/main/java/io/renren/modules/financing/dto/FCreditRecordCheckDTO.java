package io.renren.modules.financing.dto;

import io.swagger.annotations.ApiModel;
import lombok.Data;

/**
 * 银行授信审核
 */
@Data
@ApiModel(value = "银行授信审核")
public class FCreditRecordCheckDTO {
    private Long id;

    private Long creditId;

    private Integer checked;

    private Integer groupChecked;

    private String groupCheckedDes;

    /**
     * 审核意见
     */
    private String checkedDes;
}
