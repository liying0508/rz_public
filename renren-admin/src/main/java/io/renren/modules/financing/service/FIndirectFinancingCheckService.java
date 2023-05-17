package io.renren.modules.financing.service;

import io.renren.common.service.CrudService;
import io.renren.modules.financing.dto.FIndirectFinancingCheckDTO;
import io.renren.modules.financing.entity.FIndirectFinancingCheck;
import org.springframework.stereotype.Service;

public interface FIndirectFinancingCheckService extends
        CrudService<FIndirectFinancingCheck, FIndirectFinancingCheckDTO> {
}
