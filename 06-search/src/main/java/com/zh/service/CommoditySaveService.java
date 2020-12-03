package com.zh.service;

import com.zh.common.to.es.SkuEsModel;

import java.io.IOException;
import java.util.List;

public interface CommoditySaveService {

    void commodityUp(List<SkuEsModel> skuEsModels);
}
