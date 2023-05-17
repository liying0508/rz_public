package io.renren.modules.financing.dto;

import com.alibaba.fastjson.annotation.JSONField;
import io.renren.modules.financing.vo.FileListVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * @Author gaby
 * @Date 2022/6/27 18:21
 * @Description
 * @Version 1.0
 * @ClassName FDirectFinancingApprovalDTO
 **/
@Data
@ApiModel(value = "直融批文")
public class FDirectFinancingApprovalDTO {
    private static final long serialVersionUID = 1L;

    private Long id;

    @ApiModelProperty(value = "融资单位ID")
    private Long deptId;

    @ApiModelProperty(value = "融资品种")
    private String varieties;

    @ApiModelProperty(value = "融资单位名")
    private String deptName;

    @ApiModelProperty(value = "审批单位")
    private String approveOrg;

    @ApiModelProperty(value = "批文号")
    private String approveNo;

    @ApiModelProperty(value = "币种")
    private String currency;

    @ApiModelProperty(value = "审批金额")
    private BigDecimal issueQuota;

    @ApiModelProperty(value = "审核")
    private Integer isChecked;

    @ApiModelProperty(value = "发行总额")
    private Double allUseQuota;

    @ApiModelProperty(value = "发行次数")
    private Integer issue;

    /**
     * 审核意见
     */
    private String checkedDes;
    @ApiModelProperty(value = "附件上传")
    private String files;
    @ApiModelProperty(value = "附件上传列表")
    private List<FileListVO> fileList;
    @ApiModelProperty(value = "单位")
    private Integer unit;

    @ApiModelProperty(value = "集团审核状态")
    private Integer groupChecked;
    @ApiModelProperty(value = "集团审核意见")
    private String groupCheckedDes;
}
