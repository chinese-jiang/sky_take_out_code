package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.MessageConstant;
import com.sky.constant.StatusConstant;
import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.entity.DishFlavor;
import com.sky.exception.DeletionNotAllowedException;
import com.sky.mapper.DishFlavorMapper;
import com.sky.mapper.DishMapper;
import com.sky.mapper.SetmealDishMapper;
import com.sky.result.PageResult;
import com.sky.service.DishService;
import com.sky.vo.DishVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.beans.Transient;
import java.util.List;

@Service
public class DishServiceImpl implements DishService {


    @Autowired
    private DishMapper dishMapper;

    @Autowired
    private DishFlavorMapper dishFlavorMapper;

    @Autowired
    private SetmealDishMapper setmealDishMapper;
    /**
     * 新增菜品
     * @param dishDTO
     */
    @Transient
    public void add(DishDTO dishDTO) {
        Dish dish = new Dish();
        BeanUtils.copyProperties(dishDTO,dish);
        dishMapper.insert(dish);

        // 获得该菜品id
        Long dishId = dish.getId();

        // 获取口味数据
        List<DishFlavor> flavors = dishDTO.getFlavors();
        if(flavors != null && flavors.size() > 0)
        {
            // 插入口味(批量插入)
            for (DishFlavor dishFlavor : flavors) {
                dishFlavor.setDishId(dishId);
            }
            dishFlavorMapper.batchInsert(flavors);
        }
    }

    /**
     * 分页查询
     * @param dishPageQueryDTO
     * @return
     */
    public PageResult page(DishPageQueryDTO dishPageQueryDTO) {

        PageHelper.startPage(dishPageQueryDTO.getPage(), dishPageQueryDTO.getPageSize());

        Page<Dish> dishPage = dishMapper.list(dishPageQueryDTO);

        return new PageResult(dishPage.getTotal(), dishPage.getResult());
    }

    /**
     * 根据菜品id查询菜品
     * @param id
     * @return
     */
    @Transient
    public DishVO queryById(Long id) {
        DishVO dishVO = new DishVO();
        // 根据id查询菜品
        Dish dish = dishMapper.queryById(id);
        // 根据菜品id查询口味数据
        List<DishFlavor> dishFlavors = dishFlavorMapper.queryByDishId(id);
        if(dishFlavors != null && dishFlavors.size() > 0)
        {
            dishVO.setFlavors(dishFlavors);
        }
        BeanUtils.copyProperties(dish, dishVO);
        // 返回DishVO
        return dishVO;
    }

    /**
     * 修改菜品信息
     * @param dishDTO
     */
    @Transient
    public void update(DishDTO dishDTO) {
        Dish dish = new Dish();
        BeanUtils.copyProperties(dishDTO, dish);
        // 1. 修改菜品信息
        dishMapper.update(dish);
        // 先查询菜品口味
        List<DishFlavor> dishFlavors = dishFlavorMapper.queryByDishId(dishDTO.getId());
        if(dishFlavors != null && dishFlavors.size() > 0){
            // 批量删除
            dishFlavorMapper.batchDelete(dishFlavors);
        }


        // 2. 修改菜品口味信息
        List<DishFlavor> dishFlavors2 = dishDTO.getFlavors();
        for (DishFlavor flavor : dishFlavors2) {
            flavor.setDishId(dishDTO.getId());
        }
        // 先删除原有口味，再插入
        if(dishFlavors2 != null && dishFlavors2.size() > 0)
        {
            dishFlavorMapper.batchInsert(dishFlavors2);
        }

    }

    /**
     * 修改状态
     * @param status
     */
    public void startorstop(Integer status, Long id) {
        Dish dish = new Dish();
        dish.setId(id);
        dish.setStatus(status);
        // TODO 菜品停售后关联的套餐也要停售
        dishMapper.update(dish);
    }

    /**
     * 删除菜品
     * @param ids
     */
    @Transient
    public void delete(List<Long> ids) {
        // 如果该菜品状态是起售状态不能删除
        for (Long id : ids) {
            // 查询菜品起售状态
            Dish dish = dishMapper.queryById(id);
            if (dish.getStatus() == StatusConstant.ENABLE)
            {
                throw new DeletionNotAllowedException(MessageConstant.DISH_ON_SALE);
            }
        }
        // 如果该菜品关联套餐，不能删除
        List<Long> setmealIds = setmealDishMapper.getSetmealIdByDishIds(ids);
        if(setmealIds != null && setmealIds.size() > 0)
        {
            throw new DeletionNotAllowedException(MessageConstant.DISH_BE_RELATED_BY_SETMEAL);
        }
        // 删除菜品数据
        dishMapper.batchDelete(ids);
        // 删除口味数据
        dishFlavorMapper.batchDeleteByDishId(ids);
    }

    /**
     * 根据分类id查询菜品
     * @param categoryId
     * @return
     */
    public List<Dish> queryByCategoryId(Long categoryId) {
        List<Dish> dishes = dishMapper.queryByCategoryId(categoryId);
        return dishes;
    }
}
