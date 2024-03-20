package yrt.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;
import yrt.dto.DishDto;
import yrt.pojo.Category;
import yrt.pojo.Dish;
import yrt.pojo.DishFlavor;
import yrt.service.CategoryService;
import yrt.service.DishFlavorService;
import yrt.service.DishService;
import yrt.utils.R;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/dish")
@Slf4j
public class DishController {
    @Autowired
    private DishService dishService;

    @Autowired
    private DishFlavorService dishFlavorService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private RedisTemplate redisTemplate;

    @PostMapping
    public R<String> save(@RequestBody DishDto dishDto){
        log.info("接收的dishDto数据：{}",dishDto.toString());
        //保存数据到数据库
        dishService.saveWithFlavor(dishDto);

        //新增菜品时，删除改分类下的缓存数据
        String key = "dish_" + dishDto.getCategoryId() + "_1";
        redisTemplate.delete(key);
        return R.success("新增菜品成功");
    }


    /**
     * 菜品分页查询
     * @param page
     * @param pageSize
     * @param name
     * @return
     */
    @GetMapping("/page")
    public R<Page> page(int page, int pageSize, String name){
        //构造分页构造器对象
        Page<Dish> pageInfo = new Page<>(page,pageSize);
        //条件构造器
        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();
        //添加条件
        queryWrapper.like(name != null,Dish::getName,name);
        queryWrapper.orderByDesc(Dish::getUpdateTime);
        //执行分页查询
        dishService.page(pageInfo,queryWrapper);

        //dish实体类里没有categoryName只有categoryId，dishdto里有categoryname
        Page<DishDto> dishDtoPage = new Page<>();
        //拷贝page,records是忽略字段
        BeanUtils.copyProperties(pageInfo,dishDtoPage,"records");
        //将records里的cateName查询并填上
        List<Dish> records = pageInfo.getRecords();
        List<DishDto> dishDtos = new ArrayList<>();
        for(Dish dish:records){
            DishDto dishDto = new DishDto();
            BeanUtils.copyProperties(dish,dishDto);
            Category category = categoryService.getById(dish.getCategoryId());
            if(category!=null){
                dishDto.setCategoryName(category.getName());
            }
            else {
                dishDto.setCategoryName("不知道");
            }
            dishDtos.add(dishDto);
        }
        dishDtoPage.setRecords(dishDtos);
        return R.success(dishDtoPage);
    }

    /**
     * 修改菜品页面数据回显
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public R<DishDto> get(@PathVariable Long id){
        //查询
        DishDto dishDto = dishService.getByIdWithFlavor(id);

        return R.success(dishDto);
    }

    /**
     * 修改菜品信息
     * @param dishDto
     * @return
     */
    @PutMapping
    public R<String> update(@RequestBody DishDto dishDto){

        log.info("接收的dishDto数据：{}",dishDto.toString());

        //更新数据库中的数据
        dishService.updateWithFlavor(dishDto);

        //删除缓存数据
        String key = "dish_" + dishDto.getCategoryId() + "_1";
        redisTemplate.delete(key);
        return R.success("新增菜品成功");
    }

    /**
     * 查找对应分类的菜品
     * @param dish
     * @return
     */
    @GetMapping("/list")
    public R<List<DishDto>> list(Dish dish){
        //先查找redis有没有缓存
        List<DishDto> dishDtoList;
        String key = "dish_" + dish.getCategoryId() + "_" + dish.getStatus();
        dishDtoList = (List<DishDto>) redisTemplate.opsForValue().get(key);
        //如果有，则直接返回
            if (dishDtoList != null){
            return R.success(dishDtoList);
        }
        //如果无，则查询

        //构造查询条件
        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(dish.getCategoryId() != null, Dish::getCategoryId, dish.getCategoryId());
        //添加条件，查询状态为1（1为起售，0为停售）的菜品
        queryWrapper.eq(Dish::getStatus,1);

        List<Dish> list = dishService.list(queryWrapper);

        dishDtoList = list.stream().map((item) -> {
            DishDto dishDto = new DishDto();
            //对象拷贝（每一个list数据）
            BeanUtils.copyProperties(item,dishDto);
            Long categoryId = item.getCategoryId();  //分类id
            //通过categoryId查询到category内容
            Category category = categoryService.getById(categoryId);
            //判空
            if(category != null){
                String categoryName = category.getName();
                dishDto.setCategoryName(categoryName);
            }

            //获取当前菜品id
            Long dishId = item.getId();

            //构造条件构造器
            LambdaQueryWrapper<DishFlavor> dishFlavorLambdaQueryWrapper= new LambdaQueryWrapper<>();
            //添加查询条件
            dishFlavorLambdaQueryWrapper.eq(dishId != null,DishFlavor::getDishId,dishId);
            //select * from dish_flavors where dish_id = ?
            List<DishFlavor> dishFlavors = dishFlavorService.list(dishFlavorLambdaQueryWrapper);

            dishDto.setFlavors(dishFlavors);

            return dishDto;
        }).collect(Collectors.toList());

        //将查询到的数据放入redis缓存，设置60分钟
        redisTemplate.opsForValue().set(key,dishDtoList,60, TimeUnit.MINUTES);
        return R.success(dishDtoList);
    }


}

