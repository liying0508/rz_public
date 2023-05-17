package io.renren.modules.credit.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.renren.common.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.util.Date;

@Data
@EqualsAndHashCode(callSuper=false)
@TableName("f_credit_quota")
public class FCreditQuotaEntity extends BaseEntity {
    private static final long serialVersionUID = 1L;
    //授信机构id
    private Long institution;
    //直融授信总额
    private BigDecimal creditQuotaDirect;
    //间融授信总额
    private BigDecimal creditQuotaIndirect;
    //直融已使用额度
    private BigDecimal usedQuotaDirect;
    //间融已使用额度
    private BigDecimal usedQuotaIndirect;
    //文件上传列表json字符串
    private String files;
}
