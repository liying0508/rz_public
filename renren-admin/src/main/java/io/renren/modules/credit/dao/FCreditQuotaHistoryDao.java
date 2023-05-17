package io.renren.modules.credit.dao;

import io.renren.common.dao.BaseDao;
import io.renren.modules.credit.dto.FCreditQuotaHistoryDTO;
import io.renren.modules.credit.entity.FCreditQuotaHistoryEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface FCreditQuotaHistoryDao extends BaseDao<FCreditQuotaHistoryEntity> {

    List<FCreditQuotaHistoryDTO> selectAllByCreditQuotaId(Long creditQuotaId);
}
