package io.renren.modules.financing.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import com.baomidou.mybatisplus.annotation.*;
import java.util.Date;
import io.renren.common.entity.BaseEntity;

/**
 * 担保信息
 *
 * @author liuwenyong 
 * @since 3.0 2022-05-14
 */
@Data
@EqualsAndHashCode(callSuper=false)
@TableName("f_guarantee_info")
public class FGuaranteeInfoEntity extends BaseEntity {
	private static final long serialVersionUID = 1L;

	/**
	* 授信单ID
	*/
	private Long creditId;
	/**
	* 信用
	*/
	private String creditText;
	/**
	* 保证人
	*/
	private String guarantor;
	/**
	* 比例
	*/
	private Integer ratio;
	/**
	* 合同金额
	*/
	private Integer contractAmount;
	/**
	* 抵押物名称
	*/
	private String collateralName;
	/**
	* 抵押物评估值
	*/
	private Integer collateralAmount;
	/**
	* 抵押合同
	*/
	private String collateralContract;
	/**
	* 解押时间
	*/
	private Date releaseTime;
	/**
	* 质押标的
	*/
	private String pledgeType;
	/**
	* 质押人
	*/
	private String pledgeName;
	/**
	* 质押价值
	*/
	private Integer pledgeAmount;
	/**
	* 备注
	*/
	private String remarks;
}