package io.renren.modules.report.controller;

import io.renren.common.utils.Result;
import io.renren.modules.report.dto.RCreditMonthDTO;
import io.renren.modules.report.dto.RFinancingBalanceDTO;
import io.renren.modules.report.service.RFinancingBalanceService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("report/financingbalance")
@Api(tags="融资余额表")
public class RFinancingBalanceController {

    @Autowired
    RFinancingBalanceService rFinancingBalanceService;

    @GetMapping("list")
    @ApiOperation("列表")
//    @RequiresPermissions("report:list")
    public Result<List<RFinancingBalanceDTO>> listInfo(@RequestParam Map<String, Object> params){
        List<RFinancingBalanceDTO> listInfo = rFinancingBalanceService.listInfo(params);
        return new Result<List<RFinancingBalanceDTO>>().ok(listInfo);
    }



}
