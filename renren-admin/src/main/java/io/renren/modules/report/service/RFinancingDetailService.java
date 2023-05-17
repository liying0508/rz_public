package io.renren.modules.report.service;
import io.renren.modules.report.dto.RFinancingDetailDTO;

import java.util.List;
import java.util.Map;

public interface RFinancingDetailService {
    //列表
    public List<RFinancingDetailDTO> listInfo(Map<String, Object> params);
}
