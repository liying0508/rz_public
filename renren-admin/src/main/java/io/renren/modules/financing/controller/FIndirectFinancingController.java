package io.renren.modules.financing.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
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
import io.renren.modules.financing.dto.FFinanceDeptDTO;
import io.renren.modules.financing.dto.FIndirectDeptDTO;
import io.renren.modules.financing.dto.FIndirectFinancingDTO;
import io.renren.modules.financing.dto.FRepaymentPlanDTO;
import io.renren.modules.financing.entity.FIndirectFinancingEntity;
import io.renren.modules.financing.excel.FIndirectFinancingExcel;
import io.renren.modules.financing.service.FFinanceDeptService;
import io.renren.modules.financing.service.FIndirectFinancingService;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
* 间接融资信息
*
* @author liuwenyong 
* @since 3.0 2022-05-14
*/
@RestController
@RequestMapping("financing/findirectfinancing")
@Api(tags="间接融资信息")
public class FIndirectFinancingController {
    @Autowired
    private FIndirectFinancingService fIndirectFinancingService;

    @Autowired
    private FFinanceDeptService fFinanceDeptService;

    @Autowired
    ShiroService shiroService;
    @Autowired
    SysRoleDataScopeService sysRoleDataScopeService;
    @Autowired
    SysRoleUserService sysRoleUserService;


    @GetMapping("page")
    @ApiOperation("分页")
    @ApiImplicitParams({
        @ApiImplicitParam(name = Constant.PAGE, value = "当前页码，从1开始", paramType = "query", required = true, dataType="int") ,
        @ApiImplicitParam(name = Constant.LIMIT, value = "每页显示记录数", paramType = "query",required = true, dataType="int") ,
        @ApiImplicitParam(name = Constant.ORDER_FIELD, value = "排序字段", paramType = "query", dataType="String") ,
        @ApiImplicitParam(name = Constant.ORDER, value = "排序方式，可选值(asc、desc)", paramType = "query", dataType="String")
    })
   // @RequiresPermissions("financing:findirectfinancing:page")
    public Result<PageData<FIndirectFinancingDTO>> page(@ApiIgnore @RequestParam Map<String, Object> params){
        PageData<FIndirectFinancingDTO> page = fIndirectFinancingService.page(params);
        //分页数据列表
        List<FIndirectFinancingDTO> list = page.getList();
        //单位
        Integer unit = Integer.valueOf(params.get("unit").toString());
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
        List<FIndirectFinancingDTO> fIndirectFinancingDTOS =
                    fIndirectFinancingService.screenList(list,params.get("deptName").toString(),params.get("deadLine").toString());
        page.setList(fIndirectFinancingDTOS);
        PageData<FIndirectFinancingDTO> pageData = fIndirectFinancingService.indirectCast(page, unit);
        //修改total值
//        pageData.setTotal(pageData.getList().size());
        return new Result<PageData<FIndirectFinancingDTO>>().ok(pageData);
    }

    @GetMapping("{id}/{unit}")
    @ApiOperation("信息")
 //   @RequiresPermissions("financing:findirectfinancing:info")
    public Result<FIndirectFinancingDTO> get(@PathVariable("id") Long id,
                                             @PathVariable("unit") Integer unit){
        FIndirectFinancingDTO data = fIndirectFinancingService.getDealData(id);
        FIndirectDeptDTO dept = fFinanceDeptService.getDept(data);
        data.setDeptName(dept.getDeptName());
        data.setInstitutionType(dept.getType());
        data.setInstitutionName(dept.getName());
        FIndirectFinancingDTO dto = fIndirectFinancingService.indirectInfoCast(data,unit);
        return new Result<FIndirectFinancingDTO>().ok(dto);
    }

    @PostMapping
    @ApiOperation("保存")
    @LogOperation("保存")
  //  @RequiresPermissions("financing:findirectfinancing:save")
    public Result save(@RequestBody FIndirectFinancingDTO dto){
        //效验数据
//        ValidatorUtils.validateEntity(dto, AddGroup.class, DefaultGroup.class);
        FIndirectFinancingDTO dto1 = fIndirectFinancingService.indirectSaveCast(dto, dto.getUnit());
        fIndirectFinancingService.saveInfo(dto1);
        return new Result();
    }

    @PutMapping
    @ApiOperation("修改")
    @LogOperation("修改")
  //  @RequiresPermissions("financing:findirectfinancing:update")
    public Result update(@RequestBody FIndirectFinancingDTO dto){
        //效验数据
//        ValidatorUtils.validateEntity(dto, UpdateGroup.class, DefaultGroup.class);
        FIndirectFinancingDTO dto1 = fIndirectFinancingService.indirectSaveCast(dto, dto.getUnit());
        fIndirectFinancingService.updateInfo(dto1);
        return new Result();
    }

    @DeleteMapping
    @ApiOperation("删除")
    @LogOperation("删除")
    @RequiresPermissions("financing:findirectfinancing:delete")
    public Result delete(@RequestBody Long[] ids){
        //效验数据
        AssertUtils.isArrayEmpty(ids, "id");
        fIndirectFinancingService.deleteInfo(ids);
        return new Result();
    }

    @PostMapping("/interest")
    @ApiOperation("利息测算")
    @LogOperation("利息测算")
  //  @RequiresPermissions("financing:findirectfinancing:interest")
    public Result interestCalculate(@RequestBody IndirectInterestVO vo){
        //效验数据
        ValidatorUtils.validateEntity(vo, AddGroup.class, DefaultGroup.class);
        List<FRepaymentPlanDTO> data = fIndirectFinancingService.interestCalculate(vo);
        return new Result<List<FRepaymentPlanDTO>>().ok(data);
    }

    @GetMapping("export")
    @ApiOperation("导出")
    @LogOperation("导出")
  //  @RequiresPermissions("financing:findirectfinancing:export")
    public void export(@ApiIgnore @RequestParam Map<String, Object> params, HttpServletResponse response)
            throws Exception {
        List<FIndirectFinancingDTO> list = fIndirectFinancingService.list(params);
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
        List<FIndirectFinancingExcel> exportData = fIndirectFinancingService.getExportData(list);
        ExcelUtils.exportExcelToTarget
                (response, "间接融资信息表", "详细信息", exportData, FIndirectFinancingExcel.class);
    }



}