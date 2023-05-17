package io.renren.modules.financing.dao;

import io.renren.common.dao.BaseDao;
import io.renren.modules.financing.entity.FCreditRecordCheck;
import io.renren.modules.financing.entity.FGovernmentBondCheckEntity;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface FGovernmentBondCheckDao extends BaseDao<FGovernmentBondCheckEntity> {
}
