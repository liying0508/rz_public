package io.renren.modules.sys.dto;

import io.renren.common.validator.group.DefaultGroup;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
@ApiModel(value = "证券公司管理")
public class SysSecComDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "id")
    private Long id;

    @ApiModelProperty(value = "证券公司名字")
    @NotNull(message = "证券公司名字不能为空",groups = DefaultGroup.class)
    private String secComName;

    @ApiModelProperty(value = "辖区")
    @NotNull(message = "辖区名字不能为空",groups = DefaultGroup.class)
    private String area;
}
