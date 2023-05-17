package io.renren.modules.financing.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.renren.common.exception.ErrorCode;
import io.renren.common.exception.RenException;
import io.renren.common.service.impl.CrudServiceImpl;
import io.renren.common.utils.ConvertUtils;
import io.renren.modules.financing.dao.FDictDao;
import io.renren.modules.financing.dao.FDirectFinancingApprovalDao;
import io.renren.modules.financing.dao.FDirectFinancingDao;
import io.renren.modules.financing.dto.FDirectFinancingApprovalCheckDTO;
import io.renren.modules.financing.dto.FDirectFinancingApprovalDTO;
import io.renren.modules.financing.entity.FDirectFinancingApproval;
import io.renren.modules.financing.excel.FDirectFinancingApprovalExcel;
import io.renren.modules.financing.service.FDirectFinancingApprovalCheckService;
import io.renren.modules.financing.service.FDirectFinancingApprovalService;
import io.renren.modules.financing.service.FDirectFinancingService;
import io.renren.modules.financing.util.UnitCastUtil;
import io.renren.modules.financing.vo.FileListVO;
import io.renren.modules.financing.vo.FileVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @Author gaby
 * @Date 2022/6/27 18:25
 * @Description
 * @Version 1.0
 * @ClassName FDirectFinancingApprovalServiceImpl
 **/
@Service
public class  FDirectFinancingApprovalServiceImpl extends
        CrudServiceImpl<FDirectFinancingApprovalDao, FDirectFinancingApproval, FDirectFinancingApprovalDTO>
        implements FDirectFinancingApprovalService {

    @Autowired
    FDirectFinancingService fDirectFinancingService;

    @Autowired
    FDirectFinancingApprovalDao fDirectFinancingApprovalDao;

    @Autowired
    FDirectFinancingApprovalCheckService fDirectFinancingApprovalCheckService;

    @Autowired
    FDirectFinancingDao fDirectFinancingDao;

    @Autowired
    FDictDao fDictDao;

    @Override
    public QueryWrapper<FDirectFinancingApproval> getWrapper(Map<String, Object> params) {
        QueryWrapper<FDirectFinancingApproval> wrapper = new QueryWrapper<>();
        wrapper.like("approve_org",params.get("approveOrg"));
        wrapper.like("approve_no",params.get("approveNo"));
        wrapper.like("issue_quota",params.get("issueQuota"));
        if (params.get("isChecked") != null && !params.get("isChecked").equals("")){
            wrapper.eq("is_checked",params.get("isChecked"));
        }
        if (params.get("groupChecked") != null){
            wrapper.eq("is_checked","1");
            if (!params.get("groupChecked").equals("")){
                wrapper.eq("group_checked",params.get("groupChecked"));
            }
        }
        return wrapper;
    }

    @Override
    public List<FDirectFinancingApprovalDTO> listInfo() {
        //查询部门列表
        List<FDirectFinancingApprovalDTO> list = baseDao.getlist();
        list.forEach(f->{
            HashMap map= fDirectFinancingService.getData(f.getApproveNo());
            if(Objects.nonNull(map)){
                if(map.containsKey("issue")) {
                    f.setIssue(((Long) map.get("issue")).intValue());
                }
            }
        });

        return list;
    }

    @Override
    @Transactional
    public void saveInfo(FDirectFinancingApprovalDTO dto) {
        dto.setId(null);
        //判断是否有批文号重复
        FDirectFinancingApprovalDTO fDirectFinancingApprovalDTO= baseDao.getInfoByApprovalNo(dto.getApproveNo());
        if(Objects.nonNull(fDirectFinancingApprovalDTO)){
            throw new RenException(ErrorCode.APPROVAL_NO_EXI_INFO);
        }
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
    public void updateInfo(FDirectFinancingApprovalDTO dto) {
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
    public void check(FDirectFinancingApprovalCheckDTO dto) {
        FDirectFinancingApproval fDirectFinancingApproval= baseDao.selectById(dto.getApprovalId());
        FDirectFinancingApprovalDTO fDirectFinancingApprovalDTO =
                ConvertUtils.sourceToTarget(fDirectFinancingApproval, FDirectFinancingApprovalDTO.class);
        fDirectFinancingApprovalDTO.setIsChecked(dto.getChecked());
        fDirectFinancingApprovalDTO.setCheckedDes(dto.getCheckedDes());
        this.update(fDirectFinancingApprovalDTO);

        fDirectFinancingApprovalCheckService.save(dto);
    }

    @Override
    public void groupCheck(FDirectFinancingApprovalCheckDTO dto) {
        FDirectFinancingApproval fDirectFinancingApproval= baseDao.selectById(dto.getApprovalId());
        FDirectFinancingApprovalDTO fDirectFinancingApprovalDTO =
                ConvertUtils.sourceToTarget(fDirectFinancingApproval, FDirectFinancingApprovalDTO.class);
        fDirectFinancingApprovalDTO.setGroupChecked(dto.getGroupChecked());
        fDirectFinancingApprovalDTO.setGroupCheckedDes(dto.getGroupCheckedDes());
        this.update(fDirectFinancingApprovalDTO);

        fDirectFinancingApprovalCheckService.save(dto);
    }

    @Override
    public FDirectFinancingApprovalDTO getDealData(Long id) {
        FDirectFinancingApprovalDTO dealData = fDirectFinancingApprovalDao.getDealData(id);
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
    public FDirectFinancingApprovalDTO directInfoCast(FDirectFinancingApprovalDTO dto, Integer unit) {
        if (unit == 2){
            dto.setIssueQuota(UnitCastUtil.divider(dto.getIssueQuota()));
        }
        return dto;
    }

    @Override
    public FDirectFinancingApprovalDTO directSaveCast(FDirectFinancingApprovalDTO dto, Integer unit) {
        if (unit == 2){
            dto.setIssueQuota(UnitCastUtil.multiplication(dto.getIssueQuota()));
        }
        return dto;
    }

    @Override
    public List<FDirectFinancingApprovalExcel> getExportData(List<FDirectFinancingApprovalDTO> list, String approvalAmount) {
        return null;
    }

    @Override
    @Transactional
    public void deleteInfo(Long[] ids){
        delete(ids);
    }
}
