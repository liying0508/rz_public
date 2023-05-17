package io.renren.modules.report.service.impl;

import io.renren.modules.report.dao.RCreditMonthDao;
import io.renren.modules.report.dto.RCreditMonthDTO;
import io.renren.modules.report.service.RCreditMonthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class RCreditMonthServiceImpl implements RCreditMonthService {

    @Autowired
    RCreditMonthDao rCreditMonthDao;

    @Override
    public List<RCreditMonthDTO> listInfo(Map<String, Object> params) {
        //Date month = DateUtils.parse(params.get("month").toString(), "yyyy-MM");
        //得到日期条件
        String month = params.get("month").toString();
        //去数据库里查出截止到这个月为止的授信
        List<RCreditMonthDTO> rCreditMonthDTOS = rCreditMonthDao.selectDataByMonth(month);
        //赋值已经使用的额度
        return rCreditMonthDTOS;
    }
}
