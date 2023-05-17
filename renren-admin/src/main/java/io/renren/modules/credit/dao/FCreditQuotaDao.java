package io.renren.modules.credit.dao;

import io.renren.common.dao.BaseDao;
import io.renren.modules.credit.dto.FCreditQuotaDTO;
import io.renren.modules.credit.entity.FCreditQuotaEntity;
import org.apache.ibatis.annotations.Mapper;

import java.math.BigDecimal;
import java.util.List;

@Mapper
public interface FCreditQuotaDao extends BaseDao<FCreditQuotaEntity> {
    /**
     * 获得所有的授信总额记录
     * @return List<FCreditQuotaDTO>
     */
    List<FCreditQuotaDTO> selectAllData();

    /**
     * 统计指定id的金融机构的授信总额记录数目
     * @param institution 金融机构id
     * @return Integer
     */
    Integer countInstitutionById(Long institution);

    /**
     * 根据银行id更新已使用间融额度以及已使用直融额度
     * @param institution 金融机构id
     * @param usedQuotaDirect 直融已使用额度
     * @param usedQuotaIndirect 间融已使用额度
     */
    void updateUsedQuotaByInstitutionId(Long institution,BigDecimal usedQuotaDirect,BigDecimal usedQuotaIndirect);




}
