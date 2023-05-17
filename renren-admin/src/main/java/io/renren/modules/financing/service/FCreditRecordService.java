package io.renren.modules.financing.service;

import io.renren.common.service.CrudService;
import io.renren.modules.financing.dto.*;
import io.renren.modules.financing.entity.FCreditRecordEntity;
import io.renren.modules.financing.excel.FCreditRecordExcel;
import io.renren.modules.financing.excel.FIndirectFinancingExcel;

import java.util.List;
import java.util.Map;

/**
 * 银行授信
 *
 * @author liuwenyong 
 * @since 3.0 2022-05-14
 */
public interface FCreditRecordService extends CrudService<FCreditRecordEntity, FCreditRecordDTO> {

    /**
     * 分页
     * @return
     */
    List<FCreditPageDto> listInfo();

    /**
     * 获得列表
     * @return
     */
    List<FCreditPageDto> getList();
    /**
     * 保存授信单相关信息
     * @param dto
     */
    void saveInfo(FCreditRecordDTO dto);

    /**
     * 修改授信单相关信息
     * @param dto
     */
    void updateInfo(FCreditRecordDTO dto);

    /**
     * 获取数据并处理
     * @param id
     * @return
     */
    FCreditRecordDTO getDealData(Long id);

    /**
     *筛选数据
     */
    List<FCreditPageDto> screenList(List<FCreditPageDto> list,String deptName,String creditMeasures,
                                    String quota,Integer unit,Integer isChecked);

    List<FCreditPageDto> screenCheckList(List<FCreditPageDto> list,Integer groupChecked);
    /**
     * 直融详细信息单位转化
     */
    FCreditRecordDTO directInfoCast(FCreditRecordDTO dto,Integer unit);
    /**
     * 直融存进去的数据进行转化
     */
    FCreditRecordDTO directSaveCast(FCreditRecordDTO dto,Integer unit);

    /**
     *得到导出数据
     */
    List<FCreditRecordExcel> getExportData(List<FCreditPageDto> list);

    /**
     * 清理数据
     */
    void clearFCreditRecord();
}