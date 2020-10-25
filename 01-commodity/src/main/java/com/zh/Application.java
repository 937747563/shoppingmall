package com.zh;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 *  商品模块
 *      配置mybatis配置文件
 */


@MapperScan("com.zh.commodity.dao")     //扫描mapper接口
@SpringBootApplication
public class Application {

        
    public static void main(String[] args) {

        SpringApplication.run(Application.class, args);
    }

}
