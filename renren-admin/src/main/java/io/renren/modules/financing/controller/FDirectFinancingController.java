package io.renren.modules.financing.controller;

import io.renren.common.annotation.LogOperation;
import io.renren.common.constant.Constant;
import io.renren.common.exception.RenException;
import io.renren.common.page.PageData;
import io.renren.common.utils.ExcelUtils;
import io.renren.common.utils.Result;
import io.renren.common.validator.AssertUtils;
import io.renren.common.validator.ValidatorUtils;
import io.renren.common.validator.group.AddGroup;
import io.renren.common.validator.group.DefaultGroup;
import io.renren.common.validator.group.UpdateGroup;
import io.renren.modules.financing.dto.FCreditPageDto;
import io.renren.modules.financing.dto.FDirectFinancingDTO;
import io.renren.modules.financing.dto.FRepaymentPlanDTO;
import io.renren.modules.financing.excel.FDirectFinancingExcel;
import io.renren.modules.financing.service.FCreditRecordService;
import io.renren.modules.financing.service.FDirectFinancingService;
import io.renren.modules.financing.service.FFinanceDeptService;
import io.renren.modules.financing.vo.IndirectInterestVO;
import io.renren.modules.security.service.ShiroService;
import io.renren.modules.sys.service.SysRoleDataScopeService;
import io.renren.modules.sys.service.SysRoleUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
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
* 直接融资信息
*
* @author liuwenyong 
* @since 3.0 2022-05-14
*/
@RestController
@RequestMapping("financing/fdirectfinancing")
@Api(tags="直接融资信息")
public class FDirectFinancingController {
    @Autowired
    FDirectFinancingService fDirectFinancingService;
    @Autowired
    FCreditRecordService fCreditRecordService;
    @Autowired
    FFinanceDeptService fFinanceDeptService;
    @Autowired
    ShiroService shiroService;
    @Autowired
    SysRoleDataScopeService sysRoleDataScopeService;
    @Autowired
    SysRoleUserService sysRoleUserService;

    @GetMapping("list")
    @ApiOperation("列表")
//    @RequiresPermissions("financing:fdirectfinancing:list")
    public Result<List<FCreditPageDto>> list(){
        List<FCreditPageDto> list = fCreditRecordService.listInfo();
        return new Result<List<FCreditPageDto>>().ok(list);
    }

    @GetMapping("page")
    @ApiOperation("分页")
    @ApiImplicitParams({
        @ApiImplicitParam(name = Constant.PAGE, value = "当前页码，从1开始", paramType = "query", required = true, dataType="int") ,
        @ApiImplicitParam(name = Constant.LIMIT, value = "每页显示记录数", paramType = "query",required = true, dataType="int") ,
        @ApiImplicitParam(name = Constant.ORDER_FIELD, value = "排序字段", paramType = "query", dataType="String") ,
        @ApiImplicitParam(name = Constant.ORDER, value = "排序方式，可选值(asc、desc)", paramType = "query", dataType="String")
    })
    @RequiresPermissions("financing:fdirectfinancing:page")
    public Result<PageData<FDirectFinancingDTO>> page(@ApiIgnore @RequestParam Map<String, Object> params){
        PageData<FDirectFinancingDTO> page = fDirectFinancingService.page(params);
        Integer unit = Integer.valueOf(params.get("unit").toString());
        List<FDirectFinancingDTO> list = page.getList();
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
        List<FDirectFinancingDTO> pageData = fDirectFinancingService.directCast(list, unit);
        List<FDirectFinancingDTO> fDirectFinancingDTOS =
                fDirectFinancingService.screenList(pageData, params.get("approvalAmount").toString());

        page.setList(fDirectFinancingDTOS);
        //修改total值
        page.setTotal(page.getList().size());
        return new Result<PageData<FDirectFinancingDTO>>().ok(page);
    }

    @GetMapping("{id}/{unit}")
    @ApiOperation("信息")
    @RequiresPermissions("financing:fdirectfinancing:info")
    public Result<FDirectFinancingDTO> get(@PathVariable("id") Long id,
                                           @PathVariable("unit") Integer unit){
        FDirectFinancingDTO data = fDirectFinancingService.getDealData(id);
        FDirectFinancingDTO fDirectFinancingDTO = fDirectFinancingService.directInfoCast(data, unit);
        return new Result<FDirectFinancingDTO>().ok(fDirectFinancingDTO);
    }

    @PostMapping
    @ApiOperation("保存")
    @LogOperation("保存")
    @RequiresPermissions("financing:fdirectfinancing:save")
    public Result save(@RequestBody FDirectFinancingDTO dto){
        List<String> creditMeasuresList = dto.getCreditMeasuresList();
//      效验数据
        ValidatorUtils.validateEntity(dto, AddGroup.class, DefaultGroup.class);
        FDirectFinancingDTO fDirectFinancingDTO = fDirectFinancingService.directSaveCast(dto, dto.getUnit());
        fDirectFinancingService.saveInfo(fDirectFinancingDTO);
        return new Result();
    }

    @PutMapping
    @ApiOperation("修改")
    @LogOperation("修改")
    @RequiresPermissions("financing:fdirectfinancing:update")
    public Result update(@RequestBody FDirectFinancingDTO dto){
        //效验数据
        ValidatorUtils.validateEntity(dto, UpdateGroup.class, DefaultGroup.class);
        FDirectFinancingDTO fDirectFinancingDTO = fDirectFinancingService.directSaveCast(dto, dto.getUnit());
        fDirectFinancingService.updateInfo(fDirectFinancingDTO);
        return new Result();
    }

    @DeleteMapping
    @ApiOperation("删除")
    @LogOperation("删除")
    @RequiresPermissions("financing:fdirectfinancing:delete")
    public Result delete(@RequestBody Long[] ids){
        //效验数据
        AssertUtils.isArrayEmpty(ids, "id");
        fDirectFinancingService.deleteInfo(ids);
        return new Result();
    }

    @PostMapping("/interest")
    @ApiOperation("利息测算")
    @LogOperation("利息测算")
    @RequiresPermissions("financing:findirectfinancing:interest")
    public Result interestCalculate(@RequestBody IndirectInterestVO vo){
        //效验数据
        ValidatorUtils.validateEntity(vo, AddGroup.class, DefaultGroup.class);
        List<FRepaymentPlanDTO> data = fDirectFinancingService.interestCalculate(vo);
        return new Result<List<FRepaymentPlanDTO>>().ok(data);
    }

    @GetMapping("export")
    @ApiOperation("导出")
    @LogOperation("导出")
    @RequiresPermissions("financing:fdirectfinancing:export")
    public void export(@ApiIgnore @RequestParam Map<String, Object> params, HttpServletResponse response) throws Exception {
        List<FDirectFinancingDTO> list = fDirectFinancingService.list(params);
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
        String approvalAmount = params.get("approvalAmount").toString();
        List<FDirectFinancingExcel> exportData = fDirectFinancingService.getExportData(list, approvalAmount);
        if (Integer.valueOf(params.get("unit").toString()) == 2){
            throw new Exception();
        }
        ExcelUtils.exportExcelToTarget(response, null, "直接融资信息", exportData, FDirectFinancingExcel.class);
    }



}