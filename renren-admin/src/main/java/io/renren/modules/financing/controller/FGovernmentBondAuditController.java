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
import io.renren.modules.financing.dto.FGovernmentBondAuditDTO;
import io.renren.modules.financing.dto.FGovernmentBondCheckDTO;
import io.renren.modules.financing.service.FGovernmentBondAuditService;
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
@RequestMapping("financing/governmentbondaudit")
@Api(tags="政府债券审核")
public class FGovernmentBondAuditController {
    @Autowired
    private FGovernmentBondAuditService fGovernmentBondAuditService;

//    @GetMapping("list")
//    @ApiOperation("列表")
////    @RequiresPermissions("financing:approval:list")
//    public Result<List<FGovernmentBondAuditDTO>> list(){
//        List<FGovernmentBondAuditDTO> list = fGovernmentBondAuditService.listInfo();
////        List<FDirectFinancingDTO> list = F
//        System.out.println("wcnm");
//        return new Result<List<FGovernmentBondAuditDTO>>().ok(list);
//    }

    @GetMapping("page")
    @ApiOperation("分页")
    @ApiImplicitParams({
            @ApiImplicitParam(name = Constant.PAGE, value = "当前页码，从1开始", paramType = "query", required = true, dataType="int") ,
            @ApiImplicitParam(name = Constant.LIMIT, value = "每页显示记录数", paramType = "query",required = true, dataType="int") ,
            @ApiImplicitParam(name = Constant.ORDER_FIELD, value = "排序字段", paramType = "query", dataType="String") ,
            @ApiImplicitParam(name = Constant.ORDER, value = "排序方式，可选值(asc、desc)", paramType = "query", dataType="String")
    })
//    @RequiresPermissions("financing:approval:page")
    public Result<PageData<FGovernmentBondAuditDTO>> page(@ApiIgnore @RequestParam Map<String, Object> params){
        PageData<FGovernmentBondAuditDTO> page = fGovernmentBondAuditService.page(params);
        return new Result<PageData<FGovernmentBondAuditDTO>>().ok(page);
    }

    @GetMapping("{id}")
    @ApiOperation("信息")
//    @RequiresPermissions("financing:approval:info")
    public Result<FGovernmentBondAuditDTO> get(@PathVariable("id") Long id){
        FGovernmentBondAuditDTO data = fGovernmentBondAuditService.getDealData(id);
        return new Result<FGovernmentBondAuditDTO>().ok(data);
    }

    @PostMapping("check")
    @ApiOperation("审核")
    @LogOperation("审核")
//    @RequiresPermissions("financing:approval:check")
    public Result check(@RequestBody FGovernmentBondCheckDTO dto){
        //效验数据
        ValidatorUtils.validateEntity(dto, AddGroup.class, DefaultGroup.class);
        fGovernmentBondAuditService.check(dto);

        return new Result();
    }

    @PostMapping("groupcheck")
    @ApiOperation("审核")
    @LogOperation("审核")
//    @RequiresPermissions("financing:approval:check")
    public Result groupCheck(@RequestBody FGovernmentBondCheckDTO dto){
        //效验数据
        ValidatorUtils.validateEntity(dto, AddGroup.class, DefaultGroup.class);
        fGovernmentBondAuditService.groupCheck(dto);

        return new Result();
    }
}
