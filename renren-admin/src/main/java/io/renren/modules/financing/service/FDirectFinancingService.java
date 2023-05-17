package io.renren.modules.financing.service;

import io.renren.common.page.PageData;
import io.renren.common.service.CrudService;
import io.renren.modules.financing.dto.FDirectFinancingApprovalCheckDTO;
import io.renren.modules.financing.dto.FDirectFinancingDTO;
import io.renren.modules.financing.dto.FIndirectFinancingDTO;
import io.renren.modules.financing.dto.FRepaymentPlanDTO;
import io.renren.modules.financing.entity.FDirectFinancingEntity;
import io.renren.modules.financing.excel.FDirectFinancingExcel;
import io.renren.modules.financing.excel.FIndirectFinancingExcel;
import io.renren.modules.financing.vo.IndirectInterestVO;

import java.util.HashMap;
import java.util.List;

/**
 * 直接融资信息
 *
 * @author liuwenyong 
 * @since 3.0 2022-05-14
 */
public interface FDirectFinancingService extends CrudService<FDirectFinancingEntity, FDirectFinancingDTO> {
    /**
     * 保存数据
     * @param dto
     */
    void saveInfo(FDirectFinancingDTO dto);

    /**
     * 修改数据
     * @param dto
     */
    void updateInfo(FDirectFinancingDTO dto);

    /**
     * 获取开
     * @param
     * @return
     */
    HashMap<String,Object> getData(String approveNo);

    /**
     * 审核
     * @param dto
     */
    void check(FDirectFinancingApprovalCheckDTO dto);

    /**
     * 获取数据并处理
     * @param id
     * @return
     */
    FDirectFinancingDTO getDealData(Long id);
    /**
     *
     */
    List<FDirectFinancingDTO> screenList(List<FDirectFinancingDTO> list,String creditMeasures);

    /**
     *
     */
    String screen(String deptName);
    /**
     * 直融首页列表单位转化
     */
    List<FDirectFinancingDTO> directCast(List<FDirectFinancingDTO> list, Integer unit);
    /**
     * 直融详细信息单位转化
     */
    FDirectFinancingDTO directInfoCast(FDirectFinancingDTO dto,Integer unit);
    /**
     * 直融存进去的数据进行转化
     */
    FDirectFinancingDTO directSaveCast(FDirectFinancingDTO dto,Integer unit);

    /**
     * 利息测算方法
     */
    List<FRepaymentPlanDTO> interestCalculate(IndirectInterestVO vo);

    /**
     *得到导出数据
     */
    List<FDirectFinancingExcel> getExportData(List<FDirectFinancingDTO> list,String approvalAmount);

    /**
     * 删除信息（包含实际还款列表以及还款计划列表）
     */
    void deleteInfo(Long[] ids);
}