package com.rz.lease;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.web.config.EnableSpringDataWebSupport;

@SpringBootApplication(scanBasePackages = { "com.rz.lease" })
@EntityScan(basePackages = "com.rz.lease.model.entity")
@EnableSpringDataWebSupport(pageSerializationMode = EnableSpringDataWebSupport.PageSerializationMode.VIA_DTO)
@EnableJpaRepositories(
        basePackages = "com.rz.lease.web.app.repository",
        repositoryBaseClass = com.rz.lease.web.app.repository.BaseJpaRepositoryImpl.class)
public class WebAppApplication {

    public static void main(String[] args) {
        SpringApplication.run(WebAppApplication.class, args);
    }
}
