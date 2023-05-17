package io.renren.modules.mp.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import com.baomidou.mybatisplus.annotation.*;
import java.util.Date;
import io.renren.common.entity.BaseEntity;

/**
 * 公众号自定义菜单
 *
 * @author Mark sunlightcs@gmail.com
 */
@Data
@EqualsAndHashCode(callSuper=false)
@TableName("mp_menu")
public class MpMenuEntity extends BaseEntity {
	private static final long serialVersionUID = 1L;

	/**
	* 菜单json数据
	*/
	private String menu;
	/**
	* AppID
	*/
	private String appId;
	/**
	* 更新者
	*/
	@TableField(fill = FieldFill.INSERT_UPDATE)
	private Long updater;
	/**
	* 更新时间
	*/
	@TableField(fill = FieldFill.INSERT_UPDATE)
	private Date updateDate;
}