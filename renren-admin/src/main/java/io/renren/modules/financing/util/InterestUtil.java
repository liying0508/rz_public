package io.renren.modules.financing.util;

import io.renren.modules.financing.dto.FRepaymentPlanDTO;
import io.renren.modules.lpr.dto.FLprDTO;
import io.renren.modules.report.dto.LprDTO;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @Author: John
 * @Date: 2022/6/6
 * @Time: 17:30
 * @Description:
 */
public class InterestUtil {

    //一天的毫秒数
    private static int dayTime = 60*60*1000*24;

    /**
     * 浮动形计算
     * @param stDate 贷款合同开始日期
     * @param payDate 还款日期列表
     * @param amount 贷款金额
     * @param payAmount 还款本金
     * @param adjustDays 调息周期（天数）
     * @param lprInterestCycle 五年期还是一年期的lpr
     * @return
     */
    public static FRepaymentPlanDTO calInterest(Date stDate, List<Date> payDate, BigDecimal amount,
                                               List<BigDecimal> payAmount, int adjustDays,Integer interestWayRate,
                                               List<FLprDTO> lprDTOList, Date eDate, double floatRate,int lprInterestCycle){

        //初始化还款利息
        //double[] interstArr = new double[payDate.length];
        List<BigDecimal> interestList = new ArrayList<>();
        //从LPR信息表中获取LPR利率
        //double[] lprArr = new double[10];
        List<Double> lprArr = new ArrayList<>();
        //从LPR信息表中获取LPR调整日期
        //Date[] lprDate = new Date[10];
        List<Date> lprDate = new ArrayList<>();
        //初始化剩余本金
        List<BigDecimal> oddCorpus = new ArrayList<>();
        //五年期还是一年期
        for (FLprDTO fLprDTO : lprDTOList) {
            Date startTime = fLprDTO.getStartTime();
            lprDate.add(startTime);
            String rate = fLprDTO.getRate();
            String fiveRate = fLprDTO.getFiveRate();
            //判断是一年利息，还是五年利息
            if(lprInterestCycle == 1){
                lprArr.add(Double.valueOf(rate));
            }else{
                lprArr.add(Double.valueOf(fiveRate));
            }
        }
        //上一次还款日期
        Date lastPayDate = stDate;
//        List<Date> payDateList = new ArrayList<>();
//        payDateList.add(stDate);
//        for (int i = 0; i < payDate.size(); i++) {
//            payDateList.add(payDate.get(i));
//        }
        //当前还款日期
        Date currPayDate = stDate;
        long currPayTime = currPayDate.getTime();
        //上一次計算利息参考日期
        Date lastRateDate = stDate;

        //根据合同还款日期从LPR信息表获取当前的LPR利率
        double currLRP = 0.00;
        long stDateTime = stDate.getTime();
        //当日期在lpr第一个之前时
        if (stDateTime<payDate.get(0).getTime()){
            currLRP=(lprArr.get(0)+floatRate)/100;
        }else if(stDateTime>payDate.get(0).getTime() & stDateTime<payDate.get(payDate.size()).getTime()) {//当日期在第一个和最后一个中间时
            for (int i = 1; i < payDate.size(); i++) {
                long time = payDate.get(i).getTime();
                if (stDateTime < time){
                    currLRP = (lprArr.get(i-1)+floatRate)/100;
                }
            }
        }else if (stDateTime>payDate.get(payDate.size()).getTime()){
            currLRP = (lprArr.get(payDate.size())+floatRate)/100;
        }
        BigDecimal theAmount = amount;
        //根据还款日期逐一计算还款利息
        for (int i = 0; i < payDate.size(); i++) {
//            Date lastPayDate = payDateList.get(i);

            BigDecimal theInterest = new BigDecimal(0);
            //当前还款日期
            Date theDate = payDate.get(i);
            //还款日和上次还款日的天数
            Long days = (theDate.getTime() - lastPayDate.getTime())/dayTime;
            System.out.println(lastRateDate);
            System.out.println("lastpaydate"+lastPayDate);
            for (Long aLong = 0L; aLong < days; aLong++) {
                //当前还款日递增
                currPayTime+=dayTime;
                currPayDate = new Date(currPayTime);

                //达到周期的情况下重新获取当前日期的LPR
                if (currPayDate.getTime() - lastRateDate.getTime() >= adjustDays*dayTime) {
                    long lastPayDateTime = lastPayDate.getTime();
                    //当日期在lpr第一个之前时
                    if (lastPayDateTime<payDate.get(0).getTime()){
                        currLRP=(lprArr.get(0)+floatRate)/100;
                    }else if(lastPayDateTime>payDate.get(0).getTime() & lastPayDateTime<payDate.get(payDate.size()).getTime()) {//当日期在第一个和最后一个中间时
                        for (int j = 1; j < payDate.size(); j++) {
                            long time = payDate.get(j).getTime();
                            if (lastPayDateTime < time){
                                currLRP = (lprArr.get(j-1)+floatRate)/100;
                            }
                        }
                    }else if (lastPayDateTime>payDate.get(payDate.size()).getTime()){
                        currLRP = (lprArr.get(payDate.size())+floatRate)/100;
                    }

                    lastRateDate = currPayDate;
                }
                //利息累加
                //贷款余额*当前lpr/360
                double v = theInterest.doubleValue();
                double s = theAmount.doubleValue() * currLRP/ interestWayRate;
                theInterest = new BigDecimal(v+s);
            }
            //列表设置
            interestList.add(theInterest);
            //余额减
            theAmount = theAmount.subtract(payAmount.get(i));
            oddCorpus.add(theAmount);
        }
        //对日期列表做处理
            for (int i = interestList.size()-1; i > 0; i--) {
            BigDecimal bigDecimal = interestList.get(i);
            System.out.println(bigDecimal);
            BigDecimal bigDecimal1 = interestList.get(i - 1);
            System.out.println(bigDecimal1);
            interestList.set(i,bigDecimal.subtract(bigDecimal1));
        }
        //保留三位小数，四舍五入
        for (int i = 0; i < interestList.size(); i++) {
            interestList.set(i,interestList.get(i).setScale(3, BigDecimal.ROUND_HALF_UP));
        }
//        FRepaymentPlanDTO fRepaymentPlanDTO = new FRepaymentPlanDTO(interestList, oddCorpus);
        FRepaymentPlanDTO fRepaymentPlanDTO = new FRepaymentPlanDTO();
        return fRepaymentPlanDTO;
    }

