package io.renren.modules.financing.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.renren.common.entity.BaseEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * f_direct_financing_approval
 * @author 
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("f_direct_financing_approval")
public class FDirectFinancingApproval extends BaseEntity implements Serializable  {

    /**
     * 融资单位ID
     */
    private Long deptId;

    /**
     * 融资单位名
     */
    private String deptName;
    /**
     * 融资种类
     */
    private String varieties;

    /**
     * 审批单位
     */
    private String approveOrg;

    /**
     * 批文号
     */
    private String approveNo;

    /**
     * 币种
     */
    private String currency;

    /**
     * 审批金额
     */
    private BigDecimal issueQuota;

    private Integer isChecked;

    /**
     * 审核意见
     */
    private String checkedDes;

    private String files;

    private static final long serialVersionUID = 1L;

    private Integer groupChecked;

    private String groupCheckedDes;
}