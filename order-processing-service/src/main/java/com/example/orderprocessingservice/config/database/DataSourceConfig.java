package com.example.orderprocessingservice.config.database;

import com.example.orderprocessingservice.utils.CryptoUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import javax.sql.DataSource;

@Configuration
public class DataSourceConfig {

    private static final Logger LOGGER = LoggerFactory.getLogger(DataSourceConfig.class);

    @Value("${spring.datasource.url}")
    private String databaseUrl;

    @Value("${spring.datasource.username}")
    private String databaseUserName;

    @Value("${spring.datasource.password}")
    private String databasePassword;

    @Value("${spring.datasource.driver-class-name}")
    private String databaseDriverClassName;


    @Bean
    public DataSource dataSource() throws Exception {
        LOGGER.info("Initializing DataSource with URL={} and user={}", databaseUrl, databaseUserName);

        CryptoUtils cryptoUtils = new CryptoUtils();
        String decryptedPassword = cryptoUtils.decrypt(databasePassword);

        DataSource dataSource = DataSourceBuilder.create()
                .url(databaseUrl)
                .username(databaseUserName)
                .password(decryptedPassword)
                .driverClassName(databaseDriverClassName)
                .build();

        dataSource.getConnection().close();

        LOGGER.info("DataSource connected successfully!");
        return dataSource;
    }
}
