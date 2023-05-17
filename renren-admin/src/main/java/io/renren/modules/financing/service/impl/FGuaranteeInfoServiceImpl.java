package io.renren.modules.financing.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.renren.common.service.impl.CrudServiceImpl;
import io.renren.common.constant.Constant;
import io.renren.modules.financing.dao.FGuaranteeInfoDao;
import io.renren.modules.financing.dto.FGuaranteeInfoDTO;
import io.renren.modules.financing.entity.FGuaranteeInfoEntity;
import io.renren.modules.financing.service.FGuaranteeInfoService;
import io.renren.modules.financing.vo.CreditMeasuresVO;
import io.renren.modules.security.user.SecurityUser;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * 担保信息
 *
 * @author liuwenyong 
 * @since 3.0 2022-05-14
 */
@Service
public class FGuaranteeInfoServiceImpl extends CrudServiceImpl<FGuaranteeInfoDao, FGuaranteeInfoEntity, FGuaranteeInfoDTO> implements FGuaranteeInfoService {

    @Override
    public QueryWrapper<FGuaranteeInfoEntity> getWrapper(Map<String, Object> params){
        QueryWrapper<FGuaranteeInfoEntity> wrapper = new QueryWrapper<>();


        return wrapper;
    }

    @Override
    public FGuaranteeInfoDTO getInfo(Long creditId){
        FGuaranteeInfoEntity entity = baseDao.selectOne(new LambdaQueryWrapper<FGuaranteeInfoEntity>().eq(FGuaranteeInfoEntity::getCreditId, creditId));
        FGuaranteeInfoDTO dto = new FGuaranteeInfoDTO();
        BeanUtils.copyProperties(entity, dto);
        return dto;
    }

    @Override
    public CreditMeasuresVO getCreditMeasures(Long creditId) {
        List<FGuaranteeInfoEntity> entities = baseDao.selectList(new LambdaQueryWrapper<FGuaranteeInfoEntity>().eq(FGuaranteeInfoEntity::getCreditId, creditId));
        if (CollectionUtils.isNotEmpty(entities)){

        }

        return null;
    }

}