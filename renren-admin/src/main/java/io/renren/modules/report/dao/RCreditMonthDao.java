package io.renren.modules.report.dao;

import io.renren.modules.report.dto.RCreditMonthDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface RCreditMonthDao {
    List<RCreditMonthDTO> selectDataByMonth (String month);
}
