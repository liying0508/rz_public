package io.renren.modules.financing.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.renren.common.service.impl.CrudServiceImpl;
import io.renren.modules.financing.dao.FIndirectFinancingCheckDao;
import io.renren.modules.financing.dto.FIndirectFinancingCheckDTO;
import io.renren.modules.financing.entity.FDirectFinancingCheck;
import io.renren.modules.financing.entity.FIndirectFinancingCheck;
import io.renren.modules.financing.service.FIndirectFinancingCheckService;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class FIndirectFinancingCheckServiceImpl
        extends CrudServiceImpl<FIndirectFinancingCheckDao, FIndirectFinancingCheck, FIndirectFinancingCheckDTO>
        implements FIndirectFinancingCheckService {
    @Override
    public QueryWrapper<FIndirectFinancingCheck> getWrapper(Map<String, Object> params) {
        QueryWrapper<FIndirectFinancingCheck> wrapper = new QueryWrapper<>();
        return wrapper;
    }
}
