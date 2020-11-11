package com.zh;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * 商品模块
 * 配置mybatis配置文件
 */


//@MapperScan("com.zh.commodity.dao")     //扫描mapper接口
@EnableFeignClients(basePackages = "com.zh.commodity.fegin")
@EnableDiscoveryClient                  //扫描nacos
@SpringBootApplication
public class CommodityApplication {

public static void main(String[] args) {

        SpringApplication.run(CommodityApplication.class, args);
    }

}
