package io.renren.modules.financing.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.renren.common.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

@Data
@EqualsAndHashCode(callSuper = false)
@TableName("f_indirect_financing_check")
public class FIndirectFinancingCheck extends BaseEntity implements Serializable {
    private Long indirectId;

    private Integer checked;
    /**
     * 审核意见
     */
    private String checkedDes;

    private Integer groupChecked;

    private String groupCheckedDes;


    private static final long serialVersionUID = 1L;
}
