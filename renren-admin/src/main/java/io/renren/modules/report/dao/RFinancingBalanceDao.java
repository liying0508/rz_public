package io.renren.modules.report.dao;

import io.renren.modules.report.dto.RFinancingBalanceDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface RFinancingBalanceDao {

    public List<RFinancingBalanceDTO> selectListFromIndirectFinancing();

    public List<RFinancingBalanceDTO> selectListFromDirectFinancing();

    public List<RFinancingBalanceDTO> selectListFromGovernmentBond();


}