    /**
     * 固定利息计算
     * @param stDate 贷款合同开始日期
     * @param payDate 还款日期列表
     * @param amount 贷款金额
     * @param payAmount 还款本金
     * @return
     */
    public static FRepaymentPlanDTO calInterest2(Date stDate, List<Date> payDate, BigDecimal amount,
                                                 List<BigDecimal> payAmount, Integer interestWayRate, double rate){
        //先获得还款列表中的信息
        //初始化还款利息
        List<BigDecimal> interestList = new ArrayList<>();
        //初始化剩余本金
        List<BigDecimal> oddCorpus = new ArrayList<>();
        //上一次还款日期
        Date lastPayDate = stDate;
        //当前还款日期
        Date currPayDate = stDate;
        long currPayTime = currPayDate.getTime();
        //上一次計算利息参考日期
        Date lastRateDate = stDate;
        BigDecimal theAmount = amount;
        rate = rate/100;
        //遍历时间列表
        //paydate里面的值是每一期的还款时间
        for (int i = 0; i < payDate.size(); i++) {
            BigDecimal theInterest = new BigDecimal(0);
            //当前还款日期
            Date theDate = payDate.get(i);
            //还款日和上次还款日的天数
            Long days = (theDate.getTime() - lastPayDate.getTime())/dayTime;
            //算出日利息
            for (Long aLong = 0L; aLong < days; aLong++) {
                //当前还款日递增
                currPayTime+=dayTime;
                currPayDate = new Date(currPayTime);
                //利息累加
                //贷款余额*固定利率/360
                double v = theInterest.doubleValue();
                double s = theAmount.doubleValue() * rate/ interestWayRate;
                theInterest = new BigDecimal(v+s);
            }
            //列表设置
            interestList.add(theInterest);
            //余额减
            theAmount = theAmount.subtract(payAmount.get(i));
            oddCorpus.add(theAmount);
        }
        //保留三位小数，四舍五入
        //对日期列表做处理
        for (int i = interestList.size()-1; i > 0; i--) {
            BigDecimal bigDecimal = interestList.get(i);
            System.out.println(bigDecimal);
            BigDecimal bigDecimal1 = interestList.get(i - 1);
            System.out.println(bigDecimal1);
            interestList.set(i,bigDecimal.subtract(bigDecimal1));
        }

        for (int i = 0; i < interestList.size(); i++) {
            interestList.set(i,interestList.get(i).setScale(3, BigDecimal.ROUND_HALF_UP));
        }
        //        FRepaymentPlanDTO fRepaymentPlanDTO = new FRepaymentPlanDTO(interestList, oddCorpus);
        FRepaymentPlanDTO fRepaymentPlanDTO = new FRepaymentPlanDTO();
        return fRepaymentPlanDTO;
    }


