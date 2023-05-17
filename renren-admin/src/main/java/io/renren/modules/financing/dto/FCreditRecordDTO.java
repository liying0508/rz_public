package io.renren.modules.financing.dto;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.renren.common.utils.DateUtils;
import io.renren.modules.financing.vo.CreditMeasuresVO;
import io.renren.modules.financing.vo.FileListVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
* 银行授信
*
* @author liuwenyong 
* @since 3.0 2022-05-14
*/
@Data
@ApiModel(value = "银行授信")
public class FCreditRecordDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long id;
    @ApiModelProperty(value = "被授信单位ID")
    private Long deptId;
    @ApiModelProperty(value = "被授信单位名")
    private String deptName;
//    @ApiModelProperty(value = "金融机构类型")
//    private Integer financeDeptType;
//    @ApiModelProperty(value = "金融机构主键")
//    private Long financeDeptId;

    //6-9修改
    @ApiModelProperty(value = "金融机构类型")
    private Integer institutionType;
    @ApiModelProperty(value = "金融机构主键")
    private Long institution;
    @ApiModelProperty(value = "金融机构名")
    private String institutionName;

    @ApiModelProperty(value = "额度")
    private BigDecimal quota;
    @ApiModelProperty(value = "间接融资额度")
    private BigDecimal indirectQuota;

    @ApiModelProperty(value = "起始日")
    @JsonFormat(pattern = DateUtils.DATE_PATTERN)
    private Date startDate;
    @ApiModelProperty(value = "到期日")
    @JsonFormat(pattern = DateUtils.DATE_PATTERN)
    private Date endDate;

    @ApiModelProperty(value = "用途")
    private String purpose;
    @ApiModelProperty(value = "增信措施")
    private String creditMeasures;
    @ApiModelProperty(value = "增信措施集合")
    private List<String> creditMeasuresList;
    @ApiModelProperty(value = "保证")
    private String guarantee;
    @ApiModelProperty(value = "抵押")
    private String collateral;
    @ApiModelProperty(value = "质押")
    private String pledge;
    @ApiModelProperty(value = "备注")
    private String remarks;
    @ApiModelProperty(value = "审核状态")
    private Integer isChecked;
    @ApiModelProperty(value = "增信措施详情")
    @JSONField(name = "guaranteeInfo")
    private CreditMeasuresVO guaranteeInfo;
    @ApiModelProperty(value = "附件上传")
    private String files;
    @ApiModelProperty(value = "附件上传")
    private List<FileListVO> fileList;
    @ApiModelProperty(value = "单位")
    private Integer unit;
}