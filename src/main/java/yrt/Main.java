package yrt;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.cache.annotation.EnableCaching;

@ServletComponentScan
@SpringBootApplication
@EnableCaching  //开启缓存注解功能
public class Main {
    public static void main(String[] args) {
        SpringApplication.run(Main.class,args);
    }
}