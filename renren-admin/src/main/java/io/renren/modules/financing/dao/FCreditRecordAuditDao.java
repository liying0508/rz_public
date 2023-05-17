package io.renren.modules.financing.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.renren.modules.financing.dto.FCreditRecordAuditDTO;
import io.renren.modules.financing.dto.FCreditRecordDTO;
import io.renren.modules.financing.dto.FDirectFinancingAuditDTO;
import io.renren.modules.financing.dto.FIndirectFinancingAuditDTO;
import io.renren.modules.financing.entity.FCreditRecordEntity;
import io.renren.modules.financing.entity.FDirectFinancingEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface FCreditRecordAuditDao extends BaseMapper<FCreditRecordEntity> {
//    /**
//     * 银行授信列表
//     * @return
//     */
//    List<FCreditRecordAuditDTO> getlist();
//
//    FDirectFinancingAuditDTO getInfoById(Long id);

    FCreditRecordAuditDTO getDealData(Long id);
}
