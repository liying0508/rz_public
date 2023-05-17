package io.renren.modules.lpr.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.renren.common.service.impl.CrudServiceImpl;
import io.renren.common.constant.Constant;
import io.renren.modules.financing.util.InterestCalculatedUtil;
import io.renren.modules.lpr.dao.FLprDao;
import io.renren.modules.lpr.dto.FLprDTO;
import io.renren.modules.lpr.entity.FLprEntity;
import io.renren.modules.lpr.service.FLprService;
import io.renren.modules.security.user.SecurityUser;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 浮动利率表
 *
 * @author LiaoZX 
 * @since  2022-06-08
 */
@Service
public class FLprServiceImpl extends CrudServiceImpl<FLprDao, FLprEntity, FLprDTO> implements FLprService {
    @Autowired
    private FLprDao fLprDao;

    @Override
    public QueryWrapper<FLprEntity> getWrapper(Map<String, Object> params){
        QueryWrapper<FLprEntity> wrapper = new QueryWrapper<>();


        return wrapper;
    }
}