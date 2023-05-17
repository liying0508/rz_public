package io.renren.modules.financing.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.renren.common.service.impl.CrudServiceImpl;
import io.renren.modules.financing.dao.FGovernmentBondCheckDao;
import io.renren.modules.financing.dto.FGovernmentBondCheckDTO;
import io.renren.modules.financing.entity.FGovernmentBondCheckEntity;
import io.renren.modules.financing.service.FGovernmentBondCheckService;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class FGovernmentBondCheckServiceImpl extends
        CrudServiceImpl<FGovernmentBondCheckDao, FGovernmentBondCheckEntity, FGovernmentBondCheckDTO>
        implements FGovernmentBondCheckService {
    @Override
    public QueryWrapper<FGovernmentBondCheckEntity> getWrapper(Map<String, Object> params) {
        QueryWrapper<FGovernmentBondCheckEntity> wrapper = new QueryWrapper<>();
        return wrapper;
    }
}
