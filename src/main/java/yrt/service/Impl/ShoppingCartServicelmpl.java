package yrt.service.Impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import yrt.mapper.ShoppingCartMapper;
import yrt.pojo.ShoppingCart;
import yrt.service.ShoppingCartService;

@Service
public class ShoppingCartServicelmpl extends ServiceImpl<ShoppingCartMapper, ShoppingCart> implements ShoppingCartService {
}

