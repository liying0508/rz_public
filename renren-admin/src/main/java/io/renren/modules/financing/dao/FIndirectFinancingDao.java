package io.renren.modules.financing.dao;

import io.renren.common.dao.BaseDao;
import io.renren.modules.financing.dto.FIndirectFinancingDTO;
import io.renren.modules.financing.entity.FIndirectFinancingEntity;
import org.apache.ibatis.annotations.Mapper;

import java.math.BigDecimal;
import java.util.List;

/**
* 间接融资信息
*
* @author liuwenyong 
* @since 3.0 2022-05-14
*/
@Mapper
public interface FIndirectFinancingDao extends BaseDao<FIndirectFinancingEntity> {
    FIndirectFinancingDTO getDealData(Long id);

    /**
     * 根据融资机构id和金融机构id来获得已经使用的额度
     * @param deptId 融资机构id
     * @param institution 金融机构id
     * @return BigDecimal
     */
    BigDecimal getUsedQuota(Long deptId,Long institution);

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

    /**
     * 查询目前数据库内所有的间融信息汇总成《银行间接融资明细表》
     * @return
     */
    List<FIndirectFinancingDTO> selectAllData();
}