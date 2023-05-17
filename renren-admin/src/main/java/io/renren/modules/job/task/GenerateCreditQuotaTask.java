package io.renren.modules.job.task;

import com.alibaba.fastjson.JSON;
import io.renren.modules.credit.dto.FCreditQuotaDTO;
import io.renren.modules.credit.dto.FCreditQuotaHistoryDTO;
import io.renren.modules.credit.service.FCreditQuotaHistoryService;
import io.renren.modules.credit.service.FCreditQuotaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;

@Component("generateCreditTask")
public class GenerateCreditQuotaTask implements ITask{

    @Autowired
    FCreditQuotaService fCreditQuotaService;

    @Autowired
    FCreditQuotaHistoryService fCreditQuotaHistoryService;
    /**
     * 执行定时任务接口
     *
     * @param params 参数，多参数使用JSON数据
     */
    @Override
    public void run(String params) {
        //查询所有数据
        List<FCreditQuotaDTO> list = fCreditQuotaService.list(new HashMap<>());
        //克隆到历史列表
        List<FCreditQuotaHistoryDTO> historyDTOS = JSON.parseArray(JSON.toJSONString(list), FCreditQuotaHistoryDTO.class);
        //遍历，插入历史列表
        historyDTOS.forEach(dto ->{
            dto.setCreditQuotaId(dto.getId());
            dto.setId(null);
            fCreditQuotaHistoryService.save(dto);
        });
    }
}
