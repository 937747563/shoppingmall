package com.zh.commodity.fegin;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;

@FeignClient("02-stock")
public interface StockFeignService {

    @RequestMapping("/stock/purchase/test")
    public String test();
}
