package yrt.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;
import yrt.pojo.Employee;
import yrt.service.EmployeeService;
import yrt.utils.BaseContext;
import yrt.utils.R;

import java.time.LocalDateTime;

@Slf4j
@RestController
@RequestMapping("/employee")
public class EmployeeController {
    @Autowired
    private EmployeeService employeeService;

    /**
     * 登录方法
     * @param employee 接受json数据
     * @param request 如果登录成功，将员工对应的id存到session一份，这样想获取一份登录用户的信息就可以随时获取出来
     * @return
     */
    @PostMapping("/login")
    public R<Employee> login(@RequestBody Employee employee, HttpServletRequest request){

        //将页面提交的密码取出用md5加密算法处理
        String password = employee.getPassword();
        password = DigestUtils.md5DigestAsHex(password.getBytes());

        //根据页面提交的username查询数据库
        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Employee::getUsername,employee.getUsername());
        Employee emp = employeeService.getOne(queryWrapper);

        //查询失败
        if(emp==null){
            return R.error("用户名不匹配");
        }
        //密码错误
        if(!emp.getPassword().equals(password)){
            return R.error("密码错误");
        }
        //用户已禁用
        if(emp.getStatus()==0){
            return R.error("用户已禁用");
        }
        // 验证通过，将用户id存入session并返回登录成功
        request.getSession().setAttribute("employee",emp.getId());
        return R.success(emp);
    }

    /**
     * 员工登出
     * @param request 将存在session里的id删除
     * @return
     */
    @PostMapping("/logout")
    public R<String> logout(HttpServletRequest request){
        request.getSession().removeAttribute("employee");
        return R.success("退出成功");
    }

    /**
     * 管理员新增员工信息
     * @return
     */
    @PostMapping
    public R<String> save(@RequestBody Employee employee,HttpServletRequest request){
        //设置初始密码，并将密码加密
        employee.setPassword(DigestUtils.md5DigestAsHex("123456".getBytes()));

        employeeService.save(employee);
        return R.success("保存成功");
    }

    /**
     * 分页查询
     * @param page
     * @param pageSize
     * @param name
     * @return
     */
    @GetMapping("/page")
    public R<Page> page(int page,int pageSize,String name){
        //设置分页插件
        Page<Employee> pageInfo=new Page(page,pageSize);

        //添加查找条件
        //是否需要查找名字和按更新时间降序排序
        LambdaQueryWrapper<Employee> employeeLambdaQueryWrapper = new LambdaQueryWrapper<>();
        employeeLambdaQueryWrapper.like(name!=null,Employee::getName,name);
        employeeLambdaQueryWrapper.orderByDesc(Employee::getUpdateTime);

        //执行查询
        employeeService.page(pageInfo,employeeLambdaQueryWrapper);
        return R.success(pageInfo);
    }

    /**
     * 管理员设置用户是否可用
     * @param request
     * @param employee
     * @return
     */
    @PutMapping
    public R<String> update(HttpServletRequest request,@RequestBody Employee employee){

        employeeService.updateById(employee);

        return R.success("用户信息修改成功");
    }

    /**
     * 根据用户id查找用户
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public R<Employee> getById(@PathVariable Long id){
        Employee employee= employeeService.getById(id);
        if(employee!=null){
            return R.success(employee);
        }
        return R.error("没有查找到用户信息");
    }


}
