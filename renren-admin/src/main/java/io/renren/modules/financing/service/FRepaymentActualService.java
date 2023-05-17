package io.renren.modules.financing.service;

import io.renren.common.service.CrudService;
import io.renren.modules.financing.dto.*;
import io.renren.modules.financing.entity.FRepaymentActual;
import io.renren.modules.financing.entity.FRepaymentPlan;

import java.util.List;

public interface FRepaymentActualService extends CrudService<FRepaymentActual, FRepaymentActualDTO> {
    /**
     * 还本计划保存
     * @param dto
     */
    void saveRepayPrincipalActual(FRepayPrincipalActualDTO dto);

    /**
     * 还息计划保存
     * @param dto
     */
    void saveRepayInterestActual(FRepayInterestActualDTO dto);

    /**
     * 还本计划更新
     * @param dto
     */
    void updateRepayPrincipalActual(FRepayPrincipalActualDTO dto);

    /**
     * 还息计划更新
     * @param dto
     */
    void updateRepayInterestActual(FRepayInterestActualDTO dto);

    /**
     * 查询还本计划
     */
    List<FRepayPrincipalActualDTO> selectRepayPrincipalActual(Long financingId, Integer financingType);

    /**
     * 查询还息计划
     */
    List<FRepayInterestActualDTO> selectRepayInterestActual(Long financingId,Integer financingType);

    /**
     * 判断是否存在该还款计划
     * （true为存在，false为不存在）
     * @param id
     * @return
     */
    Boolean existData(Long id);
    /**
     * 查询所有还款计划id
     */
    List<Long> selectRepayActualId(Long financingId,Integer financingType);
    /**
     * 删除所有的对应数据
     */
    void deleteRepayActual(Long financingId,Integer financingType);

}
