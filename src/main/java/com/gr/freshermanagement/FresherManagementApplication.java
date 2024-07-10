package com.gr.freshermanagement;

import com.gr.freshermanagement.utils.StorageUtils;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(StorageUtils.class)
public class FresherManagementApplication {

    public static void main(String[] args) {
        SpringApplication.run(FresherManagementApplication.class, args);
    }

}
