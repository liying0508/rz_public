package io.renren.modules.financing.util;

import com.google.common.collect.ImmutableMap;
import io.renren.common.utils.DateUtils;
import io.renren.modules.financing.dto.*;

import java.math.BigDecimal;
import java.util.*;

/**
 * @Author: Eddy
 * @Date: 2022/9/22
 * @Time: 11:12
 * @Description: 关键字查询工具类
 */
public class QueryUtil {

    private final static Map<Integer,String> INSTITUTION_MAP
            = ImmutableMap.of(1, "银行机构", 2, "非银机构");

    private final static Map<Integer,String> CREDIT_MEASURES_MAP =
            ImmutableMap.of(1,"信用",2,"保证",3,"抵押",4,"质押");

    private final static Map<Integer,String> VARIETIES_MAP =
            ImmutableMap.of(1,"流贷",2,"承兑汇票",3,"信用证",4,"信托",5,"银行贷款");

    /**
     * 银行授信模块的关键字查询
     * @param condition 条件
     * @param fDictDTOS
     * @return
     */
    public static List<FDictDTO> judgeCondition(String condition, List<FDictDTO> fDictDTOS){
        fDictDTOS.removeIf(next -> !next.getDictValue().contains(condition));
        return fDictDTOS;
    }

    /**
     * 银行授信
     * @param deptName
     * @param creditMeasures 条件
     * @return
     */
    public static boolean judgeConditionCreditRecord(FCreditPageDto dto,
                                                     String deptName,String creditMeasures,
                                                     String quota){
        boolean flag = false;
        String s = dto.getQuota().toString();
        String deptName1 = dto.getDeptName();
        String cm = dto.getCreditMeasures();
        if(cm == null){
            cm = "";
        }
        if (cm.contains(creditMeasures)){
            if (s.contains(quota)){
                if (deptName1.contains(deptName)){
                    flag = true;
                }
            }
        }
        return flag;
    }

    /**
     * 直融模块的关键字查询
     * @param dto
     * @param creditMeasures
     * @return
     */
    public static boolean judgeConditionDirectFinancing(FDirectFinancingDTO dto, String approvalAmount){
        boolean flag = false;
        if (!approvalAmount.equals("")){
            if (dto.getApprovalAmount().toString().contains(approvalAmount)){
                flag =true;
            }
        }else {
            flag = true;
        }
        return flag;
    }

    /**
     * 直融审核模块的关键字查询
     * @param dto
     * @param condition
     * @return
     */
    public static boolean judgeConditionAuditDirectFinancing(FDirectFinancingAuditDTO dto, String approvalAmount){
        boolean flag = false;
        if (!approvalAmount.equals("")){
            if (dto.getApprovalAmount().toString().contains(approvalAmount)){
                flag =true;
            }
        }else {
            flag = true;
        }
        return flag;
    }

    /**
     * 间融模块的关键字查询(单位名称，期限)
     * @param dto
     * @return
     */
    public static boolean judgeConditionIndirectFinancing(FIndirectFinancingDTO dto,String deptName,String deadLine){
        boolean flag = false;
        if (dto.getDeptName().contains(deptName)){
            if (deadLine.equals("")){
                flag = true;
            }else {
                System.out.println("是否相等"+(dto.getDeadLine()==Long.valueOf(deadLine)));
                if (dto.getDeadLine()==Long.valueOf(deadLine)){
                    flag =true;
                }
            }
        }
        System.out.println("flag："+flag);
        return flag;
    }

    /**
     * 还款信息条件筛选
     * @param dto
     * @param deptName
     * @param institutionName
     * @param varieties
     * @return
     */
    public static boolean judgeConditionRepaymentInfo(FRepaymentInfoPageDTO dto,String deptName,String institutionName,
                                                      String varieties){
        boolean flag = false;
        if (deptName.equals("") || deptName == dto.getDeptName()){
            if (institutionName.equals("") || institutionName == dto.getInstitutionName()){
                if (varieties.equals("") || Integer.valueOf(varieties) == dto.getVarieties()){
                    flag = true;
                }
            }
        }
        return flag;
    }

    /**
     * 银行授信
     * @param deptName
     * @return
     */
    public static boolean judgeConditionCreditRecord(FCreditHistoryPageDto dto,String deptName,String creditMeasures,
                                                     String quota){
        boolean flag = false;
        String s = dto.getQuota().toString();
        String deptName1 = dto.getDeptName();
        String cm = dto.getCreditMeasures();
        if (cm.contains(creditMeasures)){
            if (s.contains(quota)){
                if (deptName1.contains(deptName)){
                    flag = true;
                }
            }
        }

        return flag;
    }

    public static String queryToString(Object obj){
        if (obj == null){
            return "";
        }else {
            return obj.toString();
        }
    }
}
