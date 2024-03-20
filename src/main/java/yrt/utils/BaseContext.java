package yrt.utils;

/**
 * 客户端发送的每次http请求，对应的在服务端都会分配一个新的线程来处理，在处理过程中涉及到下面类中的方法都属于相同的一个线程:
 * LoginCheckFilter的doFilter方法
 * EmployeeController的update方法
 * MyMetaObjectHandler的updateFill方法
 *
 * ThreadLocal常用方法:
 * public void set(T value) 设置当前线程的线程局部变量的值
 * public T get() 返回当前线程所对应的线程局部变量的值
 */
public class BaseContext {
    private static ThreadLocal<Long> threadLocal = new ThreadLocal<>();

    public static void setCurrentId(Long id){
        threadLocal.set(id);
    }

    public static Long getCurrentId(){
        return threadLocal.get();
    }
}
