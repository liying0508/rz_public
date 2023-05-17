package io.renren.modules.report.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
* 浮动利率表
*
* @author LiaoZX 
* @since  2022-06-08
*/
@Data
@ApiModel(value = "浮动利率表")
public class LprDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "编号")
    private Long id;
    @ApiModelProperty(value = "开始时间")
    private Date startTime;
    @ApiModelProperty(value = "结束时间")
    private Date endTime;
    @ApiModelProperty(value = "利率")
    private String rate;
    @ApiModelProperty(value = "五年利率")
    private String fiveRate;

    public Long start;
    public Long end;
}