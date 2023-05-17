package io.renren.modules.financing.dao;

import io.renren.common.dao.BaseDao;
import io.renren.modules.financing.dto.FRepayInterestPlanDTO;
import io.renren.modules.financing.dto.FRepayPrincipalPlanDTO;
import io.renren.modules.financing.dto.FRepaymentPlanDTO;
import io.renren.modules.financing.entity.FRepaymentPlan;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface FRepaymentPlanDao extends BaseDao<FRepaymentPlan> {
    /**
     * 根据融资id和融资种类查找所有的还本计划
     * @param financingId   融资id
     * @param financingType 融资种类（1直融，2间融。3政府债券）
     * @return
     */
    List<FRepayPrincipalPlanDTO> selectPrincipalPlan(Long financingId, Integer financingType);

    /**
     * 根据融资id和融资种类查找所有的还息计划
     * @param financingId   融资id
     * @param financingType 融资种类（1直融，2间融。3政府债券）
     * @return
     */
    List<FRepayInterestPlanDTO> selectInterestPlan(Long financingId,Integer financingType);

    /**
     * 根据融资id和融资种类查找所有的还款计划
     * @param financingId   融资id
     * @param financingType 融资种类（1直融，2间融。3政府债券）
     * @return
     */
    List<Long> selectAllPlanId(Long financingId, Integer financingType);

    void deletePayPlan(Long financingId, Integer financingType);

}
