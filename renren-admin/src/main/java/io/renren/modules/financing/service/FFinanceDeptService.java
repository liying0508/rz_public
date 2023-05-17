/**
 * Copyright (c) 2018 人人开源 All rights reserved.
 *
 * https://www.renren.io
 *
 * 版权所有，侵权必究！
 */

package io.renren.modules.financing.service;

import io.renren.common.service.BaseService;
import io.renren.modules.financing.dto.FDirectFinancingDTO;
import io.renren.modules.financing.dto.FFinanceDeptDTO;
import io.renren.modules.financing.dto.FIndirectDeptDTO;
import io.renren.modules.financing.dto.FIndirectFinancingDTO;
import io.renren.modules.financing.entity.FFinanceDeptEntity;

import java.util.List;
import java.util.Map;

/**
 * 部门管理
 * 
 * @author Mark sunlightcs@gmail.com
 */
public interface FFinanceDeptService extends BaseService<FFinanceDeptEntity> {

	List<FFinanceDeptDTO> list(Map<String, Object> params);

	FFinanceDeptDTO get(Long id);

	void save(FFinanceDeptDTO dto);

	void update(FFinanceDeptDTO dto);

	void delete(Long id);

	/**
	 * 根据部门ID，获取本部门及子部门ID列表
	 * @param id   部门ID
	 */
	List<Long> getSubDeptIdList(Long id);

	FIndirectDeptDTO getDept(FIndirectFinancingDTO fIndirectFinancingDTO);

	String getDeptName(String deptId);

	String getInstitutionNameById(Long id);

}