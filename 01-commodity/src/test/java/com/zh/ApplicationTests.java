package com.zh;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClient;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.model.PutObjectRequest;
import com.zh.commodity.entity.BrandEntity;
import com.zh.commodity.service.BrandService;
import com.zh.commodity.service.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.io.File;
import java.util.Arrays;

@Slf4j
@SpringBootTest
class ApplicationTests {

    @Resource
    BrandService brandService;

    @Resource
    OSSClient ossClient;

    @Resource
    CategoryService categoryService;


    @Test
    void contextLoads() {

        BrandEntity brand = new BrandEntity();
        brand.setDescript("描述1");
        brand.setName("华为");

        brandService.save(brand);
        System.out.println("保存成功");
    }



    //测试文件上传阿里云(传统方法)
    @Test
    public void testUpdate() {

        // Endpoint以杭州为例，其它Region请按实际情况填写。
        String endpoint = "oss-cn-beijing.aliyuncs.com";
        // 阿里云主账号AccessKey拥有所有API的访问权限，风险很高。强烈建议您创建并使用RAM账号进行API访问或日常运维，请登录 https://ram.console.aliyun.com 创建RAM账号。
        String accessKeyId = "LTAI4GKtiyhHHZN2RY4ubcoQ";
        String accessKeySecret = "OnWSymiVkQbU0GwiYB7TgnoTlz8pE7";

        // 创建OSSClient实例。
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);

        // 创建PutObjectRequest对象。
        PutObjectRequest putObjectRequest = new PutObjectRequest("shoppingmall-zh", "A.jpg", new File("C:\\Users\\93774\\Pictures\\0d40c24b264aa511.jpg"));

        // 如果需要上传时设置存储类型与访问权限，请参考以下示例代码。
        // ObjectMetadata metadata = new ObjectMetadata();
        // metadata.setHeader(OSSHeaders.OSS_STORAGE_CLASS, StorageClass.Standard.toString());
        // metadata.setObjectAcl(CannedAccessControlList.Private);
        // putObjectRequest.setMetadata(metadata);

        // 上传文件。
        ossClient.putObject(putObjectRequest);

        // 关闭OSSClient。
        ossClient.shutdown();
        System.out.println("1上产完成");
    }

    /**
     * 使用Springboot 配置相关信息 进行上传文件
     */
    @Test
    public void testUpdate2(){

        PutObjectRequest putObjectRequest = new PutObjectRequest("shoppingmall-zh", "B.jpg", new File("C:\\Users\\93774\\Pictures\\sad.jpg"));

        // 上传文件。
        ossClient.putObject(putObjectRequest);

        // 关闭OSSClient。
        ossClient.shutdown();
        System.out.println("2上产完成");

    }

    @Test
    public void testFindPath(){
        Long[] catelogPath = categoryService.findCatelogPath(225L);
        log.info("完整路径"+ Arrays.asList(catelogPath));


    }

    @Test
    public void test1(){

        String a="0";

        System.out.println(a.equals("0"));

    }



}
