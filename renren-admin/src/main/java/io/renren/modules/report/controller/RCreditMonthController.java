package io.renren.modules.report.controller;

import io.renren.common.utils.Result;
import io.renren.modules.report.dao.RCreditMonthDao;
import io.renren.modules.report.dto.RCreditMonthDTO;
import io.renren.modules.report.service.RCreditMonthService;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("report/credit")
@Api(tags="银行授信月度报表")
public class RCreditMonthController {

    @Autowired
    private RCreditMonthService rCreditMonthService;

    @GetMapping("list")
    @ApiOperation("列表")
//    @RequiresPermissions("report:list")
    public Result<List<RCreditMonthDTO>> listInfo(@RequestParam Map<String, Object> params){
        List<RCreditMonthDTO> listInfo = rCreditMonthService.listInfo(params);
        return new Result<List<RCreditMonthDTO>>().ok(listInfo);
    }

}
