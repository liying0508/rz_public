package io.renren.modules.financing.dao;

import io.renren.common.dao.BaseDao;
import io.renren.modules.financing.entity.FGuaranteeInfoEntity;
import org.apache.ibatis.annotations.Mapper;

/**
* 担保信息
*
* @author liuwenyong 
* @since 3.0 2022-05-14
*/
@Mapper
public interface FGuaranteeInfoDao extends BaseDao<FGuaranteeInfoEntity> {
	
}