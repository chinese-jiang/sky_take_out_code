package com.sky.mapper;

import com.sky.entity.Setmeal;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface SetmealMapper {

    /**
     * 根据分类id删除套餐信息
     * @param categoryId
     * @return
     */
    @Select("delete from sky_take_out.setmeal where category_id = #{categoryId}")
    List<Setmeal> getByCategoryId(Long categoryId);
}
