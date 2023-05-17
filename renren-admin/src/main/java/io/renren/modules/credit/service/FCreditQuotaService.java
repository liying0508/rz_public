package io.renren.modules.credit.service;

import io.renren.common.service.CrudService;
import io.renren.modules.credit.dto.FCreditQuotaDTO;
import io.renren.modules.credit.entity.FCreditQuotaEntity;

import java.util.List;
import java.util.Map;

public interface FCreditQuotaService extends CrudService<FCreditQuotaEntity, FCreditQuotaDTO> {

    public void updateUsedQuotaByInstitution(Long institution);

    //获得列表数据并处理
    List<FCreditQuotaDTO> listInfo(Map<String, Object> params);
//    //分页数据处理
//    List<FCreditQuotaDTO> pageDeal(List<FCreditQuotaDTO> list);
    //查询
    FCreditQuotaDTO dtoDeal(Long id);
    //保存
    void saveInfo(FCreditQuotaDTO dto);
    //更新
    void updateInfo(FCreditQuotaDTO dto);
    //删除
    void deleteAllInfo(Long[] ids);

    /**
     * 生成所有历史数据的快照
     * @param creditQuotaDTOList  List
     */
    void generateSnapshots(List<FCreditQuotaDTO> creditQuotaDTOList);
}
