package io.renren.modules.financing.service;

import io.renren.common.page.PageData;
import io.renren.common.service.CrudService;
import io.renren.modules.financing.dto.*;
import io.renren.modules.financing.entity.FDirectFinancingEntity;
import org.springframework.stereotype.Service;

import java.util.List;

public interface FDirectFinancingAuditService extends CrudService<FDirectFinancingEntity, FDirectFinancingAuditDTO> {
    List<FDirectFinancingAuditDTO> listInfo();

    void saveInfo(FDirectFinancingAuditDTO dto);

    void updateInfo(FDirectFinancingAuditDTO dto);

    void check(FDirectFinancingCheckDTO dto);

    void groupCheck(FDirectFinancingCheckDTO dto);

    /**
     * 获取数据并处理
     * @param id
     * @return
     */
    FDirectFinancingAuditDTO getDealData(Long id);

    List<FDirectFinancingAuditDTO> screenList(List<FDirectFinancingAuditDTO> list, String approvalAmount);

    /**
     * 间融首页列表单位转化
     */
    public List<FDirectFinancingAuditDTO> directCast(List<FDirectFinancingAuditDTO> list, Integer unit) ;
    /**
     * 间融详细信息单位转化
     */
    FDirectFinancingAuditDTO directInfoCast(FDirectFinancingAuditDTO dto,Integer unit);
}
