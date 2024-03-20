package yrt.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.sql.SQLIntegrityConstraintViolationException;

/**
 * 全局异常处理器
 */
@ControllerAdvice(annotations = {RestController.class, Controller.class})
@ResponseBody  //java对象转为json格式的数据
@Slf4j
public class GlobalExceptionHandler {

    /**
     * 我们在新增员工时输入的账号已经存在，由于employee表中对该字段加入了唯一约束，此时程序会抛出异常:
     * java.sql. SQLIntegrityConstraintViolationException: Duplicate entry 'zhangsan’for key 'idx_username
     * @param exception
     * @return
     */
    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    public R<String> exceptionHandler (SQLIntegrityConstraintViolationException exception){
        log.error(exception.getMessage());

        //如果异常信息是Duplicate entry 'zhangsan’for key 'idx_username则处理
        if (exception.getMessage().contains("Duplicate entry")){
            String[] split = exception.getMessage().split(" ");
            String msg = split[2] + "已存在";
            return R.error(msg);
        }

        return R.error("failed");
    }

    /**
     * 全局异常处理增加自定义异常
     * @param exception
     * @return
     */
    @ExceptionHandler(CustomException.class)
    public R<String> exceptionHandler (CustomException exception){
        log.error(exception.getMessage());

        return R.error(exception.getMessage());
    }
}
