package com.sky.controller.admin;

import com.sky.dto.CategoryDTO;
import com.sky.dto.CategoryPageQueryDTO;
import com.sky.entity.Category;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.CategoryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.models.auth.In;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/category")
@Api(tags = "分类相关接口")
@Slf4j
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    /**
     * 新增分类
     * @param categoryDTO
     * @return
     */
    @PostMapping
    @ApiOperation("新增分类接口")
    public Result add(@RequestBody CategoryDTO categoryDTO){
        log.info("新增分类为:{}", categoryDTO);
        categoryService.add(categoryDTO);
        return Result.success();
    }

    /**
     * 分页查询
     * @param pageQueryDTO
     * @return
     */
    @GetMapping("/page")
    @ApiOperation("分页查询接口")
    public Result<PageResult> page(CategoryPageQueryDTO pageQueryDTO){
        log.info("查询的信息为:{}", pageQueryDTO);
        PageResult pageResult = categoryService.page(pageQueryDTO);
        return Result.success(pageResult);
    }

    /**
     * 修改分类信息
     * @param categoryDTO
     * @return
     */
    @PutMapping
    @ApiOperation("修改分类接口")
    public Result update(@RequestBody CategoryDTO categoryDTO){
        log.info("修改的分类信息为:{}", categoryDTO);
        categoryService.update(categoryDTO);
        return Result.success();
    }

    /**
     * 启用或禁用
     * @return
     */
    @PostMapping("/status/{status}")
    @ApiOperation("启用禁用接口")
    public Result startorstop(@PathVariable Integer status, Long id){
        log.info("启用禁用分类id为:{}, 状态为:{}", id, status);
        categoryService.startorstop(status, id);
        return Result.success();
    }

    /**
     * 根据id删除分类
     * @param id
     * @return
     */
    @DeleteMapping
    @ApiOperation("根据id删除分类接口")
    public Result deleteById(Long id){
        log.info("删除分类信息的id:{}",id);
        categoryService.deleteById(id);
        return Result.success();
    }

    /**
     * 根据类型查询分类
     * @return
     */
    @GetMapping("/list")
    @ApiOperation("根据类型查询分类接口")
    public Result<List<Category>> getByType(Integer type){
        log.info("查询的分类类型为:{}", type == 1 ? "菜品分类": "套餐分类");
        List<Category> categories = categoryService.getByType(type);
        return Result.success(categories);
    }
}
