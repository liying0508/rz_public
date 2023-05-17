package io.renren.modules.financing.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.renren.common.exception.ErrorCode;
import io.renren.common.exception.RenException;
import io.renren.common.service.impl.CrudServiceImpl;
import io.renren.modules.financing.dao.FRepaymentPlanDao;
import io.renren.modules.financing.dto.FRepayInterestPlanDTO;
import io.renren.modules.financing.dto.FRepayPrincipalPlanDTO;
import io.renren.modules.financing.dto.FRepaymentPlanDTO;
import io.renren.modules.financing.entity.FRepaymentPlan;
import io.renren.modules.financing.service.FRepaymentPlanService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class FRepaymentPlanServiceImpl
        extends CrudServiceImpl<FRepaymentPlanDao, FRepaymentPlan, FRepaymentPlanDTO>
        implements FRepaymentPlanService {

    @Autowired
    private FRepaymentPlanDao fRepaymentPlanDao;

    @Override
    public QueryWrapper<FRepaymentPlan> getWrapper(Map<String, Object> params) {
        return null;
    }

    @Override
    public void saveRepayPrincipalPlan(FRepayPrincipalPlanDTO dto) {
        FRepaymentPlanDTO fRepaymentPlanDTO = new FRepaymentPlanDTO();
        BeanUtils.copyProperties(dto,fRepaymentPlanDTO);
        if (fRepaymentPlanDTO.getFinancingId() == null){
            throw new RenException(ErrorCode.FINANCE_INFO_NOT_SAVED_YET);
        }
        this.save(fRepaymentPlanDTO);
    }

    @Override
    public void saveRepayInterestPlan(FRepayInterestPlanDTO dto) {
        FRepaymentPlanDTO fRepaymentPlanDTO = new FRepaymentPlanDTO();
        BeanUtils.copyProperties(dto,fRepaymentPlanDTO);
        if (fRepaymentPlanDTO.getFinancingId() == null){
            throw new RenException(ErrorCode.FINANCE_INFO_NOT_SAVED_YET);
        }
        this.save(fRepaymentPlanDTO);
    }

    @Override
    public void updateRepayPrincipalPlan(FRepayPrincipalPlanDTO dto) {
        FRepaymentPlanDTO fRepaymentPlanDTO = new FRepaymentPlanDTO();
        BeanUtils.copyProperties(dto,fRepaymentPlanDTO);
        this.update(fRepaymentPlanDTO);
    }

    @Override
    public void updateRepayInterestPlan(FRepayInterestPlanDTO dto) {
        FRepaymentPlanDTO fRepaymentPlanDTO = new FRepaymentPlanDTO();
        BeanUtils.copyProperties(dto,fRepaymentPlanDTO);
        this.update(fRepaymentPlanDTO);
    }

    @Override
    public List<FRepayPrincipalPlanDTO> selectRepayPrincipalPlan(Long financingId, Integer financingType) {
        return fRepaymentPlanDao.selectPrincipalPlan(financingId, financingType);
    }

    @Override
    public List<FRepayInterestPlanDTO> selectRepayInterestPlan(Long financingId, Integer financingType) {
        return fRepaymentPlanDao.selectInterestPlan(financingId, financingType);
    }

    @Override
    public Boolean existData(Long id) {
        FRepaymentPlan fRepaymentPlan = fRepaymentPlanDao.selectById(id);
        return fRepaymentPlan != null;
    }

    @Override
    public List<Long> selectRepayPlanId(Long financingId, Integer financingType) {
        return fRepaymentPlanDao.selectAllPlanId(financingId, financingType);
    }

    @Override
    public void deleteRepayPlan(Long financingId, Integer financingType) {
        fRepaymentPlanDao.deletePayPlan(financingId,financingType);
    }




}
