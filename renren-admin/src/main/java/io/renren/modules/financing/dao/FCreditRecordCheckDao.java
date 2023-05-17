package io.renren.modules.financing.dao;

import io.renren.common.dao.BaseDao;
import io.renren.modules.financing.entity.FCreditRecordCheck;
import io.renren.modules.financing.entity.FDirectFinancingCheck;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface FCreditRecordCheckDao extends BaseDao<FCreditRecordCheck> {
}
