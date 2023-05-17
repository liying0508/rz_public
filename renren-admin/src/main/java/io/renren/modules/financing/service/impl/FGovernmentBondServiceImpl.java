package io.renren.modules.financing.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.renren.common.exception.ErrorCode;
import io.renren.common.exception.RenException;
import io.renren.common.service.impl.CrudServiceImpl;
import io.renren.common.constant.Constant;
import io.renren.modules.financing.dao.FGovernmentBondDao;
import io.renren.modules.financing.dto.*;
import io.renren.modules.financing.entity.FGovernmentBondEntity;
import io.renren.modules.financing.excel.FGovernmentBondExcel;
import io.renren.modules.financing.service.FFinanceDeptService;
import io.renren.modules.financing.service.FGovernmentBondService;
import io.renren.modules.financing.service.FRepaymentActualService;
import io.renren.modules.financing.service.FRepaymentPlanService;
import io.renren.modules.financing.util.InterestCountUtil;
import io.renren.modules.financing.util.MyExcelUtil;
import io.renren.modules.financing.util.UnitCastUtil;
import io.renren.modules.financing.vo.*;
import io.renren.modules.security.user.SecurityUser;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 政府债券
 *
 * @author liuwenyong 
 * @since 3.0 2022-05-14
 */
@Service
public class FGovernmentBondServiceImpl extends CrudServiceImpl<FGovernmentBondDao, FGovernmentBondEntity,
        FGovernmentBondDTO> implements FGovernmentBondService {

    @Autowired
    FGovernmentBondDao fGovernmentBondDao;

    @Autowired
    private FFinanceDeptService fFinanceDeptService;

    @Autowired
    private FRepaymentActualService fRepaymentActualService;

    @Autowired
    private FRepaymentPlanService fRepaymentPlanService;

    @Override
    public QueryWrapper<FGovernmentBondEntity> getWrapper(Map<String, Object> params){
        QueryWrapper<FGovernmentBondEntity> wrapper = new QueryWrapper<>();
        wrapper.like("financing_amount",params.get("financingAmount"));
        wrapper.like("project",params.get("project"));
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
    public void saveInfo(FGovernmentBondDTO dto) {
        dto.setId(null);
        dto.setIsChecked(0);
        //保存实际还款
        saveRepaymentActual(dto);
        //保存还款计划
        saveRepaymentPlan(dto);
        //保存增信措施
        saveOrUpdateCreditMeasures(dto);
        //文件列表
        List<FileListVO> fileList = dto.getFileList();
        fileList.forEach(fileListVO -> {
            List<FileVO> files = fileListVO.getFiles();
            fileListVO.setFileStr(JSON.toJSONString(files));
            fileListVO.setFiles(null);
        });
        dto.setFiles(JSON.toJSONString(fileList));
        this.save(dto);
    }

    /**
     * 保存实际还款信息
     */
    private void saveRepaymentActual(FGovernmentBondDTO dto) {
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

    /**
     * 保存还款计划信息
     */
    private void saveRepaymentPlan(FGovernmentBondDTO dto) {
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
     * 保存或者更新增信措施
     * @param dto
     */
    private void saveOrUpdateCreditMeasures(FGovernmentBondDTO dto) {
        //对增信措施的处理
        List<String> measuresList = dto.getCreditMeasuresList();
        boolean flag = CollectionUtils.isNotEmpty(measuresList);
        if (flag){
            dto.setCreditMeasures(StringUtils.join(measuresList,","));
        }

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
    }


    @Override
    public void updateInfo(FGovernmentBondDTO dto) {
        //设置实际还款
        updateRepaymentActual(dto,dto.getPayActualData());
        //设置还款计划
        updateRepaymentPlan(dto,dto.getPayPlanData());
        //对增信措施的处理
        saveOrUpdateCreditMeasures(dto);
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
        System.out.println("即将存进数据库的对象"+dto);
        this.update(dto);
    }

    /**
     * 更新还款计划
     * @param dto
     * @param payPlanData
     */
    private void updateRepaymentPlan(FGovernmentBondDTO dto, PayPlanDataVO payPlanData) {
        //存在，更新。不存在，删除
        List<FRepayInterestPlanDTO> repayInterestPlanList = payPlanData.getRepayInterestPlanList();
        fRepaymentPlanService.deleteRepayPlan(dto.getId(),1);
        //判断付息计划列表是否为空
        if(repayInterestPlanList != null){
            //遍历付息计划列表
            //填充相关信息
            repayInterestPlanList.forEach(fRepayInterestPlanDTO -> {
                fRepayInterestPlanDTO.setFinancingId(dto.getId());
                fRepayInterestPlanDTO.setFinancingType(1);
                fRepayInterestPlanDTO.setPlanType(2);
                fRepaymentPlanService.saveRepayInterestPlan(fRepayInterestPlanDTO);
            });
        }
        //还本计划
        List<FRepayPrincipalPlanDTO> repayPrincipalPlanList = payPlanData.getRepayPrincipalPlanList();
        if(repayPrincipalPlanList != null){
            repayPrincipalPlanList.forEach(fRepayPrincipalPlanDTO -> {
                fRepayPrincipalPlanDTO.setFinancingId(dto.getId());
                fRepayPrincipalPlanDTO.setFinancingType(1);
                fRepayPrincipalPlanDTO.setPlanType(1);
                fRepaymentPlanService.saveRepayPrincipalPlan(fRepayPrincipalPlanDTO);
            });
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
    private void updateRepaymentActual(FGovernmentBondDTO dto, PayActualDataVO payActualData) {
        //存在，更新。不存在，删除
        List<FRepayInterestActualDTO> repayInterestActualList = payActualData.getRepayInterestActualList();
        fRepaymentActualService.deleteRepayActual(dto.getId(),1);
        //判断实际付息列表是否为空
        if(repayInterestActualList != null){
            //遍历付息计划列表
            //填充相关信息
            repayInterestActualList.forEach(fRepayInterestActualDTO -> {
                fRepayInterestActualDTO.setFinancingId(dto.getId());
                fRepayInterestActualDTO.setFinancingType(1);
                fRepaymentActualService.saveRepayInterestActual(fRepayInterestActualDTO);
            });
        }
        //实际还本
        List<FRepayPrincipalActualDTO> repayPrincipalActualList = payActualData.getRepayPrincipalActualList();
        if(repayPrincipalActualList != null){
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
    public FGovernmentBondDTO getDealData(Long id) {


        FGovernmentBondDTO dealData = fGovernmentBondDao.getDealData(id);
        //得到银行机构名称
        dealData.setInstitutionName(fFinanceDeptService.getInstitutionNameById(dealData.getInstitution()));
        //增信措施详情
        setCreditMeasures(dealData);
        //还款计划和实际还款
        setRepaymentPlanAndActual(id,dealData);
        //文件列表
        String files = dealData.getFiles();
        if (files != null){
            System.out.println(files);
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
     * 设置增信措施
     * @param dealData
     */
    private void setCreditMeasures(FGovernmentBondDTO dealData) {
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
    private void setRepaymentPlanAndActual(Long id, FGovernmentBondDTO dealData) {
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
                = fRepaymentPlanService.selectRepayPrincipalPlan(id, 1);
        List<FRepayInterestPlanDTO> fRepayInterestPlanDTOS
                = fRepaymentPlanService.selectRepayInterestPlan(id, 1);
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
                = fRepaymentActualService.selectRepayPrincipalActual(id, 1);
        List<FRepayInterestActualDTO> fRepayInterestActualDTOS
                = fRepaymentActualService.selectRepayInterestActual(id, 1);
        payActualDataVO.setRepayPrincipalActualList(fRepayPrincipalActualDTOS);
        payActualDataVO.setRepayInterestActualList(fRepayInterestActualDTOS);
        dealData.setPayActualData(payActualDataVO);
    }

    @Override
    public FGovernmentBondDTO directInfoCast(FGovernmentBondDTO dto, Integer unit) {

        if (unit == 2){
            dto.setFinancingAmount(UnitCastUtil.divider(dto.getFinancingAmount()));
            dto.setFinancingBalance(UnitCastUtil.divider(dto.getFinancingBalance()));
            queryCast(dto);
        }
        return dto;
    }

    /**
     * 读取数据时实际还款和还款计划的单位转化
     * @param dto
     */
    private void queryCast(FGovernmentBondDTO dto) {
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
    public FGovernmentBondDTO directSaveCast(FGovernmentBondDTO dto, Integer unit) {
        if (unit == 2){
            dto.setFinancingAmount(UnitCastUtil.multiplication(dto.getFinancingAmount()));
            dto.setFinancingBalance(UnitCastUtil.multiplication(dto.getFinancingBalance()));
            saveCast(dto);
        }
        return dto;
    }

    /**
     * 保存的时候的单位转换
     * @param dto
     */
    private void saveCast(FGovernmentBondDTO dto) {
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

    @Override
    public List<FRepaymentPlanDTO> interestCalculate(IndirectInterestVO vo) {
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
        //上浮利率
        double floatRate = 0.0000;
        //贷款金额
        BigDecimal amount = vo.getWithdrawalAmount();
        //测算周期
        Integer interestMeasurementCycle = vo.getInterestMeasurementCycle();
        //是否是还本付息（否0是1）
        Integer isPayPrincipalAndInterest = vo.getIsPayPrincipalAndInterest();
        //获得固定利率
        double rate = vo.getRate();
        List<FRepaymentPlanDTO> fRepaymentPlanDTOS =null;
        if (isPayPrincipalAndInterest == null){
            throw new RenException(ErrorCode.FINANCING_INFO_GAP);
        }
        if (isPayPrincipalAndInterest == 0){
            fRepaymentPlanDTOS = InterestCountUtil.repayFixedRateInterest(principalList, interestList, floatRate,
                    amount, interestMeasurementCycle,rate);
        }else{
            fRepaymentPlanDTOS = InterestCountUtil.repayFixedRate(principalList,interestList,floatRate,amount,
                    interestMeasurementCycle,rate);
        }
        for (FRepaymentPlanDTO fRepaymentPlanDTO : fRepaymentPlanDTOS) {
        }
        return fRepaymentPlanDTOS;
    }

    @Override
    public List<FGovernmentBondExcel> getExportData(List<FGovernmentBondDTO> list) {
        List<FGovernmentBondExcel> excels = new ArrayList<>();
        for (FGovernmentBondDTO dto : list) {
            FGovernmentBondExcel excel = new FGovernmentBondExcel();
            BeanUtils.copyProperties(dto,excel);
            excels.add(MyExcelUtil.governmentBondExport(dto,excel));
        }
        return excels;
    }
}