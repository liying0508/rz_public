package io.renren.modules.credit.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.renren.common.service.impl.CrudServiceImpl;
import io.renren.modules.credit.dao.FCreditQuotaHistoryDao;
import io.renren.modules.credit.dto.FCreditQuotaHistoryDTO;
import io.renren.modules.credit.entity.FCreditQuotaHistoryEntity;
import io.renren.modules.credit.service.FCreditQuotaHistoryService;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class FCreditQuotaHistoryServiceImpl extends CrudServiceImpl<FCreditQuotaHistoryDao, FCreditQuotaHistoryEntity, FCreditQuotaHistoryDTO>
        implements FCreditQuotaHistoryService {
    @Override
    public QueryWrapper<FCreditQuotaHistoryEntity> getWrapper(Map<String, Object> params) {
        return null;
    }
}
