package io.renren.modules.financing.service;

import io.renren.common.service.CrudService;
import io.renren.modules.financing.dto.FRepayInterestPlanDTO;
import io.renren.modules.financing.dto.FRepayPrincipalPlanDTO;
import io.renren.modules.financing.dto.FRepaymentPlanDTO;
import io.renren.modules.financing.entity.FRepaymentPlan;

import java.util.List;

public interface FRepaymentPlanService extends CrudService<FRepaymentPlan, FRepaymentPlanDTO> {
    /**
     * 还本计划保存
     * @param dto
     */
    void saveRepayPrincipalPlan(FRepayPrincipalPlanDTO dto);

    /**
     * 还息计划保存
     * @param dto
     */
    void saveRepayInterestPlan(FRepayInterestPlanDTO dto);

    /**
     * 还本计划更新
     * @param dto
     */
    void updateRepayPrincipalPlan(FRepayPrincipalPlanDTO dto);

    /**
     * 还息计划更新
     * @param dto
     */
    void updateRepayInterestPlan(FRepayInterestPlanDTO dto);

    /**
     * 查询还本计划
     */
    List<FRepayPrincipalPlanDTO> selectRepayPrincipalPlan(Long financingId, Integer financingType);

    /**
     * 查询还息计划
     */
    List<FRepayInterestPlanDTO> selectRepayInterestPlan(Long financingId,Integer financingType);

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
    List<Long> selectRepayPlanId(Long financingId,Integer financingType);
    /**
     * 删除所有的对应数据
     */
    void deleteRepayPlan(Long financingId,Integer financingType);

}
