package io.renren.modules.report.dao;

import io.renren.modules.report.dto.RFinancingDetailDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface RFinancingDetailDao {
    public List<RFinancingDetailDTO> selectListFromIndirectFinancing();

    public List<RFinancingDetailDTO> selectListFromDirectFinancing();

    public List<RFinancingDetailDTO> selectListFromGovernmentBond();
}
