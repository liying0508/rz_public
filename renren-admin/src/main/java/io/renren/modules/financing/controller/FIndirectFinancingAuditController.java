package io.renren.modules.financing.controller;

import io.renren.common.annotation.LogOperation;
import io.renren.common.constant.Constant;
import io.renren.common.page.PageData;
import io.renren.common.utils.Result;
import io.renren.common.validator.AssertUtils;
import io.renren.common.validator.ValidatorUtils;
import io.renren.common.validator.group.AddGroup;
import io.renren.common.validator.group.DefaultGroup;
import io.renren.common.validator.group.UpdateGroup;
import io.renren.modules.financing.dto.FDirectFinancingAuditDTO;
import io.renren.modules.financing.dto.FDirectFinancingCheckDTO;
import io.renren.modules.financing.dto.FIndirectFinancingAuditDTO;
import io.renren.modules.financing.dto.FIndirectFinancingCheckDTO;
import io.renren.modules.financing.service.FDirectFinancingAuditService;
import io.renren.modules.financing.service.FIndirectFinancingAuditService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/financing/findirectfinancingaudit")
@Api(tags="间融信息审核")
public class FIndirectFinancingAuditController {
    @Autowired
    private FIndirectFinancingAuditService fIndirectFinancingAuditService;

    @GetMapping("{id}")
    @ApiOperation("信息")
//    @RequiresPermissions("financing:approval:info")
    public Result<FIndirectFinancingAuditDTO> get(@PathVariable("id") Long id){
        FIndirectFinancingAuditDTO data = fIndirectFinancingAuditService.getDealData(id);
        return new Result<FIndirectFinancingAuditDTO>().ok(data);
    }

    @PostMapping("check")
    @ApiOperation("审核")
    @LogOperation("审核")
//    @RequiresPermissions("financing:approval:check")
    public Result check(@RequestBody FIndirectFinancingCheckDTO dto){
        //效验数据
        ValidatorUtils.validateEntity(dto, AddGroup.class, DefaultGroup.class);
        fIndirectFinancingAuditService.check(dto);
        return new Result();
    }

    @PostMapping("groupcheck")
    @ApiOperation("审核")
    @LogOperation("审核")
//    @RequiresPermissions("financing:approval:check")
    public Result groupCheck(@RequestBody FIndirectFinancingCheckDTO dto){
        //效验数据
        ValidatorUtils.validateEntity(dto, AddGroup.class, DefaultGroup.class);
        fIndirectFinancingAuditService.groupCheck(dto);
        return new Result();
    }
}
