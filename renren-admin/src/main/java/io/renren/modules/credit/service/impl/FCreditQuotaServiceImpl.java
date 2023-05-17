package io.renren.modules.credit.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.renren.common.exception.ErrorCode;
import io.renren.common.exception.RenException;
import io.renren.common.service.impl.CrudServiceImpl;
import io.renren.common.utils.DateUtils;
import io.renren.modules.credit.dao.FCreditQuotaDao;
import io.renren.modules.credit.dao.FCreditQuotaHistoryDao;
import io.renren.modules.credit.dto.FCreditQuotaDTO;
import io.renren.modules.credit.dto.FCreditQuotaHistoryDTO;
import io.renren.modules.credit.entity.FCreditQuotaEntity;
import io.renren.modules.credit.entity.FCreditQuotaHistoryEntity;
import io.renren.modules.credit.service.FCreditQuotaHistoryService;
import io.renren.modules.credit.service.FCreditQuotaService;
import io.renren.modules.financing.dao.FDirectFinancingDao;
import io.renren.modules.financing.dao.FFinanceDeptDao;
import io.renren.modules.financing.dao.FIndirectFinancingDao;
import io.renren.modules.financing.vo.FileListVO;
import io.renren.modules.financing.vo.FileVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class FCreditQuotaServiceImpl extends CrudServiceImpl<FCreditQuotaDao, FCreditQuotaEntity, FCreditQuotaDTO>
        implements FCreditQuotaService {
    @Autowired
    FFinanceDeptDao fFinanceDeptDao;

    @Autowired
    FCreditQuotaHistoryService fCreditQuotaHistoryService;

    @Autowired
    FCreditQuotaHistoryDao fCreditQuotaHistoryDao;

    @Autowired
    FCreditQuotaDao fCreditQuotaDao;

    @Autowired
    FIndirectFinancingDao fIndirectFinancingDao;

    @Autowired
    FDirectFinancingDao fDirectFinancingDao;

    @Override
    public QueryWrapper<FCreditQuotaEntity> getWrapper(Map<String, Object> params) {
        return null;
    }

    /**
     * 根据银行id来更新已使用额度
     * @param institution 金融机构id
     */
    public void updateUsedQuotaByInstitution(Long institution){
        //先去统计间融表内该id的金融机构的已使用额度
        BigDecimal usedQuotaIndirect = fIndirectFinancingDao.getUsedQuotaByInstitutionId(institution);
        if (usedQuotaIndirect == null || usedQuotaIndirect.equals("")){
            usedQuotaIndirect = new BigDecimal(0);
        }
        //直融的也统计好
        //TODO 这里需要沟通直融占用授信额度的问题
//        BigDecimal usedQuotaDirect = fDirectFinancingDao.getUsedQuotaByInstitutionId(institution);
//        if (usedQuotaDirect == null || usedQuotaDirect.equals("")){
//            usedQuotaDirect = new BigDecimal(0);
//        }
        //然后分别更新授信总额表内的两个对应值
        fCreditQuotaDao.updateUsedQuotaByInstitutionId(institution,new BigDecimal(0),usedQuotaIndirect);
    }

    /**
     * 更新所有数据库中已有的已使用额度
     */
    private void initAllUsedQuota(){
        List<Long> allInstitution = fCreditQuotaDao.selectAllData()
                .stream()
                .map(dto -> dto.getInstitution())
                .collect(Collectors.toList());
        //遍历，把每一个都更新已使用额度
        allInstitution.forEach(this::updateUsedQuotaByInstitution);
    }

    @Override
    public List<FCreditQuotaDTO> listInfo(Map<String, Object> params) {
        //先判断目前的月份和传进来的参数信息

        Date month = null;
        try {
            month = DateUtils.parse(params.get("month").toString(), DateUtils.DATE_PATTERN);
        } catch (Exception e) {
            month = new Date();
        }
        int year = month.getYear()+1900;
        int mon = month.getMonth()+1;
        if (year == new Date().getYear()+1900){
            if (mon == new Date().getMonth()+1){
                //先更新所有的已使用额度
                initAllUsedQuota();
                //获取数据
                List<FCreditQuotaDTO> list = fCreditQuotaDao.selectAllData();
                dealCreditQuotaList(list);
                return list;
            }
        }
        //去数据库里查询
        List<FCreditQuotaHistoryEntity> historyEntities =
                fCreditQuotaHistoryDao.selectList(new QueryWrapper<FCreditQuotaHistoryEntity>()
                        .like("create_date", year + "-" + mon));
        List<FCreditQuotaDTO> list = new ArrayList<>();
        historyEntities.forEach(dto ->{
            FCreditQuotaDTO dto1 = new FCreditQuotaDTO();
            BeanUtils.copyProperties(dto,dto1);
            dto1.setId(dto.getCreditQuotaId());
            list.add(dto1);
        });
        dealCreditQuotaList(list);
        return list;
    }

    private void dealCreditQuotaList(List<FCreditQuotaDTO> list) {
        list.forEach(dto -> {
            //设置金融机构名字
            dto.setInstitutionName(fFinanceDeptDao.getById(dto.getInstitution()).getName());
            //设置直融未使用额度
            dto.setUnusedQuotaDirect(dto.getCreditQuotaDirect().subtract(dto.getUsedQuotaDirect()));
            //设置间融未使用额度
            dto.setUnusedQuotaIndirect(dto.getCreditQuotaIndirect().subtract(dto.getUsedQuotaIndirect()));
        });
    }

//    @Override
//    public List<FCreditQuotaDTO> pageDeal(List<FCreditQuotaDTO> list) {
//        for (FCreditQuotaDTO dto : list) {
//            //设置金融机构名字
//            dto.setInstitutionName(fFinanceDeptDao.getById(dto.getInstitution()).getName());
//            //设置直融未使用额度
//            dto.setUnusedQuotaDirect(dto.getCreditQuotaDirect().subtract(dto.getUsedQuotaDirect()));
//            //设置间融未使用额度
//            dto.setUnusedQuotaIndirect(dto.getCreditQuotaIndirect().subtract(dto.getUsedQuotaIndirect()));
//        }
//        return list;
//    }

    @Override
    public FCreditQuotaDTO dtoDeal(Long id) {
        FCreditQuotaDTO dto = this.get(id);
        //设置金融机构名字
        dto.setInstitutionName(fFinanceDeptDao.getById(dto.getInstitution()).getName());
        //文件设置
        String files = dto.getFiles();
        if (files != null){
            List<FileListVO> fileListVOs = JSON.parseObject(files,new TypeReference<List<FileListVO>>(){});
            fileListVOs.forEach(fileListVO -> {
                String fileStr = fileListVO.getFileStr();
                List<FileVO> fileVOs = JSON.parseObject(fileStr, new TypeReference<List<FileVO>>() {});
                fileListVO.setFiles(fileVOs);
            });
            dto.setFileList(fileListVOs);
        }
        return dto;
    }

    @Override
    public void saveInfo(FCreditQuotaDTO dto) {
        //如果这个银行已经存在，抛出异常
        if (fCreditQuotaDao.countInstitutionById(dto.getInstitution()) > 1){
            throw new RenException(ErrorCode.CREDIT_QUOTA_ERROR_INSTITUTION_IS_REPETITIVE);
        }
        //文件列表
        List<FileListVO> fileList = dto.getFileList();
        if (fileList != null && !fileList.equals("")) {
            fileList.forEach(fileListVO -> {
                List<FileVO> files = fileListVO.getFiles();
                fileListVO.setFileStr(JSON.toJSONString(files));
                fileListVO.setFiles(null);
            });
            dto.setFiles(JSON.toJSONString(fileList));
        }
        this.save(dto);
    }


    @Override
    public void updateInfo(FCreditQuotaDTO dto) {
        //文件列表
        if(dto.getFileList() == null){
            dto.setFiles(null);
        }else{
            List<FileListVO> fileList = dto.getFileList();
            fileList.forEach(fileListVO -> {
                List<FileVO> files = fileListVO.getFiles();
                fileListVO.setFileStr(JSON.toJSONString(files));
                fileListVO.setFiles(null);
            });
            dto.setFiles(JSON.toJSONString(fileList));
        }
        this.update(dto);
    }


    @Override
    public void deleteAllInfo(Long[] ids) {
        //先去历史表里删除
        Arrays.stream(ids)
                .forEach(id -> fCreditQuotaHistoryDao
                .delete(new LambdaQueryWrapper<FCreditQuotaHistoryEntity>()
                        .eq(FCreditQuotaHistoryEntity::getCreditQuotaId, id)));
        this.delete(ids);
    }

    @Override
    public void generateSnapshots(List<FCreditQuotaDTO> creditQuotaDTOList) {
        creditQuotaDTOList.forEach(dto -> {
            FCreditQuotaHistoryDTO historyDTO = new FCreditQuotaHistoryDTO();
            BeanUtils.copyProperties(dto,historyDTO);
            historyDTO.setCreditQuotaId(historyDTO.getId());
            historyDTO.setId(null);
            //保存到历史表
            fCreditQuotaHistoryService.save(historyDTO);
        });
    }
}