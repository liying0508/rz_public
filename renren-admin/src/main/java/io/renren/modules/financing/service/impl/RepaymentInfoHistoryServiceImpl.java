package io.renren.modules.financing.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.renren.common.service.impl.CrudServiceImpl;
import io.renren.modules.financing.dao.RepaymentInfoHistoryDao;
import io.renren.modules.financing.dto.RepaymentInfoHistoryDTO;
import io.renren.modules.financing.entity.FRepaymentInfoTableHistoryEntity;
import io.renren.modules.financing.service.RepaymentInfoHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class RepaymentInfoHistoryServiceImpl extends
        CrudServiceImpl<RepaymentInfoHistoryDao, FRepaymentInfoTableHistoryEntity, RepaymentInfoHistoryDTO>
        implements RepaymentInfoHistoryService {
    @Autowired
    private RepaymentInfoHistoryDao repaymentInfoHistoryDao;

    @Override
    public QueryWrapper<FRepaymentInfoTableHistoryEntity> getWrapper(Map<String, Object> params) {
        return null;
    }

    @Override
    public List<RepaymentInfoHistoryDTO> selectData(Long financingId, Integer financingType) {
        return repaymentInfoHistoryDao.selectData(financingId,financingType);
    }
}
