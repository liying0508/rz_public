package io.renren.modules.financing.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.renren.modules.financing.dto.FDirectFinancingApprovalDTO;
import io.renren.modules.financing.dto.FDirectFinancingAuditDTO;
import io.renren.modules.financing.dto.FDirectFinancingDTO;
import io.renren.modules.financing.entity.FDirectFinancingEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface FDirectFinancingAuditDao extends BaseMapper<FDirectFinancingEntity> {
    /**
     * 银行授信列表
     * @return
     */
    List<FDirectFinancingAuditDTO> getlist();

    FDirectFinancingAuditDTO getInfoByApprovalNo(String approvalNo);

    FDirectFinancingAuditDTO getDealData(Long id);
}
