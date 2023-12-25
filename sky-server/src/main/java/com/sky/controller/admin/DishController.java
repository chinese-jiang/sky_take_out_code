package com.sky.controller.admin;

import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.DishService;
import com.sky.vo.DishVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Delete;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@Api(tags = "菜品相关接口")
@RequestMapping("/admin/dish")
public class DishController {

    @Autowired
    private DishService dishService;

    /**
     * 新增菜品
     * @param dishDTO
     * @return
     */
    @PostMapping
    @ApiOperation("新增菜品接口")
    public Result add(@RequestBody DishDTO dishDTO){
        dishService.add(dishDTO);
        return Result.success();
    }

    @GetMapping("/page")
    @ApiOperation("分页查询接口")
    public Result<PageResult> page(DishPageQueryDTO dishPageQueryDTO){
        log.info("分页查询的信息为:{}", dishPageQueryDTO);
        PageResult pageResult = dishService.page(dishPageQueryDTO);
        return Result.success(pageResult);
    }

    /**
     * 修改菜品
     * @param dishDTO
     * @return
     */
    @PutMapping
    @ApiOperation("修改菜品接口")
    public Result update(@RequestBody DishDTO dishDTO){
        log.info("修改菜品信息为:{}", dishDTO);
        dishService.update(dishDTO);
        return Result.success();
    }

    /**
     * 根据id查询菜品
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    @ApiOperation("根据id查询菜品接口")
    public Result<DishVO> queryById(@PathVariable Long id){
        log.info("查询回显的菜品id:{}", id);
        DishVO dishVO = dishService.queryById(id);
        return Result.success(dishVO);
    }

    /**
     * 修改状态
     * @param status
     * @return
     */
    @PostMapping("status/{status}")
    @ApiOperation("修改状态接口")
    public Result startorstop(@PathVariable Integer status, Long id){
        log.info("修改菜品id:{}, 状态:{}", id, status);
        dishService.startorstop(status,id);
        return Result.success();
    }

    /**
     * 删除菜品
     * @param ids
     * @return
     */
    @DeleteMapping()
    @ApiOperation("删除菜品接口")
    public Result delete(@RequestParam List<Long> ids){
        log.info("删除菜品id:{}", ids);
        dishService.delete(ids);
        return Result.success();
    }

    /**
     * 根据分类id查询菜品
     * @return
     */
    @GetMapping("/list")
    @ApiOperation("根据分类id查询菜品接口")
    public Result<List<Dish>> queryByCategoryId(Long categoryId){
        log.info("查询的分类id为:{}", categoryId);
        List<Dish> dishes = dishService.queryByCategoryId(categoryId);
        return Result.success(dishes);
    }
}
