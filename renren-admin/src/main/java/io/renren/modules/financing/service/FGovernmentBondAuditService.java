package io.renren.modules.financing.service;

import io.renren.common.service.CrudService;
import io.renren.modules.financing.dto.FGovernmentBondAuditDTO;
import io.renren.modules.financing.dto.FGovernmentBondCheckDTO;
import io.renren.modules.financing.entity.FGovernmentBondEntity;

import java.util.List;

public interface FGovernmentBondAuditService
        extends CrudService<FGovernmentBondEntity, FGovernmentBondAuditDTO> {
    List<FGovernmentBondAuditDTO> listInfo();

    void check(FGovernmentBondCheckDTO dto);

    void groupCheck(FGovernmentBondCheckDTO dto);

    /**
     * 获取数据并处理
     * @param id
     * @return
     */
    FGovernmentBondAuditDTO getDealData(Long id);
}
