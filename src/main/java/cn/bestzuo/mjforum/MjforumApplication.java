package cn.bestzuo.mjforum;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication(scanBasePackages = "cn.bestzuo.mjforum")
@MapperScan(basePackages = {"cn.bestzuo.mjforum.mapper","cn.bestzuo.mjforum.admin.mapper"})
@EnableTransactionManagement
@EnableCaching
public class MjforumApplication {

    public static void main(String[] args) {
        SpringApplication.run(MjforumApplication.class, args);
    }
}
