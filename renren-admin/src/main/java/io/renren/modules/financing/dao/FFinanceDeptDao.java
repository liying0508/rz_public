/**
 * Copyright (c) 2018 人人开源 All rights reserved.
 *
 * https://www.renren.io
 *
 * 版权所有，侵权必究！
 */

package io.renren.modules.financing.dao;

import io.renren.common.dao.BaseDao;
import io.renren.modules.financing.dto.FIndirectDeptDTO;
import io.renren.modules.financing.entity.FFinanceDeptEntity;
import org.apache.ibatis.annotations.Mapper;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * 部门管理
 * 
 * @author Mark sunlightcs@gmail.com
 */
@Mapper
public interface FFinanceDeptDao extends BaseDao<FFinanceDeptEntity> {

    List<FFinanceDeptEntity> getList(Map<String, Object> params);

    FFinanceDeptEntity getById(Long id);

    /**
     * 获取所有部门的id、pid列表
     */
    List<FFinanceDeptEntity> getIdAndPidList();

    /**
     * 根据部门ID，获取所有子部门ID列表
     * @param id   部门ID
     */
    List<Long> getSubDeptIdList(String id);

    FIndirectDeptDTO getDept(Long deptId,Long institution);

    String getDeptName(String deptId);

    String getInstitutionName(Long id);

    List<String> getAllName();

    String getIdByName(String name);

    BigDecimal getApprovalAmount(String approveNo);
}