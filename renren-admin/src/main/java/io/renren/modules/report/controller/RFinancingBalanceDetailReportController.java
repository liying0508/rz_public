package io.renren.modules.report.controller;

import io.renren.common.utils.Result;
import io.renren.modules.report.dto.vo.RFinancingBalanceDetailReportVO;
import io.renren.modules.report.service.RFinancingBalanceDetailReportService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("report/balanceDetail")
@Api(tags="融资余额明细表")
public class RFinancingBalanceDetailReportController {
    @Autowired
    RFinancingBalanceDetailReportService rFinancingBalanceDetailReportService;

    @GetMapping("list")
    @ApiOperation("融资余额明细表")
    public Result<List<RFinancingBalanceDetailReportVO>> list(){
        List<RFinancingBalanceDetailReportVO> rFinancingBalanceDetailReportVOS = rFinancingBalanceDetailReportService.listInfo();
        return new Result<List<RFinancingBalanceDetailReportVO>>().ok(rFinancingBalanceDetailReportVOS);
    }
}
