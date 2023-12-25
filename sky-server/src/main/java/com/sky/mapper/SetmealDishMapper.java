package com.sky.mapper;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface SetmealDishMapper {


    /**
     * 根据菜品ids查询套餐ids
     * @param ids
     * @return
     */

    List<Long> getSetmealIdByDishIds(List<Long> ids);
}
