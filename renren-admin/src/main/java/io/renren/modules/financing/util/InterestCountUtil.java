package io.renren.modules.financing.util;

import io.renren.modules.financing.dto.FRepayInterestPlanDTO;
import io.renren.modules.financing.dto.FRepayPrincipalPlanDTO;
import io.renren.modules.financing.dto.FRepaymentPlanDTO;
import io.renren.modules.lpr.dto.FLprDTO;
import org.springframework.beans.BeanUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.*;

import static java.math.BigDecimal.ROUND_HALF_UP;

/**
 * @Author: Eddy·Song
 * @Date: 2022/9/13
 * @Time: 21:38
 * @Description: 利息测算的算法（间融，直融）
 */
public class InterestCountUtil {
    //本行数据本金：xxxx，开始日期：xxxx，结束日期：xxxx，共计xx天，执行利率为：
    //一天的毫秒数
    private static final int DAY_TIME = 60*60*1000*24;
    //还款的本金说明
    private static final String PRINCIPAL_REMARK_STR = "本行数据是本金的利息，本金为：";
    //还息的说明
    private static final String INTEREST_REMARK_STR = "本行数据是还息计划的利息说明，此时的本金为：";
    //还款的开始日期说明
    private static final String START_DATE_REMARK_STR = "开始日期：";
    //还款的结束日期说明
    private static final String END_DATE_REMARK_STR = "结束时间：";
    //还款的计算天数说明
    private static final String TOTAL_DAYS_REMARK_STR = "天数共计：";
    //执行利率说明
    private static final String STRIKE_RATE_REMARK_STR = "执行利率为：";
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
                                                                    double floatRate, BigDecimal amount, int adjustDays,
                                                                    Integer interestMeasurementCycle){
        System.out.println("本次执行的是lpr的还本付息的算法，本行代码于54行");
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

        List<FRepaymentPlanDTO> repayList = new ArrayList<>();
        List<FRepaymentPlanDTO> repayPrincipalList = new ArrayList<>();
        principalList.forEach(fRepayPrincipalPlanDTO -> {
            FRepaymentPlanDTO fRepaymentPlanDTO = new FRepaymentPlanDTO();
            BeanUtils.copyProperties(fRepayPrincipalPlanDTO, fRepaymentPlanDTO);
            repayPrincipalList.add(fRepaymentPlanDTO);
        });
        List<FRepaymentPlanDTO> repayInterestList = new ArrayList<>();
        interestList.forEach(fRepayInterestPlanDTO -> {
            FRepaymentPlanDTO fRepaymentPlanDTO = new FRepaymentPlanDTO();
            BeanUtils.copyProperties(fRepayInterestPlanDTO, fRepaymentPlanDTO);
            repayInterestList.add(fRepaymentPlanDTO);
        });
        //获取合同起息日
        Date startDate = repayInterestList.get(0).getStartDate();
        //初始化上一次还款日期
        Date lastPayDate = startDate;
        //初始化当前还款日期
        Date currPayDate = startDate;
        long currPayTime = currPayDate.getTime();
        //初始化上一次计算利息的参考日期
        Date lastRateDate = startDate;
        //初始化当前使用的lpr利率
        BigDecimal currLRP = new BigDecimal(0.000) ;
        //根据合同还款日期从LPR信息表获取当前的LPR利率
        currLRP = new BigDecimal((InterestCalculatedUtil.getLprRate(lprDTOList,startDate,lprInterestCycle)+floatRate)/100).setScale(6,ROUND_HALF_UP);
        //获取贷款金额
        BigDecimal theAmount = amount;
        //根据还款日期逐一计算还款利息
        for (int i = 0; i < repayInterestList.size(); i++) {
            if (i==0){
                lastPayDate = new Date(repayInterestList.get(i).getStartDate().getTime());
            }else {
                lastPayDate =new Date (repayInterestList.get(i).getStartDate().getTime()- DAY_TIME);
            }
            //如果在该周期中有还本计划的存在
            for (FRepaymentPlanDTO fRepayPrincipalPlanDTO : repayPrincipalList) {
                long endTime = fRepayPrincipalPlanDTO.getEndDate().getTime();
                //判断是否存在还本计划
                if (endTime>lastPayDate.getTime() &
                        endTime<repayInterestList.get(i).getEndDate().getTime()){
                    //存在还本计划，那么计算该还本计划对应的利息并加上本金
                    //初始化还款日期
                    Date sDate = repayInterestList.get(i).getStartDate();
                    long time = sDate.getTime();
                    //初始化lpr
                    BigDecimal lpr = currLRP;
                    //初始化上次的计算利息的擦考日期
                    Date lDate = lastRateDate;
                    //初始化利息
                    BigDecimal bigDecimal = new BigDecimal("0.00");
                    //获得天数
                    long day = (endTime - lastPayDate.getTime()) / DAY_TIME;
                    for (long l = 0; l < day; l++) {
                        //当前还款日递增
                        time+= DAY_TIME;
                        sDate = new Date(time);
                        //达到周期的情况下重新获取当前日期的lpr
                        if (sDate.getTime() - lDate.getTime() >= adjustDays* DAY_TIME) {
                            lpr = new BigDecimal ((InterestCalculatedUtil.getLprRate(lprDTOList,sDate,lprInterestCycle)+floatRate)/100).setScale(5,RoundingMode.HALF_UP);
                            lDate = sDate;
                        }
                        //计算出利息 当前lpr*还款本金/测算周期
                        BigDecimal v = lpr.multiply(fRepayPrincipalPlanDTO.getRepaymentOfPrincipal()).divide(BigDecimal.valueOf(interestMeasurementCycle),5,RoundingMode.HALF_UP);
                        bigDecimal = bigDecimal.add(v);
                    }
                    //利息加本金
                    //BigDecimal add = bigDecimal.add(fRepayPrincipalPlanDTO.getRepaymentOfPrincipal());
                    fRepayPrincipalPlanDTO.setInterest(bigDecimal);
                    //加上备注
                    //TODO
                    //本行数据本金：xxxx，开始日期：xxxx，结束日期：xxxx，共计xx天，执行利率为：
                    fRepayPrincipalPlanDTO.setRemark(
                            PRINCIPAL_REMARK_STR + fRepayPrincipalPlanDTO.getRepaymentOfPrincipal().toPlainString()+"元，"+
                            START_DATE_REMARK_STR + repayInterestList.get(i).getStartDate().toString()+
                            END_DATE_REMARK_STR + sDate.toString()+
                            TOTAL_DAYS_REMARK_STR + day +
                            STRIKE_RATE_REMARK_STR + lpr);
                    //余额减
                    theAmount = theAmount.subtract(fRepayPrincipalPlanDTO.getRepaymentOfPrincipal());
                    //如果还款日期等于上一次还息日期
                }else if(endTime == (repayInterestList.get(i).getStartDate().getTime()- DAY_TIME)){
                    //直接设置为0
                    fRepayPrincipalPlanDTO.setInterest(new BigDecimal(0));
                    //余额减
                    theAmount = theAmount.subtract(fRepayPrincipalPlanDTO.getRepaymentOfPrincipal());
                }
            }
            //初始化还款利息
            BigDecimal theInterest = new BigDecimal(0);
            //当前还款日期
            Date theDate = repayInterestList.get(i).getEndDate();
            //还款日和上次还款日的天数
            Long days = (theDate.getTime() - lastPayDate.getTime())/ DAY_TIME;
            for (Long aLong = 0L; aLong < days; aLong++) {
                //当前还款日递增
                currPayTime+= DAY_TIME;
                currPayDate = new Date(currPayTime);
                //达到周期的情况下重新获取当前日期的lpr
                if (currPayDate.getTime() - lastRateDate.getTime() >= adjustDays* DAY_TIME) {
                    currLRP = new BigDecimal((InterestCalculatedUtil.getLprRate(lprDTOList,currPayDate,lprInterestCycle)+floatRate)/100).setScale(5,RoundingMode.HALF_UP);
                    lastRateDate = currPayDate;
                }
                //利息累加
                //贷款余额*当前lpr/360
                BigDecimal s = theAmount.multiply(currLRP).divide(BigDecimal.valueOf(interestMeasurementCycle),3,RoundingMode.HALF_UP);
                theInterest = theInterest.add(s);
            }
            repayInterestList.get(i).setInterest(theInterest);
            repayInterestList.get(i).setOddCorpus(theAmount);
            repayInterestList.get(i).setRemark(
                            INTEREST_REMARK_STR + theAmount.toPlainString()+"元，"+
                            START_DATE_REMARK_STR + formatter.format(new Date(lastPayDate.getTime()+DAY_TIME))+"，"+
                            END_DATE_REMARK_STR + formatter.format(theDate)+"， "+
                            TOTAL_DAYS_REMARK_STR + days +"，"+
                            STRIKE_RATE_REMARK_STR + currLRP.multiply(new BigDecimal(100.00)).setScale(2, RoundingMode.HALF_UP) +"%");
        }
        //对日期列表做处理
//        for (int i = repayInterestList.size()-1; i >0; i--) {
//            BigDecimal interest = repayInterestList.get(i).getInterest();
//            BigDecimal interest1 = repayInterestList.get(i-1).getInterest();
//            repayInterestList.get(i).setInterest(interest.subtract(interest1));
//        }
        //保留三位小数，四舍五入
        for (int i = 0; i < repayInterestList.size(); i++) {
            repayInterestList.get(i).setInterest(repayInterestList.get(i).getInterest().setScale(3, BigDecimal.ROUND_HALF_UP));
        }
        //过滤掉还本计划中利息为0或者空的数据
        Iterator<FRepaymentPlanDTO> iterator = repayPrincipalList.iterator();
        while(iterator.hasNext()){
            FRepaymentPlanDTO fRepaymentPlanDTO = iterator.next();
            System.out.println(fRepaymentPlanDTO.getInterest());
            if (fRepaymentPlanDTO.getInterest() == null || fRepaymentPlanDTO.getInterest().doubleValue() == 0.00){
                iterator.remove();
            }
        }
        repayList.addAll(repayPrincipalList);
        repayList.addAll(repayInterestList);
        repayList.sort(Comparator.comparing(FRepaymentPlanDTO::getEndDate));
        return repayList;
    }

    /**
     * 间融非还本付息的算法
     * @param principalList
     * @param interestList
     * @param lprDTOList
     * @param lprInterestCycle
     * @param floatRate
     * @param amount
     * @param adjustDays
     * @param interestMeasurementCycle
     * @return
     */
    public static List<FRepaymentPlanDTO> repayInterest(List<FRepayPrincipalPlanDTO> principalList,
                                                                    List<FRepayInterestPlanDTO> interestList,
                                                                    List<FLprDTO> lprDTOList, int lprInterestCycle,
                                                                    double floatRate, BigDecimal amount, int adjustDays,
                                                                    Integer interestMeasurementCycle){
        System.out.println("本次执行的是lpr的非还本付息的算法，本行代码于208行");
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

        List<FRepaymentPlanDTO> repayPrincipalList = new ArrayList<>();
        for (FRepayPrincipalPlanDTO fRepayPrincipalPlanDTO : principalList) {
            FRepaymentPlanDTO fRepaymentPlanDTO = new FRepaymentPlanDTO();
            BeanUtils.copyProperties(fRepayPrincipalPlanDTO,fRepaymentPlanDTO);
            repayPrincipalList.add(fRepaymentPlanDTO);
        }
        //初始化还本时间Map
        Map<Date,BigDecimal> dateMap = new HashMap<>();
        for (FRepaymentPlanDTO fRepaymentPlanDTO : repayPrincipalList) {
            dateMap.put(fRepaymentPlanDTO.getEndDate(),fRepaymentPlanDTO.getRepaymentOfPrincipal());
        }
        List<FRepaymentPlanDTO> repayInterestList = new ArrayList<>();
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
        long lastPayTime = lastPayDate.getTime();
        //初始化当前还款日期
        Date currPayDate = startDate;
        long currPayTime = currPayDate.getTime();
        //初始化上一次计算利息的参考日期
        Date lastRateDate = startDate;
        long lastRateDateTime = lastRateDate.getTime();
        //初始化当前使用的lpr利率
        BigDecimal currLRP = new BigDecimal(0.000) ;
        //根据合同还款日期从LPR信息表获取当前的LPR利率
        currLRP = new BigDecimal((InterestCalculatedUtil.getLprRate(lprDTOList,startDate,lprInterestCycle)+floatRate)/100).setScale(5,RoundingMode.HALF_UP);
        //获取贷款金额
        BigDecimal theAmount = amount;
        //根据还款日期逐一计算还款利息
        for (int i = 0; i < repayInterestList.size(); i++) {
            if (i==0){
                lastPayDate = new Date(repayInterestList.get(i).getStartDate().getTime());
            }else {
                lastPayDate =new Date (repayInterestList.get(i).getStartDate().getTime()- DAY_TIME);
            }
            //初始化还款利息
            BigDecimal theInterest = new BigDecimal(0);
            //当前还款日期
            Date theDate = repayInterestList.get(i).getEndDate();
            //还款日和上次还款日的天数
            Long days = (theDate.getTime() - lastPayDate.getTime())/ DAY_TIME;
            for (Long aLong = 0L; aLong < days; aLong++) {
                //如果有还本就还款，减少本金
                if (dateMap.containsKey(currPayDate)){
                    BigDecimal bigDecimal = dateMap.get(currPayDate);
                    theAmount = theAmount.subtract(bigDecimal);
                }
                //当前还款日递增
                currPayTime+= DAY_TIME;
                currPayDate = new Date(currPayTime);
                //达到周期的情况下重新获取当前日期的lpr
                if (currPayDate.getTime() - lastRateDate.getTime() >= adjustDays* DAY_TIME) {
                    currLRP = new BigDecimal ((InterestCalculatedUtil.getLprRate(lprDTOList,currPayDate,lprInterestCycle)+floatRate)/100).setScale(5,RoundingMode.HALF_UP);
                    lastRateDate = currPayDate;
                }
                //利息累加
                //贷款余额*当前lpr/360
                BigDecimal s = theAmount.multiply(currLRP).divide(BigDecimal.valueOf(interestMeasurementCycle),3,RoundingMode.HALF_UP);
                theInterest = theInterest.add(s);
            }
            repayInterestList.get(i).setInterest(theInterest);
            repayInterestList.get(i).setOddCorpus(theAmount);
            repayInterestList.get(i).setRemark(
                            INTEREST_REMARK_STR + theAmount.toPlainString()+"元，"+
                            START_DATE_REMARK_STR + formatter.format(new Date(lastPayDate.getTime()+DAY_TIME))+"，"+
                            END_DATE_REMARK_STR + formatter.format(theDate)+"， "+
                            TOTAL_DAYS_REMARK_STR + days +"，"+
                            STRIKE_RATE_REMARK_STR + currLRP.multiply(new BigDecimal(100.00)).setScale(2, RoundingMode.HALF_UP) +"%");
        }
        //保留三位小数，四舍五入
        for (int i = 0; i < repayInterestList.size(); i++) {
            repayInterestList.get(i).setInterest(repayInterestList.get(i).getInterest().setScale(3, BigDecimal.ROUND_HALF_UP));
        }
        return repayInterestList;
    }

    /**
     * 间融固定利率还本付息的算法
     * @param principalList 还本计划列表
     * @param interestList  还息计划列表
     * @param floatRate     上浮利率
     * @param amount        贷款金额
     * @param interestMeasurementCycle   测算周期（360/365）
     * @param rate          固定利率
     */
    public static List<FRepaymentPlanDTO> repayFixedRate(List<FRepayPrincipalPlanDTO> principalList,
                                                                    List<FRepayInterestPlanDTO> interestList,
                                                                    double floatRate, BigDecimal amount,
                                                                    Integer interestMeasurementCycle,Double rate){
        System.out.println("本次执行的是固定利率的还本付息的算法，本行代码于301行");

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

        List<FRepaymentPlanDTO> repayList = new ArrayList<>();
        List<FRepaymentPlanDTO> repayPrincipalList = new ArrayList<>();
        for (FRepayPrincipalPlanDTO fRepayPrincipalPlanDTO : principalList) {
            FRepaymentPlanDTO fRepaymentPlanDTO = new FRepaymentPlanDTO();
            BeanUtils.copyProperties(fRepayPrincipalPlanDTO,fRepaymentPlanDTO);
            repayPrincipalList.add(fRepaymentPlanDTO);
        }
        List<FRepaymentPlanDTO> repayInterestList = new ArrayList<>();
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
        long lastPayTime = lastPayDate.getTime();
        //初始化当前还款日期
        Date currPayDate = startDate;
        long currPayTime = currPayDate.getTime();
        //初始化上一次计算利息的参考日期
        Date lastRateDate = startDate;
        long lastRateDateTime = lastRateDate.getTime();
        //初始化当前使用的lpr利率
        BigDecimal currLRP = new BigDecimal(0.000);
        //根据合同还款日期从LPR信息表获取当前的LPR利率
        currLRP = new BigDecimal(rate+floatRate).divide(new BigDecimal(100.00),6,RoundingMode.HALF_UP);
//        currLRP = rate/100;
        //获取贷款金额
        BigDecimal theAmount = amount;
        //根据还款日期逐一计算还款利息
        for (int i = 0; i < repayInterestList.size(); i++) {
            if (i==0){
                lastPayDate = new Date(repayInterestList.get(i).getStartDate().getTime());
            }else {
                lastPayDate =new Date (repayInterestList.get(i).getStartDate().getTime()- DAY_TIME);
            }
            //如果在该周期中有还本计划的存在
            for (FRepaymentPlanDTO fRepayPrincipalPlanDTO : repayPrincipalList) {
                long endTime = fRepayPrincipalPlanDTO.getEndDate().getTime();
                //判断是否存在还本计划
                if (endTime>lastPayDate.getTime() &
                        endTime<repayInterestList.get(i).getEndDate().getTime()){
                    //存在还本计划，那么计算该还本计划对应的利息并加上本金
                    //初始化还款日期
                    Date sDate = lastPayDate;
                    long time = sDate.getTime();
                    //初始化lpr
                    Double lpr = currLRP.doubleValue();
                    //初始化利息
                    BigDecimal bigDecimal = new BigDecimal("0.00");
                    //获得天数
                    long day = (endTime - lastPayDate.getTime()) / DAY_TIME;
                    System.out.println("进来的stdate"+sDate);
//                    System.out.println("第"+(i+1)+"次的时候，开始日期和还本日期："+new Date(endTime)+lastPayDate);
//                    System.out.println("第"+(i+1)+"次的时候，计算本金的天数:"+day);

                    for (long l = 0; l < day; l++) {
                        //当前还款日递增
                        time+= DAY_TIME;
                        sDate = new Date(time);
                        //计算出利息 当前lpr*还款本金/测算周期
                        double v = lpr * fRepayPrincipalPlanDTO.getRepaymentOfPrincipal().doubleValue() / interestMeasurementCycle;
                        bigDecimal = BigDecimal.valueOf(bigDecimal.doubleValue()+v);
                    }
//                    System.out.println("第"+(i+1)+"次的时候，利息:"+bigDecimal);
                    //利息加本金
                    //BigDecimal add = bigDecimal.add(fRepayPrincipalPlanDTO.getRepaymentOfPrincipal());
                    fRepayPrincipalPlanDTO.setInterest(bigDecimal);
                    //加上备注
                    //TODO
                    fRepayPrincipalPlanDTO.setRemark(
                            PRINCIPAL_REMARK_STR + fRepayPrincipalPlanDTO.getRepaymentOfPrincipal().toPlainString()+"元，"+
                            START_DATE_REMARK_STR + formatter.format(repayInterestList.get(i).getStartDate())+"，"+
                            END_DATE_REMARK_STR + formatter.format(sDate)+"， "+
                            TOTAL_DAYS_REMARK_STR + day +"，"+
                            STRIKE_RATE_REMARK_STR + lpr*100.00 +"%"
                    );
                    //余额减
                    theAmount = theAmount.subtract(fRepayPrincipalPlanDTO.getRepaymentOfPrincipal());
                    //如果还款日期等于上一次还息日期
                }else if(endTime == (repayInterestList.get(i).getStartDate().getTime()- DAY_TIME)){
                    //直接设置为0
                    fRepayPrincipalPlanDTO.setInterest(new BigDecimal(0));
                    //余额减
                    theAmount = theAmount.subtract(fRepayPrincipalPlanDTO.getRepaymentOfPrincipal());
                }
            }
            //初始化还款利息
            BigDecimal theInterest = new BigDecimal(0);
            //当前还款日期
            Date theDate = repayInterestList.get(i).getEndDate();
            //还款日和上次还款日的天数
            Long days = (theDate.getTime() - lastPayDate.getTime())/ DAY_TIME;
            for (Long aLong = 0L; aLong < days; aLong++) {
                //当前还款日递增
                currPayTime+= DAY_TIME;
                currPayDate = new Date(currPayTime);
                //利息累加
                //贷款余额*当前lpr/360
                double v = theInterest.doubleValue();
                double s = ((theAmount.multiply(currLRP)).divide(new BigDecimal(interestMeasurementCycle),5, RoundingMode.HALF_UP)).doubleValue();
                theInterest = new BigDecimal(v+s);
            }
            repayInterestList.get(i).setInterest(theInterest);
            repayInterestList.get(i).setOddCorpus(theAmount);
            repayInterestList.get(i).setRemark(
                    INTEREST_REMARK_STR + theAmount.toPlainString()+"元，"+
                    START_DATE_REMARK_STR + formatter.format(new Date(lastPayDate.getTime()+DAY_TIME))+"，"+
                    END_DATE_REMARK_STR + formatter.format(theDate)+"， "+
                    TOTAL_DAYS_REMARK_STR + days +"，"+
                    STRIKE_RATE_REMARK_STR + currLRP.multiply(new BigDecimal(100.00)).setScale(2, RoundingMode.HALF_UP) +"%");
        }
        //保留三位小数，四舍五入
        for (int i = 0; i < repayInterestList.size(); i++) {
            repayInterestList.get(i).setInterest(repayInterestList.get(i).getInterest().setScale(3, BigDecimal.ROUND_HALF_UP));
        }
        Iterator<FRepaymentPlanDTO> iterator = repayPrincipalList.iterator();
        //过滤掉还本计划中利息为空或者0的数据
        while(iterator.hasNext()){
            FRepaymentPlanDTO fRepaymentPlanDTO = iterator.next();
            if (fRepaymentPlanDTO.getInterest()==null || fRepaymentPlanDTO.getInterest().doubleValue() == 0.00){
                iterator.remove();
            }
        }
        repayList.addAll(repayPrincipalList);
        repayList.addAll(repayInterestList);
        repayList.sort(Comparator.comparing(FRepaymentPlanDTO::getEndDate));
        return repayList;
    }

    /**
     * 间融固定利率非还本付息的算法
     * @param principalList 还本计划列表
     * @param interestList  还息计划列表
     * @param floatRate     上浮利率
     * @param amount        贷款金额
     * @param interestMeasurementCycle   测算周期（360/365）
     * @param rate          固定利率
     */
    public static List<FRepaymentPlanDTO> repayFixedRateInterest(List<FRepayPrincipalPlanDTO> principalList,
                                                         List<FRepayInterestPlanDTO> interestList,
                                                         double floatRate, BigDecimal amount,
                                                         Integer interestMeasurementCycle,Double rate){
        System.out.println("本次执行的是固定利率的非还本付息的算法，本行代码于464行");
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

        List<FRepaymentPlanDTO> repayPrincipalList = new ArrayList<>();
        for (FRepayPrincipalPlanDTO fRepayPrincipalPlanDTO : principalList) {
            FRepaymentPlanDTO fRepaymentPlanDTO = new FRepaymentPlanDTO();
            BeanUtils.copyProperties(fRepayPrincipalPlanDTO,fRepaymentPlanDTO);
            repayPrincipalList.add(fRepaymentPlanDTO);
        }
        //初始化还本时间Map
        Map<Date,BigDecimal> dateMap = new HashMap<>();
        for (FRepaymentPlanDTO fRepaymentPlanDTO : repayPrincipalList) {
            dateMap.put(fRepaymentPlanDTO.getEndDate(),fRepaymentPlanDTO.getRepaymentOfPrincipal());
        }
        List<FRepaymentPlanDTO> repayInterestList = new ArrayList<>();
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
        long lastPayTime = lastPayDate.getTime();
        //初始化当前还款日期
        Date currPayDate = startDate;
        long currPayTime = currPayDate.getTime();
        //初始化上一次计算利息的参考日期
        Date lastRateDate = startDate;
        long lastRateDateTime = lastRateDate.getTime();
        //初始化当前使用的lpr利率
        BigDecimal currLRP = new BigDecimal(0.000);
        //根据合同还款日期从LPR信息表获取当前的LPR利率
        currLRP = new BigDecimal(rate+floatRate).divide(new BigDecimal(100.00),6,RoundingMode.HALF_UP);
        //获取贷款金额
        BigDecimal theAmount = amount;
        //根据还款日期逐一计算还款利息
        for (int i = 0; i < repayInterestList.size(); i++) {
            if (i==0){
                lastPayDate = new Date(repayInterestList.get(i).getStartDate().getTime());
            }else {
                lastPayDate =new Date (repayInterestList.get(i).getStartDate().getTime()- DAY_TIME);
            }
            //初始化还款利息
            BigDecimal theInterest = new BigDecimal(0);
            //当前还款日期
            Date theDate = repayInterestList.get(i).getEndDate();
            //还款日和上次还款日的天数
            Long days = (theDate.getTime() - lastPayDate.getTime())/ DAY_TIME;
            for (Long aLong = 0L; aLong < days; aLong++) {
                //如果有还本就还款，减少本金
                if (dateMap.containsKey(currPayDate)){
                    BigDecimal bigDecimal = dateMap.get(currPayDate);
                    theAmount = theAmount.subtract(bigDecimal);
                }
                //当前还款日递增
                currPayTime+= DAY_TIME;
                currPayDate = new Date(currPayTime);
                //利息累加
                //贷款余额*当前lpr/360
                double v = theInterest.doubleValue();
                double s = ((theAmount.multiply(currLRP)).divide(new BigDecimal(interestMeasurementCycle),4, RoundingMode.HALF_UP)).doubleValue();
                theInterest = new BigDecimal(v+s);
            }
            repayInterestList.get(i).setInterest(theInterest);
            repayInterestList.get(i).setOddCorpus(theAmount);
            repayInterestList.get(i).setRemark(
                            INTEREST_REMARK_STR + theAmount.toPlainString()+"元，"+
                            START_DATE_REMARK_STR + formatter.format(new Date(lastPayDate.getTime()+DAY_TIME))+"，"+
                            END_DATE_REMARK_STR + formatter.format(theDate)+"， "+
                            TOTAL_DAYS_REMARK_STR + days +"，"+
                            STRIKE_RATE_REMARK_STR + currLRP.multiply(new BigDecimal(100.00)).setScale(2, RoundingMode.HALF_UP) +"%");
        }
        //保留三位小数，四舍五入
        for (int i = 0; i < repayInterestList.size(); i++) {
            repayInterestList.get(i).setInterest(repayInterestList.get(i).getInterest().setScale(3, BigDecimal.ROUND_HALF_UP));
        }
        return repayInterestList;
    }
}
