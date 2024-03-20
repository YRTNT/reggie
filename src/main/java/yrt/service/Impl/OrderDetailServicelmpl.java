package yrt.service.Impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import yrt.mapper.OrderDetailMapper;
import yrt.pojo.OrderDetail;
import yrt.service.OrderDetailService;

@Service
public class OrderDetailServicelmpl extends ServiceImpl<OrderDetailMapper, OrderDetail> implements OrderDetailService {
}

