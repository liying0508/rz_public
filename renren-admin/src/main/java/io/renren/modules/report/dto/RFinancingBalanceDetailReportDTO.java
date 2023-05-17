package io.renren.modules.report.dto;

import io.renren.modules.report.dto.vo.RFinancingBalanceDetailReportVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "融资余额明细表DTO")
public class RFinancingBalanceDetailReportDTO extends RFinancingBalanceDetailReportVO {
    @ApiModelProperty(value = "融资主体id")
    private Long deptId;

    @ApiModelProperty(value = "金融机构Id")
    private Long institution;

    @ApiModelProperty(value = "融资品种")
    private String varieties;

    @ApiModelProperty(value = "直融批文")
    private String approvalNo;
}
