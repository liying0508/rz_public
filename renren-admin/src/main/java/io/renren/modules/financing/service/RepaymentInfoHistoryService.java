package io.renren.modules.financing.service;

import io.renren.common.service.CrudService;
import io.renren.modules.financing.dto.RepaymentInfoDTO;
import io.renren.modules.financing.dto.RepaymentInfoHistoryDTO;
import io.renren.modules.financing.entity.FRepaymentInfoTableHistoryEntity;

import java.util.List;

public interface RepaymentInfoHistoryService extends
        CrudService<FRepaymentInfoTableHistoryEntity, RepaymentInfoHistoryDTO> {

    /**
     * 查询所有指定的数据
     * @param financingId
     * @param financingType
     * @return
     */
    List<RepaymentInfoHistoryDTO> selectData(Long financingId, Integer financingType);
}
