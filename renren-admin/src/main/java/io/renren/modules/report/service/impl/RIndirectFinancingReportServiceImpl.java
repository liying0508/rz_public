package io.renren.modules.report.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.renren.modules.financing.dao.FFinanceDeptDao;
import io.renren.modules.financing.dao.FIndirectFinancingDao;
import io.renren.modules.financing.dto.FIndirectFinancingDTO;
import io.renren.modules.financing.entity.FFinanceDeptEntity;
import io.renren.modules.financing.service.FIndirectFinancingService;
import io.renren.modules.report.dto.RIndirectFinancingReportDTO;
import io.renren.modules.report.dto.vo.RIndirectFinancingReportVO;
import io.renren.modules.report.service.RIndirectFinancingReportService;
import io.renren.modules.sys.dao.SysDeptDao;
import io.renren.modules.sys.entity.SysDeptEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class RIndirectFinancingReportServiceImpl implements RIndirectFinancingReportService {

    private static final Long MONTH_TIME = 30*24*60*60*1000L;

    @Autowired
    FIndirectFinancingDao fIndirectFinancingDao;

    @Autowired
    FIndirectFinancingService fIndirectFinancingService;

    @Autowired
    SysDeptDao sysDeptDao;

    @Autowired
    FFinanceDeptDao fFinanceDeptDao;

    @Override
    public List<RIndirectFinancingReportVO> getReportInfo() {
        //先查出所有的间融信息
        List<FIndirectFinancingDTO> indirect
                = fIndirectFinancingDao.selectAllData();
        //过滤出所有的审核通过的信息
        List<FIndirectFinancingDTO> list = indirect
                .stream()
                .filter(dto -> dto.getIsChecked() == 1)
                .filter(dto -> dto.getGroupChecked() == 1)
                .collect(Collectors.toList());
        //复制到报表的dto集合中
        List<RIndirectFinancingReportDTO> rIndirectFinancingReportDTOS
                = JSONObject.parseArray(JSON.toJSONString(list), RIndirectFinancingReportDTO.class);
        //然后转化成vo
        //查询所有的融资机构名称
        List<SysDeptEntity> sysDeptEntities = sysDeptDao.selectList(new QueryWrapper<SysDeptEntity>());

        //得到id和名字作为键值对的map（融资主体）
        Map<Long, String> deptIdAndNameMap = sysDeptEntities
                .stream()
                .collect(Collectors.toMap(SysDeptEntity::getId, SysDeptEntity::getName));
        //所有的银行机构实体类列表
        List<FFinanceDeptEntity> fFinanceDeptEntities
                = fFinanceDeptDao.selectList(new QueryWrapper<FFinanceDeptEntity>());
        //得到id和名字作为键值对的map（银行机构）
        Map<Long, String> institutionIdAndNameMap
                = fFinanceDeptEntities.stream().collect(Collectors.toMap(FFinanceDeptEntity::getId, FFinanceDeptEntity::getName));
        //得到银行机构父机构的id（总行）
        Map<Long, Long> institutionIdAndPIdMap
                = fFinanceDeptEntities.stream().collect(Collectors.toMap(FFinanceDeptEntity::getId, FFinanceDeptEntity::getPid));
        //用list做成一个map，key是id，值是对象
        Map<Long, FIndirectFinancingDTO> fIndirectFinancingDTOMap = list
                .stream()
                .collect(Collectors.toMap(dto1 -> dto1.getId(), dto1 -> dto1));
        //遍历补全对象信息
        for (RIndirectFinancingReportDTO dto : rIndirectFinancingReportDTOS) {
            //融资主体
            dto.setDeptName(deptIdAndNameMap.get(dto.getDeptId()));
            //金融机构父机构id
            dto.setInstitutionPId(institutionIdAndPIdMap.get(dto.getInstitution()));
            //金融机构分支名称
            dto.setInstitutionBranchName(institutionIdAndNameMap.get(dto.getInstitution()));
            //金融机构总行名称
            dto.setInstitutionName(institutionIdAndNameMap.get(dto.getInstitutionPId()));
            //融资品种是否需要修改？
            //TODO 这里的融资品种还是int，是否需要修改
            dto.setProductName(dto.getVarieties());
            //去间融里复制
            //利息，需要进行判断（1是浮动利率，设置为10000，具体和前端协商）
            if (dto.getInterestRatesWay().equals("1")){
                dto.setRate(10000.00);
            }
            dto.setRate(dto.getRate()+Double.valueOf(dto.getFloatRate()));
            //期限
            dto.setDeadLine(Integer.valueOf((int) ((dto.getRepaymentDate().getTime()-dto.getLoanDate().getTime())/MONTH_TIME)));
            //从map里取出融资金额（就是提款金额）
            dto.setFinancingAmount(fIndirectFinancingDTOMap.get(dto.getId()).getWithdrawalAmount());
            //从map里取出融资余额
            dto.setFinancingBalanceAmount(fIndirectFinancingDTOMap.get(dto.getId()).getFinancingBalance());
            //从map里取出备注
            dto.setRemark(fIndirectFinancingDTOMap.get(dto.getId()).getRemarks());
        }
        //然后把dto集合转为vo集合
        List<RIndirectFinancingReportVO> rIndirectFinancingReportVOS
                = JSONObject.parseArray(JSON.toJSONString(rIndirectFinancingReportDTOS), RIndirectFinancingReportVO.class);
        return rIndirectFinancingReportVOS;
    }
}
