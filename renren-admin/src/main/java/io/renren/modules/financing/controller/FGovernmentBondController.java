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
import io.renren.modules.financing.dto.FGovernmentBondDTO;
import io.renren.modules.financing.dto.FRepaymentPlanDTO;
import io.renren.modules.financing.excel.FGovernmentBondExcel;
import io.renren.modules.financing.service.FFinanceDeptService;
import io.renren.modules.financing.service.FGovernmentBondService;
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
import java.util.Iterator;
import java.util.List;
import java.util.Map;


/**
* 政府债券
*
* @author liuwenyong 
* @since 3.0 2022-05-14
*/
@RestController
@RequestMapping("financing/fgovernmentbond")
@Api(tags="政府债券")
public class FGovernmentBondController {
    @Autowired
    private FGovernmentBondService fGovernmentBondService;

    @Autowired
    ShiroService shiroService;
    @Autowired
    SysRoleDataScopeService sysRoleDataScopeService;
    @Autowired
    SysRoleUserService sysRoleUserService;
    @Autowired
    FFinanceDeptService fFinanceDeptService;

    @GetMapping("page")
    @ApiOperation("分页")
    @ApiImplicitParams({
        @ApiImplicitParam(name = Constant.PAGE, value = "当前页码，从1开始", paramType = "query", required = true, dataType="int") ,
        @ApiImplicitParam(name = Constant.LIMIT, value = "每页显示记录数", paramType = "query",required = true, dataType="int") ,
        @ApiImplicitParam(name = Constant.ORDER_FIELD, value = "排序字段", paramType = "query", dataType="String") ,
        @ApiImplicitParam(name = Constant.ORDER, value = "排序方式，可选值(asc、desc)", paramType = "query", dataType="String")
    })
    @RequiresPermissions("financing:fgovernmentbond:page")
    public Result<PageData<FGovernmentBondDTO>> page(@ApiIgnore @RequestParam Map<String, Object> params){

        PageData<FGovernmentBondDTO> page = fGovernmentBondService.page(params);
        Integer unit = Integer.valueOf(params.get("unit").toString());

        List<FGovernmentBondDTO> list = page.getList();
        //根据权限过滤掉不属于权限的信息
        ServletRequestAttributes requestAttributes
                = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = requestAttributes.getRequest();
        Long userId = shiroService.getByToken(request.getHeader("token")).getUserId();

        if (userId != 1067246875800000001L){
            List<Long> roleIdList = sysRoleUserService.getRoleIdList(userId);
            for (Long aLong : roleIdList) {
                List<Long> deptIdList = sysRoleDataScopeService.getDeptIdList(aLong);
                list.removeIf(next -> !deptIdList.contains(next.getCreditId()));
            }
        }
        for (FGovernmentBondDTO dto : list) {
            dto.setInstitutionName(fFinanceDeptService.getInstitutionNameById(dto.getInstitution()));
            dto = fGovernmentBondService.directInfoCast(dto, unit);
        }
        return new Result<PageData<FGovernmentBondDTO>>().ok(page);
    }

    @GetMapping("{id}/{unit}")
    @ApiOperation("信息")
    @RequiresPermissions("financing:fgovernmentbond:info")
    public Result<FGovernmentBondDTO> get(@PathVariable("id") Long id,
                                          @PathVariable("unit") Integer unit){
        FGovernmentBondDTO data = fGovernmentBondService.getDealData(id);
        FGovernmentBondDTO fGovernmentBondDTO = fGovernmentBondService.directInfoCast(data, unit);
        return new Result<FGovernmentBondDTO>().ok(fGovernmentBondDTO);
    }

    @PostMapping
    @ApiOperation("保存")
    @LogOperation("保存")
    @RequiresPermissions("financing:fgovernmentbond:save")
    public Result save(@RequestBody FGovernmentBondDTO dto){
        //效验数据
        ValidatorUtils.validateEntity(dto, AddGroup.class, DefaultGroup.class);

        fGovernmentBondService.saveInfo(dto);

        return new Result();
    }

    @PutMapping
    @ApiOperation("修改")
    @LogOperation("修改")
    @RequiresPermissions("financing:fgovernmentbond:update")
    public Result update(@RequestBody FGovernmentBondDTO dto){
        //效验数据
        ValidatorUtils.validateEntity(dto, UpdateGroup.class, DefaultGroup.class);

        fGovernmentBondService.updateInfo(dto);

        return new Result();
    }

    @DeleteMapping
    @ApiOperation("删除")
    @LogOperation("删除")
    @RequiresPermissions("financing:fgovernmentbond:delete")
    public Result delete(@RequestBody Long[] ids){
        //效验数据
        AssertUtils.isArrayEmpty(ids, "id");

        fGovernmentBondService.delete(ids);

        return new Result();
    }

    @GetMapping("export")
    @ApiOperation("导出")
    @LogOperation("导出")
    @RequiresPermissions("financing:fgovernmentbond:export")
    public void export(@ApiIgnore @RequestParam Map<String, Object> params, HttpServletResponse response) throws Exception {
        List<FGovernmentBondDTO> list = fGovernmentBondService.list(params);
        List<FGovernmentBondExcel> exportData = fGovernmentBondService.getExportData(list);
        //根据权限过滤掉不属于权限的信息
        ServletRequestAttributes requestAttributes
                = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = requestAttributes.getRequest();
        Long userId = shiroService.getByToken(params.get("token").toString()).getUserId();
        if (userId != 1067246875800000001L){
            List<Long> roleIdList = sysRoleUserService.getRoleIdList(userId);
            for (Long aLong : roleIdList) {
                List<Long> deptIdList = sysRoleDataScopeService.getDeptIdList(aLong);
                list.removeIf(next -> !deptIdList.contains(Long.valueOf(next.getCreditId())));
            }
        }
        ExcelUtils.exportExcelToTarget(response, "政府债券信息表", "政府债券", exportData , FGovernmentBondExcel.class);
    }

    @PostMapping("/interest")
    @ApiOperation("利息测算")
    @LogOperation("利息测算")
//    @RequiresPermissions("financing:findirectfinancing:interest")
    public Result interestCalculate(@RequestBody IndirectInterestVO vo){
        //效验数据
        ValidatorUtils.validateEntity(vo, AddGroup.class, DefaultGroup.class);
        List<FRepaymentPlanDTO> data = fGovernmentBondService.interestCalculate(vo);
        return new Result<List<FRepaymentPlanDTO>>().ok(data);
    }

}