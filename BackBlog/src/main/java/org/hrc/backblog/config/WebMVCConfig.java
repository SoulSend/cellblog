package org.hrc.backblog.config;

import org.hrc.backblog.handler.LoginInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * webMvc的配置类
 */
@Configuration
public class WebMVCConfig implements WebMvcConfigurer {
    @Autowired
    private LoginInterceptor loginInterceptor;

    /**
     * 跨域配置
     * @param registry
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        //跨域配置
        registry.addMapping("/**")
                .allowedOrigins("http://localhost:9999","http://localhost:9528")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // 允许的方法
                .allowedHeaders("*") // 允许的头信息
                .allowCredentials(true); // 允许携带凭证;
    }

    /**
     * 配置拦截器
     * 同时设置拦截的路径，后续开发可以修改拦截路径实现权限限制
     * @param registry
     */

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(loginInterceptor)
                .addPathPatterns("/articles/publish")
                .addPathPatterns("/comments/create/change")
                .addPathPatterns("/articles/userInfo/articleList");
    }

}
