package io.renren.modules.report.service;

import io.renren.modules.report.dto.vo.RFinancingBalanceDetailReportVO;

import java.util.List;

public interface RFinancingBalanceDetailReportService {
    /**
     * 获得报表信息
     * @return List
     */
    List<RFinancingBalanceDetailReportVO> listInfo();
}
