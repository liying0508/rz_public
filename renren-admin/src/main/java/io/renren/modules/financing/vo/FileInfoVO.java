package io.renren.modules.financing.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @Author：LiaoZX
 * @Date：2022/6/7
 * @Description:
 */
@Data
@ApiModel(value = "附件信息")
public class FileInfoVO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "附件路径")
    private List<String> fileList;
    @ApiModelProperty(value = "附件名")
    private String fileName;
    @ApiModelProperty(value = "附件备注")
    private String desc;
    @ApiModelProperty(value = "大小")
    private String size;
}
