package io.renren.modules.financing.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.renren.common.exception.ErrorCode;
import io.renren.common.exception.RenException;
import io.renren.common.service.impl.CrudServiceImpl;
import io.renren.modules.financing.dao.FDirectFinancingApprovalDao;
import io.renren.modules.financing.dao.FDirectFinancingDao;
import io.renren.modules.financing.dao.FFinanceDeptDao;
import io.renren.modules.financing.dto.*;
import io.renren.modules.financing.entity.FDirectFinancingApproval;
import io.renren.modules.financing.entity.FDirectFinancingEntity;
import io.renren.modules.financing.excel.FDirectFinancingExcel;
import io.renren.modules.financing.service.FDirectFinancingService;
import io.renren.modules.financing.service.FRepaymentActualService;
import io.renren.modules.financing.service.FRepaymentPlanService;
import io.renren.modules.financing.util.InterestCountUtil;
import io.renren.modules.financing.util.MyExcelUtil;
import io.renren.modules.financing.util.QueryUtil;
import io.renren.modules.financing.util.UnitCastUtil;
import io.renren.modules.financing.vo.*;
import io.renren.modules.sys.service.SysSecComService;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;

/**
 * 直接融资信息
 *
 * @author liuwenyong 
 * @since 3.0 2022-05-14
 */
