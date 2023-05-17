package io.renren.modules.financing.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.renren.common.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
@TableName("f_dict")
public class FDictEntity extends BaseEntity {
    private static final long serialVersionUID = 1L;
    //主键
    private Long id;
    //字段名
    private String dictName;
    //字段键
    private String dictKey;
    //字段值
    private String dictValue;
    //模块名
    private String dictModule;
}
