package conf;

import org.hibernate.jpa.HibernatePersistenceProvider;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.*;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.init.DatabasePopulatorUtils;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.util.Properties;

@Configuration
@EnableWebMvc
@Import(HsqlDataSource.class)
@ComponentScan(basePackages = {"dao", "app"})
@PropertySource("classpath:/application.properties")
@EnableGlobalMethodSecurity(prePostEnabled = true)
@EnableTransactionManagement
public class DbConfig {

    @Bean
    public EntityManagerFactory entityManagerFactory(DataSource dataSource, @Qualifier("dialect") String  dialect) {

        var populator = new ResourceDatabasePopulator(
                new ClassPathResource("schema.sql"),
                new ClassPathResource("data.sql"));

        DatabasePopulatorUtils.execute(populator, dataSource);


        LocalContainerEntityManagerFactoryBean factory =
                new LocalContainerEntityManagerFactoryBean();
        factory.setPersistenceProviderClass(
                HibernatePersistenceProvider.class);
        factory.setPackagesToScan("model");
        factory.setDataSource(dataSource);
        factory.setJpaProperties(additionalProperties(dialect));
        factory.afterPropertiesSet();

        return factory.getObject();
    }

    @Bean
    public PlatformTransactionManager transactionManager(
            EntityManagerFactory entityManagerFactory) {

        return new JpaTransactionManager(entityManagerFactory);
    }

    private Properties additionalProperties(String dialect) {
        Properties properties = new Properties();
        //valideerib skeemi vastavust
        properties.setProperty("hibernate.hbm2ddl.auto", "validate");
//        koodis pole täpsest andmebaasi valikust juttu, dialekti valik toimub siin
        properties.setProperty("hibernate.dialect", dialect);
        // kirjutab konsooli
        properties.setProperty("hibernate.show_sql", "true");
        properties.setProperty("hibernate.format_sql", "true");

        return properties;
    }

}