package io.renren.modules.financing.dto;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.renren.common.utils.DateUtils;
import io.renren.common.utils.TreeNode;
import io.renren.common.validator.group.AddGroup;
import io.renren.common.validator.group.DefaultGroup;
import io.renren.common.validator.group.UpdateGroup;
import io.renren.modules.sys.dto.SysDeptDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.Value;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 银行授信分页
 * @author liuwenyong
 * @since 3.0 2022-05-17
 */
@Data
@ApiModel(value = "银行授信分页")
public class FCreditPageDto extends TreeNode implements Serializable {

    @ApiModelProperty(value = "id")
    private Long id;
    @ApiModelProperty(value = "授信单位ID")
    private Long deptId;
    @ApiModelProperty(value = "金融机构id")
    private Long institution;
    @ApiModelProperty(value = "上级ID")
    private Long pid;
    @ApiModelProperty(value = "机构名称")
//    private String name;
    private String deptName;
    @ApiModelProperty(value = "授信单ID")
    private Long creditId;
    @ApiModelProperty(value = "金融机构")
    private String institutionName;
//    private String financeDeptName;
    @ApiModelProperty(value = "金融机构类型")
    private Integer institutionType;
    @ApiModelProperty(value = "额度")
    private BigDecimal quota;
    @ApiModelProperty(value = "间接融资已使用额度")
    private BigDecimal indirectQuota;
    @ApiModelProperty(value = "起始日")
    @JsonFormat(pattern = DateUtils.DATE_PATTERN)
    private Date startDate;
    @ApiModelProperty(value = "到期日")
    @JsonFormat(pattern = DateUtils.DATE_PATTERN)
    private Date endDate;
    @ApiModelProperty(value = "增信措施")
    private String creditMeasures;
    @ApiModelProperty(value = "直接融资")
    private Integer directQuota;
    @ApiModelProperty(value = "未使用额度")
    private BigDecimal unUsedQuota;
    @ApiModelProperty(value = "审核状态")
    private Integer isChecked;
    @ApiModelProperty(value = "审核意见")
    private String checkedDes;
    @ApiModelProperty(value = "集团审核状态")
    private Integer groupChecked;
    @ApiModelProperty(value = "集团审核意见")
    private String groupCheckedDes;
}
