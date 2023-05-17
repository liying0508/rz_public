package io.renren.modules.financing.service;

import io.renren.common.service.CrudService;
import io.renren.modules.financing.dto.FCreditRecordCheckDTO;
import io.renren.modules.financing.dto.FDirectFinancingCheckDTO;
import io.renren.modules.financing.entity.FCreditRecordCheck;
import io.renren.modules.financing.entity.FDirectFinancingCheck;

public interface FCreditRecordCheckService extends
        CrudService<FCreditRecordCheck, FCreditRecordCheckDTO> {

}
