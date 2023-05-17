package io.renren.modules.report.controller;

import io.renren.common.utils.Result;
import io.renren.modules.report.dto.vo.RIndirectFinancingReportVO;
import io.renren.modules.report.service.RIndirectFinancingReportService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * @Author: Eddy
 * @Date: 2022/12/28 0028
 * @Time: 16:52
 * @Description: 银行间接融资明细表
 */
@RestController
@RequestMapping("report/Indirect")
@Api(tags="银行间接融资明细表")
public class RIndirectFinancingReportController {
    @Autowired
    RIndirectFinancingReportService rIndirectFinancingReportService;

    @GetMapping("list")
    @ApiOperation("报表信息")
//    @RequiresPermissions("report:list")
    public Result<List<RIndirectFinancingReportVO>> listInfo(@RequestParam Map<String, Object> params){
        List<RIndirectFinancingReportVO> reportInfo = rIndirectFinancingReportService.getReportInfo();
        return new Result<List<RIndirectFinancingReportVO>>().ok(reportInfo);
    }
}
