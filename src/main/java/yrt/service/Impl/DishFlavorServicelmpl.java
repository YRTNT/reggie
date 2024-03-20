package yrt.service.Impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import yrt.mapper.DishFlavorMapper;
import yrt.pojo.DishFlavor;
import yrt.service.DishFlavorService;

@Service
public class DishFlavorServicelmpl extends ServiceImpl<DishFlavorMapper, DishFlavor> implements DishFlavorService {
}

