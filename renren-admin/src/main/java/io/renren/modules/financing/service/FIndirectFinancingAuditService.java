package io.renren.modules.financing.service;

import io.renren.common.service.CrudService;
import io.renren.modules.financing.dto.FIndirectFinancingAuditDTO;
import io.renren.modules.financing.dto.FIndirectFinancingCheckDTO;
import io.renren.modules.financing.entity.FIndirectFinancingEntity;

import java.util.List;

public interface FIndirectFinancingAuditService extends
        CrudService<FIndirectFinancingEntity, FIndirectFinancingAuditDTO> {

    List<FIndirectFinancingAuditDTO> listInfo();

    void saveInfo(FIndirectFinancingAuditDTO dto);

    void updateInfo(FIndirectFinancingAuditDTO dto);

    void check(FIndirectFinancingCheckDTO dto);

    void groupCheck(FIndirectFinancingCheckDTO dto);

    /**
     * 获取数据并处理
     * @param id
     * @return
     */
    FIndirectFinancingAuditDTO getDealData(Long id);
}
