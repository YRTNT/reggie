package yrt.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import yrt.pojo.Orders;

public interface OrderService extends IService<Orders> {
    void submit(Orders orders);
}

