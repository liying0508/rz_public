package io.renren.modules.report.service;

import io.renren.modules.report.dto.vo.RIndirectFinancingReportVO;

import java.util.List;

public interface RIndirectFinancingReportService {
    /**
     * 获得报表信息
     * @return List
     */
    List<RIndirectFinancingReportVO> getReportInfo();
}
