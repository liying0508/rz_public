package io.renren.modules.financing.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.renren.common.service.impl.CrudServiceImpl;
import io.renren.modules.financing.dao.FCreditRecordDao;
import io.renren.modules.financing.dto.*;
import io.renren.modules.financing.entity.FCreditRecordEntity;
import io.renren.modules.financing.excel.FCreditRecordExcel;
import io.renren.modules.financing.service.FCreditRecordHistoryService;
import io.renren.modules.financing.service.FCreditRecordService;
import io.renren.modules.financing.util.MyExcelUtil;
import io.renren.modules.financing.util.QueryUtil;
import io.renren.modules.financing.util.UnitCastUtil;
import io.renren.modules.financing.vo.*;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;
/**
 * 银行授信
 *
 * @author liuwenyong 
 * @since 3.0 2022-05-14
 */
@Service
public class FCreditRecordServiceImpl
        extends CrudServiceImpl<FCreditRecordDao, FCreditRecordEntity, FCreditRecordDTO>
        implements FCreditRecordService {

    @Autowired
    private FCreditRecordDao fCreditRecordDao;
    @Autowired
    private FCreditRecordHistoryService fCreditRecordHistoryService;

    @Override
    public QueryWrapper<FCreditRecordEntity> getWrapper(Map<String, Object> params){
        QueryWrapper<FCreditRecordEntity> wrapper = new QueryWrapper<>();
        wrapper.like("quota",params.get("quota"));
        return wrapper;
    }

    @Override
    public List<FCreditPageDto> listInfo() {
        //查询部门列表
        List<FCreditPageDto> deptList = baseDao.getlist();
        //赋值未使用额度
        for (FCreditPageDto fCreditPageDto : deptList) {
            List<BigDecimal> bigDecimal = fCreditRecordDao.
                    getUsedQuota(fCreditPageDto.getDeptId(), fCreditPageDto.getInstitution());
            BigDecimal usedQuota = new BigDecimal(0);
            for (BigDecimal decimal : bigDecimal) {
                usedQuota = usedQuota.add(decimal);
            }
            fCreditPageDto.setIndirectQuota(usedQuota);
            fCreditPageDto.setUnUsedQuota(fCreditPageDto.getQuota().subtract(usedQuota));
        }
        return deptList;
    }

    @Override
    public List<FCreditPageDto> getList() {
        List<FCreditPageDto> getlist = fCreditRecordDao.getlist();
        return null;
    }

    @Override
    @Transactional
    public void saveInfo(FCreditRecordDTO dto) {
        dto.setId(null);
        //初始间接融资已使用额度为0
        dto.setIndirectQuota(BigDecimal.ZERO);
        //对增信措施的处理
        List<String> measuresList = dto.getCreditMeasuresList();
        boolean flag = CollectionUtils.isNotEmpty(measuresList);
        if (flag){
            dto.setCreditMeasures(StringUtils.join(measuresList,","));
        }
        //增信措施
        if (flag){
            CreditMeasuresVO info = dto.getGuaranteeInfo();
            //保证
            List<GuaranteeVO> guarantee = info.getGuarantee();
            String gua = JSONObject.toJSONString(guarantee);
            dto.setGuarantee(gua);
            //抵押
            List<CollateralVO> collateral = info.getCollateral();
            String col = JSONObject.toJSONString(collateral);
            dto.setCollateral(col);
            //质押
            List<PledgeVO> pledge = info.getPledge();
            String ple = JSONObject.toJSONString(pledge);
            dto.setPledge(ple);
        }
        //文件上传
        List<FileListVO> fileList = dto.getFileList();
        for (FileListVO fileListVO : fileList) {
            List<FileVO> files = fileListVO.getFiles();
            fileListVO.setFileStr(JSON.toJSONString(files));
            fileListVO.setFiles(null);
        }
        dto.setFiles(JSON.toJSONString(fileList));
        this.save(dto);

    }

    @Override
    @Transactional
    public void updateInfo(FCreditRecordDTO dto) {
        //文件列表
        if(dto.getFileList() == null){
            dto.setFiles(null);
        }else{
            List<FileListVO> fileList = dto.getFileList();
            for (FileListVO fileListVO : fileList) {
                List<FileVO> files = fileListVO.getFiles();
                fileListVO.setFileStr(JSON.toJSONString(files));
                fileListVO.setFiles(null);
            }
            dto.setFiles(JSON.toJSONString(fileList));
        }
        this.update(dto);
    }

    @Override
    public FCreditRecordDTO getDealData(Long id) {
        //增信措施详情
        FCreditRecordDTO dealData = fCreditRecordDao.getDealData(id);
        String guarantee = dealData.getGuarantee();
        String pledge = dealData.getPledge();
        String collateral = dealData.getCollateral();
        List<GuaranteeVO> gua = JSON.parseObject(guarantee,new TypeReference<List<GuaranteeVO>>(){});
        List<PledgeVO> ple = JSON.parseObject(pledge,new TypeReference<List<PledgeVO>>(){});
        List<CollateralVO> col = JSON.parseObject(collateral,new TypeReference<List<CollateralVO>>(){});
        CreditMeasuresVO creditMeasuresVO = new CreditMeasuresVO();
        creditMeasuresVO.setCollateral(col);
        creditMeasuresVO.setGuarantee(gua);
        creditMeasuresVO.setPledge(ple);
        dealData.setGuaranteeInfo(creditMeasuresVO);

        //文件列表
        String files = dealData.getFiles();
        if (files != null){
            List<FileListVO> fileListVOs = JSON.parseObject(files,new TypeReference<List<FileListVO>>(){});
            for (FileListVO fileListVO : fileListVOs) {
                String fileStr = fileListVO.getFileStr();
                List<FileVO> fileVOs = JSON.parseObject(fileStr,new TypeReference<List<FileVO>>(){});
                fileListVO.setFiles(fileVOs);
            }
            dealData.setFileList(fileListVOs);
        }
        return dealData;
    }

    @Override
    public List<FCreditPageDto> screenList(List<FCreditPageDto> list,String deptName,String creditMeasures,
                                           String quota, Integer unit,Integer isChecked) {
        if(unit == 2){
            for (FCreditPageDto fCreditPageDto : list) {
                fCreditPageDto.setQuota(UnitCastUtil.divider(fCreditPageDto.getQuota()));
                fCreditPageDto.setIndirectQuota(UnitCastUtil.divider(fCreditPageDto.getIndirectQuota()));
                fCreditPageDto.setUnUsedQuota(UnitCastUtil.divider(fCreditPageDto.getUnUsedQuota()));
            }
        }
        list.removeIf(next -> !QueryUtil.judgeConditionCreditRecord(next, deptName, creditMeasures, quota));
        if (isChecked != null){
            list.removeIf(next -> next.getIsChecked()!=isChecked);
        }
        return list;
    }

    @Override
    public List<FCreditPageDto> screenCheckList(List<FCreditPageDto> list, Integer groupChecked) {
        if (groupChecked != null){
            list.removeIf(next -> next.getGroupChecked()!=groupChecked);
        }
        return list;
    }

    @Override
    public FCreditRecordDTO directInfoCast(FCreditRecordDTO dto, Integer unit) {
        if (unit == 2){
            dto.setQuota(UnitCastUtil.divider(dto.getQuota()));
        }
        return dto;
    }

    @Override
    public FCreditRecordDTO directSaveCast(FCreditRecordDTO dto, Integer unit) {
        if (unit == 2){
            dto.setQuota(UnitCastUtil.multiplication(dto.getQuota()));
        }
        return dto;
    }

    @Override
    public List<FCreditRecordExcel> getExportData(List<FCreditPageDto> list) {
        List<FCreditRecordExcel> excels = new ArrayList<>();
        for (FCreditPageDto dto : list) {
            FCreditRecordExcel excel = new FCreditRecordExcel();
            BeanUtils.copyProperties(dto,excel);
            FCreditRecordExcel excel1 = MyExcelUtil.creditRecordExport(dto, excel);
            excels.add(excel1);
        }

        return excels;
    }

    @Override
    public void clearFCreditRecord() {
        List<FCreditRecordDTO> pendingData = new ArrayList<>();
        List<FCreditRecordDTO> allData = fCreditRecordDao.getAllData();
        for (FCreditRecordDTO data : allData) {
            if (data.getEndDate().getTime() < new Date().getTime()){
                pendingData.add(data);
            }
        }
        for (FCreditRecordDTO data : pendingData) {
            fCreditRecordHistoryService.save(data);
            this.deleteById(data.getId());
        }
    }
}