package yrt.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import yrt.pojo.Orders;

@Mapper
public interface OrderMapper extends BaseMapper<Orders> {
}

