package com.zh.commodity.fegin;

import com.zh.common.to.es.SkuEsModel;
import com.zh.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient("06-search")
public interface SearchFeignService {

    @PostMapping("/search/save/commodity/up")
    R commodityUp(@RequestBody List<SkuEsModel> skuEsModels);
}
