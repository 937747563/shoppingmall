package com.zh;

import com.aliyun.oss.OSSClient;
import com.aliyun.oss.model.PutObjectRequest;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.io.File;

@SpringBootTest
class ThirdPartyApplicationTests {

    @Resource
    OSSClient ossClient;

    @Test
    void contextLoads() {
    }

    /**
     * 使用Springboot 配置相关信息 进行上传文件
     */
    @Test
    public void testUpdate2(){

        PutObjectRequest putObjectRequest = new PutObjectRequest("shoppingmall-zh", "C.jpg", new File("C:\\Users\\93774\\Pictures\\sad.jpg"));

        // 上传文件。
        ossClient.putObject(putObjectRequest);

        // 关闭OSSClient。
        ossClient.shutdown();
        System.out.println("2上产完成");

    }

}
