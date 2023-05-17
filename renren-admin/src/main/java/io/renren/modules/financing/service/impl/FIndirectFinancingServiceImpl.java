package io.renren.modules.financing.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.renren.common.exception.ErrorCode;
import io.renren.common.exception.RenException;
import io.renren.common.page.PageData;
import io.renren.common.service.impl.CrudServiceImpl;
import io.renren.modules.financing.dao.FCreditRecordDao;
import io.renren.modules.financing.dao.FFinanceDeptDao;
import io.renren.modules.financing.dao.FIndirectFinancingDao;
import io.renren.modules.financing.dto.*;
import io.renren.modules.financing.entity.FIndirectFinancingEntity;
import io.renren.modules.financing.excel.FIndirectFinancingExcel;
import io.renren.modules.financing.service.FIndirectFinancingService;
import io.renren.modules.financing.service.FRepaymentActualService;
import io.renren.modules.financing.service.FRepaymentPlanService;
import io.renren.modules.financing.util.*;
import io.renren.modules.financing.vo.*;
import io.renren.modules.lpr.dao.FLprDao;
import io.renren.modules.lpr.dto.FLprDTO;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

/**
 * 间接融资信息
 *
 * @author liuwenyong 
 * @since 3.0 2022-05-14
 */
@Service
public class FIndirectFinancingServiceImpl
        extends CrudServiceImpl<FIndirectFinancingDao, FIndirectFinancingEntity, FIndirectFinancingDTO>
        implements FIndirectFinancingService {

    @Autowired
    private FIndirectFinancingDao fInDirectFinancingDao;

    @Autowired
    private FCreditRecordDao fCreditRecordDao;

    @Autowired
    private FFinanceDeptDao fFinanceDeptDao;


    @Autowired
    private FRepaymentPlanService fRepaymentPlanService;

    @Autowired
    private FLprDao fLprDao;

    @Autowired
    private FRepaymentActualService fRepaymentActualService;

    @Override
    public QueryWrapper<FIndirectFinancingEntity> getWrapper(Map<String, Object> params){
        QueryWrapper<FIndirectFinancingEntity> wrapper = new QueryWrapper<>();
        //先判断本次请求的单位是什么
        Integer unit = Integer.valueOf(params.get("unit").toString());
        if (unit == 2){
            BigDecimal bigDecimal = BigDecimal.valueOf(1000);
            if (!params.get("contractAmount").equals("")){
                BigDecimal contractAmount =new BigDecimal(params.get("contractAmount").toString());
                String ca = contractAmount.divide(bigDecimal, 3, RoundingMode.HALF_UP).toString();
                params.put("contractAmount",ca);
            }
            if (!params.get("withdrawalAmount").equals("")){
                BigDecimal withdrawalAmount =new BigDecimal(params.get("withdrawalAmount").toString());
                String wa = withdrawalAmount.divide(bigDecimal, 3, RoundingMode.HALF_UP).toString();
                params.put("withdrawalAmount",wa);
            }
        }
        //合同金额
        wrapper.like(!params.get("contractAmount").equals(""),"contract_amount",params.get("contractAmount"));
        //提款金额
        wrapper.like(!params.get("contractAmount").equals(""),"withdrawal_amount",params.get("withdrawalAmount"));
        //融资品种
        wrapper.like(!params.get("varieties").equals(""),"varieties",params.get("varieties"));
        //增信措施
        wrapper.like(params.get("creditMeasures") != null && !params.get("creditMeasures").equals(""),"credit_measures",params.get("creditMeasures"));
        if (params.get("isChecked") != null && !params.get("isChecked").equals("")){
            wrapper.eq("is_checked",params.get("isChecked"));
        }
        if (params.get("groupChecked") != null){
            wrapper.eq("is_checked","1");
            if (!params.get("groupChecked").equals("")){
                wrapper.eq("group_checked",params.get("groupChecked"));
            }
        }
        return wrapper;
    }


    @Override
    public void saveInfo(FIndirectFinancingDTO dto) {
        dto.setId(null);
        dto.setIsChecked(0);
        //保存还款计划
        saveRepaymentPlan(dto);
        //保存实际还款
        saveRepaymentActual(dto);

        //对增信措施的处理
        List<String> measuresList = dto.getCreditMeasuresList();
        boolean flag = CollectionUtils.isNotEmpty(measuresList);
        if (flag){
            dto.setCreditMeasures(StringUtils.join(measuresList,","));
        }
        //对中介机构的处理
        List<IntermediaryStructureVo> intermediaryStructureList = dto.getIntermediaryStructureList();
        String inter = JSONObject.toJSONString(intermediaryStructureList);
        dto.setIntermediaryStructure(inter);
        //对投资人的处理
        List<InvestorVO> investorList = dto.getInvestorList();
        String inv = JSONObject.toJSONString(investorList);
        dto.setInvestor(inv);


        //增信措施
        if(flag){
            CreditMeasuresVO info = dto.getGuaranteeInfo();
            //保证
            List<GuaranteeVO> guarantee = info.getGuarantee();
            String gua = JSONObject.toJSONString(guarantee);
            dto.setGuarantee(gua);
            //抵押
            List<CollateralVO> collateral = info.getCollateral();
            String col = JSONObject.toJSONString(collateral);
            dto.setCollateral(col);
            //质押
            List<PledgeVO> pledge = info.getPledge();
            String ple = JSONObject.toJSONString(pledge);
            dto.setPledge(ple);
        }
        //文件列表
        List<FileListVO> fileList = dto.getFileList();
        for (FileListVO fileListVO : fileList) {
            List<FileVO> files = fileListVO.getFiles();
            fileListVO.setFileStr(JSON.toJSONString(files));
            fileListVO.setFiles(null);
        }
        dto.setFiles(JSON.toJSONString(fileList));
        //进行还本计划和还息计划存入数据库的操作
        this.save(dto);
    }

    /**
     * 保存还款计划信息
     */
    private void saveRepaymentPlan(FIndirectFinancingDTO dto) {
        //对还款计划的处理
        PayPlanDataVO payPlanData = dto.getPayPlanData();
        //还息计划
        List<FRepayInterestPlanDTO> repayInterestPlanList = payPlanData.getRepayInterestPlanList();
        if(repayInterestPlanList != null){
            for (FRepayInterestPlanDTO fRepayInterestPlanDTO : repayInterestPlanList) {
                fRepayInterestPlanDTO.setFinancingId(dto.getId());
                fRepayInterestPlanDTO.setFinancingType(1);
                fRepayInterestPlanDTO.setPlanType(2);
                fRepaymentPlanService.saveRepayInterestPlan(fRepayInterestPlanDTO);
            }
        }
        //还本计划
        List<FRepayPrincipalPlanDTO> repayPrincipalPlanList = payPlanData.getRepayPrincipalPlanList();
        if(repayPrincipalPlanList != null){
            for (FRepayPrincipalPlanDTO fRepayPrincipalPlanDTO : repayPrincipalPlanList) {
                fRepayPrincipalPlanDTO.setFinancingId(dto.getId());
                fRepayPrincipalPlanDTO.setFinancingType(1);
                fRepayPrincipalPlanDTO.setPlanType(1);
                fRepaymentPlanService.saveRepayPrincipalPlan(fRepayPrincipalPlanDTO);
            }
        }
        //还息计划保存
        PayPlanInfoVo payPlanInfoVo = new PayPlanInfoVo(payPlanData.getInterestMeasurementCycle(), payPlanData.getEndNeedCount());
        String s = JSONObject.toJSONString(payPlanInfoVo);
        dto.setRepaymentPlan(s);
    }

    /**
     * 保存实际还款信息
     */
    private void saveRepaymentActual(FIndirectFinancingDTO dto) {
        PayActualDataVO payActualData = dto.getPayActualData();
        //保存实际付息
        List<FRepayInterestActualDTO> repayInterestActualList = payActualData.getRepayInterestActualList();
        for (FRepayInterestActualDTO dto1 : repayInterestActualList) {
            dto1.setFinancingId(dto.getId());
            dto1.setFinancingType(1);
            fRepaymentActualService.saveRepayInterestActual(dto1);
        }
        //保存实际还本
        List<FRepayPrincipalActualDTO> repayPrincipalActualList = payActualData.getRepayPrincipalActualList();
        for (FRepayPrincipalActualDTO dto1 : repayPrincipalActualList) {
            dto1.setFinancingId(dto.getId());
            dto1.setFinancingType(1);
            fRepaymentActualService.saveRepayPrincipalActual(dto1);
        }
        //保存实际还款详情
        PayActualInfoVo payActualInfoVo = new PayActualInfoVo();
        BeanUtils.copyProperties(payActualData,payActualInfoVo);
        dto.setRepaymentActual(JSONObject.toJSONString(payActualInfoVo));
    }

    @Override
    public FIndirectFinancingDTO getDealData(Long id) {
        //增信措施详情
        FIndirectFinancingDTO dealData = fInDirectFinancingDao.getDealData(id);
        //设置还款计划和实际还款
        setRepaymentPlanAndActual(id,dealData);
        //设置增信措施
        creditMeasuresJsonStrToObject(dealData);
        //中介机构
        String intermediaryStructure = dealData.getIntermediaryStructure();
        List<IntermediaryStructureVo> intermediaryStructureVos =
                JSON.parseObject(intermediaryStructure, new TypeReference<List<IntermediaryStructureVo>>() {});
        dealData.setIntermediaryStructureList(intermediaryStructureVos);
        //投资人
        String investor = dealData.getInvestor();
        List<InvestorVO> investorVOS = JSON.parseObject(investor, new TypeReference<List<InvestorVO>>() {});
        dealData.setInvestorList(investorVOS);
        //文件列表
        String files = dealData.getFiles();
        if (files != null){
            List<FileListVO> fileListVOs = JSON.parseObject(files,new TypeReference<List<FileListVO>>(){});
            for (FileListVO fileListVO : fileListVOs) {
                String fileStr = fileListVO.getFileStr();
                List<FileVO> fileVOs = JSON.parseObject(fileStr,new TypeReference<List<FileVO>>(){});
                fileListVO.setFiles(fileVOs);
            }
            dealData.setFileList(fileListVOs);
        }
        return dealData;
    }

    /**
     * 增信措施json字符串转换为对象
     * @param dealData
     */
    private void creditMeasuresJsonStrToObject(FIndirectFinancingDTO dealData) {
        String guarantee = dealData.getGuarantee();
        String pledge = dealData.getPledge();
        String collateral = dealData.getCollateral();
        List<GuaranteeVO> gua = JSON.parseObject(guarantee,new TypeReference<List<GuaranteeVO>>(){});
        List<PledgeVO> ple = JSON.parseObject(pledge,new TypeReference<List<PledgeVO>>(){});
        List<CollateralVO> col = JSON.parseObject(collateral,new TypeReference<List<CollateralVO>>(){});
        CreditMeasuresVO creditMeasuresVO = new CreditMeasuresVO();
        creditMeasuresVO.setCollateral(col);
        creditMeasuresVO.setGuarantee(gua);
        creditMeasuresVO.setPledge(ple);
        dealData.setGuaranteeInfo(creditMeasuresVO);
    }

    /**
     * 设置实际还款和还款计划
     * @param id
     * @param dealData
     */
    private void setRepaymentPlanAndActual(Long id, FIndirectFinancingDTO dealData) {
        //还款计划
        dealData.setPayPlanData(new PayPlanDataVO());
        String repaymentPlan = dealData.getRepaymentPlan();
        if(!repaymentPlan.equals("")){
            PayPlanInfoVo payPlanInfoVo = JSON.parseObject(repaymentPlan, PayPlanInfoVo.class);
            dealData.getPayPlanData().setEndNeedCount(payPlanInfoVo.getEndNeedCount());
            dealData.getPayPlanData().setInterestMeasurementCycle(payPlanInfoVo.getInterestMeasurementCycle());
        }
        //还本计划和还息计划
        List<FRepayPrincipalPlanDTO> fRepayPrincipalPlanDTOS
                = fRepaymentPlanService.selectRepayPrincipalPlan(id, 2);
        List<FRepayInterestPlanDTO> fRepayInterestPlanDTOS
                = fRepaymentPlanService.selectRepayInterestPlan(id, 2);
        PayPlanDataVO payPlanData = dealData.getPayPlanData();
        payPlanData.setRepayInterestPlanList(fRepayInterestPlanDTOS);
        payPlanData.setRepayPrincipalPlanList(fRepayPrincipalPlanDTOS);

        //实际还本和实际付息
        //还款计划
        String repaymentActual = dealData.getRepaymentActual();
        PayActualDataVO payActualDataVO = new PayActualDataVO();
        if (repaymentActual!=null && !repaymentActual.equals("")){
            PayActualInfoVo payActualInfoVo = JSON.parseObject(repaymentActual, PayActualInfoVo.class);
            BeanUtils.copyProperties(payActualInfoVo,payActualDataVO);
        }
        //实际还本和实际付息的列表
        List<FRepayPrincipalActualDTO> fRepayPrincipalActualDTOS
                = fRepaymentActualService.selectRepayPrincipalActual(id, 2);
        List<FRepayInterestActualDTO> fRepayInterestActualDTOS
                = fRepaymentActualService.selectRepayInterestActual(id, 2);
        payActualDataVO.setRepayPrincipalActualList(fRepayPrincipalActualDTOS);
        payActualDataVO.setRepayInterestActualList(fRepayInterestActualDTOS);
        dealData.setPayActualData(payActualDataVO);
    }

    @Override
    public BigDecimal getQuota(Long deptId, Long institution) {
        BigDecimal quota = fCreditRecordDao.getQuota(deptId, institution);
        if(quota == null){
            quota = BigDecimal.ZERO;
        }
        return quota;
    }

    @Override
    public BigDecimal getUsedQuota(Long deptId,Long institution) {
        BigDecimal usedQuota = fInDirectFinancingDao.getUsedQuota(deptId, institution);
        if(usedQuota == null){
            usedQuota = BigDecimal.ZERO;
        }
        return usedQuota;
    }

    @Override
    public void updateInfo(FIndirectFinancingDTO dto) {
        if (dto.getWithdrawalAmount().subtract(dto.getPayActualData().getTotalRepayPrincipalActual()).doubleValue()<0.00){
            throw new RenException(ErrorCode.REPAYMENT_PRINCIPAL_TO_BIG_ERROR);
        }
        //更新还款计划
        updateRepaymentPlan(dto, dto.getPayPlanData());
        //更新实际还款
        updateRepaymentActual(dto,dto.getPayActualData());
        //对增信措施的处理
        List<String> measuresList = dto.getCreditMeasuresList();
        boolean flag = CollectionUtils.isNotEmpty(measuresList);
        if (flag){
            dto.setCreditMeasures(StringUtils.join(measuresList,","));
        }
        //对中介机构的处理
        List<IntermediaryStructureVo> intermediaryStructureList = dto.getIntermediaryStructureList();
        String inter = JSONObject.toJSONString(intermediaryStructureList);
        dto.setIntermediaryStructure(inter);
        //对投资人的处理
        List<InvestorVO> investorList = dto.getInvestorList();
        String inv = JSONObject.toJSONString(investorList);
        dto.setInvestor(inv);


        //增信措施
        if(flag){
            CreditMeasuresVO info = dto.getGuaranteeInfo();
            //保证
            List<GuaranteeVO> guarantee = info.getGuarantee();
            String gua = JSONObject.toJSONString(guarantee);
            dto.setGuarantee(gua);
            //抵押
            List<CollateralVO> collateral = info.getCollateral();
            String col = JSONObject.toJSONString(collateral);
            dto.setCollateral(col);
            //质押
            List<PledgeVO> pledge = info.getPledge();
            String ple = JSONObject.toJSONString(pledge);
            dto.setPledge(ple);
        }
        //文件列表
        if(dto.getFileList() == null){
            dto.setFiles(null);
        }else{
            List<FileListVO> fileList = dto.getFileList();
            for (FileListVO fileListVO : fileList) {
                List<FileVO> files = fileListVO.getFiles();
                fileListVO.setFileStr(JSON.toJSONString(files));
                fileListVO.setFiles(null);
            }
            dto.setFiles(JSON.toJSONString(fileList));
        }
        dto.setFinancingBalance(dto.getAvailableBalance());
        this.update(dto);
    }

    /**
     * 更新还款计划
     * @param dto
     * @param payPlanData
     */
    private void updateRepaymentPlan(FIndirectFinancingDTO dto, PayPlanDataVO payPlanData) {
        //存在，更新。不存在，删除
        List<FRepayInterestPlanDTO> repayInterestPlanList = payPlanData.getRepayInterestPlanList();
        fRepaymentPlanService.deleteRepayPlan(dto.getId(),1);
        //判断付息计划列表是否为空
        if(repayInterestPlanList != null){
            //遍历付息计划列表
            for (FRepayInterestPlanDTO fRepayInterestPlanDTO : repayInterestPlanList) {
                //填充相关信息
//                longs.add(fRepayInterestPlanDTO.getId());
                fRepayInterestPlanDTO.setFinancingId(dto.getId());
                fRepayInterestPlanDTO.setFinancingType(1);
                fRepayInterestPlanDTO.setPlanType(2);
                fRepaymentPlanService.saveRepayInterestPlan(fRepayInterestPlanDTO);
            }
        }
        //还本计划
        List<FRepayPrincipalPlanDTO> repayPrincipalPlanList = payPlanData.getRepayPrincipalPlanList();
        if(repayPrincipalPlanList != null){
            for (FRepayPrincipalPlanDTO fRepayPrincipalPlanDTO : repayPrincipalPlanList) {
//                longs.add(fRepayPrincipalPlanDTO.getId());
                fRepayPrincipalPlanDTO.setFinancingId(dto.getId());
                fRepayPrincipalPlanDTO.setFinancingType(1);
                fRepayPrincipalPlanDTO.setPlanType(1);
                fRepaymentPlanService.saveRepayPrincipalPlan(fRepayPrincipalPlanDTO);
            }
        }
        //还息计划保存
        PayPlanInfoVo payPlanInfoVo = new PayPlanInfoVo(payPlanData.getInterestMeasurementCycle(), payPlanData.getEndNeedCount());
        String s = JSONObject.toJSONString(payPlanInfoVo);
        dto.setRepaymentPlan(s);
    }

    /**
     * 更新实际还款
     * @param dto
     * @param payActualData
     */
    private void updateRepaymentActual(FIndirectFinancingDTO dto, PayActualDataVO payActualData) {
        //存在，更新。不存在，删除
        List<FRepayInterestActualDTO> repayInterestActualList = payActualData.getRepayInterestActualList();
        fRepaymentActualService.deleteRepayActual(dto.getId(),1);
        //判断实际付息列表是否为空
        if(repayInterestActualList != null){
            //遍历付息计划列表
            //填充相关信息
            //                longs.add(fRepayInterestPlanDTO.getId());
            repayInterestActualList.forEach(fRepayInterestActualDTO -> {
                fRepayInterestActualDTO.setFinancingId(dto.getId());
                fRepayInterestActualDTO.setFinancingType(1);
                fRepaymentActualService.saveRepayInterestActual(fRepayInterestActualDTO);
            });
        }
        //实际还本
        List<FRepayPrincipalActualDTO> repayPrincipalActualList = payActualData.getRepayPrincipalActualList();
        if(repayPrincipalActualList != null){
            //                longs.add(fRepayPrincipalPlanDTO.getId());
            repayPrincipalActualList.forEach(fRepayPrincipalActualDTO -> {
                fRepayPrincipalActualDTO.setFinancingId(dto.getId());
                fRepayPrincipalActualDTO.setFinancingType(1);
                fRepaymentActualService.saveRepayPrincipalActual(fRepayPrincipalActualDTO);
            });
        }
        //保存实际还款详情
        PayActualInfoVo payActualInfoVo = new PayActualInfoVo();
        BeanUtils.copyProperties(dto.getPayActualData(),payActualInfoVo);
        dto.setRepaymentActual(JSONObject.toJSONString(payActualInfoVo));

    }

    @Override
    public List<FIndirectFinancingDTO> screenList(List<FIndirectFinancingDTO> list, String deptName,String deadLine) {
        //遍历列表
        for (FIndirectFinancingDTO fIndirectFinancingDTO : list) {
            //1 先获得本条间融的融资单位对象
            Long deptId = fIndirectFinancingDTO.getDeptId();
            Long institution = fIndirectFinancingDTO.getInstitution();
            FIndirectDeptDTO dept = fFinanceDeptDao.getDept(deptId,institution);
            //2 设置融资单位名
            fIndirectFinancingDTO.setDeptName(dept.getDeptName());
            //3 设置金融机构种类
            fIndirectFinancingDTO.setInstitutionType(dept.getType());
            //4 设置金融机构名称
            fIndirectFinancingDTO.setInstitutionName(dept.getName());
            //5 设置可用余额
            fIndirectFinancingDTO.setAvailableBalance(fIndirectFinancingDTO.getFinancingBalance());
            //6 设置授信额度
            fIndirectFinancingDTO.setQuota(this.getQuota(fIndirectFinancingDTO.getDeptId(),
                    fIndirectFinancingDTO.getInstitution()));
            //7 设置同机构和融资id的单位已使用的额度
            fIndirectFinancingDTO.setUsedQuota(this.getUsedQuota(fIndirectFinancingDTO.getDeptId(),
                    fIndirectFinancingDTO.getInstitution()));
            //8 设置期限
            fIndirectFinancingDTO.setDeadLine((fIndirectFinancingDTO.getRepaymentDate().getTime()
                    - fIndirectFinancingDTO.getLoanDate().getTime())/(30L*24*60*60*1000));
            //9 设置浮动利率
            //先判断InterestRatesWay如果等于1就是浮动利率，那么就去查出当期的lpr并加上浮动的数值赋值给rate
            if (fIndirectFinancingDTO.getInterestRatesWay().equals("1")){
                double lprRate = InterestCalculatedUtil.getLprRate(fLprDao.getAllLpr(), fIndirectFinancingDTO.getLoanDate(),
                        fIndirectFinancingDTO.getLprInterestCycle());
                fIndirectFinancingDTO.setRate(String.valueOf(lprRate + Double.valueOf(fIndirectFinancingDTO.getFloatRate())));
            }
        }
        list.removeIf(next -> !QueryUtil.judgeConditionIndirectFinancing(next, deptName, deadLine));
        return list;
    }

    @Override
    public List<FIndirectFinancingExcel> getExportData(List<FIndirectFinancingDTO> list) {
        List<FIndirectFinancingExcel> excelList = new ArrayList<>();
        for (FIndirectFinancingDTO dto : list) {
            Long deptId = dto.getDeptId();
            Long institution = dto.getInstitution();
            FIndirectDeptDTO dealData = fFinanceDeptDao.getDept(deptId, institution);
            dto.setDeptName(dealData.getDeptName());
            dto.setInstitutionName(dealData.getName());
            dto.setInstitutionType(dealData.getType());
            dto.setDeadLine((dto.getRepaymentDate().getTime() - dto.getLoanDate().getTime())/(30L*24*60*60*1000));
            FIndirectFinancingExcel excel = new FIndirectFinancingExcel();
            BeanUtils.copyProperties(dto,excel);
            FIndirectFinancingExcel excel1 = MyExcelUtil.indirectExcelExport(dto, excel);
            excelList.add(excel1);
        }
        return excelList;
    }

    @Override
    public  List<FRepaymentPlanDTO> interestCalculate(IndirectInterestVO vo) {
        //还本计划表
        List<FRepayPrincipalPlanDTO> principalList = vo.getPrincipalList();
        for (FRepayPrincipalPlanDTO fRepayPrincipalPlanDTO : principalList) {
            fRepayPrincipalPlanDTO.setPlanType(1);
        }
        //单位换算
        if (vo.getUnit() == 2){
            for (FRepayPrincipalPlanDTO fRepayPrincipalPlanDTO : principalList) {
                fRepayPrincipalPlanDTO.setOddCorpus(UnitCastUtil.multiplication(fRepayPrincipalPlanDTO.getOddCorpus()));
                fRepayPrincipalPlanDTO.setRepaymentOfPrincipal(UnitCastUtil.multiplication(fRepayPrincipalPlanDTO.getRepaymentOfPrincipal()));
            }
            vo.setWithdrawalAmount(UnitCastUtil.multiplication(vo.getWithdrawalAmount()));
        }
        //还息计划表
        List<FRepayInterestPlanDTO> interestList = vo.getInterestList();
        for (FRepayInterestPlanDTO fRepayInterestPlanDTO : interestList) {
            fRepayInterestPlanDTO.setPlanType(2);
        }
        //lpr列表
        List<FLprDTO> allLpr = fLprDao.getAllLpr();
        //lpr是一年还是五年
        int lprInterestCycle = vo.getLprInterestCycle();
        //上浮利率
        double floatRate = vo.getFloatRate();
        //贷款金额
        BigDecimal amount = vo.getWithdrawalAmount();
        //测算周期
        Integer interestMeasurementCycle = vo.getInterestMeasurementCycle();
        //调戏天数
        if (vo.getLprCycle() == null){
            vo.setLprCycle(0);
        }
        Integer lprCycle = vo.getLprCycle();
        Integer adjustDays = lprCycle*30;
        //是否是还本付息（否0是1）
        Integer isPayPrincipalAndInterest = vo.getIsPayPrincipalAndInterest();
        //获得固定利率
        double rate = vo.getRate();
        List<FRepaymentPlanDTO> fRepaymentPlanDTOS =null;
        //获得利率类型
        Integer interestRatesWay = vo.getInterestRatesWay();
        if (isPayPrincipalAndInterest == null){
            throw new RenException(ErrorCode.FINANCING_INFO_GAP);
        }
        if (interestRatesWay == 0 ){
            if (isPayPrincipalAndInterest == 0){
                fRepaymentPlanDTOS = InterestCountUtil.repayFixedRateInterest(principalList, interestList, floatRate,
                        amount, interestMeasurementCycle,rate);
            }else{
                fRepaymentPlanDTOS = InterestCountUtil.repayFixedRate(principalList,interestList,floatRate,amount,
                        interestMeasurementCycle,rate);
            }
        }else{
            if (isPayPrincipalAndInterest == 0){
                fRepaymentPlanDTOS = InterestCountUtil.repayInterest(principalList, interestList,
                        allLpr, lprInterestCycle, floatRate, amount, adjustDays, interestMeasurementCycle);
            }else {
                fRepaymentPlanDTOS = InterestCountUtil.repayPrincipalAndInterest(principalList, interestList,
                        allLpr, lprInterestCycle, floatRate, amount, adjustDays, interestMeasurementCycle);
            }
        }
        for (FRepaymentPlanDTO fRepaymentPlanDTO : fRepaymentPlanDTOS) {
        }
        return fRepaymentPlanDTOS;
    }

    /**
     * 间融首页列表单位转化
     */
    @Override
    public PageData<FIndirectFinancingDTO> indirectCast(PageData<FIndirectFinancingDTO> page,Integer unit){
        if (unit == 2) {
            List<FIndirectFinancingDTO> list = page.getList();
            for (FIndirectFinancingDTO fIndirectFinancingDTO : list) {
                fIndirectFinancingDTO = this.indirectInfoCast(fIndirectFinancingDTO,unit);
            }
        }
        return page;
    }

    @Override
    public FIndirectFinancingDTO indirectInfoCast(FIndirectFinancingDTO dto,Integer unit) {
        if(unit == 2){
            //合同金额
            dto.setContractAmount(UnitCastUtil.divider(dto.getContractAmount()));
            //开票金额
            dto.setInvoiceAmount(UnitCastUtil.divider(dto.getInvoiceAmount()));
            //提款金额
            dto.setWithdrawalAmount(UnitCastUtil.divider(dto.getWithdrawalAmount()));
            //可用余额
            dto.setAvailableBalance(UnitCastUtil.divider(dto.getFinancingBalance()));
            queryCast(dto);
        }
        return dto;
    }

    /**
     * 读取数据时实际还款和还款计划的单位转化
     * @param dto
     */
    private void queryCast(FIndirectFinancingDTO dto) {
        //还本计划中的金额
        if (dto.getPayPlanData() != null && dto.getPayPlanData().getRepayPrincipalPlanList() != null){
            List<FRepayPrincipalPlanDTO> repayPrincipalPlanList = dto.getPayPlanData().getRepayPrincipalPlanList();
            //还本计划剩余本金
            //还本计划还款金额
            repayPrincipalPlanList.forEach(fRepayPrincipalPlanDTO -> {
                fRepayPrincipalPlanDTO.setOddCorpus(UnitCastUtil.divider(fRepayPrincipalPlanDTO.getOddCorpus()));
                fRepayPrincipalPlanDTO.setRepaymentOfPrincipal(UnitCastUtil.divider(fRepayPrincipalPlanDTO.getRepaymentOfPrincipal()));
            });
        }
        if (dto.getPayActualData() !=null){
            //总实际还本
            dto.getPayActualData().setTotalRepayPrincipalActual(
                    UnitCastUtil.divider(dto.getPayActualData().getTotalRepayPrincipalActual()));
            if (dto.getPayActualData().getRepayPrincipalActualList() != null){
                //实际还本金额
                dto.getPayActualData().getRepayPrincipalActualList().
                        forEach(fRepayPrincipalActualDTO -> fRepayPrincipalActualDTO.setActualRepayPrincipal
                                (UnitCastUtil.divider(fRepayPrincipalActualDTO.getActualRepayPrincipal())));
            }
        }
    }

    @Override
    public FIndirectFinancingDTO indirectSaveCast(FIndirectFinancingDTO dto, Integer unit) {
        if(unit == 2){
            //合同金额
            dto.setContractAmount(UnitCastUtil.multiplication(dto.getContractAmount()));
            //开票金额
            dto.setInvoiceAmount(UnitCastUtil.multiplication(dto.getInvoiceAmount()));
            //提款金额
            dto.setWithdrawalAmount(UnitCastUtil.multiplication(dto.getWithdrawalAmount()));
            //可用余额
            dto.setFinancingBalance(UnitCastUtil.multiplication(dto.getAvailableBalance()));
            dto.setAvailableBalance(UnitCastUtil.multiplication(dto.getAvailableBalance()));
            //还本计划中的金额
            saveCast(dto);
        }
        return dto;
    }

    /**
     * 保存的时候的单位转换
     * @param dto
     */
    private void saveCast(FIndirectFinancingDTO dto) {
        //还本计划中的金额
        if (dto.getPayPlanData().getRepayPrincipalPlanList() != null){
            List<FRepayPrincipalPlanDTO> repayPrincipalPlanList = dto.getPayPlanData().getRepayPrincipalPlanList();
            //还本计划剩余本金
            //还本计划还款金额
            repayPrincipalPlanList.forEach(fRepayPrincipalPlanDTO -> {
                fRepayPrincipalPlanDTO.setOddCorpus(UnitCastUtil.multiplication(fRepayPrincipalPlanDTO.getOddCorpus()));
                fRepayPrincipalPlanDTO.setRepaymentOfPrincipal(UnitCastUtil.multiplication(fRepayPrincipalPlanDTO.getRepaymentOfPrincipal()));
            });
        }
        if (dto.getPayActualData() !=null){
            //总实际还本
            dto.getPayActualData().setTotalRepayPrincipalActual(
                    UnitCastUtil.multiplication(dto.getPayActualData().getTotalRepayPrincipalActual()));
            if (dto.getPayActualData().getRepayPrincipalActualList() != null){
                //实际还本金额
                dto.getPayActualData().getRepayPrincipalActualList().
                        forEach(fRepayPrincipalActualDTO -> fRepayPrincipalActualDTO.setActualRepayPrincipal
                                (UnitCastUtil.multiplication(fRepayPrincipalActualDTO.getActualRepayPrincipal())));
            }
        }
    }

    /**
     * 删除信息（包含实际还款列表以及还款计划列表）
     *
     * @param ids
     */
    @Override
    @Transactional
    public void deleteInfo(Long[] ids) {
        for (Long id : ids) {
            //删除所有的还款计划
            fRepaymentPlanService.deleteRepayPlan(id,1);
            //删除所有的实际还款
            fRepaymentActualService.deleteRepayActual(id,1);
        }
        delete(ids);
    }

}