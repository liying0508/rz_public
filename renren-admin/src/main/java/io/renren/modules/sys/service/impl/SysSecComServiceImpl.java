package io.renren.modules.sys.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.renren.common.service.impl.BaseServiceImpl;
import io.renren.common.service.impl.CrudServiceImpl;
import io.renren.modules.sys.dao.SysSecComDao;
import io.renren.modules.sys.dto.SysSecComDTO;
import io.renren.modules.sys.entity.SysSecComEntity;
import io.renren.modules.sys.service.SysSecComService;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class SysSecComServiceImpl extends CrudServiceImpl<SysSecComDao, SysSecComEntity, SysSecComDTO> implements SysSecComService {
    @Override
    public QueryWrapper<SysSecComEntity> getWrapper(Map<String, Object> params) {
        return null;
    }
}
