package com.zh.commodity.fegin;

import com.zh.commodity.vo.SkuHasStockVo;
import com.zh.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@FeignClient("02-stock")
public interface StockFeignService {

    @RequestMapping("/stock/purchase/test")
    public String test();

    @PostMapping("/stock/waresku/hasstock")
    R<List<SkuHasStockVo>> getSkusHasStock(@RequestBody List<Long> skuIds);

}
