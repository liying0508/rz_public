package io.renren.modules.financing.service;

import io.renren.common.service.CrudService;
import io.renren.modules.financing.dto.FCreditRecordDTO;
import io.renren.modules.financing.dto.FDirectFinancingApprovalCheckDTO;
import io.renren.modules.financing.dto.FDirectFinancingApprovalDTO;
import io.renren.modules.financing.dto.FDirectFinancingDTO;
import io.renren.modules.financing.entity.FDirectFinancingApproval;
import io.renren.modules.financing.excel.FDirectFinancingApprovalExcel;
import io.renren.modules.financing.excel.FDirectFinancingExcel;

import java.util.List;

public interface FDirectFinancingApprovalService extends CrudService<FDirectFinancingApproval, FDirectFinancingApprovalDTO> {
    List<FDirectFinancingApprovalDTO> listInfo();

    void saveInfo(FDirectFinancingApprovalDTO dto);

    void updateInfo(FDirectFinancingApprovalDTO dto);

    void check(FDirectFinancingApprovalCheckDTO dto);

    void groupCheck(FDirectFinancingApprovalCheckDTO dto);

    FDirectFinancingApprovalDTO getDealData(Long id);

    /**
     * 直融详细信息单位转化
     */
    FDirectFinancingApprovalDTO directInfoCast(FDirectFinancingApprovalDTO dto, Integer unit);
    /**
     * 直融存进去的数据进行转化
     */
    FDirectFinancingApprovalDTO directSaveCast(FDirectFinancingApprovalDTO dto,Integer unit);

    /**
     *得到导出数据
     */
    List<FDirectFinancingApprovalExcel> getExportData(List<FDirectFinancingApprovalDTO> list, String approvalAmount);
    /**
     * 删除
     */
    void deleteInfo(Long[] ids);
}
