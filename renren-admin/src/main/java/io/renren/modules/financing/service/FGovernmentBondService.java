package io.renren.modules.financing.service;

import io.renren.common.service.CrudService;
import io.renren.modules.financing.dto.*;
import io.renren.modules.financing.entity.FGovernmentBondEntity;
import io.renren.modules.financing.excel.FCreditRecordExcel;
import io.renren.modules.financing.excel.FGovernmentBondExcel;
import io.renren.modules.financing.vo.IndirectInterestVO;

import java.util.HashMap;
import java.util.List;

/**
 * 政府债券
 *
 * @author liuwenyong 
 * @since 3.0 2022-05-14
 */
public interface FGovernmentBondService
        extends CrudService<FGovernmentBondEntity, FGovernmentBondDTO> {

    /**
     * 保存数据
     * @param dto
     */
    void saveInfo(FGovernmentBondDTO dto);

    /**
     * 修改数据
     * @param dto
     */
    void updateInfo(FGovernmentBondDTO dto);

//    /**
//     * 获取开
//     * @param
//     * @return
//     */
//    HashMap<String,Object> getData(String approveNo);

//    /**
//     * 审核
//     * @param dto
//     */
//    void check(FGovernmentBondDTO dto);

    /**
     * 获取数据并处理
     * @param id
     * @return
     */
    FGovernmentBondDTO getDealData(Long id);

    /**
     * 政府债券详细信息单位转化
     */
    FGovernmentBondDTO directInfoCast(FGovernmentBondDTO dto, Integer unit);
    /**
     * 政府债券存进去的数据进行转化
     */
    FGovernmentBondDTO directSaveCast(FGovernmentBondDTO dto,Integer unit);
    /**
     * 利息测算方法
     */
    List<FRepaymentPlanDTO> interestCalculate(IndirectInterestVO vo);

    /**
     *得到导出数据
     */
    List<FGovernmentBondExcel> getExportData(List<FGovernmentBondDTO> list);
}