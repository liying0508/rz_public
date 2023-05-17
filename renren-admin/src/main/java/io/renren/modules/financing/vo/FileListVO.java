package io.renren.modules.financing.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
@ApiModel(value = "附件信息")
public class FileListVO implements Serializable {
    private static final long serialVersionUID = 1L;
    @ApiModelProperty(value = "附件描述")
    private String desc;
    @ApiModelProperty(value = "文件信息")
    private List<FileVO> files;
    @ApiModelProperty(value = "文件名")
    private String fileName;
    @ApiModelProperty(value = "主键")
    private String id;
    @ApiModelProperty(value = "文件大小")
    private String size;
    @ApiModelProperty(value = "文件信息字符串")
    private String fileStr;
}
