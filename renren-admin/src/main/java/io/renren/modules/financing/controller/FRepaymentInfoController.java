package io.renren.modules.financing.controller;

import io.renren.common.annotation.LogOperation;
import io.renren.common.utils.ExcelUtils;
import io.renren.common.utils.Result;
import io.renren.common.validator.ValidatorUtils;
import io.renren.common.validator.group.AddGroup;
import io.renren.common.validator.group.DefaultGroup;
import io.renren.modules.financing.dto.*;
import io.renren.modules.financing.excel.FGovernmentBondExcel;
import io.renren.modules.financing.excel.FRepaymentInfoExcel;
import io.renren.modules.financing.service.FRepaymentInfoService;
import io.renren.modules.security.service.ShiroService;
import io.renren.modules.sys.service.SysRoleDataScopeService;
import io.renren.modules.sys.service.SysRoleUserService;
import io.swagger.annotations.Api;
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

@RestController
@RequestMapping("financing/repaymentinfo")
@Api(tags="还款信息")
public class FRepaymentInfoController {
    @Autowired
    FRepaymentInfoService fRepaymentInfoService;

    @Autowired
    ShiroService shiroService;
    @Autowired
    SysRoleDataScopeService sysRoleDataScopeService;
    @Autowired
    SysRoleUserService sysRoleUserService;

    @GetMapping("list")
    @ApiOperation("列表")
//    @RequiresPermissions("financing:fcreditrecord:list")
    public Result<List<FRepaymentInfoPageDTO>> list(@RequestParam Map<String, Object> params){
        Integer unit = Integer.valueOf(params.get("unit").toString());

        Integer isChecked = null;
        if (params.get("isChecked")!= null){
            if (!params.get("isChecked").equals("")){
                isChecked = Integer.valueOf(params.get("isChecked").toString());
            }else {
                isChecked = 5;
            }
        }
        //集团审核状态
        Integer groupChecked = null;
        if (params.get("groupChecked")!=null ){
            if (!params.get("groupChecked").equals("")) {
                groupChecked = Integer.valueOf(params.get("groupChecked").toString());
            }else {
                groupChecked = 5;
            }
        }
        List<FRepaymentInfoPageDTO> list = fRepaymentInfoService.listInfo();
        //清理数据库（包括详细信息表，还款信息表）
        fRepaymentInfoService.clearFRepaymentInfo(list);
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
        fRepaymentInfoService.initFRepaymentInfo(list);
        List<FRepaymentInfoPageDTO> fRepaymentInfoPageDTOS1 = fRepaymentInfoService.addStatus(list);
        //根据条件筛选
        List<FRepaymentInfoPageDTO> fRepaymentInfoPageDTOS =
                fRepaymentInfoService.pageScreen(fRepaymentInfoPageDTOS1, params,isChecked,groupChecked);
        List<FRepaymentInfoPageDTO> pageDTOList = fRepaymentInfoService.unitCast(fRepaymentInfoPageDTOS, unit);
        return new Result<List<FRepaymentInfoPageDTO>>().ok(pageDTOList);
    }

    @GetMapping
    @ApiOperation("信息")
    //@RequiresPermissions("financing:fcreditrecord:info")
    public Result<FRepaymentInfoDTO> get(@RequestParam("financingId")String financingId,
                                         @RequestParam("financingType")String financingType,
                                         @RequestParam("unit")Integer unit){
        FRepaymentInfoDTO data = fRepaymentInfoService.getDealData(Long.parseLong(financingId),Integer.parseInt(financingType));
        FRepaymentInfoDTO fRepaymentInfoDTO = fRepaymentInfoService.unitInfoCast(data, unit);
        return new Result<FRepaymentInfoDTO>().ok(fRepaymentInfoDTO);
    }

    @PutMapping
    @ApiOperation("修改")
    @LogOperation("修改")
//    @RequiresPermissions("financing:fcreditrecord:update")
    public Result update(@RequestBody FRepaymentInfoDTO dto){
        //效验数据
//        ValidatorUtils.validateEntity(dto, UpdateGroup.class, DefaultGroup.class);
        fRepaymentInfoService.updateInfo(dto);
        return new Result();
    }

    @DeleteMapping
    @ApiOperation("删除")
    @LogOperation("删除")
    @RequiresPermissions("financing:fcreditrecord:delete")
    public Result delete(@RequestBody Long[] ids){
//        //效验数据
//        AssertUtils.isArrayEmpty(ids, "id");
        fRepaymentInfoService.delete(ids);
        return new Result();
    }

    @PostMapping("check")
    @ApiOperation("审核")
    @LogOperation("审核")
//    @RequiresPermissions("financing:approval:check")
    public Result check(@RequestBody FRepaymentInfoCheckDTO dto){
        //效验数据
        ValidatorUtils.validateEntity(dto, AddGroup.class, DefaultGroup.class);
        fRepaymentInfoService.check(dto);
        return new Result();
    }

    @PostMapping("groupcheck")
    @ApiOperation("审核")
    @LogOperation("审核")
//    @RequiresPermissions("financing:approval:check")
    public Result groupCheck(@RequestBody FRepaymentInfoCheckDTO dto){
        //效验数据
        ValidatorUtils.validateEntity(dto, AddGroup.class, DefaultGroup.class);
        fRepaymentInfoService.groupCheck(dto);
        return new Result();
    }

    @GetMapping("export")
    @ApiOperation("导出")
    @LogOperation("导出")
//    @RequiresPermissions("financing:fgovernmentbond:export")
    public void export(@ApiIgnore @RequestParam Map<String, Object> params, HttpServletResponse response) throws Exception {
        List<FRepaymentInfoPageDTO> list1 = fRepaymentInfoService.listInfo();
        System.out.println();
        //根据条件筛选
        List<FRepaymentInfoPageDTO> list =
                fRepaymentInfoService.pageScreen(list1, params,null,null);
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
        List<FRepaymentInfoPageDTO> lists = fRepaymentInfoService.addStatus(list);

        List<FRepaymentInfoExcel> exportData = fRepaymentInfoService.getExportData(lists);
        ExcelUtils.exportExcelToTarget(response, "还款信息表", "还款信息", exportData , FRepaymentInfoExcel.class);
    }


}
