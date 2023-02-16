package com.example.checkout;

import org.flywaydb.core.Flyway;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

@Configuration
public class CheckoutApplicationAppConfig {

    @Value("${spring.datasource.url}")
    private String dataSourceUrl;

    @Value("${spring.datasource.username}")
    private String dataSourceUsername;

    @Value("${spring.datasource.password}")
    private String dataSourcePassword;

    @Value("${spring.datasource.driver-class-name}")
    private String dataSourceDriverClassName;

    @Value("${spring.datasource.hikari.maximum-pool-size}")
    private int dataSourceMaximumPoolSize;

    @Value("${spring.datasource.hikari.connection-timeout}")
    private long dataSourceConnectionTimeout;

    @Bean
    public HikariDataSource dataSource() {
        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setJdbcUrl(dataSourceUrl);
        dataSource.setUsername(dataSourceUsername);
        dataSource.setPassword(dataSourcePassword);
        dataSource.setDriverClassName(dataSourceDriverClassName);
        dataSource.setMaximumPoolSize(dataSourceMaximumPoolSize);
        dataSource.setConnectionTimeout(dataSourceConnectionTimeout);

        return dataSource;
    }

    @Bean
    public JdbcTemplate db(DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }


    @Bean
    public Flyway flyway() {
        Flyway flyway = Flyway.configure()
                .dataSource(dataSource())
                .locations("classpath:db/postgresql")
                .load();
        flyway.migrate();
        return flyway;
    }


}
