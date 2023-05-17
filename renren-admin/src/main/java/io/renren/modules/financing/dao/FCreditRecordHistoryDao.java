package io.renren.modules.financing.dao;

import io.renren.common.dao.BaseDao;
import io.renren.modules.financing.dto.FCreditHistoryPageDto;
import io.renren.modules.financing.dto.FCreditPageDto;
import io.renren.modules.financing.dto.FCreditRecordDTO;
import io.renren.modules.financing.entity.FCreditRecordHistoryEntity;
import org.apache.ibatis.annotations.Mapper;

import java.math.BigDecimal;
import java.util.List;

@Mapper
public interface FCreditRecordHistoryDao extends BaseDao<FCreditRecordHistoryEntity> {

    /**
     * 银行授信列表
     * @return
     */
    List<FCreditHistoryPageDto> getlist();

    /**
     * 根据主键获取银行授信信息
     * @param id
     * @return
     */
    FCreditRecordDTO getInfo(Long id);

    FCreditRecordDTO getDealData(Long id);

    BigDecimal getQuota(Long deptId, Long institution);

    List<BigDecimal> getUsedQuota(Long deptId,Long institution);

}
