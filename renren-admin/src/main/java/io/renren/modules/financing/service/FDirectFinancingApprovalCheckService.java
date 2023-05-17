package io.renren.modules.financing.service;

import io.renren.common.service.CrudService;
import io.renren.modules.financing.dto.FDirectFinancingApprovalCheckDTO;
import io.renren.modules.financing.dto.FDirectFinancingApprovalDTO;
import io.renren.modules.financing.entity.FDirectFinancingApproval;
import io.renren.modules.financing.entity.FDirectFinancingApprovalCheck;

public interface FDirectFinancingApprovalCheckService extends
        CrudService<FDirectFinancingApprovalCheck, FDirectFinancingApprovalCheckDTO> {

}
