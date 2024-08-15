package com.atguigu.tingshu.album.config;

import com.qcloud.vod.VodUploadClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * ClassName: VodConfig
 * Package: com.atguigu.tingshu.album.config
 * Description:
 *
 * @Author Final-Coke
 * @Create 2024/8/15 2:49 PM
 * @Version 1.0
 */

/**
 * vod云点播的bean初始化
 */
@Configuration
public class VodConfig {

   @Autowired
   private VodConstantProperties vodConstantProperties;

   /**
    * 云点播客户端对象的初始化
    * @return
    */
   @Bean
   public VodUploadClient vodUploadClient(){
      //初始化一个上传客户端对象
      return new  VodUploadClient(vodConstantProperties.getSecretId()
              , vodConstantProperties.getSecretKey());

   }
}
















