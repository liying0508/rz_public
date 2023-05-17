package io.renren.modules.financing.service;

import io.renren.common.service.CrudService;
import io.renren.modules.financing.dto.FCreditRecordAuditDTO;
import io.renren.modules.financing.dto.FCreditRecordCheckDTO;
import io.renren.modules.financing.dto.FCreditRecordDTO;
import io.renren.modules.financing.entity.FCreditRecordEntity;
import org.springframework.stereotype.Service;


import java.util.List;

public interface FCreditRecordAuditService
        extends CrudService<FCreditRecordEntity, FCreditRecordAuditDTO> {

    List<FCreditRecordAuditDTO> listInfo();

    void saveInfo(FCreditRecordAuditDTO dto);

    void updateInfo(FCreditRecordAuditDTO dto);

    void check(FCreditRecordCheckDTO dto);

    void groupCheck(FCreditRecordCheckDTO dto);

    /**
     * 获取数据并处理
     * @param id
     * @return
     */
    FCreditRecordAuditDTO getDealData(Long id);

    /**
     * 直融详细信息单位转化
     */
    FCreditRecordAuditDTO directInfoCast(FCreditRecordAuditDTO dto, Integer unit);
    /**
     * 直融存进去的数据进行转化
     */
    FCreditRecordAuditDTO directSaveCast(FCreditRecordAuditDTO dto,Integer unit);
}
