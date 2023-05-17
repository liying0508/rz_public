package io.renren.modules.financing.service;

import io.renren.common.service.CrudService;
import io.renren.modules.financing.dto.FCreditHistoryPageDto;
import io.renren.modules.financing.dto.FCreditPageDto;
import io.renren.modules.financing.dto.FCreditRecordDTO;
import io.renren.modules.financing.entity.FCreditRecordHistoryEntity;

import java.util.List;

public interface FCreditRecordHistoryService extends CrudService<FCreditRecordHistoryEntity, FCreditRecordDTO> {
    /**
     * 分页
     * @return
     */
    List<FCreditHistoryPageDto> listInfo();

    /**
     *筛选数据
     */
    List<FCreditHistoryPageDto> screenList(List<FCreditHistoryPageDto> list,String deptName,String creditMeasures,
                                           String quota,Integer unit,Integer isChecked);

    /**
     * 获取数据并处理
     * @param id
     * @return
     */
    FCreditRecordDTO getDealData(Long id);
    /**
     * 直融详细信息单位转化
     */
    FCreditRecordDTO directInfoCast(FCreditRecordDTO dto,Integer unit);
}
