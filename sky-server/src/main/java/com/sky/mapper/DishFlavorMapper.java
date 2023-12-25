package com.sky.mapper;

import com.sky.entity.DishFlavor;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface DishFlavorMapper {

    /**
     * 批量插入口味数据
     * @param flavors
     */
    void batchInsert(List<DishFlavor> flavors);

    /**
     * 根据菜品id查询对应口味数据
     * @param DishId
     * @return
     */
    @Select("select * from sky_take_out.dish_flavor where dish_id = #{DishId}")
    List<DishFlavor> queryByDishId(Long DishId);

    /**
     * 批量删除
     * @param dishFlavors
     */
    void batchDelete(List<DishFlavor> dishFlavors);

    /**
     * 根据dishid批量删除
     * @param ids
     */
    void batchDeleteByDishId(List<Long> ids);
}
