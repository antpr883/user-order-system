package com.userorder.config.jpa;

import com.cosium.spring.data.jpa.entity.graph.repository.support.EntityGraphJpaRepositoryFactoryBean;
import com.userorder.UserServiceApplication;
import com.userorder.persistence.repository.impl.BaseCustomJpaRepositoryImpl;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableJpaAuditing
@Configuration
@ConditionalOnClass(CustomJpaRepositories.class)// Автоконфігурація спрацює тільки якщо в класпаті є CustomJpaRepositories
@Import(BaseRepositoryAutoConfiguration.JpaConfiguration.class)// Імпортує внутрішню конфігурацію для JPA репозиторіїв
public class BaseRepositoryAutoConfiguration {


    /*
    * @EnableJpaRepositories активує Spring Data JPA і дозволяє Spring створювати репозиторії на основі інтерфейсів.
   🔸 repositoryBaseClass вказує на кастомну реалізацію репозиторію, щоб додати свої методи.
     repositoryFactoryBeanClass — спеціальна фабрика, яка створює репозиторії з підтримкою EntityGraph (це дозволяє ефективніше працювати з графами сутностей для оптимізації SQL-запитів).
    *
    *
    * */
    @Configuration
    @EntityScan(basePackageClasses = UserServiceApplication.class) // Сканування сутностей у пакеті DemoShopApplication
    @EnableJpaRepositories(
            basePackageClasses = UserServiceApplication.class, // Вмикає JPA репозиторії
            repositoryBaseClass = BaseCustomJpaRepositoryImpl.class, // Вказує кастомний базовий клас для репозиторіїв
            repositoryFactoryBeanClass = EntityGraphJpaRepositoryFactoryBean.class // Фабрика для створення репозиторіїв із підтримкою Entity Graph
    )
    public class JpaConfiguration { }

}
