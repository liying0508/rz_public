package io.renren.modules.financing.util;

import io.renren.modules.financing.dto.FRepaymentInfoDTO;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @Author: Eddy
 * @Date: 2022/9/6
 * @Time: 16:00 
 * @Description:
 */
public class RepayUtil {
    /**
     *
     * @param allRepayPlan
     * @param judgeList
     * @return
     */
    public static List<Long> delList(List<Long> allRepayPlan,List<Long> judgeList){
        List<Long> longs = new ArrayList<>();
        for (Long aLong : allRepayPlan) {
            if(!judgeList.contains(aLong)){
                longs.add(aLong);
            }
        }
        return longs;
    }

    /**
     * 合并并排序
     * @param dateList
     * @param dateSet
     * @return
     */
    public static List<Date> sortDate(List<Date> dateList, Set<Date> dateSet){
        for (Date date : dateSet) {
            dateList.add(date);
        }
        dateList.sort(new Comparator<Date>() {
            @Override
            public int compare(Date o1, Date o2) {
                return o1.compareTo(o2);
            }
        });
        return dateList;
    }

    public static boolean containsTypeAndId(List<FRepaymentInfoDTO> list,FRepaymentInfoDTO dto){
        boolean flag = false;
        for (FRepaymentInfoDTO fRepaymentInfoDTO : list) {
            boolean a = fRepaymentInfoDTO.getFinancingType().equals(dto.getFinancingType()) ;
            boolean b = fRepaymentInfoDTO.getFinancingId().equals(dto.getFinancingId());
            if (fRepaymentInfoDTO.getFinancingType().equals(dto.getFinancingType()) && fRepaymentInfoDTO.getFinancingId().equals(dto.getFinancingId())){
                flag = true;
            }
        }
        return flag;
    }
}
