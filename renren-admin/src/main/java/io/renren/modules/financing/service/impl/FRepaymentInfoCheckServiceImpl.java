package io.renren.modules.financing.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.renren.common.service.impl.CrudServiceImpl;
import io.renren.modules.financing.dao.FRepaymentInfoCheckDao;
import io.renren.modules.financing.dto.FRepaymentInfoCheckDTO;
import io.renren.modules.financing.entity.FRepaymentInfoCheck;
import io.renren.modules.financing.service.FRepaymentInfoCheckService;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class FRepaymentInfoCheckServiceImpl extends
        CrudServiceImpl<FRepaymentInfoCheckDao, FRepaymentInfoCheck, FRepaymentInfoCheckDTO>
        implements FRepaymentInfoCheckService {
    @Override
    public QueryWrapper<FRepaymentInfoCheck> getWrapper(Map<String, Object> params) {
        return null;
    }
}
