package com.atguigu.tingshu;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * 专辑微服务的启动类
 */
@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
public class ServiceAlbumApplication {
    /***
     * 启动方法
     * @param args
     *  springboot是什么？工具
     *  springmvc + spring + mybatis:
     *  1.配置:自动装配/配置
     *      spring.xml springmvc.xml spring-mybatis.xml web.xml...
     *      <bean id="" name ="" class="" ></bean>
     *  2.pom文件优化---jar包：坐标：名字 版本（敏感）--起步依赖：a.父工程 b.maven依赖传递
     *
     * @param args:jvm参数jar-jar a.jar -Xxm -Xms
     */
    public static void main(String[] args) {
        /**
         * 1.创建了一个容器：装bean
         * 2.识别SpringBootApplication注解：
         *   SpringBootConfiguration：标识启动类是一个配置类
         *   EnableAutoConfiguration：通过spring官方自动配置类包下定义的全部可能用到的配置类进行加载（反射）
         *   ，能加载成功的放入容器，加载失败下一个
         *   ComponentScan:扫描启动类所在包下所有类的所有注解，
         *      扫描启动类所在包下子包中所有的类的所有注解（自定义的bean）
         *          controller service mapper
         */
        SpringApplication.run(ServiceAlbumApplication.class, args);
    }

}























