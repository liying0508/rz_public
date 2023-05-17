package io.renren.modules.lpr.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import com.baomidou.mybatisplus.annotation.*;
import java.util.Date;

/**
 * 浮动利率表
 *
 * @author LiaoZX 
 * @since  2022-06-08
 */
@Data
@EqualsAndHashCode(callSuper=false)
@TableName("f_lpr")
public class FLprEntity {
	private static final long serialVersionUID = 1L;

	/**
	* 编号
	*/
	@TableId
	private Long id;
	/**
	* 开始时间
	*/
	private Date startTime;
	/**
	* 结束时间
	*/
	private Date endTime;
	/**
	* 利率
	*/
	private String rate;
    /**
     * 五年利率
     */
    private String fiveRate;
}