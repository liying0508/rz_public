package io.renren.modules.financing.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.renren.common.service.impl.CrudServiceImpl;
import io.renren.common.utils.ConvertUtils;
import io.renren.modules.financing.dao.*;
import io.renren.modules.financing.dto.*;
import io.renren.modules.financing.entity.FRepaymentInfo;
import io.renren.modules.financing.entity.FRepaymentInfoHistory;
import io.renren.modules.financing.entity.FRepaymentInfoTableEntity;
import io.renren.modules.financing.entity.FRepaymentInfoTableHistoryEntity;
import io.renren.modules.financing.excel.FRepaymentInfoExcel;
import io.renren.modules.financing.service.*;
import io.renren.modules.financing.util.MyExcelUtil;
import io.renren.modules.financing.util.QueryUtil;
import io.renren.modules.financing.util.RepayUtil;
import io.renren.modules.financing.util.UnitCastUtil;
import io.renren.modules.financing.dto.RepaymentInfoDTO;
import io.renren.modules.financing.vo.RepaymentInfoVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;

@Service
public class FRepaymentInfoServiceImpl
        extends CrudServiceImpl<FRepaymentInfoDao, FRepaymentInfo, FRepaymentInfoDTO>
        implements FRepaymentInfoService {

    @Autowired
    private FRepaymentInfoDao fRepaymentInfoDao;

    @Autowired
    private RepaymentInfoService repaymentInfoService;

    @Autowired
    private FRepaymentInfoCheckService fRepaymentInfoCheckService;

    @Autowired
    private FGovernmentBondDao fGovernmentBondDao;

    @Autowired
    private FDirectFinancingDao fDirectFinancingDao;

    @Autowired
    private FIndirectFinancingDao fIndirectFinancingDao;

    @Autowired
    private FRepaymentInfoHistoryService fRepaymentInfoHistoryService;

    @Autowired
    private RepaymentInfoHistoryService repaymentInfoHistoryService;

    @Override
    public QueryWrapper<FRepaymentInfo> getWrapper(Map<String, Object> params) {
        QueryWrapper<FRepaymentInfo> wrapper = new QueryWrapper<>();
        return wrapper;
    }

    /**
     * 分页列表
     * @return List<FRepaymentInfoPageDTO>
     */
    @Override
    public List<FRepaymentInfoPageDTO> listInfo() {
//        FRepaymentInfoPageDTO fRepaymentInfoPageDTO = new FRepaymentInfoPageDTO();
        //政府债券的数据
        List<FRepaymentInfoPageDTO> government = fRepaymentInfoDao.getGovernment();
        for (FRepaymentInfoPageDTO repaymentInfoPageDTO : government) {
            repaymentInfoPageDTO.setFinancingType(0);
//            FRepaymentInfoDTO repaymentInfo = fRepaymentInfoDao.
//                    getRepaymentInfo(repaymentInfoPageDTO.getFinancingId(), 0);
        }
        //直融的数据
        List<FRepaymentInfoPageDTO> direct = fRepaymentInfoDao.getDirect();
        for (FRepaymentInfoPageDTO repaymentInfoPageDTO : direct) {
            repaymentInfoPageDTO.setFinancingType(1);
//            FRepaymentInfoDTO repaymentInfo = fRepaymentInfoDao.
//                    getRepaymentInfo(repaymentInfoPageDTO.getFinancingId(), 1);
        }
        //间融的数据
        List<FRepaymentInfoPageDTO> indirect = fRepaymentInfoDao.getIndirect();
        for (FRepaymentInfoPageDTO repaymentInfoPageDTO : indirect) {
            repaymentInfoPageDTO.setFinancingType(2);
//            FRepaymentInfoDTO repaymentInfo = fRepaymentInfoDao.
//                    getRepaymentInfo(repaymentInfoPageDTO.getFinancingId(), 2);
        }
        government.addAll(direct);
        government.addAll(indirect);
        return government;
    }

    /**
     * 根据融资id获得对应信息并处理
     * @param financingId 融资单id
     * @return FRepaymentInfoDTO
     */
    @Override
    public FRepaymentInfoDTO getDealData(Long financingId,Integer financingType) {
        //现在数据库中查出基础的数据
        FRepaymentInfoDTO dealData = fRepaymentInfoDao.getRepaymentInfo(financingId,financingType);
        //设置repayment
        //1 还息列表
        List<RepaymentInfoDTO> repayInterestInfoList = new ArrayList<>();
        //2 还本列表
        List<RepaymentInfoDTO> repayPrincipalInfoList = new ArrayList<>();
        //3 从数据库里根据融资单id和融资单类型查出所有的还款信息
        List<RepaymentInfoDTO> repaymentInfoDTOS = repaymentInfoService.selectData(financingId, financingType);
        //4 当上一步有查到数据时
        if (repaymentInfoDTOS != null){
            //5 遍历还款列表，根据融资信息单类型来分类
            for (RepaymentInfoDTO repaymentInfoDTO : repaymentInfoDTOS) {
                if (repaymentInfoDTO.getInfoType() == 2){
                    repayInterestInfoList.add(repaymentInfoDTO);
                }else {
                    repayPrincipalInfoList.add(repaymentInfoDTO);
                }
            }
            //6 新建一个RepaymentInfoVO来存储两种类型的列表
            RepaymentInfoVO repaymentInfoVO = new RepaymentInfoVO(repayInterestInfoList,repayPrincipalInfoList);
            //7 存储进基础数据
            dealData.setRepaymentInfo(repaymentInfoVO);
        }
        return dealData;
    }

    /**
     * 更新数据
     * @param dto
     */
    @Override
    public void updateInfo(FRepaymentInfoDTO dto) {
        //单位处理
        Integer unit = dto.getUnit();
        if (unit == 2){
            FRepaymentInfoDTO fRepaymentInfoDTO = this.unitSaveCast(dto, unit);
            dto = fRepaymentInfoDTO;
        }
        //先更新还款信息列表
        //得到请求里的融资id
        Long financingId = dto.getFinancingId();
        //得到请求里的融资种类
        Integer financingType = dto.getFinancingType();
        //删除还款信息表里的对应数据
        repaymentInfoService.delData(financingId,financingType);
        //得到请求里的还款信息列表
        RepaymentInfoVO repaymentInfo = dto.getRepaymentInfo();
        //把还款信息列表里的还本列表和还息列表分离
        List<RepaymentInfoDTO> repayInterestInfoList = repaymentInfo.getRepayInterestInfoList();
        List<RepaymentInfoDTO> repayPrincipalInfoList = repaymentInfo.getRepayPrincipalInfoList();
        //当还息计划不为空时
        //将融资类型和融资id设置好，然后逐条存入f_repayment_info_table表中
        if (repayInterestInfoList != null){
            for (RepaymentInfoDTO repaymentInfoDTO : repayInterestInfoList) {
                repaymentInfoDTO.setFinancingType(financingType);
                repaymentInfoDTO.setFinancingId(financingId);
                repaymentInfoDTO.setInfoType(2);
                repaymentInfoService.save(repaymentInfoDTO);
            }
        }
        if (repayPrincipalInfoList != null){
            for (RepaymentInfoDTO repaymentInfoDTO : repayPrincipalInfoList) {
                repaymentInfoDTO.setFinancingType(financingType);
                repaymentInfoDTO.setFinancingId(financingId);
                repaymentInfoDTO.setInfoType(1);
                repaymentInfoService.save(repaymentInfoDTO);
            }
        }
        //将其他的数据保存进还款信息表
        this.update(dto);
    }

    @Override
    public void check(FRepaymentInfoCheckDTO dto) {
        FRepaymentInfo fRepaymentInfo= baseDao.selectById(dto.getRepaymentId());
        System.out.println(fRepaymentInfo.toString());
        FRepaymentInfoDTO fRepaymentInfoDTO= ConvertUtils.
                sourceToTarget(fRepaymentInfo, FRepaymentInfoDTO.class);
        fRepaymentInfoDTO.setIsChecked(dto.getChecked());
        fRepaymentInfoDTO.setCheckedDes(dto.getCheckedDes());
        this.update(fRepaymentInfoDTO);
        fRepaymentInfoCheckService.save(dto);
    }

    @Override
    public void groupCheck(FRepaymentInfoCheckDTO dto) {
        FRepaymentInfo fRepaymentInfo= baseDao.selectById(dto.getRepaymentId());

        System.out.println(fRepaymentInfo.toString());
        FRepaymentInfoDTO fRepaymentInfoDTO= ConvertUtils.
                sourceToTarget(fRepaymentInfo, FRepaymentInfoDTO.class);
        fRepaymentInfoDTO.setGroupChecked(dto.getGroupChecked());
        //根据融资类型和融资id更新各个融资单的实际还款金额
        Long financingId = fRepaymentInfoDTO.getFinancingId();
        Integer financingType = fRepaymentInfoDTO.getFinancingType();
        //判断该次审核是否为通过
        if (fRepaymentInfoDTO.getGroupChecked() == 1){
            //当集团审核通过时
            //先设置该条还款信息的实际还款金额
            fRepaymentInfoDTO.setActualRepayment(fRepaymentInfoDTO.getRepaymentMoney());
            //融资余额
            BigDecimal financingBalance = fRepaymentInfoDTO.getPrincipal().subtract(fRepaymentInfoDTO.getRepaymentMoney());
            //判断是什么类型（0政府债券，1直接融资，2间接融资）
            if (financingType == 0){
                //更新政府债券表的实际还款金额
                fGovernmentBondDao.updateActualRepaymentById(financingId,fRepaymentInfoDTO.getRepaymentMoney());
                //更新融资余额
                fGovernmentBondDao.updateFinancingBalanceById(financingId,financingBalance);
            }else if (financingType == 1){
                //更新直融表的实际还款金额
                fDirectFinancingDao.updateActualRepaymentById(financingId,fRepaymentInfoDTO.getRepaymentMoney());
                //更新融资余额
                fDirectFinancingDao.updateFinancingBalanceById(financingId,financingBalance);
            }else if (financingType == 2){
                //更新间融表的实际还款金额
                fIndirectFinancingDao.updateActualRepaymentById(financingId,fRepaymentInfoDTO.getRepaymentMoney());
                //更新融资余额
                fIndirectFinancingDao.updateFinancingBalanceById(financingId,financingBalance);
            }
        }else{
            //全部设置成0
            BigDecimal zero = new BigDecimal(0);
            //先设置该条还款信息的实际还款金额
            fRepaymentInfo.setActualRepayment(zero);
            //融资余额
            BigDecimal financingBalance = fRepaymentInfoDTO.getPrincipal();
            //判断是什么类型（0政府债券，1直接融资，2间接融资）
            if (financingType == 0){
                //更新政府债券表的实际还款金额
                fGovernmentBondDao.updateActualRepaymentById(financingId,zero);
                //更新融资余额
                fGovernmentBondDao.updateFinancingBalanceById(financingId,financingBalance);
            }else if (financingType == 1){
                //更新直融表的实际还款金额
                fDirectFinancingDao.updateActualRepaymentById(financingId,zero);
                //更新融资余额
                fDirectFinancingDao.updateFinancingBalanceById(financingId,financingBalance);
            }else if (financingType == 2){
                //更新间融表的实际还款金额
                fIndirectFinancingDao.updateActualRepaymentById(financingId,zero);
                //更新融资余额
                fIndirectFinancingDao.updateFinancingBalanceById(financingId,financingBalance);
            }
        }
        fRepaymentInfoDTO.setGroupCheckedDes(dto.getGroupCheckedDes());
        this.update(fRepaymentInfoDTO);
        fRepaymentInfoCheckService.save(dto);
    }

    @Override
    public void initFRepaymentInfo(List<FRepaymentInfoPageDTO> fRepaymentInfoPageDTOList) {
        //遍历进来的数据
        for (FRepaymentInfoPageDTO pageDto : fRepaymentInfoPageDTOList) {
            //新建一个FRepaymentInfoDTO对象
            FRepaymentInfoDTO dto = new FRepaymentInfoDTO();
            //设置本金
            dto.setPrincipal(pageDto.getTotalAmount());
            //设置融资单id
            dto.setFinancingId(pageDto.getFinancingId());
            //设置融资类型
            dto.setFinancingType(pageDto.getFinancingType());
            //初始化审核状态
            dto.setIsChecked(0);
            //根据id和类型查询数据库中是否已经有该条融资的还款信息记录
            FRepaymentInfoDTO repaymentInfo = fRepaymentInfoDao.getRepaymentInfo(dto.getFinancingId(), dto.getFinancingType());
            if(repaymentInfo == null){
                //如果数据库里面没有那就保存
                this.save(dto);
            }else{
                //如果数据库中已有该条融资信息
                //设置审核状态和审核意见
                dto.setIsChecked(repaymentInfo.getIsChecked());
                dto.setCheckedDes(repaymentInfo.getCheckedDes());
                //更新数据库中的该条记录
                this.update(dto);
            }
        }

    }

    @Override
    public List<FRepaymentInfoPageDTO> addStatus(List<FRepaymentInfoPageDTO> list) {
        for (FRepaymentInfoPageDTO dto : list) {
            FRepaymentInfoDTO repaymentInfo = fRepaymentInfoDao.
                    getRepaymentInfo(dto.getFinancingId(), dto.getFinancingType());
            //审核状态
            dto.setIsChecked(repaymentInfo.getIsChecked());
            //集团审核状态
            dto.setGroupChecked(repaymentInfo.getGroupChecked());
            if(dto.getIsChecked() == 1 && dto.getGroupChecked() == 1){
                //已还本金，利息和未还本金
//                String repaymentList = repaymentInfo.getRepaymentList();
                Long financingId = dto.getFinancingId();
                Integer financingType = dto.getFinancingType();
                List<RepaymentInfoDTO> repaymentInfoDTOS = repaymentInfoService.selectData(financingId, financingType);
                if(repaymentInfoDTOS == null){
                    dto.setPrincipalRepay("0");
                    dto.setInterestRepay("0");
                    dto.setOutstandingPrincipal(dto.getTotalAmount().toString());
                }else{
                    BigDecimal repayPrincipal = new BigDecimal(0);
                    BigDecimal repayInterest = new BigDecimal(0);
                    for (RepaymentInfoDTO repaymentInfoDTO : repaymentInfoDTOS) {
                        BigDecimal principal = repaymentInfoDTO.getAlreadyRepayPrincipal();
                        BigDecimal interest = repaymentInfoDTO.getAlreadyRepayInterest();
                        if (principal== null){
                            principal = new BigDecimal("0");
                        }
                        if (interest == null){
                            interest = new BigDecimal("0");
                        }
                        repayPrincipal = repayPrincipal.add(principal);
                        repayInterest = repayInterest.add(interest);
                    }
                    dto.setPrincipalRepay(repayPrincipal.toString());
                    dto.setInterestRepay(repayInterest.toString());
                    BigDecimal outPrincipal = dto.getTotalAmount().subtract(repayPrincipal);
                    dto.setOutstandingPrincipal(outPrincipal.toString());
                }
            }else{
                dto.setPrincipalRepay("0");
                dto.setInterestRepay("0");
                dto.setOutstandingPrincipal(dto.getTotalAmount().toString());
            }

        }
        return list;
    }

    @Override
    public List<FRepaymentInfoPageDTO> pageScreen(List<FRepaymentInfoPageDTO> list,Map<String,Object> params,
                                                  Integer isChecked,Integer groupChecked) {
        //得到融资单位
        String deptName = params.get("deptName").toString();
        //得到金融机构
        String institutionName = params.get("institutionName").toString();
        //得到融资品种
        String varieties = params.get("varieties").toString();
        //根据条件筛选
        list.removeIf(next -> !QueryUtil.judgeConditionRepaymentInfo(next, deptName,institutionName,varieties));

        //都为空时就是管理模块
        if (isChecked == null){
            if (groupChecked != null){
                //此情况为集团审核模块
                //把子公司审核情况为审核中和审核未通过的移除
                list.removeIf(next -> next.getIsChecked() != 1);
                if (groupChecked != 5){
                    //此情况为集团审核审核状态条件不为空时
                    list.removeIf(next -> next.getGroupChecked() != groupChecked);
                }
            }
        }else if (isChecked != 5){
            //此情况为子公司审核模块筛选条件不为空时
            list.removeIf(next -> next.getIsChecked() != isChecked);
        }
        return list;
    }

    @Override
    public List<FRepaymentInfoPageDTO> unitCast(List<FRepaymentInfoPageDTO> fRepaymentInfoPageDTOS,Integer unit) {
        if(unit == 2){
            for (FRepaymentInfoPageDTO fRepaymentInfoPageDTO : fRepaymentInfoPageDTOS) {
                //融资总金额
                fRepaymentInfoPageDTO.setTotalAmount(UnitCastUtil.divider(fRepaymentInfoPageDTO.getTotalAmount()));
                //已还本金
                fRepaymentInfoPageDTO.setPrincipalRepay(UnitCastUtil.divider(new BigDecimal(fRepaymentInfoPageDTO
                        .getPrincipalRepay())).toString());
                //已还本金
                fRepaymentInfoPageDTO.setOutstandingPrincipal(UnitCastUtil.divider(new BigDecimal(fRepaymentInfoPageDTO
                        .getOutstandingPrincipal())).toString());
            }
        }
        return fRepaymentInfoPageDTOS;
    }

    @Override
    public FRepaymentInfoDTO unitInfoCast(FRepaymentInfoDTO dto, Integer unit) {
        if (unit == 2){
            //总本金
            dto.setPrincipal(UnitCastUtil.divider(dto.getPrincipal()));
            //还本计划列表
            List<RepaymentInfoDTO> repayPrincipalInfoList = dto.getRepaymentInfo().getRepayPrincipalInfoList();
            if (repayPrincipalInfoList != null){
                for (RepaymentInfoDTO repaymentInfoDTO : repayPrincipalInfoList) {
                    repaymentInfoDTO.setAlreadyRepayPrincipal(UnitCastUtil.divider(repaymentInfoDTO.getAlreadyRepayPrincipal()));
                }
            }
        }
        return dto;
    }

    @Override
    public FRepaymentInfoDTO unitSaveCast(FRepaymentInfoDTO dto, Integer unit) {
        if (unit == 2){
            //总本金
            dto.setPrincipal(UnitCastUtil.multiplication(dto.getPrincipal()));
            //还款计划列表
            List<RepaymentInfoDTO> repayPrincipalInfoList = dto.getRepaymentInfo().getRepayPrincipalInfoList();
            if (repayPrincipalInfoList != null){
                for (RepaymentInfoDTO repaymentInfoDTO : repayPrincipalInfoList) {
                    repaymentInfoDTO.setAlreadyRepayPrincipal(UnitCastUtil.multiplication(repaymentInfoDTO.getAlreadyRepayPrincipal()));
                }
            }
        }
        return dto;
    }

    @Override
    public void initRepaymentInfo() {

    }

    @Override
    public void clearFRepaymentInfo(List<FRepaymentInfoPageDTO> fRepaymentInfoPageDTOList) {
        List<FRepaymentInfoDTO> newList = new ArrayList<>();
        //遍历进来的数据
        for (FRepaymentInfoPageDTO pageDto : fRepaymentInfoPageDTOList) {
            //新建一个FRepaymentInfoDTO对象
            FRepaymentInfoDTO dto = new FRepaymentInfoDTO();
            //设置本金
            dto.setPrincipal(pageDto.getTotalAmount());
            //设置融资单id
            dto.setFinancingId(pageDto.getFinancingId());
            //设置融资类型
            dto.setFinancingType(pageDto.getFinancingType());
            //初始化审核状态
            dto.setIsChecked(0);
            newList.add(dto);
        }
        //先查出所有的数据库内的数据
        List<FRepaymentInfoDTO> list = this.list(new HashMap<>());
        //初始化需要移到历史表内的数据的主键的列表
        List<Long> idList = new ArrayList<>();
        List<FRepaymentInfoDTO> oldList = new ArrayList<>();
        //遍历list对比newList
        for (FRepaymentInfoDTO dto : list) {
            //判断newList是否包含这个对象
            boolean b = RepayUtil.containsTypeAndId(newList, dto);
            if (!b){
                //不包含就把他添加进idList
                idList.add(dto.getFinancingId());
                oldList.add(dto);
            }
        }
        //根据oldList依次把数据放进历史表内
        for (FRepaymentInfoDTO dto : oldList) {
            FRepaymentInfoHistory fRepaymentInfoHistory = fRepaymentInfoHistoryService.selectById(dto.getId());
            if (fRepaymentInfoHistory != null) {
                fRepaymentInfoHistoryService.update(dto);
            } else {
                FRepaymentInfoHistory history = new FRepaymentInfoHistory();
                BeanUtils.copyProperties(dto, history);
                fRepaymentInfoHistoryService.insert(history);
            }
        }
        //TODO
        //还要删除原来数据库里的数据
        this.clearRepaymentInfo(oldList);
    }

    @Override
    public void clearRepaymentInfo(List<FRepaymentInfoDTO> oldList) {
        //1 遍历oldlist
        for (FRepaymentInfoDTO dto : oldList) {
            //2 分别得到融资单类型，融资单id
            Integer financingType = dto.getFinancingType();
            Long financingId = dto.getFinancingId();
            //3 根据上一步得到的两个参数查出现在的还款信息表内的相关数据
            List<RepaymentInfoDTO> repaymentInfoDTOS = repaymentInfoService.selectData(financingId, financingType);
            //4 如果已有数据
            if (repaymentInfoDTOS != null){
                //5 遍历上一步查出来的还款列表数据
                for (RepaymentInfoDTO repaymentInfoDTO : repaymentInfoDTOS) {
                    //6 新建一个RepaymentInfoHistoryDTO并把原始表的对象复制进新的对象
                    RepaymentInfoHistoryDTO repaymentInfoHistoryDTO = new RepaymentInfoHistoryDTO();
                    BeanUtils.copyProperties(repaymentInfoDTO,repaymentInfoHistoryDTO);
                    //7 再查询历史表中是否存在该条还款记录
                    List<RepaymentInfoHistoryDTO> repaymentInfoHistoryDTOS =
                            (List<RepaymentInfoHistoryDTO>) repaymentInfoHistoryService.selectById(repaymentInfoHistoryDTO.getId());
                    //8 如果已经存在就更新，不存在就存储进去
                    if (repaymentInfoHistoryDTOS != null){
                        repaymentInfoHistoryService.save(repaymentInfoHistoryDTO);
                    }else {
                        repaymentInfoHistoryService.update(repaymentInfoHistoryDTO);
                    }
                }
            }
        }
    }

    @Override
    public List<FRepaymentInfoExcel> getExportData(List<FRepaymentInfoPageDTO> list) {
        List<FRepaymentInfoExcel> excelList = new ArrayList<>();
        for (FRepaymentInfoPageDTO dto : list) {
            FRepaymentInfoExcel excel = new FRepaymentInfoExcel();
            BeanUtils.copyProperties(dto,excel);
            MyExcelUtil.fRepaymentInfoExport(dto,excel);
            excelList.add(excel);
        }
        return excelList;
    }


}
