package io.github.gstfnk.controller;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Set;

@Configuration
public class MvcConfiguration implements WebMvcConfigurer {
    private final Set<HandlerInterceptor> interceptorSet;

    MvcConfiguration(Set<HandlerInterceptor> interceptorSet) {
        this.interceptorSet = interceptorSet;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        interceptorSet.forEach(registry::addInterceptor);
    }
}
