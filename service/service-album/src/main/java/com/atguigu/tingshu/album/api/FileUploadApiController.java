package com.atguigu.tingshu.album.api;

import com.atguigu.tingshu.album.config.MinioConstantProperties;
import com.atguigu.tingshu.common.result.Result;
import io.minio.BucketExistsArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@Tag(name = "上传管理接口")
@RestController
@RequestMapping("/api/album")
public class FileUploadApiController {

    @Autowired
    private MinioConstantProperties minioConstantProperties;

    /**
     * 文件上传
     * @param file
     * @return
     */
    @PostMapping(value = "/fileUpload")
    @SneakyThrows
    public Result fileUpload(@RequestParam("file") MultipartFile file){
        // 创建一个 MinioClient 对象，用于与 MinIO 服务器进行交互
        MinioClient minioClient = MinioClient
                .builder()  // 使用 MinioClient 的构建器模式来创建客户端实例
                .endpoint(minioConstantProperties.getEndpointUrl())  // 设置 MinIO 服务器的 URL 终端点
                .credentials(minioConstantProperties.getAccessKey(), minioConstantProperties.getSecreKey())  // 使用访问密钥（AccessKey）
                // 和秘密密钥（SecretKey）进行身份验证
                .build();  // 构建 MinioClient 实例
        // 判断桶是否存在，不存在就创建
        if (!minioClient.bucketExists
                (BucketExistsArgs
                        .builder()
                        .bucket(minioConstantProperties.getBucketName())
                        .build())){

            // 不存在则创建桶
            minioClient.makeBucket(
                    MakeBucketArgs
                            .builder()
                            .bucket(minioConstantProperties.getBucketName())
                            .build()
            );
        }
        //随机生成一个文件名
        String fileName = UUID.randomUUID().toString().replace("-", "");
        // 使用客户端对象上传图片文件
        minioClient.putObject(
                PutObjectArgs
                        .builder()
                        .stream(file.getInputStream(),file.getSize(),-1)
                        .contentType(file.getContentType())
                        .bucket(minioConstantProperties.getBucketName())
                        .object(fileName)
                        .build());
        //拼接访问地址：minio的地址 + 桶名 + 文件名
        String url = minioConstantProperties.getEndpointUrl()
                + "/" + minioConstantProperties.getBucketName()
                + "/" + fileName;
        //返回文件的访问地址

        return Result.ok(url);
    }


}
















