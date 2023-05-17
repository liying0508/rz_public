package io.renren.modules.lpr.dao;

import io.renren.common.dao.BaseDao;
import io.renren.modules.lpr.dto.FLprDTO;
import io.renren.modules.lpr.entity.FLprEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
* 浮动利率表
*
* @author LiaoZX 
* @since  2022-06-08
*/
@Mapper
public interface FLprDao extends BaseDao<FLprEntity> {
    List<FLprDTO> getAllLpr();
}