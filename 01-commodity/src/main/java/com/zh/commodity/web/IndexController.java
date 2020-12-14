package com.zh.commodity.web;

import com.zh.commodity.entity.CategoryEntity;
import com.zh.commodity.service.CategoryService;
import com.zh.commodity.vo.Catelog2Vo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;

@Controller
public class IndexController {

    @Autowired
    CategoryService categoryService;


    @GetMapping({"/","/index.html"})
    public String index(Model model){

        //查出所有一级分类
        List<CategoryEntity> categoryEntitys = categoryService.getLevel1Categorys();

        model.addAttribute("categorys",categoryEntitys);

        return "index";
    }

    /**
     * 
     */
    @ResponseBody
    @GetMapping("index/catalog.json")
    public Map<String, List<Catelog2Vo>> getCatalogJson(){

        Map<String, List<Catelog2Vo>> catalogJson =categoryService.getCatalogJson();

        return catalogJson;
    }
}
