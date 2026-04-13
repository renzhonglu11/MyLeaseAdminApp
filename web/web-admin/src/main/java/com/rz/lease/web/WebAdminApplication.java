package com.rz.lease.web;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = { "com.rz.lease" })
@EntityScan(basePackages = "com.rz.lease.model.entity")
@EnableSpringDataWebSupport(pageSerializationMode = EnableSpringDataWebSupport.PageSerializationMode.VIA_DTO)
@EnableJpaRepositories( // Tell spring data jpa not use SimpleJpaRepository
        basePackages = "com.rz.lease.web.admin.repository", repositoryBaseClass = com.rz.lease.web.admin.repository.BaseJpaRepositoryImpl.class)
@EnableScheduling
public class WebAdminApplication {

    public static void main(String[] args) {
        SpringApplication.run(WebAdminApplication.class, args);
    }
}
