package io.renren.modules.financing.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.renren.common.service.impl.CrudServiceImpl;
import io.renren.modules.financing.dao.FCreditRecordCheckDao;
import io.renren.modules.financing.dto.FCreditRecordCheckDTO;
import io.renren.modules.financing.entity.FCreditRecordCheck;
import io.renren.modules.financing.service.FCreditRecordCheckService;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class FCreditRecordCheckServiceImpl extends
        CrudServiceImpl<FCreditRecordCheckDao, FCreditRecordCheck, FCreditRecordCheckDTO>
        implements FCreditRecordCheckService {
    @Override
    public QueryWrapper<FCreditRecordCheck> getWrapper(Map<String, Object> params) {
        QueryWrapper<FCreditRecordCheck> wrapper = new QueryWrapper<>();
        return wrapper;
    }
}
