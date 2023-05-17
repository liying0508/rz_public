package io.renren.modules.financing.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.renren.common.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;

/**
 * f_direct_financing_approval_check
 * @author 
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("f_direct_financing_approval_check")
public class FDirectFinancingApprovalCheck extends BaseEntity implements Serializable  {


    private Long approvalId;

    private Integer checked;
    /**
     * 审核意见
     */
    private String checkedDes;

    private Integer groupChecked;

    private String groupCheckedDes;

    private static final long serialVersionUID = 1L;
}