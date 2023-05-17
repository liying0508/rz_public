package io.renren.modules.financing.util;

import io.renren.modules.financing.dto.*;
import io.renren.modules.financing.excel.*;

/**
 * @Author: Eddy
 * @Date: 2022/11/7
 * @Time: 16:14
 * @Description: 这是一个进行Excel导入导出时用到的工具类
 */
public class MyExcelUtil {

    /**
     * 间接融资信息导出转换工具
     * @param dto 间融信息单
     * @param excel 导出信息
     * @return 导出信息
     */
    public static FIndirectFinancingExcel indirectExcelExport(FIndirectFinancingDTO dto, FIndirectFinancingExcel excel){
        excel.setInstitutionType(institutionTypeStr(dto.getInstitutionType()));
        excel.setVarieties(varietiesStr(dto.getVarieties()));
        excel.setInterestMethod(interestMethodStr(dto.getInterestMethod()));
        excel.setCreditMeasures(creditMeasuresStr(dto.getCreditMeasures()));
        excel.setInterestRatesWay(interestRatesWayStr(dto.getInterestRatesWay()));
        return excel;
    }

    /**
     * 直接融资信息导出转换工具
     * @param dto
     * @param excel
     * @return
     */
    public static FDirectFinancingExcel directExcelExport(FDirectFinancingDTO dto,FDirectFinancingExcel excel){
        excel.setCreditMeasures(creditMeasuresStr(dto.getCreditMeasures()));
        return excel;
    }

    /**
     * 银行授信信息导出转换工具
     * @param dto
     * @param excel
     * @return
     */
    public static FCreditRecordExcel creditRecordExport(FCreditPageDto dto, FCreditRecordExcel excel){
        excel.setCreditMeasures(creditMeasuresStr(dto.getCreditMeasures()));
        excel.setInstitutionType(institutionTypeStr(dto.getInstitutionType()));
        return excel;
    }

    /**
     * 政府债券导出excel转换工具
     * @param dto
     * @param excel
     * @return
     */
    public static FGovernmentBondExcel governmentBondExport(FGovernmentBondDTO dto,FGovernmentBondExcel excel){
        excel.setVarieties(varietiesStr(dto.getVarieties()));
        excel.setInterestMethod(interestMethodStr(dto.getInterestMethod()));
        excel.setPrincipalMethod(interestMethodStr(dto.getPrincipalMethod()));
        excel.setCreditMeasures(creditMeasuresStr(dto.getCreditMeasures()));

        return excel;
    }

    public static FRepaymentInfoExcel fRepaymentInfoExport(FRepaymentInfoPageDTO dto,FRepaymentInfoExcel excel){
        excel.setVarieties(varietiesStr(dto.getVarieties()));
        return excel;
    }

    /**
     * 根据输入的数字判断机构类型
     * @param type 要判断的数字
     * @return
     */
    private static String institutionTypeStr(Integer type){
        //金融机构类型的转换
        if (type == 1){
            return "银行机构";
        }else {
            return "非银行机构";
        }
    }

    /**
     * 根据输入的数字判断融资品种
     * @param var 融资品种数字
     * @return
     */
    private static String varietiesStr(Integer var){
        //融资品种的转换
        switch (var){
            case 1:
                return "流贷";
            case 2:
                return "承兑汇票";
            case 3:
                return "信用证";
            case 4:
                return "信托";
            case 5:
                return "项目贷款";
            default:
                return "";
        }
    }

    /**
     * 根据输入的字符串数值判断付息方式
     * @param interestMethod
     * @return
     */
    private static String interestMethodStr(String interestMethod){
        switch (interestMethod){
            case "1":
                return "按月";
            case "3":
                return "按季";
            case "6":
                return "按半年";
            case "12":
                return "按年";
            default:
                return "其他";
        }
    }

    /**
     * 根据输入的增信措施字符串的数值判断征信措施
     * @param creditMeasures 增信措施数字字符串
     * @return
     */
    private static String creditMeasuresStr(String creditMeasures){
        String s = "";
        if (creditMeasures != null){
            if (creditMeasures.contains("1")){
                s=s+"信用 ";
            }
            if (creditMeasures.contains("2")){
                s=s+"保证 ";
            }
            if (creditMeasures.contains("3")){
                s=s+"抵押 ";
            }
            if (creditMeasures.contains("4")){
                s=s+"质押 ";
            }
        }
        return s;
    }

    /**
     * 根据输入的付息周期数值字符串返回字典值字符串
     * @param interestRatesWay 付息周期字符串
     * @return 字典值
     */
    private static String interestRatesWayStr(String interestRatesWay){
        switch (interestRatesWay){
            case "1":
                return "浮动利率";
            case "0":
                return "固定利率";
            default:
                return "";
        }
    }


}
