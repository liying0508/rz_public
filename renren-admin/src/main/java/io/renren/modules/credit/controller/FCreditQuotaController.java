package io.renren.modules.credit.controller;

import io.renren.common.annotation.LogOperation;
import io.renren.common.utils.Result;
import io.renren.common.validator.AssertUtils;
import io.renren.common.validator.ValidatorUtils;
import io.renren.common.validator.group.AddGroup;
import io.renren.common.validator.group.DefaultGroup;
import io.renren.common.validator.group.UpdateGroup;
import io.renren.modules.credit.dto.FCreditQuotaDTO;
import io.renren.modules.credit.service.FCreditQuotaService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.List;
import java.util.Map;

/**
 * @Author: Eddy
 * @Date: 2022/11/17
 * @Time: 17:18
 * @Description:
 */
@RestController
@RequestMapping("creditQuota")
@Api(tags="授信总额")
public class FCreditQuotaController {

    @Autowired
    private FCreditQuotaService fCreditQuotaService;

    @GetMapping("list")
    @ApiOperation("列表")
    @RequiresPermissions("creditQuota:creditQuota:list")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "month",value = "查询的年份以及月份",paramType = "query",required = true,dataType = "Date")
    })
    public Result<List<FCreditQuotaDTO>> List(@ApiIgnore @RequestParam Map<String, Object> params){
        List<FCreditQuotaDTO> list = fCreditQuotaService.listInfo(params);
        return new Result<List<FCreditQuotaDTO>>().ok(list);
    }

//    @GetMapping("page")
//    @ApiOperation("分页")
//    @ApiImplicitParams({
//            @ApiImplicitParam(name = Constant.PAGE, value = "当前页码，从1开始", paramType = "query", required = true, dataType="int") ,
//            @ApiImplicitParam(name = Constant.LIMIT, value = "每页显示记录数", paramType = "query",required = true, dataType="int") ,
//            @ApiImplicitParam(name = Constant.ORDER_FIELD, value = "排序字段", paramType = "query", dataType="String") ,
//            @ApiImplicitParam(name = Constant.ORDER, value = "排序方式，可选值(asc、desc)", paramType = "query", dataType="String")
//    })
//    @RequiresPermissions("creditQuota:creditQuota:list")
//    public Result<PageData<FCreditQuotaDTO>> page(@ApiIgnore @RequestParam Map<String, Object> params){
//        PageData<FCreditQuotaDTO> page = fCreditQuotaService.page(params);
//
//        page.setList(fCreditQuotaService.pageDeal(page.getList()));
//        return new Result<PageData<FCreditQuotaDTO>>().ok(page);
//    }

    @GetMapping("{id}")
    @ApiOperation("信息")
    @RequiresPermissions("creditQuota:creditQuota:info")
    public Result<FCreditQuotaDTO> get(@PathVariable("id") Long id){
        FCreditQuotaDTO dto = fCreditQuotaService.dtoDeal(id);
        return new Result<FCreditQuotaDTO>().ok(dto);
    }

    @PostMapping
    @ApiOperation("保存")
    @LogOperation("保存")
    @RequiresPermissions("creditQuota:creditQuota:save")
    public Result save(@RequestBody FCreditQuotaDTO dto){
        //效验数据
        ValidatorUtils.validateEntity(dto, AddGroup.class, DefaultGroup.class);

        fCreditQuotaService.saveInfo(dto);

        return new Result();
    }

    @PutMapping
    @ApiOperation("修改")
    @LogOperation("修改")
    @RequiresPermissions("creditQuota:creditQuota:update")
    public Result update(@RequestBody FCreditQuotaDTO dto){
        //效验数据
        ValidatorUtils.validateEntity(dto, UpdateGroup.class, DefaultGroup.class);

        fCreditQuotaService.updateInfo(dto);

        return new Result();
    }

    @DeleteMapping
    @ApiOperation("删除")
    @LogOperation("删除")
    @RequiresPermissions("creditQuota:creditQuota:delete")
    public Result delete(@RequestBody Long[] ids){
        //效验数据
        AssertUtils.isArrayEmpty(ids, "id");

        fCreditQuotaService.deleteAllInfo(ids);

        return new Result();
    }
}