    /**
     * 浮动形计算
     * @param stDate 贷款合同开始日期
     * @param payDate 还款日期列表
     * @param amount 贷款金额
     * @param payAmount 还款本金
     * @param adjustDays 调息周期（天数）
     * @return
     */
    public static FRepaymentPlanDTO calInterest1(Date stDate, List<Date> payDate, BigDecimal amount,
                                                 List<BigDecimal> payAmount, int adjustDays,Integer interestWayRate,
                                                 List<FLprDTO> lprDTOList, Date eDate, double floatRate,
                                                 Integer lprInterestCycle){

        //初始化还款利息
        //double[] interstArr = new double[payDate.length];
        List<BigDecimal> interestList = new ArrayList<>();
        //从LPR信息表中获取LPR利率
        //double[] lprArr = new double[10];
        List<Double> lprArr = new ArrayList<>();
        //从LPR信息表中获取LPR调整日期
        //Date[] lprDate = new Date[10];
        List<Date> lprDate = new ArrayList<>();
        //初始化剩余本金
        List<BigDecimal> oddCorpus = new ArrayList<>();
        //五年期还是一年期
        int flag;
        long l = eDate.getTime() - stDate.getTime();
        if (l>=5L*360*dayTime){
            flag = 5;
        }else {
            flag = 1;
        }
        for (FLprDTO fLprDTO : lprDTOList) {
            Date startTime = fLprDTO.getStartTime();
            lprDate.add(startTime);
            String rate = fLprDTO.getRate();
            String fiveRate = fLprDTO.getFiveRate();
            //判断是一年利息，还是五年利息
            if(flag == 1){
                lprArr.add(Double.valueOf(rate));
            }else{
                lprArr.add(Double.valueOf(fiveRate));
            }
        }
        //上一次还款日期
        Date lastPayDate = stDate;
//        List<Date> payDateList = new ArrayList<>();
//        payDateList.add(stDate);
//        for (int i = 0; i < payDate.size(); i++) {
//            payDateList.add(payDate.get(i));
//        }
        //当前还款日期
        Date currPayDate = stDate;
        long currPayTime = currPayDate.getTime();
        //上一次計算利息参考日期
        Date lastRateDate = stDate;

        //根据合同还款日期从LPR信息表获取当前的LPR利率
        double currLRP = 0.00;
        long stDateTime = stDate.getTime();
        //当日期在lpr第一个之前时
        if (stDateTime<payDate.get(0).getTime()){
            currLRP=(lprArr.get(0)+floatRate)/100;
        }else if(stDateTime>payDate.get(0).getTime() & stDateTime<payDate.get(payDate.size()).getTime()) {//当日期在第一个和最后一个中间时
            for (int i = 1; i < payDate.size(); i++) {
                long time = payDate.get(i).getTime();
                if (stDateTime < time){
                    currLRP = (lprArr.get(i-1)+floatRate)/100;
                }
            }
        }else if (stDateTime>payDate.get(payDate.size()).getTime()){
            currLRP = (lprArr.get(payDate.size())+floatRate)/100;
        }
        BigDecimal theAmount = amount;
        //根据还款日期逐一计算还款利息
        for (int i = 0; i < payDate.size(); i++) {
//            Date lastPayDate = payDateList.get(i);

            BigDecimal theInterest = new BigDecimal(0);
            //当前还款日期
            Date theDate = payDate.get(i);
            //还款日和上次还款日的天数
            Long days = (theDate.getTime() - lastPayDate.getTime())/dayTime;
            System.out.println(lastRateDate);
            System.out.println("lastpaydate"+lastPayDate);
            for (Long aLong = 0L; aLong < days; aLong++) {
                //当前还款日递增
                currPayTime+=dayTime;
                currPayDate = new Date(currPayTime);

                //达到周期的情况下重新获取当前日期的LPR
                if (currPayDate.getTime() - lastRateDate.getTime() >= adjustDays*dayTime) {
                    long lastPayDateTime = lastPayDate.getTime();
                    //当日期在lpr第一个之前时
                    if (lastPayDateTime<payDate.get(0).getTime()){
                        currLRP=(lprArr.get(0)+floatRate)/100;
                    }else if(lastPayDateTime>payDate.get(0).getTime() & lastPayDateTime<payDate.get(payDate.size()).getTime()) {//当日期在第一个和最后一个中间时
                        for (int j = 1; j < payDate.size(); j++) {
                            long time = payDate.get(j).getTime();
                            if (lastPayDateTime < time){
                                currLRP = (lprArr.get(j-1)+floatRate)/100;
                            }
                        }
                    }else if (lastPayDateTime>payDate.get(payDate.size()).getTime()){
                        currLRP = (lprArr.get(payDate.size())+floatRate)/100;
                    }

                    lastRateDate = currPayDate;
                }
                //利息累加
                //贷款余额*当前lpr/360
                double v = theInterest.doubleValue();
                double s = theAmount.doubleValue() * currLRP/ interestWayRate;
                theInterest = new BigDecimal(v+s);
            }
            //列表设置
            interestList.add(theInterest);
            //余额减
            theAmount = theAmount.subtract(payAmount.get(i));
            oddCorpus.add(theAmount);
        }
        //对日期列表做处理
        for (int i = interestList.size()-1; i > 0; i--) {
            BigDecimal bigDecimal = interestList.get(i);
            System.out.println(bigDecimal);
            BigDecimal bigDecimal1 = interestList.get(i - 1);
            System.out.println(bigDecimal1);
            interestList.set(i,bigDecimal.subtract(bigDecimal1));
        }
        //保留三位小数，四舍五入
        for (int i = 0; i < interestList.size(); i++) {
            interestList.set(i,interestList.get(i).setScale(3, BigDecimal.ROUND_HALF_UP));
        }
        //        FRepaymentPlanDTO fRepaymentPlanDTO = new FRepaymentPlanDTO(interestList, oddCorpus);
        FRepaymentPlanDTO fRepaymentPlanDTO = new FRepaymentPlanDTO();
        return fRepaymentPlanDTO;
    }

}


