package com.gr.freshermanagement.utils;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "storage")
public class StorageUtils {
    private String location = "Files-Upload";

}
