package cn.bestzuo.zuoforum;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication(scanBasePackages = "cn.bestzuo.zuoforum")
@MapperScan(basePackages = {"cn.bestzuo.zuoforum.mapper","cn.bestzuo.zuoforum.admin.mapper"})
@EnableTransactionManagement
@EnableCaching
public class ZuoforumApplication {

    public static void main(String[] args) {
        SpringApplication.run(ZuoforumApplication.class, args);
    }

}
