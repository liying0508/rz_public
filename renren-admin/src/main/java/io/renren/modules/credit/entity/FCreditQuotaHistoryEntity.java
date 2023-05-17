package io.renren.modules.credit.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.renren.common.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

@Data
@EqualsAndHashCode(callSuper=false)
@TableName("f_credit_quota_history")
public class FCreditQuotaHistoryEntity extends BaseEntity {

    //授信总额id
    private Long creditQuotaId;
    //金融机构
    private Long institution;

    //直融授信总额
    private BigDecimal creditQuotaDirect;

    //间融授信总额
    private BigDecimal creditQuotaIndirect;

    //直融已使用额度
    private BigDecimal usedQuotaDirect;

    //间融已使用额度
    private BigDecimal usedQuotaIndirect;
}
