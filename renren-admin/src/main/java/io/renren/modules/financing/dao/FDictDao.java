package io.renren.modules.financing.dao;

import io.renren.common.dao.BaseDao;
import io.renren.modules.financing.dto.FDictDTO;
import io.renren.modules.financing.entity.FDictEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface FDictDao extends BaseDao<FDictEntity> {

    //获取对应模块的所有字典
    List<FDictDTO> selectModuleAllKey(String dictModule);
}
