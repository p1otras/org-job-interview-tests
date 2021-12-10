package org.job.interview.tests.configuration;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:test.properties")
@Data
public class TestPropertiesResolver {

    @Value("${test.appId}")
    private String appId;
    @Value("${test.appKey}")
    private String appKey;
}