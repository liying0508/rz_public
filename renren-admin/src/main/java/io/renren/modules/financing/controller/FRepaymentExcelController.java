package io.renren.modules.financing.controller;

import com.alibaba.excel.EasyExcel;
import io.renren.common.exception.ErrorCode;
import io.renren.common.exception.RenException;
import io.renren.common.utils.ExcelUtils;
import io.renren.common.utils.Result;
import io.renren.modules.demo.excel.ExcelDataExcel;
import io.renren.modules.financing.entity.FRepaymentInfo;
import io.renren.modules.financing.excel.FRepayInterestListExcel;
import io.renren.modules.financing.excel.FRepayPrincipalListExcel;
import io.renren.modules.financing.excel.FRepaymentInfoInterestListExcel;
import io.renren.modules.financing.excel.FRepaymentInfoPrincipalListExcel;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("financing/excel")
@Api(tags="Excel导入演示")
public class FRepaymentExcelController {
    @PostMapping("importForm")
    @ApiOperation("表格导入")
//    @RequiresPermissions("demo:excel:all")
    @ApiImplicitParam(name = "file", value = "文件", paramType = "query", dataType="file")
    public Result importForm(@RequestParam("file") MultipartFile file,
                             @RequestParam("templateType") Integer templateType) throws Exception {
        //解析并返回给前端
        List<Object> objects = new ArrayList<>();
        switch (templateType){
            case (1):
                objects = EasyExcel.read(file.getInputStream()).head(FRepayInterestListExcel.class).sheet().doReadSync();
                break;
            case (2):
                objects = EasyExcel.read(file.getInputStream()).head(FRepayPrincipalListExcel.class).sheet().doReadSync();
                break;
            case (3):
                objects = EasyExcel.read(file.getInputStream()).head(FRepaymentInfoInterestListExcel.class).sheet().doReadSync();
                break;
            case (4):
                objects = EasyExcel.read(file.getInputStream()).head(FRepaymentInfoPrincipalListExcel.class).sheet().doReadSync();
                break;
            default:
                throw new RenException(ErrorCode.REPAYMENT_INFO_SYS_ERROR);
        }

        return new Result<List<Object>>().ok(objects);
    }

    @GetMapping("exportFormTemplate")
    @ApiOperation("导出表格模板")
//    @RequiresPermissions("demo:excel:all")
    public void exportFormTemplate(@ApiIgnore @RequestParam Map<String, Object> params, HttpServletResponse response) throws Exception {
        //获得本次请求模板类型（1 三大融资的还息，2 三大融资的还本，3还款信息的付息，4还款信息的还本）
        Integer templateType = Integer.valueOf(params.get("templateType").toString());
        Integer unit = Integer.valueOf(params.get("unit").toString());
        ArrayList<Object> objects = new ArrayList<>();

        switch (templateType){
            case (1):
                ExcelUtils.exportExcelToTarget(response, "付息计划",
                        "付息模板", objects, FRepayInterestListExcel.class);
                break;
            case (2):
                if (unit.equals(1)) {
                    ExcelUtils.exportExcelToTarget(response, "还本计划",
                            "还本模板(单位：元)", objects, FRepayPrincipalListExcel.class);
                }else {
                    ExcelUtils.exportExcelToTarget(response, "还本计划",
                            "还本模板(单位：万元)", objects, FRepayPrincipalListExcel.class);
                }
                break;
            case (3):
                ExcelUtils.exportExcelToTarget(response, "实际付息信息",
                        "付息模板", objects, FRepaymentInfoInterestListExcel.class);
                break;
            case (4):
                if (unit.equals(1)) {
                    ExcelUtils.exportExcelToTarget(response, "实际还本信息",
                            "还本模板(单位：元)", objects, FRepaymentInfoPrincipalListExcel.class);
                }else {
                    ExcelUtils.exportExcelToTarget(response, "还本计划",
                            "还本模板(单位：万元)", objects, FRepaymentInfoPrincipalListExcel.class);
                }
                break;
            default:
                throw new RenException(ErrorCode.REPAYMENT_INFO_SYS_ERROR);

        }


    }
}
