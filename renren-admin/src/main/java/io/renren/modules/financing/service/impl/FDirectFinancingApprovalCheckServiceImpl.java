package io.renren.modules.financing.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.renren.common.service.impl.CrudServiceImpl;
import io.renren.modules.financing.dao.FDirectFinancingApprovalCheckDao;
import io.renren.modules.financing.dto.FDirectFinancingApprovalCheckDTO;
import io.renren.modules.financing.entity.FDirectFinancingApprovalCheck;
import io.renren.modules.financing.service.FDirectFinancingApprovalCheckService;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class FDirectFinancingApprovalCheckServiceImpl extends CrudServiceImpl<FDirectFinancingApprovalCheckDao, FDirectFinancingApprovalCheck, FDirectFinancingApprovalCheckDTO> implements FDirectFinancingApprovalCheckService {
    @Override
    public QueryWrapper<FDirectFinancingApprovalCheck> getWrapper(Map<String, Object> params) {
        QueryWrapper<FDirectFinancingApprovalCheck> wrapper = new QueryWrapper<>();
        return wrapper;
    }
}
