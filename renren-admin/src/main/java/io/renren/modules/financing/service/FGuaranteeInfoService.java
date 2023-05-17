package io.renren.modules.financing.service;

import io.renren.common.service.CrudService;
import io.renren.modules.financing.dto.FGuaranteeInfoDTO;
import io.renren.modules.financing.entity.FGuaranteeInfoEntity;
import io.renren.modules.financing.vo.CreditMeasuresVO;

/**
 * 担保信息
 *
 * @author liuwenyong 
 * @since 3.0 2022-05-14
 */
public interface FGuaranteeInfoService extends CrudService<FGuaranteeInfoEntity, FGuaranteeInfoDTO> {
    /**
     * 根据银行授信ID获取增信措施信息
     * @param creditId
     */
    FGuaranteeInfoDTO getInfo(Long creditId);

    /**
     * 获取增信措施详情
     * @param creditId 直融或者间融信息ID
     * @return
     */
    CreditMeasuresVO getCreditMeasures(Long creditId);
}