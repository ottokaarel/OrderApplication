package config

import org.springframework.web.servlet.config.annotation.EnableWebMvc
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Bean
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator
import org.springframework.core.io.ClassPathResource
import org.springframework.jdbc.datasource.init.DatabasePopulatorUtils
import model.OrderDao
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import
import javax.sql.DataSource

@Configuration
@EnableWebMvc
@Import(
    HsqlDataSource::class
)
@ComponentScan(basePackages = ["app"])
open class MvcConfig {

    @Bean
    open fun getTemplate(ds: DataSource): JdbcTemplate {
        val populator = ResourceDatabasePopulator(
            ClassPathResource("schema.sql"),
            ClassPathResource("data.sql")
        )
        DatabasePopulatorUtils.execute(populator, ds)
        return JdbcTemplate(ds)
    }

    @Bean
    open fun getOrderDao(template: JdbcTemplate?): OrderDao {
        return OrderDao(template!!)
    }
}