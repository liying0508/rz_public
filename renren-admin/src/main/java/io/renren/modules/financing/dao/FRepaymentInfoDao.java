package io.renren.modules.financing.dao;

import io.renren.common.dao.BaseDao;
import io.renren.modules.financing.dto.FRepaymentInfoDTO;
import io.renren.modules.financing.dto.FRepaymentInfoPageDTO;
import io.renren.modules.financing.entity.FRepaymentInfo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface FRepaymentInfoDao extends BaseDao<FRepaymentInfo> {
    /**
     * 获得政府债券基础数据
     * @return
     */
    List<FRepaymentInfoPageDTO> getGovernment();
    /**
     * 获得直融基础数据
     * @return
     */
    List<FRepaymentInfoPageDTO> getDirect();

    /**
     * 获得间融基础数据
     * @return
     */
    List<FRepaymentInfoPageDTO> getIndirect();

    /**
     *根据id和类型查出还款信息
     * @param financingId 融资单id
     * @param financingType 融资类型：0政府债券，1直接融资，2间接融资
     * @return FRepaymentInfoDTO
     */
    FRepaymentInfoDTO getRepaymentInfo(Long financingId,Integer financingType);

    FRepaymentInfoDTO getDealData(Long financingId);

    FRepaymentInfoDTO getRepaymentInfoHistory(Long financingId,Integer financingType);

    void insertInfoInHistory();
}