@Service
public class FDirectFinancingServiceImpl
        extends CrudServiceImpl<FDirectFinancingDao, FDirectFinancingEntity, FDirectFinancingDTO>
        implements FDirectFinancingService {
    @Autowired
    private FDirectFinancingDao fDirectFinancingDao;

    @Autowired
    private FFinanceDeptDao fFinanceDeptDao;

    @Autowired
    private FRepaymentPlanService fRepaymentPlanService;

    @Autowired
    private SysSecComService sysSecComService;

    @Autowired
    private FRepaymentActualService fRepaymentActualService;

    @Autowired
    private FDirectFinancingApprovalDao fDirectFinancingApprovalDao;

    @Override
    public QueryWrapper<FDirectFinancingEntity> getWrapper(Map<String, Object> params){
        QueryWrapper<FDirectFinancingEntity> wrapper = new QueryWrapper<>();
        wrapper.like(params.get("creditMeasures") != null && !params.get("creditMeasures").equals(""),"credit_measures",params.get("creditMeasures"));
        wrapper.like(!params.get("issueQuota").equals(""),"issue_quota",params.get("issueQuota"));
        wrapper.like(!params.get("varieties").equals(""),"varieties",params.get("varieties"));
        return wrapper;
    }





    @Override
    public void saveInfo(FDirectFinancingDTO dto) {
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
        //证券公司
        dto.setSecComIdStr(JSON.toJSONString(dto.getSecComIdList()));
        this.save(dto);
    }

    /**
     * 保存实际还款信息
     */
    private void saveRepaymentActual(FDirectFinancingDTO dto) {
        PayActualDataVO payActualData = dto.getPayActualData();
        //保存实际付息
        List<FRepayInterestActualDTO> repayInterestActualList = payActualData.getRepayInterestActualList();
        repayInterestActualList.forEach(dto1 -> {
            dto1.setFinancingId(dto.getId());
            dto1.setFinancingType(1);
            fRepaymentActualService.saveRepayInterestActual(dto1);
        });
        //保存实际还本
        List<FRepayPrincipalActualDTO> repayPrincipalActualList = payActualData.getRepayPrincipalActualList();
        repayPrincipalActualList.forEach(dto1 -> {
            dto1.setFinancingId(dto.getId());
            dto1.setFinancingType(1);
            fRepaymentActualService.saveRepayPrincipalActual(dto1);
        });
        //保存实际还款详情
        PayActualInfoVo payActualInfoVo = new PayActualInfoVo();
        BeanUtils.copyProperties(payActualData,payActualInfoVo);
        dto.setRepaymentActual(JSONObject.toJSONString(payActualInfoVo));
    }

    /**
     * 保存还款计划信息
     */
    private void saveRepaymentPlan(FDirectFinancingDTO dto) {
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

    @Override
    public void updateInfo(FDirectFinancingDTO dto) {
        if (dto.getIssueQuota().subtract(dto.getPayActualData().getTotalRepayPrincipalActual()).doubleValue()<0.00){
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
        //证券公司
        dto.setSecComIdStr(JSON.toJSONString(dto.getSecComIdList()));
        this.update(dto);
    }

    /**
     * 更新还款计划
     * @param dto
     * @param payPlanData
     */
    private void updateRepaymentPlan(FDirectFinancingDTO dto, PayPlanDataVO payPlanData) {
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
    private void updateRepaymentActual(FDirectFinancingDTO dto, PayActualDataVO payActualData) {
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
    public HashMap<String,Object> getData(String approveNo) {
        return baseDao.getData(approveNo);
    }

    @Override
    public void check(FDirectFinancingApprovalCheckDTO dto) {
//        FDirectFinancingApproval fDirectFinancingApproval= baseDao.selectById(dto.getApprovalId());
//        FDirectFinancingApprovalDTO fDirectFinancingApprovalDTO= ConvertUtils.sourceToTarget(fDirectFinancingApproval, FDirectFinancingApprovalDTO.class);
//        fDirectFinancingApprovalDTO.setIsChecked(dto.getChecked());
//        fDirectFinancingApprovalDTO.setCheckedDes(dto.getCheckedDes());
//        this.update(fDirectFinancingApprovalDTO);
//
//        fDirectFinancingApprovalCheckService.save(dto);
    }

    /**
     * 对查出来的数据做还原处理
     * @param id
     * @return FDirectFinancingDTO
     */
    @Override
    public FDirectFinancingDTO getDealData(Long id) {
        //增信措施详情
        FDirectFinancingDTO dealData = fDirectFinancingDao.getDealData(id);
        creditMeasuresJsonStrToObject(dealData);

        //设置实际还款和还款计划
        setRepaymentPlanAndActual(id, dealData);

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
        //审批金额
        BigDecimal approvalAmount = fFinanceDeptDao.getApprovalAmount(dealData.getApproveNo());
        dealData.setApprovalAmount(approvalAmount);
        //证券公司的处理
        dealData.setSecComIdList(JSON.parseObject(dealData.getSecComIdStr(),
                new TypeReference<List<Long>>(){}));
        return dealData;
    }

    /**
     * 设置实际还款和还款计划
     * @param id
     * @param dealData
     */
    private void setRepaymentPlanAndActual(Long id, FDirectFinancingDTO dealData) {
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

    /**
     * 增信措施json字符串转换为对象
     * @param dealData
     */
    private void creditMeasuresJsonStrToObject(FDirectFinancingDTO dealData) {
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

    @Override
    public List<FDirectFinancingDTO> screenList(List<FDirectFinancingDTO> list, String approvalAmount) {
        list.forEach(dto -> {
            dto.setDeptName(fFinanceDeptDao.getDeptName(dto.getDeptId()));
            dto.setApprovalAmount(fFinanceDeptDao.getApprovalAmount(dto.getApproveNo()));
            FDirectFinancingApprovalDTO infoByApprovalNo = fDirectFinancingApprovalDao.getInfoByApprovalNo(dto.getApproveNo());
            dto.setVarieties(fDirectFinancingApprovalDao.getInfoByApprovalNo(dto.getApproveNo()).getVarieties());
        });
        list.removeIf(next -> !QueryUtil.judgeConditionDirectFinancing(next, approvalAmount));
        return list;
    }

    @Override
    public String screen(String deptName) {
   //     String deptId = fFinanceDeptDao.getDeptId(deptName);
        return null;
    }

    @Override
    public List<FDirectFinancingDTO> directCast(List<FDirectFinancingDTO> list, Integer unit) {
        if (unit == 2) {
            for (FDirectFinancingDTO fDirectFinancingDTO : list) {
                fDirectFinancingDTO = this.directInfoCast(fDirectFinancingDTO,unit);
            }
        }
        return list;
    }
    //

    @Override
    public FDirectFinancingDTO directInfoCast(FDirectFinancingDTO dto, Integer unit) {
        if(unit == 2){
            //合同金额
            dto.setForeignCurrencyAmount(UnitCastUtil.divider(dto.getForeignCurrencyAmount()));
            //开票金额
            dto.setIssueQuota(UnitCastUtil.divider(dto.getIssueQuota()));
            //提款金额
            dto.setFinancingBalance(UnitCastUtil.divider(dto.getFinancingBalance()));
            //可用余额
            dto.setOtherExpenses(UnitCastUtil.divider(dto.getOtherExpenses()));
            //审批金额
            dto.setApprovalAmount(UnitCastUtil.divider(dto.getApprovalAmount()));
            queryCast(dto);
        }
        return dto;
    }

    /**
     * 读取数据时实际还款和还款计划的单位转化
     * @param dto
     */
    private void queryCast(FDirectFinancingDTO dto) {
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
    public FDirectFinancingDTO directSaveCast(FDirectFinancingDTO dto, Integer unit) {
        if(unit == 2){
            //合同金额
            dto.setForeignCurrencyAmount(UnitCastUtil.multiplication(dto.getForeignCurrencyAmount()));
            //开票金额
            dto.setIssueQuota(UnitCastUtil.multiplication(dto.getIssueQuota()));
            //提款金额
            dto.setFinancingBalance(UnitCastUtil.multiplication(dto.getFinancingBalance()));
            //可用余额
            dto.setOtherExpenses(UnitCastUtil.multiplication(dto.getOtherExpenses()));
            //审批金额
            dto.setApprovalAmount(UnitCastUtil.multiplication(dto.getApprovalAmount()));
            //还本计划中的金额
            saveCast(dto);
        }
        return dto;
    }

    /**
     * 保存的时候的单位转换
     * @param dto
     */
    private void saveCast(FDirectFinancingDTO dto) {
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
            if (fRepayPrincipalPlanDTO.getEndDate()==null){
                throw new RenException(ErrorCode.REPAYMENT_INFO_MISSING_REPAY_DATE);
            }
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
            if (fRepayInterestPlanDTO.getEndDate()==null){
                throw new RenException(ErrorCode.REPAYMENT_INFO_MISSING_REPAY_DATE);
            }
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
        return fRepaymentPlanDTOS;
    }

    @Override
    public List<FDirectFinancingExcel> getExportData(List<FDirectFinancingDTO> list,String approvalAmount) {
        //新建一个excel列表对象
        List<FDirectFinancingExcel> excels = new ArrayList<>();
        //给列表里的每一个对象的单位名称以及审批金额赋值
        for (FDirectFinancingDTO dto : list) {
            dto.setDeptName(fFinanceDeptDao.getDeptName(dto.getDeptId()));
            dto.setApprovalAmount(fFinanceDeptDao.getApprovalAmount(dto.getApproveNo()));
        }
        //过滤掉不符合查询条件的对象
        list.removeIf(next -> !QueryUtil.judgeConditionDirectFinancing(next, approvalAmount));
        //excels添加对象
        for (FDirectFinancingDTO dto : list) {
            FDirectFinancingExcel excel = new FDirectFinancingExcel();
            BeanUtils.copyProperties(dto,excel);
            MyExcelUtil.directExcelExport(dto,excel);
            excels.add(excel);
        }
        return excels;
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