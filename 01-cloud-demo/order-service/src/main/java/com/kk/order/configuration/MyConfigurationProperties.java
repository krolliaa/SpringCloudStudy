package com.kk.order.configuration;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
//@ConfigurationProperties(prefix = "pattern")
public class MyConfigurationProperties {
    private String dateformat;
    private String sharedvalue;
    private String sharedservice;
}
