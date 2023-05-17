package io.renren.modules.financing.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.renren.common.service.impl.CrudServiceImpl;
import io.renren.common.utils.ConvertUtils;
import io.renren.modules.financing.dao.FGovernmentBondAuditDao;
import io.renren.modules.financing.dto.FGovernmentBondAuditDTO;
import io.renren.modules.financing.dto.FGovernmentBondCheckDTO;
import io.renren.modules.financing.entity.FGovernmentBondEntity;
import io.renren.modules.financing.service.FFinanceDeptService;
import io.renren.modules.financing.service.FGovernmentBondAuditService;
import io.renren.modules.financing.service.FGovernmentBondCheckService;
import io.renren.modules.financing.vo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class FGovernmentBondAuditServiceImpl extends
        CrudServiceImpl<FGovernmentBondAuditDao, FGovernmentBondEntity, FGovernmentBondAuditDTO>
        implements FGovernmentBondAuditService {

    @Autowired
    private FGovernmentBondCheckService fGovernmentBondCheckService;

    @Autowired
    private FGovernmentBondAuditDao fGovernmentBondAuditDao;

    @Autowired
    private FFinanceDeptService fFinanceDeptService;

    @Override
    public List<FGovernmentBondAuditDTO> listInfo() {
        return null;
    }

    @Override
    public void check(FGovernmentBondCheckDTO dto) {
        FGovernmentBondEntity fGovernmentBondEntity= baseDao.selectById(dto.getBondId());
        FGovernmentBondAuditDTO fGovernmentBondAuditDTO=
                ConvertUtils.sourceToTarget(fGovernmentBondEntity, FGovernmentBondAuditDTO.class);
        fGovernmentBondAuditDTO.setIsChecked(dto.getChecked());
        fGovernmentBondAuditDTO.setCheckedDes(dto.getCheckedDes());
        this.update(fGovernmentBondAuditDTO);
        fGovernmentBondCheckService.save(dto);
    }

    @Override
    public void groupCheck(FGovernmentBondCheckDTO dto) {
        FGovernmentBondEntity fGovernmentBondEntity= baseDao.selectById(dto.getBondId());
        FGovernmentBondAuditDTO fGovernmentBondAuditDTO=
                ConvertUtils.sourceToTarget(fGovernmentBondEntity, FGovernmentBondAuditDTO.class);
        fGovernmentBondAuditDTO.setGroupChecked(dto.getGroupChecked());
        fGovernmentBondAuditDTO.setGroupCheckedDes(dto.getGroupCheckedDes());
        this.update(fGovernmentBondAuditDTO);
        fGovernmentBondCheckService.save(dto);
    }

    @Override
    public FGovernmentBondAuditDTO getDealData(Long id) {

        FGovernmentBondAuditDTO dealData = fGovernmentBondAuditDao.getDealData(id);
        //得到银行机构名称
        dealData.setInstitutionName(fFinanceDeptService.getInstitutionNameById(dealData.getInstitution()));
        //增信措施详情
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
        //TODO
        return dealData;
    }

    @Override
    public QueryWrapper<FGovernmentBondEntity> getWrapper(Map<String, Object> params) {
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
}
