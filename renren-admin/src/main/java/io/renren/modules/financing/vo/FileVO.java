package io.renren.modules.financing.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
@ApiModel(value = "附件信息")
public class FileVO implements Serializable {
    private static final long serialVersionUID = 1L;
    @ApiModelProperty(value = "文件名")
    private String name;
    @ApiModelProperty(value = "资源地址")
    private String url;
}
