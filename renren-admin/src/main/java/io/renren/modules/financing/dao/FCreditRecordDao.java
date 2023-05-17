package io.renren.modules.financing.dao;

import io.renren.common.dao.BaseDao;
import io.renren.modules.financing.dto.FCreditPageDto;
import io.renren.modules.financing.dto.FCreditRecordDTO;
import io.renren.modules.financing.dto.FDirectFinancingDTO;
import io.renren.modules.financing.entity.FCreditRecordEntity;
import org.apache.ibatis.annotations.Mapper;

import java.math.BigDecimal;
import java.util.List;

/**
* 银行授信
*
* @author liuwenyong 
* @since 3.0 2022-05-14
*/
@Mapper
public interface FCreditRecordDao extends BaseDao<FCreditRecordEntity> {
    /**
     * 银行授信列表
     * @return
     */
	List<FCreditPageDto> getlist();

    /**
     * 根据主键获取银行授信信息
     * @param id
     * @return
     */
    FCreditRecordDTO getInfo(Long id);

    FCreditRecordDTO getDealData(Long id);

    BigDecimal getQuota(Long deptId,Long institution);

    List<BigDecimal> getUsedQuota(Long deptId,Long institution);

    //根据机构id和授信单位id查询
    List<FCreditRecordDTO> getDataByDeptIdAndInstitutionId(Long deptId,Long institutionId);
    //根据季候id和授信单位id删除
    void deleteByDeptIdAndInstitutionId(Long deptId,Long institutionId);

    //获得所有信息
    List<FCreditRecordDTO> getAllData();
}