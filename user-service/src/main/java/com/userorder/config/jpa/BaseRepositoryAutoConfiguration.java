package com.userorder.config.jpa;

import com.cosium.spring.data.jpa.entity.graph.repository.support.EntityGraphJpaRepositoryFactoryBean;

import com.userorder.UserServiceApplication;
import com.userorder.persistance.repository.impl.BaseCustomJpaRepositoryImpl;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableJpaAuditing
@Configuration
@ConditionalOnClass(CustomJpaRepositories.class) // Auto-configuration will only work if CustomJpaRepositories is in classpath
@Import(BaseRepositoryAutoConfiguration.JpaConfiguration.class) // Imports inner configuration for JPA repositories
public class BaseRepositoryAutoConfiguration {

    /**
     * JPA Configuration that activates Spring Data JPA with entity graph support
     *
     * - @EnableJpaRepositories activates Spring Data JPA and allows Spring to create repositories from interfaces
     * - repositoryBaseClass specifies the custom repository implementation class to add custom methods
     * - repositoryFactoryBeanClass is a special factory that creates repositories with EntityGraph support
     *   (this allows more efficient work with entity graphs for optimizing SQL queries)
     */
    @Configuration
    @EntityScan(basePackageClasses = UserServiceApplication.class) // Scan for entities in the application package
    @EnableJpaRepositories(
            basePackageClasses = UserServiceApplication.class, // Enable JPA repositories
            repositoryBaseClass = BaseCustomJpaRepositoryImpl.class, // Specify custom base class for repositories
            repositoryFactoryBeanClass = EntityGraphJpaRepositoryFactoryBean.class // Factory for creating repositories with Entity Graph support
    )
    public class JpaConfiguration { }
}