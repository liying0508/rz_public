/**
 * Copyright (c) 2018 人人开源 All rights reserved.
 *
 * https://www.renren.io
 *
 * 版权所有，侵权必究！
 */

package io.renren.modules.financing.controller;

import io.renren.common.annotation.LogOperation;
import io.renren.common.utils.Result;
import io.renren.common.validator.AssertUtils;
import io.renren.common.validator.ValidatorUtils;
import io.renren.common.validator.group.AddGroup;
import io.renren.common.validator.group.DefaultGroup;
import io.renren.common.validator.group.UpdateGroup;
import io.renren.modules.financing.dto.FFinanceDeptDTO;
import io.renren.modules.financing.service.FFinanceDeptService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

/**
 * 部门管理
 * 
 * @author Mark sunlightcs@gmail.com
 */
@RestController
@RequestMapping("/financing/finance-dept")
@Api(tags="部门管理")
public class FFinanceDeptController {
	@Autowired
	private FFinanceDeptService financeDeptService;

	@GetMapping("list")
	@ApiOperation("列表")
//	@RequiresPermissions("financing:finance-dept:list")
	public Result<List<FFinanceDeptDTO>> list(){
		List<FFinanceDeptDTO> list = financeDeptService.list(new HashMap<>(1));
		return new Result<List<FFinanceDeptDTO>>().ok(list);
	}

	@GetMapping("{id}")
	@ApiOperation("信息")
//	@RequiresPermissions("financing:finance-dept:info")
	public Result<FFinanceDeptDTO> get(@PathVariable("id") Long id){
		FFinanceDeptDTO data = financeDeptService.get(id);
		return new Result<FFinanceDeptDTO>().ok(data);
	}

	@PostMapping
	@ApiOperation("保存")
	@LogOperation("保存")
//	@RequiresPermissions("financing:finance-dept:save")
	public Result save(@RequestBody FFinanceDeptDTO dto){
		//效验数据
		ValidatorUtils.validateEntity(dto, AddGroup.class, DefaultGroup.class);
		financeDeptService.save(dto);
		return new Result();
	}

	@PutMapping
	@ApiOperation("修改")
	@LogOperation("修改")
//	@RequiresPermissions("financing:finance-dept:update")
	public Result update(@RequestBody FFinanceDeptDTO dto){
		//效验数据
		ValidatorUtils.validateEntity(dto, UpdateGroup.class, DefaultGroup.class);
		financeDeptService.update(dto);
		return new Result();
	}

	@DeleteMapping("{id}")
	@ApiOperation("删除")
	@LogOperation("删除")
//	@RequiresPermissions("financing:finance-dept:delete")
	public Result delete(@PathVariable("id") Long id){
		//效验数据
		AssertUtils.isNull(id, "id");
		financeDeptService.delete(id);
		return new Result();
	}
	
}