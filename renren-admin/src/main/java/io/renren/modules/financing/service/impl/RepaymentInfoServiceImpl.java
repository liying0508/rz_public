package io.renren.modules.financing.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.renren.common.service.impl.CrudServiceImpl;
import io.renren.modules.financing.dao.RepaymentInfoDao;
import io.renren.modules.financing.dto.RepaymentInfoDTO;
import io.renren.modules.financing.entity.FRepaymentInfoTableEntity;
import io.renren.modules.financing.service.RepaymentInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class RepaymentInfoServiceImpl extends CrudServiceImpl<RepaymentInfoDao, FRepaymentInfoTableEntity, RepaymentInfoDTO>
        implements RepaymentInfoService {
    @Autowired
    private RepaymentInfoDao repaymentInfoDao;

    @Override
    public QueryWrapper<FRepaymentInfoTableEntity> getWrapper(Map<String, Object> params) {
        return null;
    }

    @Override
    public void delData(Long financingId, Integer financingType) {
        repaymentInfoDao.delData(financingId,financingType);
    }

    @Override
    public List<RepaymentInfoDTO> selectData(Long financingId, Integer financingType) {
        return repaymentInfoDao.selectData(financingId,financingType);
    }
}
