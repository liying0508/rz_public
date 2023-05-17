package io.renren;


import io.renren.modules.financing.dao.FDictDao;
import io.renren.modules.financing.dto.FRepayInterestPlanDTO;
import io.renren.modules.financing.dto.FRepayPrincipalPlanDTO;
import io.renren.modules.financing.dto.FRepaymentPlanDTO;
import io.renren.modules.financing.service.FRepaymentInfoService;
import io.renren.modules.financing.service.FRepaymentPlanService;
import io.renren.modules.financing.util.InterestCalculatedUtil;
import io.renren.modules.oss.cloud.OSSFactory;
import io.renren.modules.report.dao.RCreditMonthDao;
import io.renren.modules.report.dto.vo.RIndirectFinancingReportVO;
import io.renren.modules.report.service.RFinancingBalanceService;
import io.renren.modules.report.service.RIndirectFinancingReportService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TestCode {

    @Autowired
    private RCreditMonthDao rCreditMonthDao;

    @Autowired
    private FRepaymentPlanService fRepaymentPlanService;

    @Autowired
    private FRepaymentInfoService fRepaymentInfoService;

    @Autowired
    private FDictDao fDictDao;

    @Autowired
    RIndirectFinancingReportService rIndirectFinancingReportService;

    @Autowired
    RFinancingBalanceService rFinancingBalanceService;
    @Test
    public void test() throws ParseException {
        DateFormat dateFormat1 = new SimpleDateFormat("yyyy-MM-dd");
        List<FRepayInterestPlanDTO> interestList = new ArrayList<>();
        List<FRepayPrincipalPlanDTO> principalList = new ArrayList<>();
        FRepayInterestPlanDTO fRepayInterestPlanDTO = new FRepayInterestPlanDTO();
        fRepayInterestPlanDTO.setStartDate(dateFormat1.parse("2022-01-01"));
        fRepayInterestPlanDTO.setEndDate(dateFormat1.parse("2022-02-01"));
        fRepayInterestPlanDTO.setId(1L);
        interestList.add(fRepayInterestPlanDTO);
        FRepayInterestPlanDTO fRepayInterestPlanDTO1 = new FRepayInterestPlanDTO();
        fRepayInterestPlanDTO1.setStartDate(dateFormat1.parse("2021-01-01"));
        fRepayInterestPlanDTO1.setEndDate(dateFormat1.parse("2021-02-01"));
        fRepayInterestPlanDTO1.setId(2L);
        interestList.add(fRepayInterestPlanDTO1);
        FRepayPrincipalPlanDTO fRepayPrincipalPlanDTO = new FRepayPrincipalPlanDTO();
        fRepayPrincipalPlanDTO.setStartDate(dateFormat1.parse("2021-01-01"));
        fRepayPrincipalPlanDTO.setEndDate(dateFormat1.parse("2021-02-01"));
        fRepayPrincipalPlanDTO.setId(3L);
        principalList.add(fRepayPrincipalPlanDTO);
        FRepayPrincipalPlanDTO fRepayPrincipalPlanDTO1 = new FRepayPrincipalPlanDTO();
        fRepayPrincipalPlanDTO1.setStartDate(dateFormat1.parse("2023-02-01"));
        fRepayPrincipalPlanDTO1.setEndDate(dateFormat1.parse("2023-03-01"));
        fRepayPrincipalPlanDTO1.setId(4L);
        principalList.add(fRepayPrincipalPlanDTO1);
        List<FRepaymentPlanDTO> fRepaymentPlanDTOS = InterestCalculatedUtil.mergeList(principalList, interestList);
        for (FRepaymentPlanDTO fRepaymentPlanDTO : fRepaymentPlanDTOS) {
            System.out.println(fRepaymentPlanDTO.toString());
        }
//        IndirectInterestVO vo = new IndirectInterestVO();
//        vo.setInterestWayRate(1);
//        vo.setLprCycle(12);
//        vo.setFloatRate(12);
//        vo.setLoanDate(TimeUtils.timeStamp2Date("2022-02-20 00:00:00"));
//        vo.setRepaymentDate(TimeUtils.timeStamp2Date("2024-09-01 00:00:00"));
//        vo.setWithdrawalAmount(new BigDecimal(200000000.00));
//        PayPlanDataVO pp = new PayPlanDataVO();
//        pp.setInterestMeasurementCycle("360");
//        pp.setEndNeedCount(0);
//        List<PayPlanVO> list = new ArrayList<>();
//        PayPlanVO payPlanVO = new PayPlanVO();
//        payPlanVO.setStartDate("2029-02-20");
//        payPlanVO.setEndDate("2024-03-19");
//        payPlanVO.setRepaymentOfPrincipal("0");
//        list.add(payPlanVO);
//        PayPlanVO payPlanVO1 = new PayPlanVO();
//        payPlanVO1.setStartDate("2024-03-20");
//        payPlanVO1.setEndDate("2024-04-19");
//        payPlanVO1.setRepaymentOfPrincipal("500000");
//        list.add(payPlanVO1);
//        pp.setPayPlan(list);
//        vo.setPayPlanData(pp);
//        IndirectInterestVO vo1 = fIndirectFinancingService.interestCalculate(vo);
//        List<PayPlanVO> payPlan = vo1.getPayPlanData().getPayPlan();
//        for (PayPlanVO planVO : payPlan) {
//            System.out.println(planVO.getInterest());
//        }
    }
    @Test
    public void test2(){
        rFinancingBalanceService.listInfo(new HashMap<>());
    }

    @Test
    public void test3(){
//        OSSFactory.build().delete("https://financingmanagemens.oss-cn-shenzhen.aliyuncs.com/20221011/537240a93ac64182b95154b44357d106.jpeg");
        OSSFactory.build().clearRubbish();
    }

    @Test
    public void test4(){
        List<RIndirectFinancingReportVO> reportInfo = rIndirectFinancingReportService.getReportInfo();
    }
}
