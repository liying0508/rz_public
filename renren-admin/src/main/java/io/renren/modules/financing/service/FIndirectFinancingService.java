package io.renren.modules.financing.service;

import io.renren.common.page.PageData;
import io.renren.common.service.CrudService;
import io.renren.modules.financing.dto.*;
import io.renren.modules.financing.entity.FIndirectFinancingEntity;
import io.renren.modules.financing.excel.FIndirectFinancingExcel;
import io.renren.modules.financing.util.UnitCastUtil;
import io.renren.modules.financing.vo.IndirectInterestVO;

import java.math.BigDecimal;
import java.util.List;

/**
 * 间接融资信息
 *
 * @author liuwenyong 
 * @since 3.0 2022-05-14
 */
public interface FIndirectFinancingService extends CrudService<FIndirectFinancingEntity, FIndirectFinancingDTO> {
    /**
     * 保存数据
     * @param dto
     */
    void saveInfo(FIndirectFinancingDTO dto);
    /**
     * 获取数据并处理
     * @param id
     * @return
     */
    FIndirectFinancingDTO getDealData(Long id);
    /**
     * 得到指定融资id和机构id的融资额度
     */
    BigDecimal getQuota(Long deptId,Long institution);
    /**
     * 得到指定融资id和机构id的已使用额度
     */
    BigDecimal getUsedQuota(Long deptId,Long institution);
    /**
     * 更新数据
     * @param dto
     */
    void updateInfo(FIndirectFinancingDTO dto);
    /**
     *
     */
    List<FIndirectFinancingDTO> screenList(List<FIndirectFinancingDTO> list,String deptName,String deadLine);
    /**
     *得到导出数据
     */
    List<FIndirectFinancingExcel> getExportData(List<FIndirectFinancingDTO> list);
    /**
     * 利息测算方法
     */
    List<FRepaymentPlanDTO> interestCalculate(IndirectInterestVO vo);
    /**
     * 间融首页列表单位转化
     */
    PageData<FIndirectFinancingDTO> indirectCast(PageData<FIndirectFinancingDTO> page,Integer unit);
    /**
     * 间融详细信息单位转化
     */
    FIndirectFinancingDTO indirectInfoCast(FIndirectFinancingDTO dto,Integer unit);
    /**
     * 间融详细信息单位转化
     */
    FIndirectFinancingDTO indirectSaveCast(FIndirectFinancingDTO dto,Integer unit);

    void deleteInfo(Long[] ids);
}