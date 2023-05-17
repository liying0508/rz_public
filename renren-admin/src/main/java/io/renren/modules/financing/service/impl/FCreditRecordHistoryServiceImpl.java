package io.renren.modules.financing.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.renren.common.service.impl.CrudServiceImpl;
import io.renren.common.utils.TreeUtils;
import io.renren.modules.financing.dao.FCreditRecordHistoryDao;
import io.renren.modules.financing.dto.FCreditHistoryPageDto;
import io.renren.modules.financing.dto.FCreditPageDto;
import io.renren.modules.financing.dto.FCreditRecordDTO;
import io.renren.modules.financing.entity.FCreditRecordHistoryEntity;
import io.renren.modules.financing.service.FCreditRecordHistoryService;
import io.renren.modules.financing.util.QueryUtil;
import io.renren.modules.financing.util.UnitCastUtil;
import io.renren.modules.financing.vo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class FCreditRecordHistoryServiceImpl
        extends CrudServiceImpl<FCreditRecordHistoryDao, FCreditRecordHistoryEntity, FCreditRecordDTO>
        implements FCreditRecordHistoryService {

    @Autowired
    FCreditRecordHistoryDao fCreditRecordHistoryDao;

    @Override
    public QueryWrapper<FCreditRecordHistoryEntity> getWrapper(Map<String, Object> params) {
        return null;
    }

    @Override
    public List<FCreditHistoryPageDto> listInfo() {
        //查询部门列表
        List<FCreditHistoryPageDto> deptList = baseDao.getlist();
        //赋值未使用额度
        for (FCreditHistoryPageDto fCreditPageDto : deptList) {
            List<BigDecimal> bigDecimal = fCreditRecordHistoryDao.
                    getUsedQuota(fCreditPageDto.getDeptId(), fCreditPageDto.getInstitution());
            BigDecimal usedQuota = new BigDecimal(0);
            for (BigDecimal decimal : bigDecimal) {
                usedQuota = usedQuota.add(decimal);
            }
            fCreditPageDto.setIndirectQuota(usedQuota);
            fCreditPageDto.setUnUsedQuota(fCreditPageDto.getQuota().subtract(usedQuota));
        }
        return deptList;
    }

    @Override
    public List<FCreditHistoryPageDto> screenList(List<FCreditHistoryPageDto> list, String deptName,String creditMeasures,
                                                  String quota, Integer unit, Integer isChecked) {
        if(unit == 2){
            for (FCreditHistoryPageDto fCreditPageDto : list) {
                fCreditPageDto.setQuota(UnitCastUtil.divider(fCreditPageDto.getQuota()));
                fCreditPageDto.setIndirectQuota(UnitCastUtil.divider(fCreditPageDto.getIndirectQuota()));
                fCreditPageDto.setUnUsedQuota(UnitCastUtil.divider(fCreditPageDto.getUnUsedQuota()));
            }
        }
        list.removeIf(next -> !QueryUtil.judgeConditionCreditRecord(next,  deptName,creditMeasures,quota));
        if (isChecked != null){
            list.removeIf(next -> next.getIsChecked()!=isChecked);
        }
        return list;
    }

    @Override
    public FCreditRecordDTO getDealData(Long id) {
        //增信措施详情
        FCreditRecordDTO dealData = fCreditRecordHistoryDao.getDealData(id);
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

    @Override
    public FCreditRecordDTO directInfoCast(FCreditRecordDTO dto, Integer unit) {
        if (unit == 2){
            dto.setQuota(UnitCastUtil.divider(dto.getQuota()));
        }
        return dto;
    }

}
