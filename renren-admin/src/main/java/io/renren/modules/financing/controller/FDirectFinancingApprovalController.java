package io.renren.modules.financing.controller;

import io.renren.common.annotation.LogOperation;
import io.renren.common.constant.Constant;
import io.renren.common.page.PageData;
import io.renren.common.utils.ExcelUtils;
import io.renren.common.utils.Result;
import io.renren.common.validator.AssertUtils;
import io.renren.common.validator.ValidatorUtils;
import io.renren.common.validator.group.AddGroup;
import io.renren.common.validator.group.DefaultGroup;
import io.renren.common.validator.group.UpdateGroup;
import io.renren.modules.financing.dto.*;
import io.renren.modules.financing.excel.FDirectFinancingApprovalExcel;
import io.renren.modules.financing.excel.FDirectFinancingExcel;
import io.renren.modules.financing.service.FDirectFinancingApprovalService;
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
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * @Author gaby
 * @Date 2022/6/27 18:17
 * @Description
 * @Version 1.0
 * @ClassName FDirectFinancingApproval
 **/
@RestController
@RequestMapping("financing/approval")
@Api(tags="直融批文")
public class FDirectFinancingApprovalController {

    @Autowired
    private FDirectFinancingApprovalService fDirectFinancingApprovalService;
    @Autowired
    ShiroService shiroService;
    @Autowired
    SysRoleDataScopeService sysRoleDataScopeService;
    @Autowired
    SysRoleUserService sysRoleUserService;

    @GetMapping("list")
    @ApiOperation("列表")
//    @RequiresPermissions("financing:approval:list")
    public Result<List<FDirectFinancingApprovalDTO>> list(){
        List<FDirectFinancingApprovalDTO> list = fDirectFinancingApprovalService.listInfo();
        return new Result<List<FDirectFinancingApprovalDTO>>().ok(list);
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
    public Result<PageData<FDirectFinancingApprovalDTO>> page(@ApiIgnore @RequestParam Map<String, Object> params){
        PageData<FDirectFinancingApprovalDTO> page = fDirectFinancingApprovalService.page(params);
        List<FDirectFinancingApprovalDTO> list = page.getList();
        for (FDirectFinancingApprovalDTO fDirectFinancingApprovalDTO : list) {
            FDirectFinancingApprovalDTO unit = fDirectFinancingApprovalService.
                    directInfoCast(fDirectFinancingApprovalDTO, Integer.valueOf(params.get("unit").toString()));
            fDirectFinancingApprovalDTO.setIssueQuota(unit.getIssueQuota());
        }
        page.setList(list);
        //根据权限过滤掉不属于权限的信息
        ServletRequestAttributes requestAttributes
                = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = requestAttributes.getRequest();
        Long userId = shiroService.getByToken(request.getHeader("token")).getUserId();
        if (userId != 1067246875800000001L){
            List<Long> roleIdList = sysRoleUserService.getRoleIdList(userId);
            for (Long aLong : roleIdList) {
                List<Long> deptIdList = sysRoleDataScopeService.getDeptIdList(aLong);
                list.removeIf(next -> !deptIdList.contains(next.getDeptId()));
            }
        }
        return new Result<PageData<FDirectFinancingApprovalDTO>>().ok(page);
    }

    @GetMapping("{id}/{unit}")
    @ApiOperation("信息")
//    @RequiresPermissions("financing:approval:info")
    public Result<FDirectFinancingApprovalDTO> get(@PathVariable("id") Long id,
                                                   @PathVariable("unit") Integer unit){
        FDirectFinancingApprovalDTO data = fDirectFinancingApprovalService.getDealData(id);
        FDirectFinancingApprovalDTO fDirectFinancingApprovalDTO = fDirectFinancingApprovalService.directInfoCast(data, unit);
        return new Result<FDirectFinancingApprovalDTO>().ok(fDirectFinancingApprovalDTO);
    }

    @PostMapping
    @ApiOperation("保存")
    @LogOperation("保存")
//    @RequiresPermissions("financing:approval:save")
    public Result save(@RequestBody FDirectFinancingApprovalDTO dto){
//        //效验数据
//        ValidatorUtils.validateEntity(dto, AddGroup.class, DefaultGroup.class);
        FDirectFinancingApprovalDTO fDirectFinancingApprovalDTO = fDirectFinancingApprovalService.directSaveCast(dto, dto.getUnit());
        fDirectFinancingApprovalService.saveInfo(fDirectFinancingApprovalDTO);
        return new Result();
    }

    @PutMapping
    @ApiOperation("修改")
    @LogOperation("修改")
//    @RequiresPermissions("financing:approval:update")
    public Result update(@RequestBody FDirectFinancingApprovalDTO dto){
        //效验数据
        ValidatorUtils.validateEntity(dto, UpdateGroup.class, DefaultGroup.class);
        FDirectFinancingApprovalDTO fDirectFinancingApprovalDTO = fDirectFinancingApprovalService.directSaveCast(dto, dto.getUnit());
        fDirectFinancingApprovalService.updateInfo(fDirectFinancingApprovalDTO);
        return new Result();
    }

    @DeleteMapping
    @ApiOperation("删除")
    @LogOperation("删除")
//    @RequiresPermissions("financing:approval:delete")
    public Result delete(@RequestBody Long[] ids){
        //效验数据
        AssertUtils.isArrayEmpty(ids, "id");
        fDirectFinancingApprovalService.deleteInfo(ids);
        return new Result();
    }

    @PostMapping("check")
    @ApiOperation("审核")
    @LogOperation("审核")
//    @RequiresPermissions("financing:approval:check")
    public Result check(@RequestBody FDirectFinancingApprovalCheckDTO dto){
        //效验数据
        ValidatorUtils.validateEntity(dto, AddGroup.class, DefaultGroup.class);
        fDirectFinancingApprovalService.check(dto);
        return new Result();
    }

    @PostMapping("groupcheck")
    @ApiOperation("审核")
    @LogOperation("审核")
//    @RequiresPermissions("financing:approval:check")
    public Result groupCheck(@RequestBody FDirectFinancingApprovalCheckDTO dto){
        //效验数据
        ValidatorUtils.validateEntity(dto, AddGroup.class, DefaultGroup.class);
        fDirectFinancingApprovalService.groupCheck(dto);
        return new Result();
    }

    @GetMapping("export")
    @ApiOperation("导出")
    @LogOperation("导出")
    public void export(@ApiIgnore @RequestParam Map<String, Object> params, HttpServletResponse response) throws Exception {
        List<FDirectFinancingApprovalDTO> list = fDirectFinancingApprovalService.list(params);
        //根据权限过滤掉不属于权限的信息
        ServletRequestAttributes requestAttributes
                = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = requestAttributes.getRequest();
        Long userId = shiroService.getByToken(params.get("token").toString()).getUserId();
        if (userId != 1067246875800000001L){
            List<Long> roleIdList = sysRoleUserService.getRoleIdList(userId);
            for (Long aLong : roleIdList) {
                List<Long> deptIdList = sysRoleDataScopeService.getDeptIdList(aLong);
                list.removeIf(next -> !deptIdList.contains(Long.valueOf(next.getDeptId())));
            }
        }
        if (Integer.valueOf(params.get("unit").toString()) == 2){
            throw new Exception();
        }
        ExcelUtils.exportExcelToTarget(response, "直融批文信息表", "直融批文表", list, FDirectFinancingApprovalExcel.class);
    }
}
