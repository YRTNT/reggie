package yrt.service.Impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import yrt.mapper.UserMapper;
import yrt.pojo.User;
import yrt.service.UserService;

@Service
public class UserServicelmpl extends ServiceImpl<UserMapper, User> implements UserService {
}

