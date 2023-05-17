package io.renren.modules.financing.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.renren.common.exception.ErrorCode;
import io.renren.common.exception.RenException;
import io.renren.common.page.PageData;
import io.renren.common.service.impl.CrudServiceImpl;
import io.renren.common.utils.ConvertUtils;
import io.renren.modules.financing.dao.FDirectFinancingApprovalDao;
import io.renren.modules.financing.dao.FDirectFinancingAuditDao;
import io.renren.modules.financing.dao.FDirectFinancingDao;
import io.renren.modules.financing.dao.FFinanceDeptDao;
import io.renren.modules.financing.dto.*;
import io.renren.modules.financing.entity.FDirectFinancingApproval;
import io.renren.modules.financing.entity.FDirectFinancingEntity;
import io.renren.modules.financing.service.*;
import io.renren.modules.financing.util.QueryUtil;
import io.renren.modules.financing.util.UnitCastUtil;
import io.renren.modules.financing.vo.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;

@Service
public class FDirectFinancingAuditServiceImpl extends
        CrudServiceImpl<FDirectFinancingAuditDao, FDirectFinancingEntity, FDirectFinancingAuditDTO> implements
        FDirectFinancingAuditService {

    @Autowired
    private FDirectFinancingService fDirectFinancingService;

    @Autowired
    private FDirectFinancingCheckService fDirectFinancingCheckService;

    @Autowired
    private FDirectFinancingAuditDao fDirectFinancingAuditDao;

    @Autowired
    private FFinanceDeptDao fFinanceDeptDao;

    @Autowired
    private FRepaymentPlanService fRepaymentPlanService;

    @Autowired
    private FRepaymentActualService fRepaymentActualService;

    @Autowired
    private FDirectFinancingDao fDirectFinancingDao;


    @Override
    public QueryWrapper<FDirectFinancingEntity> getWrapper(Map<String, Object> params) {
        QueryWrapper<FDirectFinancingEntity> wrapper = new QueryWrapper<>();
        wrapper.like(!params.get("creditMeasures").equals(""),"credit_measures",params.get("creditMeasures"));
        wrapper.like(!params.get("issueQuota").equals(""),"issue_quota",params.get("issueQuota"));
        wrapper.like(!params.get("varieties").equals(""),"varieties",params.get("varieties"));
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
    public List<FDirectFinancingAuditDTO> listInfo() {
        //查询部门列表
        List<FDirectFinancingAuditDTO> list = baseDao.getlist();
        list.stream().forEach(f->{
            HashMap map= fDirectFinancingService.getData(f.getApproveNo());
            if(Objects.nonNull(map)){
                if(map.containsKey("issue")) {
                    f.setIssue(((Long) map.get("issue")).intValue());
                }
            }
        });

        return list;
    }

    @Override
    @Transactional
    public void saveInfo(FDirectFinancingAuditDTO dto) {
        dto.setId(null);
        //判断是否有批文号重复
        FDirectFinancingAuditDTO fDirectFinancingAuditDTO= baseDao.getInfoByApprovalNo(dto.getApproveNo());
        if(Objects.nonNull(fDirectFinancingAuditDTO)){
            throw new RenException(ErrorCode.APPROVAL_NO_EXI_INFO);
        }
        this.save(dto);
    }

    @Override
    @Transactional
    public void updateInfo(FDirectFinancingAuditDTO dto) {
        this.update(dto);
    }

    @Override
    public void check(FDirectFinancingCheckDTO dto) {
        FDirectFinancingEntity fDirectFinancingEntity= baseDao.selectById(dto.getFinancingId());
        FDirectFinancingAuditDTO fDirectFinancingAuditDTO= ConvertUtils.sourceToTarget(fDirectFinancingEntity, FDirectFinancingAuditDTO.class);
        fDirectFinancingAuditDTO.setIsChecked(dto.getChecked());
        fDirectFinancingAuditDTO.setCheckedDes(dto.getCheckedDes());
        this.update(fDirectFinancingAuditDTO);
        fDirectFinancingCheckService.save(dto);
    }

    @Override
    @Transactional
    public void groupCheck(FDirectFinancingCheckDTO dto) {
        //修改集团审核结果
        FDirectFinancingEntity fDirectFinancingEntity= baseDao.selectById(dto.getFinancingId());
        FDirectFinancingAuditDTO fDirectFinancingAuditDTO
                = ConvertUtils.sourceToTarget(fDirectFinancingEntity, FDirectFinancingAuditDTO.class);
        fDirectFinancingAuditDTO.setGroupChecked(dto.getGroupChecked());
        fDirectFinancingAuditDTO.setGroupCheckedDes(dto.getGroupCheckedDes());
        this.update(fDirectFinancingAuditDTO);
        fDirectFinancingCheckService.save(dto);
        //如果是审核通过的话，修改直融的融资余额与已还金额
        updateDirectBalanceAndActualAmount(dto);
    }

    /**
     * 更新直融融资余额以及已还金额
     * @param dto FDirectFinancingCheckDTO
     */
    private void updateDirectBalanceAndActualAmount(FDirectFinancingCheckDTO dto) {
        if (dto.getGroupChecked().equals(1)){
            //先获得所有还本金额之和
            BigDecimal totalAmount = new BigDecimal(0);
            //直接去查这个直融实际还款详情，把里面的已还本金加进去
            BigDecimal totalRepaymentActual = fDirectFinancingService.getDealData(dto.getFinancingId())
                    .getPayActualData().getTotalRepayPrincipalActual();
            totalAmount = totalAmount.add(totalRepaymentActual);
            //融资余额
            BigDecimal financingBalance = fDirectFinancingService.getDealData(dto.getFinancingId())
                    .getIssueQuota().subtract(totalAmount);
            //然后修改本条融资单
            //更新直融表的实际还款金额
            fDirectFinancingDao.updateActualRepaymentById(dto.getFinancingId(),totalAmount);
            //更新融资余额
            fDirectFinancingDao.updateFinancingBalanceById(dto.getFinancingId(),financingBalance);
        }else {
            //融资余额设置为折合人民币，实际还款金额设置为0
            //更新融资余额
            fDirectFinancingDao.updateFinancingBalanceById(dto.getFinancingId(),
                    fDirectFinancingService.getDealData(dto.getFinancingId()).getIssueQuota());
            //更新实际还款金额
            //更新直融表的实际还款金额
            fDirectFinancingDao.updateActualRepaymentById(dto.getFinancingId(),new BigDecimal(0.00));
        }
    }

    @Override
    public FDirectFinancingAuditDTO getDealData(Long id) {

        //增信措施详情
        FDirectFinancingAuditDTO dealData = fDirectFinancingAuditDao.getDealData(id);

        setRepaymentPlanAndActual(id, dealData);


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
        //还款计划
        dealData.setPayPlanData(new PayPlanDataVO());
        String repaymentPlan = dealData.getRepaymentPlan();
        if(!repaymentPlan.equals("")){
            PayPlanInfoVo payPlanInfoVo = JSON.parseObject(repaymentPlan, PayPlanInfoVo.class);
            dealData.getPayPlanData().setEndNeedCount(payPlanInfoVo.getEndNeedCount());
            dealData.getPayPlanData().setInterestMeasurementCycle(payPlanInfoVo.getInterestMeasurementCycle());
        }
        //还本计划和还息计划
        List<FRepayPrincipalPlanDTO> fRepayPrincipalPlanDTOS = fRepaymentPlanService.selectRepayPrincipalPlan(id, 1);
        List<FRepayInterestPlanDTO> fRepayInterestPlanDTOS = fRepaymentPlanService.selectRepayInterestPlan(id, 1);
        PayPlanDataVO payPlanData = dealData.getPayPlanData();
        payPlanData.setRepayInterestPlanList(fRepayInterestPlanDTOS);
        payPlanData.setRepayPrincipalPlanList(fRepayPrincipalPlanDTOS);
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
    private void setRepaymentPlanAndActual(Long id, FDirectFinancingAuditDTO dealData) {
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
    @Transactional
    public void delete(Long[] ids) {
        super.delete(ids);
    }

    @Override
    public List<FDirectFinancingAuditDTO> screenList(List<FDirectFinancingAuditDTO> list, String approvalAmount) {
        for (FDirectFinancingAuditDTO dto : list) {
            dto.setDeptName(fFinanceDeptDao.getDeptName(dto.getDeptId()));
            dto.setApprovalAmount(fFinanceDeptDao.getApprovalAmount(dto.getApproveNo()));
        }
        list.removeIf(next -> !QueryUtil.judgeConditionAuditDirectFinancing(next, approvalAmount));
        return list;
    }

    @Override
    public List<FDirectFinancingAuditDTO> directCast(List<FDirectFinancingAuditDTO> list, Integer unit) {
        if (unit == 2) {
            for (FDirectFinancingAuditDTO fDirectFinancingAuditDTO : list) {
                fDirectFinancingAuditDTO = this.directInfoCast(fDirectFinancingAuditDTO,unit);
            }
        }
        return list;
    }

    @Override
    public FDirectFinancingAuditDTO directInfoCast(FDirectFinancingAuditDTO dto, Integer unit) {
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
        }
        return dto;
    }
}
