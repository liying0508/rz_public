//package io.renren.modules.report.controller;
//
//import io.renren.common.annotation.LogOperation;
//import io.renren.common.constant.Constant;
//import io.renren.common.page.PageData;
//import io.renren.common.utils.ExcelUtils;
//import io.renren.common.utils.FileUpload;
//import io.renren.common.utils.Result;
//import io.renren.common.validator.AssertUtils;
//import io.renren.common.validator.ValidatorUtils;
//import io.renren.common.validator.group.AddGroup;
//import io.renren.common.validator.group.DefaultGroup;
//import io.renren.common.validator.group.UpdateGroup;
//import io.renren.modules.financing.dto.FCreditRecordDTO;
//import io.renren.modules.financing.excel.FCreditRecordExcel;
//import io.renren.modules.financing.service.FCreditRecordService;
//import io.renren.modules.sys.dto.SysUserDTO;
//import io.renren.modules.sys.excel.SysUserExcel;
//import io.swagger.annotations.Api;
//import io.swagger.annotations.ApiImplicitParam;
//import io.swagger.annotations.ApiImplicitParams;
//import io.swagger.annotations.ApiOperation;
//import org.apache.shiro.authz.annotation.RequiresPermissions;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.web.bind.annotation.*;
//import springfox.documentation.annotations.ApiIgnore;
//
//import javax.servlet.http.HttpServletResponse;
//import java.util.List;
//import java.util.Map;
//
//
///**
//* 银行授信
//*
//* @author liuwenyong
//* @since 3.0 2022-05-14
//*/
//@RestController
//@RequestMapping("report/credit")
//@Api(tags="银行授信月度报表")
//public class RCreditController {
//    @Autowired
//    ReportService reportService;
//
//    @GetMapping("page")
//    @ApiOperation("分页")
//    @ApiImplicitParams({
//        @ApiImplicitParam(name = Constant.PAGE, value = "当前页码，从1开始", paramType = "query", required = true, dataType="int") ,
//        @ApiImplicitParam(name = Constant.LIMIT, value = "每页显示记录数", paramType = "query",required = true, dataType="int") ,
//        @ApiImplicitParam(name = Constant.ORDER_FIELD, value = "排序字段", paramType = "query", dataType="String") ,
//        @ApiImplicitParam(name = "no", value = "授信单编号", paramType = "query", dataType="String"), @ApiImplicitParam(name = Constant.ORDER, value = "排序方式，可选值(asc、desc)", paramType = "query", dataType="String")
//    })
//    @RequiresPermissions("report:page")
//    public Result<PageData<FReportOneDTO>> page(@ApiIgnore @RequestParam Map<String, Object> params){
//        PageData<FReportOneDTO> page = reportService.page1(params);
//        return new Result<PageData<FReportOneDTO>>().ok(page);
//    }
//
//    @GetMapping("credit-moth-list")
//    @ApiOperation("分页")
//    @RequiresPermissions("report:page")
//    public Result<List<RCreditMonthDto>> creditMothList(@ApiIgnore @RequestParam Map<String, Object> params){
//        List<RCreditMonthDto> list = reportService.getCreditMothList(params);
//        return new Result<List<RCreditMonthDto>>().ok(list);
//    }
//
//    @Value("${rz.excel-file}")
//    private String path;
//
//    @GetMapping("export-balance")
//    @ApiOperation("导出")
//    @LogOperation("导出")
//    @RequiresPermissions("report:page")
//    @ApiImplicitParam(name = "username", value = "用户名", paramType = "query", dataType="String")
//    public void export1(HttpServletResponse response) throws Exception {
//        FileUpload.downLoad(path+"balance.xlsx", response, true);
//    }
//
//    @GetMapping("export-subsidiary")
//    @ApiOperation("导出")
//    @LogOperation("导出")
//    @RequiresPermissions("report:page")
//    @ApiImplicitParam(name = "username", value = "用户名", paramType = "query", dataType="String")
//    public void export2(HttpServletResponse response) throws Exception {
//        FileUpload.downLoad(path+"subsidiary.xlsx", response, true);
//    }
//
//    @GetMapping("export-structure")
//    @ApiOperation("导出")
//    @LogOperation("导出")
//    @RequiresPermissions("report:page")
//    @ApiImplicitParam(name = "username", value = "用户名", paramType = "query", dataType="String")
//    public void export3(HttpServletResponse response) throws Exception {
//        FileUpload.downLoad(path+"structure.xlsx", response, true);
//    }
//
//    @GetMapping("export-schedule")
//    @ApiOperation("导出")
//    @LogOperation("导出")
//    @RequiresPermissions("report:page")
//    @ApiImplicitParam(name = "username", value = "用户名", paramType = "query", dataType="String")
//    public void export4(HttpServletResponse response) throws Exception {
//        FileUpload.downLoad(path+"schedule.xlsx", response, true);
//    }
//
//
//}