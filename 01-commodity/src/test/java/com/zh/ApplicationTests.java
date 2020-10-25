package com.zh;

import com.zh.commodity.entity.BrandEntity;
import com.zh.commodity.service.BrandService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

@SpringBootTest
class ApplicationTests {

    @Resource
    BrandService brandService;

    @Test
    void contextLoads() {

        BrandEntity brand=new BrandEntity();
        brand.setDescript("描述1");
        brand.setName("华为");

        brandService.save(brand);
        System.out.println("保存成功");
    }

}
