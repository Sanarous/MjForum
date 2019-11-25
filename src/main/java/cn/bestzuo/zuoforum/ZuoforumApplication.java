package cn.bestzuo.zuoforum;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@SpringBootApplication(scanBasePackages = "cn.bestzuo.zuoforum")
@MapperScan(basePackages = {"cn.bestzuo.zuoforum.mapper"})
@EnableTransactionManagement
public class ZuoforumApplication {

    public static void main(String[] args) {
        SpringApplication.run(ZuoforumApplication.class, args);
    }

}
