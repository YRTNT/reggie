package yrt.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import yrt.pojo.Employee;

@Mapper
public interface EmployeeMapper extends BaseMapper<Employee> {
}
