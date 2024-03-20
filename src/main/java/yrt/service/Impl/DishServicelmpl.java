package yrt.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import yrt.dto.DishDto;
import yrt.mapper.DishMapper;
import yrt.pojo.Dish;
import yrt.pojo.DishFlavor;
import yrt.service.DishFlavorService;
import yrt.service.DishService;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DishServicelmpl extends ServiceImpl<DishMapper, Dish> implements DishService {
    @Autowired
    private DishFlavorService dishFlavorService;

    /**
     * 将dishdto的数据分别保存在两张表中
     * @param dishDto
     */
    public void saveWithFlavor(DishDto dishDto){
        //保存在dish表中
        this.save(dishDto);

        //mp保存数据后生成的id会回显到实体类中
        Long id = dishDto.getId();

        //将每条flavor的dishid都赋值
        List<DishFlavor> flavors = dishDto.getFlavors();
        for(DishFlavor flavor:flavors){
            flavor.setDishId(id);
        }

        //保存菜品口味数据到菜品口味表
        dishFlavorService.saveBatch(flavors);
    }

    @Override
    public DishDto getByIdWithFlavor(Long id) {
        //通过id查询菜品基本信息
        Dish dish = super.getById(id);

        //创建dto对象
        DishDto dishDto = new DishDto();

        //对象拷贝
        BeanUtils.copyProperties(dish,dishDto);

        //条件查询flavor
        LambdaQueryWrapper<DishFlavor> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(DishFlavor::getDishId,dish.getId());
        List<DishFlavor> list = dishFlavorService.list(queryWrapper);

        //将查询到的flavor赋值到dto对象中
        dishDto.setFlavors(list);

        return dishDto;
    }

    @Override
    public void updateWithFlavor(DishDto dishDto) {
        //根据id修改菜品的基本信息
        super.updateById(dishDto);

        //通过dish_id,删除菜品的flavor
        LambdaQueryWrapper<DishFlavor> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(DishFlavor::getDishId,dishDto.getId());
        dishFlavorService.remove(queryWrapper);

        //获取前端提交的flavor数据
        List<DishFlavor> flavors = dishDto.getFlavors();

        //将每条flavor的dishId赋值
        for(DishFlavor flavor:flavors){
            flavor.setDishId(dishDto.getId());
        }

        //将数据批量保存到dish_flavor数据库
        dishFlavorService.saveBatch(flavors);
    }


}

