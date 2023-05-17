package io.renren.modules.financing.util;

import io.renren.modules.financing.dto.FRepayInterestPlanDTO;
import io.renren.modules.financing.dto.FRepayPrincipalPlanDTO;
import io.renren.modules.financing.dto.FRepaymentPlanDTO;
import io.renren.modules.lpr.dto.FLprDTO;
import org.springframework.beans.BeanUtils;

import java.math.BigDecimal;
import java.util.*;

/**
 * @Author: Eddy
 * @Date: 2022/9/9
 * @Time: 14:47
 * @Description: 利息测算算法
 */
public class InterestCalculatedUtil {

    //一天的时间毫秒数
    private static Long dayTime = 60L*60*1000*24;

    /**
     * 间融还本付息的算法
     * @param principalList 还本计划列表
     * @param interestList  还息计划列表
     * @param lprDTOList    lpr列表
     * @param lprInterestCycle  lpr是一年还是五年
     * @param floatRate     上浮利率
     * @param amount        贷款金额
     * @param adjustDays    调息天数
     * @param interestMeasurementCycle   测算周期（360/365）
     */
    public static List<FRepaymentPlanDTO> repayPrincipalAndInterest(List<FRepayPrincipalPlanDTO> principalList,
                                                                    List<FRepayInterestPlanDTO> interestList,
                                                                    List<FLprDTO> lprDTOList, int lprInterestCycle,
                                                                    double floatRate, BigDecimal amount,int adjustDays,
                                                                    Integer interestMeasurementCycle){

        //对输入的数据进行处理后得到一个返回的列表雏形
        List<FRepaymentPlanDTO> repayList = InterestCalculatedUtil.mergeList(principalList, interestList);
        //获取合同起息日
        Date startDate = repayList.get(0).getStartDate();
        long startDateTime = startDate.getTime();
        //初始化上一次还款日期
        Date lastPayDate = startDate;
        long lastPayDateTime = lastPayDate.getTime();
        //初始化当前还款日期
        Date currPayDate = startDate;
        long currPayDateTime = currPayDate.getTime();
        //初始化上一次计算利息的参考日期
        Date lastRateDate = startDate;
        long lastRateDateTime = lastRateDate.getTime();
        //初始化当前使用的lpr利率
        double currLRP = 0.000;
        //根据合同还款日期从LPR信息表获取当前的LPR利率
        currLRP = (InterestCalculatedUtil.getLprRate(lprDTOList,startDate,lprInterestCycle)+floatRate)/100;
        //获取贷款金额
        BigDecimal theAmount = amount;
        //根据还款日期逐一计算repayList中的每一条还款信息
        for (int i = 0; i < repayList.size(); i++) {
            //初始化还款利息
            BigDecimal theInterest = new BigDecimal(0);
            //当前的还款日期
            Date theDate = repayList.get(i).getEndDate();
            long theDateTime = theDate.getTime();
            //还款日期和上次还款日期之间间隔的天数
            Long days = (theDateTime-lastPayDateTime)/dayTime;
            //循环这个天数，进行计算
            for (Long aLong = 0L;aLong<days;aLong++){
                //当前还款日递增
                currPayDateTime+=dayTime;
                currPayDate = new Date(currPayDateTime);

                //达到周期的情况下重新获取当前日期的lpr
                if(currPayDate.getTime() - lastRateDate.getTime() >= adjustDays*dayTime){
                    Date lastPayTime = lastPayDate;
                    currLRP = (InterestCalculatedUtil.getLprRate(lprDTOList, lastPayTime, lprInterestCycle)+floatRate)/100;
                    lastRateDate = currPayDate;
                }
                //利息累加
                //贷款余额*当前lpr/360
                double v = theInterest.doubleValue();
                double s = theAmount.doubleValue() * currLRP/ interestMeasurementCycle;
                theInterest = new BigDecimal(v+s);
            }
            //设置该行数据的利息
            repayList.get(i).setInterest(theInterest);
            //余额的设置
            BigDecimal repaymentOfPrincipal = repayList.get(i).getRepaymentOfPrincipal();
            if (repaymentOfPrincipal == null){
                repaymentOfPrincipal = new BigDecimal(0);
            }
            theAmount = theAmount.subtract(repaymentOfPrincipal);
            repayList.get(i).setOddCorpus(theAmount);
        }
        //保留三位小数
        for (FRepaymentPlanDTO fRepaymentPlanDTO : repayList) {
            fRepaymentPlanDTO.getInterest().setScale(3, BigDecimal.ROUND_HALF_UP);
        }
        return repayList;
    }

