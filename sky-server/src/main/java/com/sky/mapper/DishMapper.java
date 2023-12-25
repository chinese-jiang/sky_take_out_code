package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.annotation.AutoFill;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.enumeration.OperationType;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface DishMapper {

    /**
     * 根据分类id查询菜品信息
     * @param categoryId
     * @return
     */
    @Select("select * from sky_take_out.dish where category_id = #{categoryId}")
    List<Dish> getByCategoryId(Long categoryId);

    /**
     * 新增一个菜品
     * @param dish
     */
    @AutoFill(value = OperationType.INSERT)
    void insert(Dish dish);

    /**
     * 分页查询
     * @param dishPageQueryDTO
     * @return
     */
    Page<Dish> list(DishPageQueryDTO dishPageQueryDTO);

    /**
     * 根据主键id查询菜品
     * @param id
     * @return
     */
    @Select("select * from sky_take_out.dish where id = #{id}")
    Dish queryById(Long id);

    /**
     * 修改菜品信息
     * @param dish
     */
    @AutoFill(value = OperationType.UPDATE)
    void update(Dish dish);

    /**
     * 批量删除
     * @param ids
     */
    void batchDelete(List<Long> ids);

    /**
     * 根据分类id查询菜品
     * @param categoryId
     * @return
     */
    @Select("select * from sky_take_out.dish where category_id = #{categoryId}")
    List<Dish> queryByCategoryId(Long categoryId);
}
