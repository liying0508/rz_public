package io.renren.modules.credit.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.renren.common.utils.DateUtils;
import io.renren.common.validator.group.DefaultGroup;
import io.renren.modules.financing.vo.FileListVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Data
@ApiModel(value = "授信总额DTO")
public class FCreditQuotaDTO {

    @ApiModelProperty(value = "id")
    private Long id;

    @NotNull(message = "机构名称不能为空",groups = DefaultGroup.class)
    @ApiModelProperty("机构id")
    private Long institution;
    //授信总额
    @ApiModelProperty("机构名称")
    private String institutionName;

    @ApiModelProperty(value = "直融授信总额")
    private BigDecimal creditQuotaDirect;

    @ApiModelProperty(value = "间融授信总额")
    private BigDecimal creditQuotaIndirect;

    @ApiModelProperty(value = "直融已使用额度")
    private BigDecimal usedQuotaDirect;

    @ApiModelProperty(value = "间融已使用额度")
    private BigDecimal usedQuotaIndirect;

    @ApiModelProperty(value = "直融未使用额度")
    private BigDecimal unusedQuotaDirect;

    @ApiModelProperty(value = "间融未使用额度")
    private BigDecimal unusedQuotaIndirect;

    @ApiModelProperty(value = "附件上传")
    private String files;
    @ApiModelProperty(value = "附件上传")
    private List<FileListVO> fileList;
}
