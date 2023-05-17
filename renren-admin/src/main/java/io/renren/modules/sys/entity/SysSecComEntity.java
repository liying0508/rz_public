package io.renren.modules.sys.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.renren.common.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
@TableName("sys_sec_com")
public class SysSecComEntity extends BaseEntity {

    private static final long serialVersionUID = 1L;

    //证券公司名字
    private String secComName;

    //辖区
    private String area;

}
