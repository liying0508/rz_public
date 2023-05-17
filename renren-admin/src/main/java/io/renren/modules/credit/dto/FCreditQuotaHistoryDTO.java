package io.renren.modules.credit.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.renren.common.utils.DateUtils;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class FCreditQuotaHistoryDTO {

    @ApiModelProperty(value = "主键")
    private Long id;

    @ApiModelProperty(value = "授信总额id")
    private Long creditQuotaId;

    @ApiModelProperty("机构id")
    private Long institution;

    @ApiModelProperty(value = "直融授信总额")
    private BigDecimal creditQuotaDirect;

    @ApiModelProperty(value = "间融授信总额")
    private BigDecimal creditQuotaIndirect;

    @ApiModelProperty(value = "直融已使用额度")
    private BigDecimal usedQuotaDirect;

    @ApiModelProperty(value = "间融已使用额度")
    private BigDecimal usedQuotaIndirect;

    @ApiModelProperty(value = "创建时间")
    @JsonFormat(pattern = DateUtils.DATE_TIME_PATTERN)
    private Date createDate;
}
