package akdogan.usercheck.common.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import akdogan.usercheck.common.RequestTimingInterceptor;

@Configuration
public class WebConfig implements WebMvcConfigurer{
    
    private final RequestTimingInterceptor requestTimingInterceptor;

    public WebConfig(RequestTimingInterceptor requestTimingInterceptor) {
        this.requestTimingInterceptor = requestTimingInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(requestTimingInterceptor);
    }
    
    
}
