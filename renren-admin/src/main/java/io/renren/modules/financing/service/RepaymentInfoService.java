package io.renren.modules.financing.service;

import io.renren.common.service.CrudService;
import io.renren.modules.financing.dto.RepaymentInfoDTO;
import io.renren.modules.financing.entity.FRepaymentInfoTableEntity;

import java.util.List;

public interface RepaymentInfoService extends CrudService<FRepaymentInfoTableEntity, RepaymentInfoDTO> {
    /**
     * 删除指定的数据
     * @param financingId
     * @param financingType
     */
    void delData(Long financingId,Integer financingType);

    /**
     * 查询所有指定的数据
     * @param financingId
     * @param financingType
     * @return
     */
    List<RepaymentInfoDTO> selectData(Long financingId, Integer financingType);
}
