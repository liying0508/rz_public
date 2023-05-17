package io.renren.modules.financing.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.renren.common.service.impl.CrudServiceImpl;
import io.renren.modules.financing.dao.FDirectFinancingApprovalCheckDao;
import io.renren.modules.financing.dao.FDirectFinancingCheckDao;
import io.renren.modules.financing.dto.FDirectFinancingApprovalCheckDTO;
import io.renren.modules.financing.dto.FDirectFinancingCheckDTO;
import io.renren.modules.financing.entity.FDirectFinancingApprovalCheck;
import io.renren.modules.financing.entity.FDirectFinancingCheck;
import io.renren.modules.financing.service.FDirectFinancingCheckService;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class FDirectFinancingCheckServiceImpl
        extends CrudServiceImpl<FDirectFinancingCheckDao, FDirectFinancingCheck, FDirectFinancingCheckDTO>
        implements FDirectFinancingCheckService {
    @Override
    public QueryWrapper<FDirectFinancingCheck> getWrapper(Map<String, Object> params) {
        QueryWrapper<FDirectFinancingCheck> wrapper = new QueryWrapper<>();
        return wrapper;
    }
}
