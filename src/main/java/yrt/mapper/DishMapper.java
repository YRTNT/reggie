package yrt.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import org.apache.ibatis.annotations.Mapper;
import yrt.pojo.Dish;

@Mapper
public interface DishMapper extends BaseMapper<Dish> {
}
