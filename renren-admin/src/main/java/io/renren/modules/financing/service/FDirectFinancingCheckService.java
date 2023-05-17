package io.renren.modules.financing.service;

import io.renren.common.service.CrudService;
import io.renren.modules.financing.dto.FDirectFinancingCheckDTO;
import io.renren.modules.financing.entity.FDirectFinancingCheck;
import org.springframework.stereotype.Service;

public interface FDirectFinancingCheckService extends
        CrudService<FDirectFinancingCheck, FDirectFinancingCheckDTO> {

}
