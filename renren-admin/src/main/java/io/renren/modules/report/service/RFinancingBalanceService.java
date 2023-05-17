package io.renren.modules.report.service;

import io.renren.modules.report.dto.RFinancingBalanceDTO;

import java.util.List;
import java.util.Map;

public interface RFinancingBalanceService {
    //列表
    public List<RFinancingBalanceDTO> listInfo(Map<String, Object> params);
}
