package io.renren.modules.financing.dao;


import io.renren.common.dao.BaseDao;
import io.renren.modules.financing.dto.RepaymentInfoDTO;
import io.renren.modules.financing.dto.RepaymentInfoHistoryDTO;
import io.renren.modules.financing.entity.FRepaymentInfoTableHistoryEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface RepaymentInfoHistoryDao extends BaseDao<FRepaymentInfoTableHistoryEntity> {
    List<RepaymentInfoHistoryDTO> selectData(Long financingId, Integer financingType);
}
