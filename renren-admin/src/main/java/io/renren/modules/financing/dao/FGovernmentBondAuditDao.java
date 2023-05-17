package io.renren.modules.financing.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.renren.modules.financing.dto.FGovernmentBondAuditDTO;
import io.renren.modules.financing.dto.FGovernmentBondDTO;
import io.renren.modules.financing.entity.FGovernmentBondEntity;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface FGovernmentBondAuditDao extends BaseMapper<FGovernmentBondEntity> {

    FGovernmentBondAuditDTO getDealData(Long id);

    Integer updateChecked(Long id,Integer isChecked,String checkedDes);
}
