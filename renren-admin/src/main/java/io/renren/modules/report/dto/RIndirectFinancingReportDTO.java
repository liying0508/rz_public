package io.renren.modules.report.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.renren.common.utils.DateUtils;
import io.renren.modules.report.dto.vo.RIndirectFinancingReportVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @Author: Eddy
 * @Date: 2022/12/28 0028
 * @Time: 17:38
 * @Description: 银行间接融资明细表DTO
 */
@Data
@ApiModel(value = "银行间接融资明细表DTO")
public class RIndirectFinancingReportDTO extends RIndirectFinancingReportVO {
    @ApiModelProperty(value = "主键")
    private Long id;
    @ApiModelProperty(value = "支行主键")
    private Long institution;
    @ApiModelProperty(value = "银行机构主键")
    private Long institutionPId;
    @ApiModelProperty(value = "融资主体主键")
    private Long deptId;
    @ApiModelProperty(value = "融资品种->产品名称")
    private Integer varieties;

    //以下都是用来计算利率的参数（还有父类中的loanDate）
    @ApiModelProperty(value = "利率类型")
    private String interestRatesWay;
    @ApiModelProperty(value = "一年期还是五年期利率")
    private Integer lprInterestCycle;
    @ApiModelProperty(value = "利率")
    private String floatRate;


}
