package yrt.service;

import com.baomidou.mybatisplus.extension.service.IService;
import yrt.dto.DishDto;
import yrt.pojo.Dish;

public interface DishService extends IService<Dish> {
    public void saveWithFlavor(DishDto dishDto);

    public DishDto getByIdWithFlavor(Long id);

    void updateWithFlavor(DishDto dishDto);
}

