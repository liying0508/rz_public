package io.renren.modules.financing.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.renren.modules.financing.dto.FDirectFinancingAuditDTO;

import io.renren.modules.financing.dto.FIndirectFinancingAuditDTO;
import io.renren.modules.financing.entity.FIndirectFinancingEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface FIndirectFinancingAuditDao extends BaseMapper<FIndirectFinancingEntity> {
    /**
     * 银行授信列表
     * @return
     */
    List<FIndirectFinancingAuditDTO> getList();

//    FIndirectFinancingAuditDTO getInfoByApprovalNo(String approvalNo);

    FIndirectFinancingAuditDTO getDealData(Long id);
}
