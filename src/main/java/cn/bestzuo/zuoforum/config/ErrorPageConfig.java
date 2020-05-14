package cn.bestzuo.zuoforum.config;

import org.springframework.boot.web.server.ConfigurableWebServerFactory;
import org.springframework.boot.web.server.ErrorPage;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;

/**
 * 自定义404页面
 * @author zuoxiang
 * @date 2020/5/5 22:15
 */
@Configuration
public class ErrorPageConfig {

    //@Bean必须加上
    @Bean
    public WebServerFactoryCustomizer<ConfigurableWebServerFactory> webServerFactoryCustomizer() {
        //java8 lambda写法
        return (factory -> {
            ErrorPage errorPage404 = new ErrorPage(HttpStatus.NOT_FOUND, "/404.html");
            factory.addErrorPages(errorPage404);
        });
    }

}
