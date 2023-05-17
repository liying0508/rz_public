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
import io.renren.modules.financing.dto.*;
import io.renren.modules.financing.service.FDirectFinancingAuditService;
import io.renren.modules.security.service.ShiroService;
import io.renren.modules.sys.service.SysRoleDataScopeService;
import io.renren.modules.sys.service.SysRoleUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("financing/audit")
@Api(tags="直融信息审核")
public class FDirectFinancingAuditController {
    @Autowired
    private FDirectFinancingAuditService fDirectFinancingAuditService;

    @Autowired
    ShiroService shiroService;
    @Autowired
    SysRoleDataScopeService sysRoleDataScopeService;
    @Autowired
    SysRoleUserService sysRoleUserService;
    @GetMapping("list")
    @ApiOperation("列表")
//    @RequiresPermissions("financing:approval:list")
    public Result<List<FDirectFinancingAuditDTO>> list(){
        List<FDirectFinancingAuditDTO> list = fDirectFinancingAuditService.listInfo();
        return new Result<List<FDirectFinancingAuditDTO>>().ok(list);
    }

    @GetMapping("page")
    @ApiOperation("分页")
    @ApiImplicitParams({
            @ApiImplicitParam(name = Constant.PAGE, value = "当前页码，从1开始", paramType = "query", required = true, dataType="int") ,
            @ApiImplicitParam(name = Constant.LIMIT, value = "每页显示记录数", paramType = "query",required = true, dataType="int") ,
            @ApiImplicitParam(name = Constant.ORDER_FIELD, value = "排序字段", paramType = "query", dataType="String") ,
            @ApiImplicitParam(name = Constant.ORDER, value = "排序方式，可选值(asc、desc)", paramType = "query", dataType="String")
    })
//    @RequiresPermissions("financing:approval:page")
    public Result<PageData<FDirectFinancingAuditDTO>> page(@ApiIgnore @RequestParam Map<String, Object> params){
        PageData<FDirectFinancingAuditDTO> page = fDirectFinancingAuditService.page(params);
        Integer unit = Integer.valueOf(params.get("unit").toString());
        List<FDirectFinancingAuditDTO> list = page.getList();
        //根据权限过滤掉不属于权限的信息
        ServletRequestAttributes requestAttributes
                = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = requestAttributes.getRequest();
        Long userId = shiroService.getByToken(request.getHeader("token")).getUserId();
        if (userId != 1067246875800000001L){
            List<Long> roleIdList = sysRoleUserService.getRoleIdList(userId);
            for (Long aLong : roleIdList) {
                List<Long> deptIdList = sysRoleDataScopeService.getDeptIdList(aLong);
                list.removeIf(next -> !deptIdList.contains(Long.valueOf(next.getDeptId())));
            }
        }
        List<FDirectFinancingAuditDTO> pageData = fDirectFinancingAuditService.directCast(list, unit);
        List<FDirectFinancingAuditDTO> fDirectFinancingDTOS =
                fDirectFinancingAuditService.screenList(pageData, params.get("approvalAmount").toString());

        page.setList(fDirectFinancingDTOS);

        return new Result<PageData<FDirectFinancingAuditDTO>>().ok(page);
    }

    @GetMapping("{id}/{unit}")
    @ApiOperation("信息")
//    @RequiresPermissions("financing:approval:info")
    public Result<FDirectFinancingAuditDTO> get(@PathVariable("id") Long id,
                                                @PathVariable("unit") Integer unit){
        FDirectFinancingAuditDTO data = fDirectFinancingAuditService.getDealData(id);
        FDirectFinancingAuditDTO fDirectFinancingDTO = fDirectFinancingAuditService.directInfoCast(data, unit);
        return new Result<FDirectFinancingAuditDTO>().ok(fDirectFinancingDTO);
    }

    @PostMapping
    @ApiOperation("保存")
    @LogOperation("保存")
//    @RequiresPermissions("financing:approval:save")
    public Result save(@RequestBody FDirectFinancingAuditDTO dto){
        //效验数据
        ValidatorUtils.validateEntity(dto, AddGroup.class, DefaultGroup.class);
        fDirectFinancingAuditService.saveInfo(dto);
        return new Result();
    }

    @PutMapping
    @ApiOperation("修改")
    @LogOperation("修改")
//    @RequiresPermissions("financing:approval:update")
    public Result update(@RequestBody FDirectFinancingAuditDTO dto){
        //效验数据
        ValidatorUtils.validateEntity(dto, UpdateGroup.class, DefaultGroup.class);
        fDirectFinancingAuditService.updateInfo(dto);
        return new Result();
    }

    @DeleteMapping
    @ApiOperation("删除")
    @LogOperation("删除")
//    @RequiresPermissions("financing:approval:delete")
    public Result delete(@RequestBody Long[] ids){
        //效验数据
        AssertUtils.isArrayEmpty(ids, "id");
        fDirectFinancingAuditService.delete(ids);
        return new Result();
    }

    @PostMapping("check")
    @ApiOperation("审核")
    @LogOperation("审核")
//    @RequiresPermissions("financing:approval:check")
    public Result check(@RequestBody FDirectFinancingCheckDTO dto){
        //效验数据
        ValidatorUtils.validateEntity(dto, AddGroup.class, DefaultGroup.class);
        fDirectFinancingAuditService.check(dto);
        return new Result();
    }

    @PostMapping("groupcheck")
    @ApiOperation("集团审核")
    @LogOperation("集团审核")
//    @RequiresPermissions("financing:approval:check")
    public Result groupCheck(@RequestBody FDirectFinancingCheckDTO dto){
        //效验数据
        ValidatorUtils.validateEntity(dto, AddGroup.class, DefaultGroup.class);
        fDirectFinancingAuditService.groupCheck(dto);
        return new Result();
    }
}
