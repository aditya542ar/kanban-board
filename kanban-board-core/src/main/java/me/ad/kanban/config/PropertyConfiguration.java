package me.ad.kanban.config;

import me.ad.kanban.entity.Project;
import me.ad.kanban.entity.User;
import me.ad.kanban.filter.FilterBuilder;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;

@Configuration
@EnableConfigurationProperties({
        CustomMessageProperties.class
})
public class PropertyConfiguration {

    @Bean
    public CustomMessageProperties message() {
        return new CustomMessageProperties();
    }

    @Bean
    @Scope(BeanDefinition.SCOPE_PROTOTYPE)
    public FilterBuilder<Project> projectFilterBuilder(CustomMessageProperties message) {
        return new FilterBuilder<>(message);
    }

    @Bean
    @Scope(BeanDefinition.SCOPE_PROTOTYPE)
    public FilterBuilder<User> userFilterBuilder(CustomMessageProperties message) {
        return new FilterBuilder<>(message);
    }
}