    /**
     * 间融还本付息的算法(还本付息)
     * @param principalList 还本计划列表
     * @param interestList  还息计划列表
     * @param lprDTOList    lpr列表
     * @param lprInterestCycle  lpr是一年还是五年
     * @param floatRate     上浮利率
     * @param amount        贷款金额
     * @param adjustDays    调息天数
     * @param interestMeasurementCycle   测算周期（360/365）
     */
    public static List<FRepaymentPlanDTO> repayPrincipalAndInterest1(List<FRepayPrincipalPlanDTO> principalList,
                                                                    List<FRepayInterestPlanDTO> interestList,
                                                                    List<FLprDTO> lprDTOList, int lprInterestCycle,
                                                                    double floatRate, BigDecimal amount,int adjustDays,
                                                                    Integer interestMeasurementCycle){
        List<FRepaymentPlanDTO> repayList = new ArrayList<>();
        List<FRepaymentPlanDTO> repayPrincipalList = new ArrayList<>();
        List<FRepaymentPlanDTO> repayInterestList = new ArrayList<>();
        for (FRepayPrincipalPlanDTO fRepayPrincipalPlanDTO : principalList) {
            FRepaymentPlanDTO fRepaymentPlanDTO = new FRepaymentPlanDTO();
            BeanUtils.copyProperties(fRepayPrincipalPlanDTO,fRepaymentPlanDTO);
            repayPrincipalList.add(fRepaymentPlanDTO);
        }
        for (FRepayInterestPlanDTO fRepayInterestPlanDTO : interestList) {
            FRepaymentPlanDTO fRepaymentPlanDTO = new FRepaymentPlanDTO();
            BeanUtils.copyProperties(fRepayInterestPlanDTO,fRepaymentPlanDTO);
            repayInterestList.add(fRepaymentPlanDTO);
        }

        //获取合同起息日
        Date startDate = repayInterestList.get(0).getStartDate();
        long startDateTime = startDate.getTime();
        //初始化上一次还款日期
        Date lastPayDate = startDate;
        long lastPayDateTime = lastPayDate.getTime();
        //初始化当前还款日期
        Date currPayDate = startDate;
        long currPayDateTime = currPayDate.getTime();
        //初始化上一次计算利息的参考日期
        Date lastRateDate = startDate;
        long lastRateDateTime = lastRateDate.getTime();
        //初始化当前使用的lpr利率
        double currLRP = 0.000;
        //根据合同还款日期从LPR信息表获取当前的LPR利率
        currLRP = (InterestCalculatedUtil.getLprRate(lprDTOList,startDate,lprInterestCycle)+floatRate)/100;
        //获取贷款金额
        BigDecimal theAmount = amount;
        //根据还款日期逐一计算repayList中的每一条还款信息
        for (int i = 0; i < repayInterestList.size(); i++) {
            //初始化还款利息
            BigDecimal theInterest = new BigDecimal(0);
            //当前的还款日期
            Date theDate = repayInterestList.get(i).getEndDate();
            long theDateTime = theDate.getTime();
            //上一次的还款时间戳
            long lastTime = repayInterestList.get(i).getStartDate().getTime() - dayTime;
            //遍历还本计划列表
            for (FRepaymentPlanDTO fRepaymentPlanDTO : repayPrincipalList) {
                //如果在这一期还息计划中有还本计划的存在
                if (fRepaymentPlanDTO.getEndDate().getTime()<theDateTime &
                        fRepaymentPlanDTO.getEndDate().getTime()>lastTime){
                    BigDecimal repaymentOfPrincipal = fRepaymentPlanDTO.getRepaymentOfPrincipal();
                    theAmount = theAmount.subtract(repaymentOfPrincipal);
                    //算出本次还本金额对应的利息并且加上本金存入利息中
                    Double lpr = (InterestCalculatedUtil.getLprRate(lprDTOList,startDate,lprInterestCycle)+floatRate)/100;
                    double v = repaymentOfPrincipal.doubleValue() * lpr / interestMeasurementCycle;
                    double result = v * (fRepaymentPlanDTO.getEndDate().getTime() - lastTime) / dayTime;
                    fRepaymentPlanDTO.setInterest(BigDecimal.valueOf(result));
                }else if(fRepaymentPlanDTO.getEndDate().getTime() == lastTime){
                    BigDecimal repaymentOfPrincipal = fRepaymentPlanDTO.getRepaymentOfPrincipal();
                    theAmount = theAmount.subtract(repaymentOfPrincipal);
                    fRepaymentPlanDTO.setInterest(new BigDecimal(0));
                }
            }
            //还款日期和上次还款日期之间间隔的天数
            Long days = (theDateTime-lastPayDateTime)/dayTime;
            //循环这个天数，进行计算
            for (Long aLong = 0L;aLong<days;aLong++){
                //当前还款日递增
                currPayDateTime+=dayTime;
                currPayDate = new Date(currPayDateTime);

                //达到周期的情况下重新获取当前日期的lpr
                if(currPayDate.getTime() - lastRateDate.getTime() >= adjustDays*dayTime){
                    Date lastPayTime = lastPayDate;
                    currLRP = (InterestCalculatedUtil.getLprRate(lprDTOList, lastPayTime, lprInterestCycle)+floatRate)/100;
                    lastRateDate = currPayDate;
                }
                //利息累加
                //贷款余额*当前lpr/360
                double v = theInterest.doubleValue();
                double s = theAmount.doubleValue() * currLRP/ interestMeasurementCycle;
                theInterest = new BigDecimal(v+s);
            }
            //设置该行数据的利息
            repayInterestList.get(i).setInterest(theInterest);
            //余额的设置
//            BigDecimal repaymentOfPrincipal = repayInterestList.get(i).getRepaymentOfPrincipal();
//            if (repaymentOfPrincipal == null){
//                repaymentOfPrincipal = new BigDecimal(0);
//            }
//            theAmount = theAmount.subtract(repaymentOfPrincipal);
//            repayInterestList.get(i).setOddCorpus(theAmount);
        }
        //保留三位小数
        for (FRepaymentPlanDTO fRepaymentPlanDTO : repayInterestList) {
            fRepaymentPlanDTO.getInterest().setScale(3, BigDecimal.ROUND_HALF_UP);
        }
        List<FRepayInterestPlanDTO> list = new ArrayList<>();
        for (FRepaymentPlanDTO fRepaymentPlanDTO : repayInterestList) {
            FRepayInterestPlanDTO fRepayInterestPlanDTO = new FRepayInterestPlanDTO();
            BeanUtils.copyProperties(fRepaymentPlanDTO,fRepayInterestPlanDTO);
            list.add(fRepayInterestPlanDTO);
        }
        repayList = InterestCalculatedUtil.mergeList(principalList,list);
        return repayList;
    }

