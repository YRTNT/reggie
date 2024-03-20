package yrt.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import yrt.pojo.User;

@Mapper
public interface UserMapper extends BaseMapper<User> {
}

