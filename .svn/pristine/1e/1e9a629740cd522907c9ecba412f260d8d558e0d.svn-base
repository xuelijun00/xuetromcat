package com.yks.bigdata.web.system;

import com.yks.bigdata.web.interceptor.LoginInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * Created by Administrator on 2017/6/27.
 */
@Configuration
public class MyWebMvcConfigurer extends WebMvcConfigurerAdapter {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new LoginInterceptor())
                //.addPathPatterns("/index")
                //.addPathPatterns("/LogisticChannel/**")
                //.addPathPatterns("/account/**")
                .excludePathPatterns("/logout")
                .excludePathPatterns("/login")
                .excludePathPatterns("/common")
                .excludePathPatterns("/");
        super.addInterceptors(registry);
    }

}
