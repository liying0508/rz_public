package io.renren.modules.report.service;

import io.renren.modules.report.dto.RCreditMonthDTO;

import java.util.List;
import java.util.Map;

public interface RCreditMonthService {

    //查询所有的数据
    public List<RCreditMonthDTO> listInfo(Map<String, Object> params);
}
