package me.ad.kanban.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig {
    private static final String UI_BASE_URL = "http://localhost:4200";
    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**").allowedOrigins(UI_BASE_URL)
                        .allowedMethods(HttpMethod.GET.name(),
                                HttpMethod.HEAD.name(),
                                HttpMethod.OPTIONS.name(),
                                HttpMethod.DELETE.name(),
                                HttpMethod.PATCH.name(),
                                HttpMethod.POST.name(),
                                HttpMethod.PUT.name(),
                                HttpMethod.TRACE.name());
                registry.addMapping("*").allowedOrigins(UI_BASE_URL)
                        .allowedMethods(HttpMethod.GET.name(),
                                HttpMethod.HEAD.name(),
                                HttpMethod.OPTIONS.name(),
                                HttpMethod.DELETE.name(),
                                HttpMethod.PATCH.name(),
                                HttpMethod.POST.name(),
                                HttpMethod.PUT.name(),
                                HttpMethod.TRACE.name());
                /*registry.addMapping("/user/*").allowedOrigins(UI_BASE_URL);
                registry.addMapping("/team/*").allowedOrigins(UI_BASE_URL);
                registry.addMapping("/stage/*").allowedOrigins(UI_BASE_URL);
                registry.addMapping("/task/*").allowedOrigins(UI_BASE_URL);*/
            }
        };
    }
}
