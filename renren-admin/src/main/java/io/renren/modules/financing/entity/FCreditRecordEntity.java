package io.renren.modules.financing.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import com.baomidou.mybatisplus.annotation.*;

import java.math.BigDecimal;
import java.util.Date;

import io.renren.common.entity.BaseEntity;

/**
 * 银行授信
 *
 * @author liuwenyong
 * @since 3.0 2022-05-14
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("f_credit_record")
public class FCreditRecordEntity extends BaseEntity {
    private static final long serialVersionUID = 1L;
    /**
     * 主键
     */
    private Long id;
    /**
     * 组织机构主键
     */
    private Long deptId;
    /**
     * 金融机构主键
     */
    private Long institution;
    /**
     * 额度
     */
    private BigDecimal quota;
    /**
     * 间接融资余额
     */
    private BigDecimal indirectQuota;
    /**
     * 起始日
     */
    private Date startDate;
    /**
     * 到期日
     */
    private Date endDate;
    /**
     * 用途
     */
    private String purpose;
    /**
     * 增信措施
     */
    private String creditMeasures;
    /**
     * 备注
     */
    private String remarks;

    private String guarantee;

    private String collateral;

    private String pledge;

    private String files;

    private Integer isChecked;

    private String checkedDes;

    private Integer groupChecked;

    private String groupCheckedDes;
}