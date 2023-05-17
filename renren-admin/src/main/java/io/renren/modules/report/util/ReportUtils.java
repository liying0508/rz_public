package io.renren.modules.report.util;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.renren.modules.financing.dao.FDirectFinancingApprovalDao;
import io.renren.modules.financing.dao.FFinanceDeptDao;
import io.renren.modules.financing.entity.FDirectFinancingApproval;
import io.renren.modules.financing.entity.FFinanceDeptEntity;
import io.renren.modules.sys.dao.SysDeptDao;
import io.renren.modules.sys.entity.SysDeptEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public final class ReportUtils {

    @Autowired
    private SysDeptDao sysDeptDao1;

    @Autowired
    private FFinanceDeptDao fFinanceDeptDao1;

    @Autowired
    private FDirectFinancingApprovalDao fDirectFinancingApprovalDao1;

    private static SysDeptDao sysDeptDao;

    private static FFinanceDeptDao fFinanceDeptDao;

    private static FDirectFinancingApprovalDao fDirectFinancingApprovalDao;

    @PostConstruct
    public void init(){
        sysDeptDao = sysDeptDao1;
        fFinanceDeptDao = fFinanceDeptDao1;
        fDirectFinancingApprovalDao = fDirectFinancingApprovalDao1;
    }

    /**
     * 获得融资主体id和名称对应的map
     * @return Map（key:deptId; value:deptName）
     */
    public static Map<Long,String> getDeptIdAndName(){
        //查询所有的融资机构名称
        List<SysDeptEntity> sysDeptEntities = sysDeptDao.selectList(new QueryWrapper<SysDeptEntity>());

        //得到id和名字作为键值对的map（融资主体）
        Map<Long, String> deptIdAndNameMap = sysDeptEntities
                .stream()
                .collect(Collectors.toMap(SysDeptEntity::getId, SysDeptEntity::getName));

        return deptIdAndNameMap;
    }

    /**
     * 获得金融机构id和名称对应的map
     * @return Map(key:institutionId, value:institutionName)
     */
    public static Map<Long,String> getInstitutionIdAndName(){
        //所有的银行机构实体类列表
        List<FFinanceDeptEntity> fFinanceDeptEntities
                = fFinanceDeptDao.selectList(new QueryWrapper<FFinanceDeptEntity>());
        //得到id和名字作为键值对的map（银行机构）
        Map<Long, String> institutionIdAndNameMap
                = fFinanceDeptEntities.stream().collect(Collectors.toMap(FFinanceDeptEntity::getId, FFinanceDeptEntity::getName));
        return institutionIdAndNameMap;
    }

    /**
     * 获得直融批文号和融资种类对应的map
     * @return Map(key:approvalNo, value:varieties)
     */
    public static Map<String,String> getApprovalNoAndVarieties(){
        //所有直融批文实体类集合
        List<FDirectFinancingApproval> fDirectFinancingApprovals
                = fDirectFinancingApprovalDao.selectList(new LambdaQueryWrapper<FDirectFinancingApproval>());
        //得到map
        Map<String, String> approvalNoAndVarietiesMap = fDirectFinancingApprovals
                .stream()
                .collect(Collectors.toMap(FDirectFinancingApproval::getApproveNo, FDirectFinancingApproval::getVarieties));
        return approvalNoAndVarietiesMap;
    }
}
