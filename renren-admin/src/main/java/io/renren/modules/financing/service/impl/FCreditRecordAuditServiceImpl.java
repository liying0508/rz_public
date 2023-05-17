package io.renren.modules.financing.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.renren.common.service.impl.CrudServiceImpl;
import io.renren.common.utils.ConvertUtils;
import io.renren.modules.financing.dao.FCreditRecordAuditDao;
import io.renren.modules.financing.dto.*;
import io.renren.modules.financing.entity.FCreditRecordCheck;
import io.renren.modules.financing.entity.FCreditRecordEntity;
import io.renren.modules.financing.entity.FDirectFinancingEntity;
import io.renren.modules.financing.entity.FIndirectFinancingEntity;
import io.renren.modules.financing.service.FCreditRecordAuditService;
import io.renren.modules.financing.service.FCreditRecordCheckService;
import io.renren.modules.financing.util.UnitCastUtil;
import io.renren.modules.financing.vo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class FCreditRecordAuditServiceImpl extends
        CrudServiceImpl<FCreditRecordAuditDao, FCreditRecordEntity, FCreditRecordAuditDTO> implements
        FCreditRecordAuditService {

    @Autowired
    private FCreditRecordAuditDao fCreditRecordAuditDao;

    @Autowired
    private FCreditRecordCheckService fCreditRecordCheckService;

    @Override
    public QueryWrapper<FCreditRecordEntity> getWrapper(Map<String, Object> params) {
        QueryWrapper<FCreditRecordEntity> wrapper = new QueryWrapper<>();
        return wrapper;
    }

    @Override
    public List<FCreditRecordAuditDTO> listInfo() {
        return null;
    }

    @Override
    public void saveInfo(FCreditRecordAuditDTO dto) {
        dto.setId(null);
        this.save(dto);
    }

    @Override
    public void updateInfo(FCreditRecordAuditDTO dto) {
        this.update(dto);
    }

    @Override
    public void check(FCreditRecordCheckDTO dto) {
        FCreditRecordEntity fCreditRecordEntity= baseDao.selectById(dto.getCreditId());
        FCreditRecordAuditDTO fCreditRecordAuditDTO= ConvertUtils.
                sourceToTarget(fCreditRecordEntity, FCreditRecordAuditDTO.class);
        fCreditRecordAuditDTO.setIsChecked(dto.getChecked());
        fCreditRecordAuditDTO.setCheckedDes(dto.getCheckedDes());
        this.update(fCreditRecordAuditDTO);
        fCreditRecordCheckService.save(dto);
    }

    @Override
    public void groupCheck(FCreditRecordCheckDTO dto) {
        FCreditRecordEntity fCreditRecordEntity= baseDao.selectById(dto.getCreditId());
        FCreditRecordAuditDTO fCreditRecordAuditDTO= ConvertUtils.
                sourceToTarget(fCreditRecordEntity, FCreditRecordAuditDTO.class);
        fCreditRecordAuditDTO.setGroupChecked(dto.getGroupChecked());
        fCreditRecordAuditDTO.setGroupCheckedDes(dto.getGroupCheckedDes());
        this.update(fCreditRecordAuditDTO);
        fCreditRecordCheckService.save(dto);
    }


    @Override
    public FCreditRecordAuditDTO getDealData(Long id) {
        //增信措施详情
        FCreditRecordAuditDTO dealData = fCreditRecordAuditDao.getDealData(id);
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

    @Override
    public FCreditRecordAuditDTO directInfoCast(FCreditRecordAuditDTO dto, Integer unit) {
        if (unit == 2){
            dto.setQuota(UnitCastUtil.divider(dto.getQuota()));
        }
        return dto;
    }

    @Override
    public FCreditRecordAuditDTO directSaveCast(FCreditRecordAuditDTO dto, Integer unit) {
        if (unit == 2){
            dto.setQuota(UnitCastUtil.multiplication(dto.getQuota()));
        }
        return dto;
    }

}
