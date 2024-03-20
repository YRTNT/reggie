package yrt.service.Impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import yrt.mapper.AddressBookMapper;
import yrt.pojo.AddressBook;
import yrt.service.AddressBookService;

@Service
public class AddressBookServicelmpl extends ServiceImpl<AddressBookMapper, AddressBook> implements AddressBookService {
}

