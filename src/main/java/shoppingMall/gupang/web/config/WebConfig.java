package shoppingMall.gupang.web.config;

import jodd.net.HttpMethod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import shoppingMall.gupang.web.interceptor.LoginInterceptor;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new LoginInterceptor())
                .order(1)
                .addPathPatterns("/**")
                .excludePathPatterns("/", "/signup", "/checkId", "/login",
                        "/logout", "/category/**", "/css/**", "*/ico",
                        "/error", "/review/item/**", "/item/**", "/seller/**", "/member/**");
    }
}
