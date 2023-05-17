package io.renren.modules.financing.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
@ApiModel(value = "后端字典")
public class FDictDTO implements Serializable {
    private static final long serialVersionUID = 1L;
    @ApiModelProperty(value = "主键")
    private Long id;

    @ApiModelProperty(value = "字段名")
    private String dictName;

    @ApiModelProperty(value = "字段键")
    private String dictKey;

    @ApiModelProperty(value = "字段值")
    private String dictValue;

    @ApiModelProperty(value = "模块名")
    private String dictModule;
}
