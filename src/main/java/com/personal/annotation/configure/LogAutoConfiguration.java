/*
 * @Copyright: 2005-2018 www.hyjf.com. All rights reserved.
 */
package com.personal.annotation.configure;

import com.personal.annotation.aspect.LogAspect;
import com.personal.annotation.properties.LogProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author sunpeikai
 * @version LogAutoConfiguration, v0.1 2020/9/25 14:16
 * @description
 */
@Configuration
@EnableConfigurationProperties({LogProperties.class})
@ConditionalOnProperty(name = {"log.enable"}, havingValue = "true")
public class LogAutoConfiguration {

    private static final Logger log = LoggerFactory.getLogger(LogAutoConfiguration.class);

    public LogAutoConfiguration() {
    }

    @Bean
    @ConditionalOnMissingBean
    public LogAspect logAspect(LogProperties logProperties){
        return new LogAspect(logProperties);
    }
}
