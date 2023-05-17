package io.renren.modules.financing.dao;

import io.renren.common.dao.BaseDao;
import io.renren.modules.financing.dto.RepaymentInfoDTO;
import io.renren.modules.financing.entity.FRepaymentInfoTableEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface RepaymentInfoDao extends BaseDao<FRepaymentInfoTableEntity> {
    void delData(Long financingId,Integer financingType);

    List<RepaymentInfoDTO> selectData(Long financingId, Integer financingType);
}
