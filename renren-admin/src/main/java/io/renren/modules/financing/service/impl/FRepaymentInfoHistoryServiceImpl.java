package io.renren.modules.financing.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.renren.common.service.impl.CrudServiceImpl;
import io.renren.modules.financing.dao.FRepaymentInfoHistoryDao;
import io.renren.modules.financing.dto.FRepaymentInfoDTO;
import io.renren.modules.financing.entity.FRepaymentInfo;
import io.renren.modules.financing.entity.FRepaymentInfoHistory;
import io.renren.modules.financing.service.FRepaymentInfoHistoryService;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class FRepaymentInfoHistoryServiceImpl extends CrudServiceImpl<FRepaymentInfoHistoryDao, FRepaymentInfoHistory, FRepaymentInfoDTO>
        implements FRepaymentInfoHistoryService {
    @Override
    public QueryWrapper<FRepaymentInfoHistory> getWrapper(Map<String, Object> params) {
        QueryWrapper<FRepaymentInfoHistory> wrapper = new QueryWrapper<>();
        return wrapper;
    }
}
