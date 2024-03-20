package yrt.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.transaction.annotation.Transactional;
import yrt.dto.SetmealDto;
import yrt.pojo.Setmeal;

import java.util.List;


public interface SetmealService extends IService<Setmeal> {
    @Transactional
    void saveWithDish(SetmealDto setmealDto);

    @Transactional
    void removeWithDish(List<Long> ids);
}

