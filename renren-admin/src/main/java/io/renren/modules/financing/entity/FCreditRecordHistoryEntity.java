package io.renren.modules.financing.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.renren.common.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @Author: Eddy
 * @Date: 2022/9/26
 * @Time: 9:47
 * @Description: 银行授信历史记录
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("f_credit_record_history")
public class FCreditRecordHistoryEntity extends BaseEntity {
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
}