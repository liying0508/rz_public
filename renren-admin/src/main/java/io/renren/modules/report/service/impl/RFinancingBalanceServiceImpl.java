package io.renren.modules.report.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import io.renren.modules.financing.dao.FFinanceDeptDao;
import io.renren.modules.financing.vo.CollateralVO;
import io.renren.modules.financing.vo.GuaranteeVO;
import io.renren.modules.lpr.dao.FLprDao;
import io.renren.modules.lpr.dto.FLprDTO;
import io.renren.modules.report.dao.RFinancingBalanceDao;
import io.renren.modules.report.dto.RFinancingBalanceDTO;
import io.renren.modules.report.service.RFinancingBalanceService;
import io.renren.modules.sys.dao.SysDeptDao;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class RFinancingBalanceServiceImpl implements RFinancingBalanceService {

    @Autowired
    RFinancingBalanceDao rFinancingBalanceDao;

    @Autowired
    FFinanceDeptDao fFinanceDeptDao;

    @Autowired
    SysDeptDao sysDeptDao;

    @Autowired
    FLprDao fLprDao;

    private final static String ONE_YEAR_LPR = "一年期lpr";

    private final static String FIVE_YEAR_LPR = "五年期lpr";

    //一天的时间毫秒数
    private static Long MONTH_TIME = 60L*60*1000*24*30;

    private final static String GUARANTEE_BONDSMAN = "保证担保人：";

    private final static String COLLATERAL_BONDSMAN  = "抵押担保人：";

    private final static String PLEDGE_BONDSMAN  = "质押担保人：";

    @Override
    public List<RFinancingBalanceDTO> listInfo(Map<String, Object> params) {
        //1 先拿到所有的数据
        List<RFinancingBalanceDTO> allList = new ArrayList<>();
        //2 得到所有的间融数据
        allList.addAll(this.indirectListInfo());
        //3 得到所有的直融数据
        allList.addAll(this.directListInfo());
        //4 得到所有的政府债券数据
        allList.addAll(this.governmentBondListInfo());
        //5 筛选，如果融资余额为0的话就是还完了
        allList.removeIf(dto -> (dto.getFinancingBalance() != null && dto.getFinancingBalance().doubleValue()==0));
        //添加到总列表中
        return allList;
    }

    private List<RFinancingBalanceDTO> indirectListInfo(){
        //1 先去查大部分间融数据
        List<RFinancingBalanceDTO> indirect = rFinancingBalanceDao.selectListFromIndirectFinancing();
        //2 再根据已有的数据对整体进行补全
        for (RFinancingBalanceDTO dto : indirect) {
            //金融机构名称
            dto.setInstitutionName(fFinanceDeptDao.getById(dto.getInstitution()).getName());
            //先确定这笔融资是否有lpr
            if (dto.getInterestRatesWay().equals(1)){
//                List<FLprDTO> allLpr = fLprDao.getAllLpr();
                //在确定是一年期还是五年期
                if (dto.getLprInterestCycle() != null){
                    if (dto.getLprInterestCycle().equals(1)){
                        dto.setLprStr(ONE_YEAR_LPR);
                    }else {
                        dto.setLprStr(FIVE_YEAR_LPR);
                    }
                }
            }
            //设置融资种类
            dto.setFinancingType(2);
            //进行公共化信息补全
            RFinancingBalanceDTO dealInfo = this.publicDealInfo(dto);
            //克隆到dto
            BeanUtils.copyProperties(dealInfo,dto);
        }
        return indirect;
    }

    private List<RFinancingBalanceDTO> directListInfo(){
        //1 先去查大部分直融数据
        List<RFinancingBalanceDTO> direct = rFinancingBalanceDao.selectListFromDirectFinancing();
        //2 再遍历列表并补全信息
        for (RFinancingBalanceDTO dto : direct) {
            //进行公共化信息补全
            RFinancingBalanceDTO dealInfo = this.publicDealInfo(dto);
            //克隆到dto
            BeanUtils.copyProperties(dealInfo,dto);
            //设置融资种类
            dto.setFinancingType(1);
        }
        return direct;
    }

    private List<RFinancingBalanceDTO> governmentBondListInfo(){
        List<RFinancingBalanceDTO> governmentBond = rFinancingBalanceDao.selectListFromGovernmentBond();
        for (RFinancingBalanceDTO dto : governmentBond) {
            //进行公共化信息补全
            RFinancingBalanceDTO dealInfo = this.publicDealInfo(dto);
            //克隆到dto
            BeanUtils.copyProperties(dealInfo,dto);
            //设置融资种类
            dto.setFinancingType(3);

        }
        return governmentBond;
    }


    private RFinancingBalanceDTO publicDealInfo(RFinancingBalanceDTO dto){
        if (dto.getDeptName()==null){
            //融资主体名称
            dto.setDeptName(sysDeptDao.getById(dto.getDeptId()).getName());
        }
        //设置期限
        int i = (int) ((dto.getExpireDate().getTime() - dto.getLoanDate().getTime()) / MONTH_TIME);
        dto.setDeadLine((int) ((dto.getExpireDate().getTime() - dto.getLoanDate().getTime())/MONTH_TIME));
        //设置担保主体
        //是否包含保证
        if (dto.getCreditMeasures() != null && dto.getCreditMeasures().contains("2")){
            //不为空时
            if (!dto.getGuarantee().equals("[]")){
                //解析json字符串
                List< GuaranteeVO > gua = JSON.parseObject(dto.getGuarantee(),new TypeReference<List<GuaranteeVO>>(){});
                //获得担保人
                String guaStr = "";
                for (GuaranteeVO vo : gua) {
                    guaStr = guaStr + vo.getGuarantor();
                }
                //设置到dto
                if (dto.getGuaranteeBody()==null){
                    dto.setGuaranteeBody("");
                }
                dto.setGuaranteeBody(dto.getGuaranteeBody()+GUARANTEE_BONDSMAN+guaStr);
            }
        }
        return dto;
    }
}
