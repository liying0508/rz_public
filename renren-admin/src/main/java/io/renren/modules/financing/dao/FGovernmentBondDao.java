package io.renren.modules.financing.dao;

import io.renren.common.dao.BaseDao;
import io.renren.modules.financing.dto.FDirectFinancingDTO;
import io.renren.modules.financing.dto.FGovernmentBondDTO;
import io.renren.modules.financing.entity.FGovernmentBondEntity;
import org.apache.ibatis.annotations.Mapper;

import java.math.BigDecimal;

/**
* 政府债券
*
* @author liuwenyong 
* @since 3.0 2022-05-14
*/
@Mapper
public interface FGovernmentBondDao extends BaseDao<FGovernmentBondEntity> {
    FGovernmentBondDTO getDealData(Long id);

    void updateActualRepaymentById(Long id, BigDecimal actualRepayment);

    void updateFinancingBalanceById(Long id,BigDecimal financingBalance);
}