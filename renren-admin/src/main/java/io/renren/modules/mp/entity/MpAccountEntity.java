package io.renren.modules.mp.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import com.baomidou.mybatisplus.annotation.*;
import java.util.Date;
import io.renren.common.entity.BaseEntity;

/**
 * 公众号账号管理
 *
 * @author Mark sunlightcs@gmail.com
 */
@Data
@EqualsAndHashCode(callSuper=false)
@TableName("mp_account")
public class MpAccountEntity extends BaseEntity {
	private static final long serialVersionUID = 1L;

	/**
	* 名称
	*/
	private String name;
	/**
	* AppID
	*/
	private String appId;
	/**
	* AppSecret
	*/
	private String appSecret;
	/**
	*
	*/
	private String token;
	/**
	* EncodingAESKey
	*/
	private String aesKey;
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