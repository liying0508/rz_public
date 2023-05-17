package io.renren.modules.financing.dao;

import io.renren.common.dao.BaseDao;
import io.renren.modules.financing.entity.FDirectFinancingApprovalCheck;
import io.renren.modules.financing.entity.FDirectFinancingCheck;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface FDirectFinancingCheckDao extends BaseDao<FDirectFinancingCheck> {
}
