package io.renren.modules.financing.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "间接融资信息的对应金融机构信息")
public class FIndirectDeptDTO {
    @ApiModelProperty(value = "金融机构名称")
    private String name;
    @ApiModelProperty(value = "融资单位名称")
    private String deptName;
    @ApiModelProperty(value = "金融机构类型")
    private Integer type;

}