    /**
     * 将两个list合并成一个列表
     * @param principalList
     * @param interestList
     */
    public static List<FRepaymentPlanDTO> mergeList(List<FRepayPrincipalPlanDTO> principalList, List<FRepayInterestPlanDTO> interestList){
        //初始化返回的列表
        List<FRepaymentPlanDTO> repayList = new ArrayList<>();
        //将还本计划和还息计划结合为一个新的List
        for (FRepayPrincipalPlanDTO fRepayPrincipalPlanDTO : principalList) {
            FRepaymentPlanDTO fRepaymentPlanDTO = new FRepaymentPlanDTO();
            BeanUtils.copyProperties(fRepayPrincipalPlanDTO,fRepaymentPlanDTO);
            repayList.add(fRepaymentPlanDTO);
        }
        for (FRepayInterestPlanDTO fRepayInterestPlanDTO : interestList) {
            FRepaymentPlanDTO fRepaymentPlanDTO = new FRepaymentPlanDTO();
            BeanUtils.copyProperties(fRepayInterestPlanDTO,fRepaymentPlanDTO);
            repayList.add(fRepaymentPlanDTO);
        }
        //按开始日期排序(从小到大)
        repayList.sort(Comparator.comparing(FRepaymentPlanDTO::getEndDate));
        for (int i = 0; i < repayList.size()-1; i++) {
            //当两个开始日期相等时
            FRepaymentPlanDTO fRepaymentPlanDTO = repayList.get(i);
            FRepaymentPlanDTO fRepaymentPlanDTO1 = repayList.get(i+1);
            if (fRepaymentPlanDTO.getStartDate().getTime() == fRepaymentPlanDTO1.getStartDate().getTime()){
                //谁的结束日期小谁排前面，并且把小的结束日期改成大的开始日期
                if(fRepaymentPlanDTO.getEndDate().getTime()>fRepaymentPlanDTO1.getEndDate().getTime()){
                    //如果前一个的结束日期大于后一个的结束日期，那么二者的结束日期换位
                    Date endDate = fRepaymentPlanDTO.getEndDate();
                    fRepaymentPlanDTO.setEndDate(fRepaymentPlanDTO1.getEndDate());
                    fRepaymentPlanDTO1.setEndDate(endDate);
                    //然后把后一个的开始日期换成此时前一个的结束日期
                    fRepaymentPlanDTO1.setStartDate(fRepaymentPlanDTO.getEndDate());
                }else {
                    //直接把前一个的结束日期放到后一个的开始日期
                    fRepaymentPlanDTO1.setStartDate(fRepaymentPlanDTO.getEndDate());
                }
            }
        }
        //去除此时队列中开始日期等于结束日期的对象
        Iterator<FRepaymentPlanDTO> iterator = repayList.iterator();
        while(iterator.hasNext()){
            FRepaymentPlanDTO fRepaymentPlanDTO = iterator.next();
            if (fRepaymentPlanDTO.getEndDate().equals(fRepaymentPlanDTO.getStartDate())){
                iterator.remove();
            }
        }
        return repayList;
    }

