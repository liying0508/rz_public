package io.renren.modules.financing.dao;


import io.renren.common.dao.BaseDao;
import io.renren.modules.financing.dto.FCreditPageDto;
import io.renren.modules.financing.dto.FCreditRecordDTO;
import io.renren.modules.financing.dto.FDirectFinancingApprovalCheckDTO;
import io.renren.modules.financing.dto.FDirectFinancingApprovalDTO;
import io.renren.modules.financing.entity.FDirectFinancingApproval;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface FDirectFinancingApprovalDao extends BaseDao<FDirectFinancingApproval> {
    /**
     * 银行授信列表
     * @return
     */
    List<FDirectFinancingApprovalDTO> getlist();

    FDirectFinancingApprovalDTO getInfoByApprovalNo(String approvalNo);

    FDirectFinancingApprovalDTO getDealData(Long id);
}