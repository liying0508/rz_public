package io.renren.modules.financing.dao;

import io.renren.common.dao.BaseDao;
import io.renren.modules.financing.dto.FRepayInterestActualDTO;
import io.renren.modules.financing.dto.FRepayPrincipalActualDTO;
import io.renren.modules.financing.entity.FRepaymentActual;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @Author: Eddy
 * @Date: 2022/12/5 0005
 * @Time: 14:16
 * @Description: 实际还款信息持久层
 */
@Mapper
public interface FRepaymentActualDao extends BaseDao<FRepaymentActual> {
    /**
     * 根据融资id和融资种类查找所有的实际还本信息
     * @param financingId   融资id
     * @param financingType 融资种类（1直融，2间融。3政府债券）
     * @return List<FRepayPrincipalActualDTO>
     */
    List<FRepayPrincipalActualDTO> selectPrincipalActual(Long financingId, Integer financingType);

    /**
     * 根据融资id和融资种类查找所有的实际还息信息
     * @param financingId   融资id
     * @param financingType 融资种类（1直融，2间融。3政府债券）
     * @return List<FRepayInterestActualDTO>
     */
    List<FRepayInterestActualDTO> selectInterestActual(Long financingId, Integer financingType);

    /**
     * 根据融资id和融资种类查找所有的实际还款
     * @param financingId   融资id
     * @param financingType 融资种类（1直融，2间融。3政府债券）
     * @return List<Long>
     */
    List<Long> selectAllActualId(Long financingId, Integer financingType);

    /**
     * 根据融资单id和种类删除名下的所有实际还款信息
     * @param financingId   融资id
     * @param financingType 融资种类（1直融，2间融。3政府债券）
     */
    void deletePayActual(Long financingId, Integer financingType);

}
