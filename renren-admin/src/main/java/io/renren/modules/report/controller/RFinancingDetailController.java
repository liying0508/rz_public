package io.renren.modules.report.controller;

import io.renren.common.utils.Result;
import io.renren.modules.report.dto.RFinancingDetailDTO;
import io.renren.modules.report.service.RFinancingDetailService;
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
@RequestMapping("report/financingDetail")
@Api(tags="融资明细表")
public class RFinancingDetailController {
    @Autowired
    RFinancingDetailService rFinancingDetailService;

    @GetMapping("list")
    @ApiOperation("列表")
//    @RequiresPermissions("report:list")
    public Result<List<RFinancingDetailDTO>> listInfo(@RequestParam Map<String, Object> params){
        List<RFinancingDetailDTO> listInfo = rFinancingDetailService.listInfo(params);
        return new Result<List<RFinancingDetailDTO>>().ok(listInfo);
    }
}
