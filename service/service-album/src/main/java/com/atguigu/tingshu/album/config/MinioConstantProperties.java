package com.atguigu.tingshu.album.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix="minio") //读取节点
@Data
public class MinioConstantProperties {

    private String endpointUrl;
    private String accessKey;
    private String secreKey;
    private String bucketName;
}

/**
 * @ConfigurationProperties(prefix="minio") 是 Spring 框架中的一个注解，主要用于将配置文件中的属性值自动映射到 Java 对象的字段中
 * 。这个注解通常用于将外部化的配置（如 application.properties 或 application.yml 文件）加载到 Spring 管理的 Bean 中。
 */