    /**
     * 根据日期确定lpr
     * @param lprDTOList
     * @param startDate
     * @return  rates
     */
    public static double getLprRate(List<FLprDTO> lprDTOList,Date startDate,int lprInterestCycle){
        //将lprDTOList排序
        lprDTOList.sort(Comparator.comparing(FLprDTO::getStartTime));
        //初始化利息
        double rate = 0.00;
        //初始化开始日期的时间戳
        long startDateTime = startDate.getTime();
        for (FLprDTO fLprDTO : lprDTOList) {
            //当时间小于lpr的结束时间大于lpr开始时间
            if (fLprDTO.getStartTime().getTime()<startDateTime & fLprDTO.getEndTime().getTime()>startDateTime){
                //根据lprInterestCycle取利息
                if (lprInterestCycle == 1){
                    rate = Double.parseDouble(fLprDTO.getRate());
                    break;
                }else {
                    rate = Double.parseDouble(fLprDTO.getFiveRate());
                    break;
                }
            }
        }
        if (rate == 0.00){
            if (lprDTOList.get(0).getStartTime().getTime()>startDateTime){
                //根据lprInterestCycle取利息
                if (lprInterestCycle == 1){
                    rate = Double.parseDouble(lprDTOList.get(0).getRate());
                }else {
                    rate = Double.parseDouble(lprDTOList.get(0).getFiveRate());
                }
            }else if (lprDTOList.get(lprDTOList.size()-1).getEndTime().getTime()<startDateTime){
                //根据lprInterestCycle取利息
                if (lprInterestCycle == 1){
                    rate = Double.parseDouble(lprDTOList.get(lprDTOList.size()-1).getRate());
                }else {
                    rate = Double.parseDouble(lprDTOList.get(lprDTOList.size()-1).getFiveRate());
                }
            }
        }
        return rate;
    }

}
