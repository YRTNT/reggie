package yrt.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import yrt.pojo.AddressBook;

@Mapper
public interface AddressBookMapper extends BaseMapper<AddressBook> {
}

