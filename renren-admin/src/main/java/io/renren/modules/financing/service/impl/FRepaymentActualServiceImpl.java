package io.renren.modules.financing.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.renren.common.service.impl.CrudServiceImpl;
import io.renren.modules.financing.dao.FRepaymentActualDao;
import io.renren.modules.financing.dto.FRepayInterestActualDTO;
import io.renren.modules.financing.dto.FRepayPrincipalActualDTO;
import io.renren.modules.financing.dto.FRepaymentActualDTO;
import io.renren.modules.financing.entity.FRepaymentActual;
import io.renren.modules.financing.service.FRepaymentActualService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class FRepaymentActualServiceImpl
        extends CrudServiceImpl<FRepaymentActualDao, FRepaymentActual, FRepaymentActualDTO>
        implements FRepaymentActualService {

    @Autowired
    private FRepaymentActualDao fRepaymentActualDao;

    @Override
    public QueryWrapper<FRepaymentActual> getWrapper(Map<String, Object> params) {
        return null;
    }

    @Override
    public void saveRepayPrincipalActual(FRepayPrincipalActualDTO dto) {
        FRepaymentActualDTO fRepaymentActualDTO = new FRepaymentActualDTO();
        //克隆到dto
        BeanUtils.copyProperties(dto,fRepaymentActualDTO);
        //设置为还本
        fRepaymentActualDTO.setActualType(1);
        //保存
        save(fRepaymentActualDTO);
    }

    @Override
    public void saveRepayInterestActual(FRepayInterestActualDTO dto) {
        FRepaymentActualDTO fRepaymentActualDTO = new FRepaymentActualDTO();
        //克隆到dto
        BeanUtils.copyProperties(dto,fRepaymentActualDTO);
        //设置为付息
        fRepaymentActualDTO.setActualType(2);
        //保存
        save(fRepaymentActualDTO);
    }

    @Override
    public void updateRepayPrincipalActual(FRepayPrincipalActualDTO dto) {
        FRepaymentActualDTO fRepaymentActualDTO = new FRepaymentActualDTO();
        //克隆到dto
        BeanUtils.copyProperties(dto,fRepaymentActualDTO);
        //设置为还本
        fRepaymentActualDTO.setActualType(1);
        //更新
        update(fRepaymentActualDTO);
    }

    @Override
    public void updateRepayInterestActual(FRepayInterestActualDTO dto) {
        FRepaymentActualDTO fRepaymentActualDTO = new FRepaymentActualDTO();
        //克隆到dto
        BeanUtils.copyProperties(dto,fRepaymentActualDTO);
        //设置为付息
        fRepaymentActualDTO.setActualType(2);
        //更新
        update(fRepaymentActualDTO);
    }

    @Override
    public List<FRepayPrincipalActualDTO> selectRepayPrincipalActual(Long financingId, Integer financingType) {
        return fRepaymentActualDao.selectPrincipalActual(financingId,financingType);
    }

    @Override
    public List<FRepayInterestActualDTO> selectRepayInterestActual(Long financingId, Integer financingType) {
        return fRepaymentActualDao.selectInterestActual(financingId,financingType);
    }

    @Override
    public Boolean existData(Long id) {
        return fRepaymentActualDao.selectById(id) != null;
    }

    @Override
    public List<Long> selectRepayActualId(Long financingId, Integer financingType) {
        return fRepaymentActualDao.selectAllActualId(financingId,financingType);
    }

    @Override
    public void deleteRepayActual(Long financingId, Integer financingType) {
        fRepaymentActualDao.deletePayActual(financingId,financingType);
    }


}
