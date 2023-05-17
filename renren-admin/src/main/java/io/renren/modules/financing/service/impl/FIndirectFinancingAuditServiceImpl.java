package io.renren.modules.financing.service.impl;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.renren.common.exception.ErrorCode;
import io.renren.common.exception.RenException;
import io.renren.common.service.impl.CrudServiceImpl;
import io.renren.common.utils.ConvertUtils;
import io.renren.modules.financing.dao.FCreditRecordDao;
import io.renren.modules.financing.dao.FIndirectFinancingAuditDao;
import io.renren.modules.financing.dao.FIndirectFinancingDao;
import io.renren.modules.financing.dto.FCreditRecordDTO;
import io.renren.modules.financing.dto.FIndirectFinancingAuditDTO;
import io.renren.modules.financing.dto.FIndirectFinancingCheckDTO;
import io.renren.modules.financing.dto.FIndirectFinancingDTO;
import io.renren.modules.financing.entity.FIndirectFinancingEntity;
import io.renren.modules.financing.service.FIndirectFinancingAuditService;
import io.renren.modules.financing.service.FIndirectFinancingCheckService;
import io.renren.modules.financing.service.FIndirectFinancingService;
import io.renren.modules.financing.vo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Service
public class FIndirectFinancingAuditServiceImpl  extends
        CrudServiceImpl<FIndirectFinancingAuditDao, FIndirectFinancingEntity, FIndirectFinancingAuditDTO> implements
        FIndirectFinancingAuditService {
    @Autowired
    FIndirectFinancingService fIndirectFinancingService;

    @Autowired
    FIndirectFinancingCheckService fIndirectFinancingCheckService;

    @Autowired
    FIndirectFinancingAuditDao fIndirectFinancingAuditDao;

    @Autowired
    FCreditRecordDao fCreditRecordDao;

    @Autowired
    FIndirectFinancingDao fIndirectFinancingDao;


    @Override
    public QueryWrapper<FIndirectFinancingEntity> getWrapper(Map<String, Object> params) {
        QueryWrapper<FIndirectFinancingEntity> wrapper = new QueryWrapper<>();
        return wrapper;
    }

    @Override
    public List<FIndirectFinancingAuditDTO> listInfo() {
//        //查询部门列表
//        List<FIndirectFinancingAuditDTO> list = baseDao.getlist();
//        list.stream().forEach(f->{
//            HashMap map= fIndirectFinancingService.getData(f.getApproveNo());
//            if(Objects.nonNull(map)){
//                if(map.containsKey("issue")) {
//                    f.setIssue(((Long) map.get("issue")).intValue());
//                }
////                if(map.containsKey("allUseQuota")) {
////                    f.setAllUseQuota(((BigDecimal) map.get("allUseQuota")).doubleValue());
////                }
//            }
//        });
        //return list;
        return null;
    }

    @Override
    @Transactional
    public void saveInfo(FIndirectFinancingAuditDTO dto) {
        dto.setId(null);
        //判断是否有批文号重复
//        FDirectFinancingAuditDTO fDirectFinancingAuditDTO= baseDao.getInfoByApprovalNo(dto.getApproveNo());
//        if(Objects.nonNull(fDirectFinancingAuditDTO)){
//            throw new RenException(ErrorCode.APPROVAL_NO_EXI_INFO);
//        }
        this.save(dto);
    }

    @Override
    @Transactional
    public void updateInfo(FIndirectFinancingAuditDTO dto) {
        this.update(dto);
    }

    @Override
    public void check(FIndirectFinancingCheckDTO dto) {
        //得到数据库中的数据
        FIndirectFinancingEntity fIndirectFinancingEntity= baseDao.selectById(dto.getIndirectId());
        //进行审核前需要判断是否存在对应的授信，该授信是否已经通过审核了，如果还没有的话需要抛出异常
        //首先根据entity里的deptId和institutionId查询授信数据库中的数据
        List<FCreditRecordDTO> creditRecordDTOS = fCreditRecordDao.
                getDataByDeptIdAndInstitutionId(fIndirectFinancingEntity.getDeptId(), fIndirectFinancingEntity.getInstitution());
        //如果银行授信表里不存在记录，抛出异常
        if (creditRecordDTOS == null) {
            throw new RenException(ErrorCode.CREDIT_RECORD_IS_NOT_EXIST);
        }
        //首先判断授信额度是否足够
        BigDecimal creditQuota = new BigDecimal(0);
        //遍历，累加03

        for (FCreditRecordDTO creditRecordDTO : creditRecordDTOS) {
            creditQuota = creditQuota.add(creditRecordDTO.getQuota());
        }
        //判断总的授信额度是否大于该笔间融金额
        //TODO
        //克隆到dto
        FIndirectFinancingAuditDTO fIndirectFinancingAuditDTO= ConvertUtils.
                sourceToTarget(fIndirectFinancingEntity, FIndirectFinancingAuditDTO.class);
        //修改审核状态和审核意见
        fIndirectFinancingAuditDTO.setIsChecked(dto.getChecked());
        fIndirectFinancingAuditDTO.setCheckedDes(dto.getCheckedDes());
        //更新数据库
        this.update(fIndirectFinancingAuditDTO);
        //保存修改信息
        fIndirectFinancingCheckService.save(dto);
    }

    @Override
    public void groupCheck(FIndirectFinancingCheckDTO dto) {
        FIndirectFinancingEntity fIndirectFinancingEntity= baseDao.selectById(dto.getIndirectId());
        FIndirectFinancingAuditDTO fIndirectFinancingAuditDTO= ConvertUtils.
                sourceToTarget(fIndirectFinancingEntity, FIndirectFinancingAuditDTO.class);
        fIndirectFinancingAuditDTO.setGroupChecked(dto.getGroupChecked());
        fIndirectFinancingAuditDTO.setGroupCheckedDes(dto.getGroupCheckedDes());
        //当审核通过时
        update(fIndirectFinancingAuditDTO);
        fIndirectFinancingCheckService.save(dto);
        updateIndirectBalanceAndActualAmount(dto);
    }

    /**
     * 更新直融融资余额以及已还金额
     * @param dto FDirectFinancingCheckDTO
     */
    private void updateIndirectBalanceAndActualAmount(FIndirectFinancingCheckDTO dto) {
        if (dto.getGroupChecked().equals(1)){
            //先获得所有还本金额之和
            BigDecimal totalAmount = new BigDecimal(0);
            //直接去查这个直融实际还款详情，把里面的已还本金加进去
            BigDecimal totalRepaymentActual = fIndirectFinancingService.getDealData(dto.getIndirectId())
                    .getPayActualData().getTotalRepayPrincipalActual();
            totalAmount = totalAmount.add(totalRepaymentActual);
            //融资余额
            FIndirectFinancingDTO dealData = fIndirectFinancingService.getDealData(dto.getIndirectId());
            BigDecimal financingBalance = fIndirectFinancingService.getDealData(dto.getIndirectId())
                    .getWithdrawalAmount().subtract(totalAmount);
            //然后修改本条融资单
            //更新直融表的实际还款金额
            fIndirectFinancingDao.updateActualRepaymentById(dto.getIndirectId(),totalAmount);
            //更新融资余额
            fIndirectFinancingDao.updateFinancingBalanceById(dto.getIndirectId(),financingBalance);
        }else {
            //融资余额设置为折合人民币，实际还款金额设置为0
            //更新融资余额
            fIndirectFinancingDao.updateFinancingBalanceById(dto.getIndirectId(),
                    fIndirectFinancingService.getDealData(dto.getIndirectId()).getWithdrawalAmount());
            //更新实际还款金额
            //更新直融表的实际还款金额
            fIndirectFinancingDao.updateActualRepaymentById(dto.getIndirectId(),new BigDecimal(0.00));
        }
    }

    @Override
    public FIndirectFinancingAuditDTO getDealData(Long id) {
        //增信措施详情
        FIndirectFinancingAuditDTO dealData = fIndirectFinancingAuditDao.getDealData(id);
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
        //还款计划和实际还款
        //TODO
        //中介机构
        String intermediaryStructure = dealData.getIntermediaryStructure();
        List<IntermediaryStructureVo> intermediaryStructureVos =
                JSON.parseObject(intermediaryStructure, new TypeReference<List<IntermediaryStructureVo>>() {});
        dealData.setIntermediaryStructureList(intermediaryStructureVos);
        //投资人
        String investor = dealData.getInvestor();
        List<InvestorVO> investorVOS = JSON.parseObject(investor, new TypeReference<List<InvestorVO>>() {});
        dealData.setInvestorList(investorVOS);
        return dealData;
    }

    @Override
    @Transactional
    public void delete(Long[] ids) {
        super.delete(ids);
    }
}
