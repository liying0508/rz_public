package io.renren.modules.report.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import io.renren.common.utils.DateUtils;
import io.renren.modules.financing.dao.FDirectFinancingApprovalDao;
import io.renren.modules.financing.dao.FDirectFinancingDao;
import io.renren.modules.financing.dao.FGovernmentBondDao;
import io.renren.modules.financing.dao.FIndirectFinancingDao;
import io.renren.modules.financing.entity.FDirectFinancingApproval;
import io.renren.modules.financing.entity.FDirectFinancingEntity;
import io.renren.modules.financing.entity.FGovernmentBondEntity;
import io.renren.modules.financing.entity.FIndirectFinancingEntity;
import io.renren.modules.financing.vo.GuaranteeVO;
import io.renren.modules.report.dto.RFinancingBalanceDetailReportDTO;
import io.renren.modules.report.dto.vo.RFinancingBalanceDetailReportVO;
import io.renren.modules.report.service.RFinancingBalanceDetailReportService;
import io.renren.modules.report.util.ReportUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class RFinancingBalanceDetailReportServiceImpl implements RFinancingBalanceDetailReportService {

    private static final Long YEAR_TIME = 12*30*24*60*60*1000L;

    @Autowired
    FIndirectFinancingDao fIndirectFinancingDao;

    @Autowired
    FGovernmentBondDao fGovernmentBondDao;

    @Autowired
    FDirectFinancingApprovalDao fDirectFinancingApprovalDao;

    @Autowired
    FDirectFinancingDao fDirectFinancingDao;
    @Override
    public List<RFinancingBalanceDetailReportVO> listInfo() {
        //初始化报表返回值
        List<RFinancingBalanceDetailReportVO> rFinancingBalanceDetailReportVOS = new ArrayList<>();
        //获得间融数据
        List<RFinancingBalanceDetailReportVO> indirect = getIndirectFinancingBalanceDetail();
        //获得直融数据
        List<RFinancingBalanceDetailReportVO> direct = getDirectFinancingBalanceDetail();
        //获得政府债券数据
        List<RFinancingBalanceDetailReportVO> government = getGovernmentBondBalanceDetail();
        //总结到一起
        rFinancingBalanceDetailReportVOS.addAll(indirect);
        rFinancingBalanceDetailReportVOS.addAll(direct);
        rFinancingBalanceDetailReportVOS.addAll(government);
        return rFinancingBalanceDetailReportVOS;
    }

    /**
     * 获得政府债券数据的集合
     * @return
     */
    private List<RFinancingBalanceDetailReportVO> getGovernmentBondBalanceDetail(){
        //初始化一个报表的DTOList
        List<RFinancingBalanceDetailReportDTO> rFinancingBalanceDetailReportDTOS = new ArrayList<>();
        //从政府债券去查找数据填充
        List<FGovernmentBondEntity> fGovernmentBondEntities
                = fGovernmentBondDao.selectList(new LambdaQueryWrapper<FGovernmentBondEntity>()
                .eq(FGovernmentBondEntity::getIsChecked,1)
                .eq(FGovernmentBondEntity::getGroupChecked,1));
        //做出一个map（key是id，value是实体类）
        Map<Long, FGovernmentBondEntity> governmentMap = fGovernmentBondEntities
                .stream()
                .collect(Collectors.toMap(FGovernmentBondEntity::getId, entity -> entity));
        //得到id的set
        Set<Long> governmentIdSet = governmentMap.keySet();
        //遍历keySet，根据id来设置对象
        governmentIdSet.forEach(governmentId->{
            RFinancingBalanceDetailReportDTO dto = new RFinancingBalanceDetailReportDTO();
            FGovernmentBondEntity entity = governmentMap.get(governmentId);
            //设置融资种类()
            dto.setFinancingTypeName(entity.getVarieties().toString());
            //设置设置融资主体名字
            dto.setDeptName(entity.getDeptName());
            //设置金融机构名字
            dto.setInstitutionName("/");
            //设置融资产品
            dto.setFinanceProduct(entity.getVarieties().toString());
            //设置执行利率
            dto.setStrikeRate(entity.getRate());
            //设置期限/年
            //设置年限
            dto.setDeadLine((int) ((DateUtils.parse(entity.getRepaymentDate(),DateUtils.DATE_PATTERN).getTime())
                    - (DateUtils.parse(entity.getLoanDate(),DateUtils.DATE_PATTERN).getTime())/YEAR_TIME));
            //放款日期和到期日期
            dto.setLoanDate(DateUtils.parse(entity.getLoanDate(),DateUtils.DATE_PATTERN));
            dto.setEndDate(DateUtils.parse(entity.getRepaymentDate(),DateUtils.DATE_PATTERN));
            //设置贷款金额(发行金额)
            dto.setFinancingAmount(entity.getFinancingAmount());
            //设置贷款余额
            dto.setFinancingBalance(entity.getFinancingBalance());
            //设置增信措施
            dto.setCreditMeasures(entity.getCreditMeasures());
            //设置决策依据
            dto.setDecisionBasis("/");
            //设置备注
            dto.setRemarks(entity.getRemarks());
            //添加进报表
            rFinancingBalanceDetailReportDTOS.add(dto);
        });
        //复制到vo里
        List<RFinancingBalanceDetailReportVO> rFinancingBalanceDetailReportVOS
                = JSONObject.parseArray(JSON.toJSONString(rFinancingBalanceDetailReportDTOS), RFinancingBalanceDetailReportVO.class);
        return rFinancingBalanceDetailReportVOS;
    }

    /**
     * 设置直融信息
     * @return List RFinancingBalanceDetailReportVO
     */
    private List<RFinancingBalanceDetailReportVO> getDirectFinancingBalanceDetail() {
        //初始化一个报表的DTOList
        List<RFinancingBalanceDetailReportDTO> rFinancingBalanceDetailReportDTOS = new ArrayList<>();
        //从直融去数据填充
        List<FDirectFinancingEntity> fDirectFinancingEntities = fDirectFinancingDao
                .selectList(new LambdaQueryWrapper<FDirectFinancingEntity>()
                        .eq(FDirectFinancingEntity::getIsChecked,1)
                        .eq(FDirectFinancingEntity::getGroupChecked,1));
        //做出一个map（key是id，value是实体类）
        Map<Long, FDirectFinancingEntity> directMap = fDirectFinancingEntities
                .stream()
                .collect(Collectors.toMap(FDirectFinancingEntity::getId, entity -> entity));
        //得到key的set
        Set<Long> directIdSet = directMap.keySet();
        //得到直融批文的List
        List<FDirectFinancingApproval> fDirectFinancingApprovals
                = fDirectFinancingApprovalDao.selectList(new LambdaQueryWrapper<FDirectFinancingApproval>());

        //遍历keySet，并且根据id来设置返回对象
        directIdSet.forEach(directId->{
            RFinancingBalanceDetailReportDTO dto = new RFinancingBalanceDetailReportDTO();
            FDirectFinancingEntity entity = directMap.get(directId);
            //设置融资种类
            dto.setFinancingTypeName("市场化直接融资");
            //设置融资主体Id
            dto.setDeptId(Long.valueOf(entity.getDeptId()));
            //设置金融机构Id
            //TODO 直接融资没有金融机构只有承销机构，需哟啊沟通是否修改
            //dto.setInstitution(entity.getInstitution());
            //设置直融批文号
            dto.setApprovalNo(entity.getApproveNo());
            //设置执行利率(直融就是利率)
            dto.setStrikeRate(Double.valueOf(entity.getRate()));
            //设置年限
            dto.setDeadLine((int) ((entity.getDueDate().getTime()-entity.getValueDate().getTime())/YEAR_TIME));
            //放款日期和到期日期
            dto.setLoanDate(entity.getValueDate());
            dto.setEndDate(entity.getDueDate());
            //设置贷款金额(发行金额)
            dto.setFinancingAmount(entity.getIssueQuota());
            //设置贷款余额
            dto.setFinancingBalance(entity.getFinancingBalance());
            //设置增信措施
            dto.setCreditMeasures(entity.getCreditMeasures());
            //设置决策依据
            dto.setDecisionBasis(entity.getDecisionBasis());
            //设置备注
            dto.setRemarks(entity.getRemarks());
            //添加进报表
            rFinancingBalanceDetailReportDTOS.add(dto);
        });
        //得到融资主体和名字对应的map以及金融机构和名字对应的map
        Map<Long, String> deptIdAndNameMap = ReportUtils.getDeptIdAndName();
        Map<String, String> approvalNoAndVarietiesMap = ReportUtils.getApprovalNoAndVarieties();
        //遍历处理融资主体名字和融资产品
        rFinancingBalanceDetailReportDTOS.forEach(dto->{
            dto.setDeptName(deptIdAndNameMap.get(dto.getDeptId()));
            dto.setVarieties(approvalNoAndVarietiesMap.get(dto.getApprovalNo()));
        });
        //复制到vo里
        List<RFinancingBalanceDetailReportVO> rFinancingBalanceDetailReportVOS
                = JSONObject.parseArray(JSON.toJSONString(rFinancingBalanceDetailReportDTOS), RFinancingBalanceDetailReportVO.class);
        return rFinancingBalanceDetailReportVOS;
    }

    /**
     * 得到间融的数据
     * @return
     */
    private List<RFinancingBalanceDetailReportVO> getIndirectFinancingBalanceDetail() {
        //初始化一个报表的DTOList
        List<RFinancingBalanceDetailReportDTO> rFinancingBalanceDetailReportDTOS = new ArrayList<>();
        //从间融数据库取数据填充
        List<FIndirectFinancingEntity> fIndirectFinancingEntities = fIndirectFinancingDao
                .selectList(new LambdaQueryWrapper<FIndirectFinancingEntity>()
                        .eq(FIndirectFinancingEntity::getIsChecked,1)
                        .eq(FIndirectFinancingEntity::getGroupChecked,1));
        //做出一个map（key是id，value是实体类）
        Map<Long, FIndirectFinancingEntity> indirectMap = fIndirectFinancingEntities
                .stream()
                .collect(Collectors.toMap(FIndirectFinancingEntity::getId, entity -> entity));
        //得到key的set
        Set<Long> indirectIdSet = indirectMap.keySet();
        //遍历keySet，并且根据id来设置返回对象
        indirectIdSet.forEach(indirectId->{
            RFinancingBalanceDetailReportDTO dto = new RFinancingBalanceDetailReportDTO();
            FIndirectFinancingEntity entity = indirectMap.get(indirectId);
            //设置融资种类
            dto.setFinancingTypeName("间接融资");
            //设置融资主体Id
            dto.setDeptId(entity.getDeptId());
            //设置金融机构Id
            dto.setInstitution(entity.getInstitution());
            //设置融资种类(转化成字符串)
            dto.setVarieties(entity.getVarieties().toString());
            //设置执行利率相关的参数
            //利息，需要进行判断（1是浮动利率，设置为10000，具体和前端协商）
            if (entity.getInterestRatesWay().equals("1")){
                dto.setStrikeRate(10000.00);
                dto.setStrikeRate(dto.getStrikeRate()+Double.valueOf(entity.getFloatRate()));
            }else {
                dto.setStrikeRate(Double.valueOf(entity.getRate())+Double.valueOf(entity.getFloatRate()));
            }
            //期限(单位：年)
            dto.setDeadLine(Integer.valueOf((int) ((entity.getRepaymentDate().getTime()-entity.getLoanDate().getTime())/YEAR_TIME)));
            //放款日期和到期日期
            dto.setLoanDate(entity.getLoanDate());
            dto.setEndDate(entity.getRepaymentDate());
            //设置贷款金额
            dto.setFinancingAmount(entity.getWithdrawalAmount());
            //设置贷款余额
            dto.setFinancingBalance(entity.getFinancingBalance());
            //设置增信措施
            dto.setCreditMeasures(entity.getCreditMeasures());
            //设置担保主体
            //TODO 设置担保相关的信息，需要和甲方沟通，因为实体类中现存的是集合，无法单独提取数据，需要解决
            String guarantor = "";
            List<GuaranteeVO> guarantee = JSON.parseObject(entity.getGuarantee(),new TypeReference<List<GuaranteeVO>>(){});
            for (GuaranteeVO gua : guarantee) {
                guarantor=guarantor+gua.getGuarantor()+" ";
            }
            dto.setGuarantor(guarantor);
            //设置决策依据
            dto.setDecisionBasis(entity.getDecisionBasis());
            //设置备注
            dto.setRemarks(entity.getRemarks());
            //添加进报表
            rFinancingBalanceDetailReportDTOS.add(dto);
        });
        //得到融资主体和名字对应的map以及金融机构和名字对应的map
        Map<Long, String> institutionIdAndNameMap = ReportUtils.getInstitutionIdAndName();
        Map<Long, String> deptIdAndNameMap = ReportUtils.getDeptIdAndName();

        rFinancingBalanceDetailReportDTOS.forEach(dto->{
            //遍历dto，设置金融机构
            dto.setDeptName(deptIdAndNameMap.get(dto.getDeptId()));
            //遍历dto，设置融资主体的名字
            dto.setInstitutionName(institutionIdAndNameMap.get(dto.getInstitution()));
            //遍历dto，设置产品名称
            dto.setFinanceProduct(dto.getVarieties());
        });

        //复制到vo里
        List<RFinancingBalanceDetailReportVO> rFinancingBalanceDetailReportVOS
                = JSONObject.parseArray(JSON.toJSONString(rFinancingBalanceDetailReportDTOS), RFinancingBalanceDetailReportVO.class);
        return rFinancingBalanceDetailReportVOS;
    }

}
