package io.renren.modules.financing.service;

import io.renren.common.service.CrudService;
import io.renren.modules.financing.dto.*;
import io.renren.modules.financing.entity.FRepaymentInfo;
import io.renren.modules.financing.excel.FGovernmentBondExcel;
import io.renren.modules.financing.excel.FRepaymentInfoExcel;

import java.util.List;
import java.util.Map;

/**
 * @Author: Eddy
 * @Date: 2022/9/24
 * @Time: 19:58
 * @Description: 还款信息service
 */
public interface FRepaymentInfoService extends CrudService<FRepaymentInfo, FRepaymentInfoDTO> {
    /**
     * 分页
     * @return
     */
    List<FRepaymentInfoPageDTO> listInfo();
    /**
     *
     */
    FRepaymentInfoDTO getDealData(Long financingId,Integer financingType);

//    /**
//     * 保存
//     */
//    void saveInfo(FRepaymentInfoDTO dto);

    void updateInfo(FRepaymentInfoDTO dto);

    void check(FRepaymentInfoCheckDTO dto);

    void groupCheck(FRepaymentInfoCheckDTO dto);

    /**
     * 初始化数据库
     * @param fRepaymentInfoPageDTOList
     */
    void initFRepaymentInfo(List<FRepaymentInfoPageDTO> fRepaymentInfoPageDTOList);

    List<FRepaymentInfoPageDTO> addStatus(List<FRepaymentInfoPageDTO> list);

    List<FRepaymentInfoPageDTO> pageScreen(List<FRepaymentInfoPageDTO> list,
                                           Map<String,Object> params, Integer isChecked, Integer groupChecked);

    /**
     * 回显列表的单位转化
     * @param fRepaymentInfoPageDTOS
     * @param unit
     * @return
     */
    List<FRepaymentInfoPageDTO> unitCast(List<FRepaymentInfoPageDTO> fRepaymentInfoPageDTOS,Integer unit);

    /**
     * 详细信息回显单位转化
     * @param dto
     * @param unit
     * @return
     */
    FRepaymentInfoDTO unitInfoCast(FRepaymentInfoDTO dto,Integer unit);

    /**
     * 保存或者更新的单位转化
     * @param dto
     * @param unit
     * @return
     */
    FRepaymentInfoDTO unitSaveCast(FRepaymentInfoDTO dto, Integer unit);

    void initRepaymentInfo();

    /**
     * 清理f_repayment_info表（即存储还款详细信息的表）
     * @param fRepaymentInfoPageDTOList
     */
    public void clearFRepaymentInfo(List<FRepaymentInfoPageDTO> fRepaymentInfoPageDTOList) ;

    /**
     * 清理f_repayment_info_table表（即存储还款列表信息的表）
     * @param
     */
    public void clearRepaymentInfo(List<FRepaymentInfoDTO> oldList) ;

    /**
     *得到导出数据
     */
    List<FRepaymentInfoExcel> getExportData(List<FRepaymentInfoPageDTO> list);

}
