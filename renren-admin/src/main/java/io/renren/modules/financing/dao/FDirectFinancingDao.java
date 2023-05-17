package io.renren.modules.financing.dao;

import io.renren.common.dao.BaseDao;
import io.renren.modules.financing.dto.FDirectFinancingApprovalDTO;
import io.renren.modules.financing.dto.FDirectFinancingDTO;
import io.renren.modules.financing.entity.FDirectFinancingEntity;
import org.apache.ibatis.annotations.Mapper;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;

/**
* 直接融资信息
*
* @author liuwenyong 
* @since 3.0 2022-05-14
*/
@Mapper
public interface FDirectFinancingDao extends BaseDao<FDirectFinancingEntity> {
	HashMap getData(String approveNo);

	FDirectFinancingDTO getDealData(Long id);

	/**
	 * 得到数据库中所有的文件JSON数据
	 * @return List
	 */
	List<String> getAllFilesInDataBase();

	/**
	 * 根据id更新实际还款
	 * @param id 主键
	 * @param actualRepayment 实际还款金额
	 */
	void updateActualRepaymentById(Long id, BigDecimal actualRepayment);

	/**
	 * 根据id更新融资余额
	 * @param id 主键
	 * @param financingBalance 融资余额
	 */
	void updateFinancingBalanceById(Long id,BigDecimal financingBalance);

	/**
	 * 根据金融机构id查询已使用额度
	 * @param institution 金融机构id
	 * @return BigDecimal
	 */
	BigDecimal getUsedQuotaByInstitutionId(Long institution);
}