package io.renren.modules.sys.controller;

import com.alibaba.excel.EasyExcel;
import io.renren.common.annotation.LogOperation;
import io.renren.common.constant.Constant;
import io.renren.common.page.PageData;
import io.renren.common.utils.ExcelDataListener;
import io.renren.common.utils.Result;
import io.renren.common.validator.AssertUtils;
import io.renren.common.validator.ValidatorUtils;
import io.renren.common.validator.group.AddGroup;
import io.renren.common.validator.group.DefaultGroup;
import io.renren.common.validator.group.UpdateGroup;
import io.renren.modules.sys.dto.SysSecComDTO;
import io.renren.modules.sys.excel.SysSecComExcel;
import io.renren.modules.sys.service.SysSecComService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import springfox.documentation.annotations.ApiIgnore;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/sys/seccom")
@Api(tags="证券公司")
public class SysSecComController {

    @Autowired
    private SysSecComService sysSecComService;

    @GetMapping("list")
    @ApiOperation("列表")
//	@RequiresPermissions("sys:dept:list")
    public Result<List<SysSecComDTO>> list(){
        List<SysSecComDTO> list = sysSecComService.list(new HashMap<>());

        return new Result<List<SysSecComDTO>>().ok(list);
    }

    @GetMapping("page")
    @ApiOperation("列表")
//	@RequiresPermissions("sys:dept:list")
    @ApiImplicitParams({
            @ApiImplicitParam(name = Constant.PAGE, value = "当前页码，从1开始", paramType = "query", required = true, dataType="int") ,
            @ApiImplicitParam(name = Constant.LIMIT, value = "每页显示记录数", paramType = "query",required = true, dataType="int") ,
            @ApiImplicitParam(name = Constant.ORDER_FIELD, value = "排序字段", paramType = "query", dataType="String") ,
            @ApiImplicitParam(name = Constant.ORDER, value = "排序方式，可选值(asc、desc)", paramType = "query", dataType="String") ,
    })
    public Result<PageData<SysSecComDTO>> page(@ApiIgnore @RequestParam Map<String, Object> params){
        PageData<SysSecComDTO> page = sysSecComService.page(params);
        return new Result<PageData<SysSecComDTO>>().ok(page);
    }

    @PostMapping
    @ApiOperation("保存")
    @LogOperation("保存")
//    @RequiresPermissions("sys:dept:save")
    public Result save(@RequestBody SysSecComDTO dto){
        //效验数据
        ValidatorUtils.validateEntity(dto, AddGroup.class, DefaultGroup.class);

        sysSecComService.save(dto);

        return new Result();
    }

    @PutMapping
    @ApiOperation("修改")
    @LogOperation("修改")
//    @RequiresPermissions("sys:dept:update")
    public Result update(@RequestBody SysSecComDTO dto){
        //效验数据
        ValidatorUtils.validateEntity(dto, UpdateGroup.class, DefaultGroup.class);

        sysSecComService.update(dto);

        return new Result();
    }

    @DeleteMapping
    @ApiOperation("删除")
    @LogOperation("删除")
//    @RequiresPermissions("sys:dept:delete")
    public Result delete(@RequestBody Long[] ids){
        //效验数据
        AssertUtils.isNull(ids, "id");

        sysSecComService.delete(ids);

        return new Result();
    }

    @PostMapping("import")
    @ApiOperation("导入")
//    @RequiresPermissions("demo:excel:all")
    @ApiImplicitParam(name = "file", value = "文件", paramType = "query", dataType="file")
    public Result importExcel(@RequestParam("file") MultipartFile file) throws Exception {
        //解析并保存到数据库
        EasyExcel.read(file.getInputStream(), SysSecComExcel.class,
                new ExcelDataListener<>(sysSecComService)).sheet().doRead();
        return new Result();
    }
}
