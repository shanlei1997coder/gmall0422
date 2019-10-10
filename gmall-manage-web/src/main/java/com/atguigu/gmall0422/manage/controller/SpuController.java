package com.atguigu.gmall0422.manage.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.gmall0422.bean.BaseSaleAttr;
import com.atguigu.gmall0422.bean.SpuInfo;
import com.atguigu.gmall0422.service.ManageService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @program: gmall0422
 * @description: 商品SPU控制层
 * @author: Mr.Lei
 * @create: 2019-10-09 21:03
 **/
@RestController
@CrossOrigin
public class SpuController {

    //服务消费者
    @Reference
    private ManageService manageService;

    /**
     * 查询所有商品基本属性
     * @return
     */
    //http://localhost:8082/baseSaleAttrList
    @RequestMapping("baseSaleAttrList")
    public List<BaseSaleAttr> baseSaleAttrList(){
        return manageService.getSaleAttrList();
    }

    /**
     *  保存商品spu属性信息
     */
    //http://localhost:8082/saveSpuInfo
    @RequestMapping("saveSpuInfo")
    public String saveSpuInfo(@RequestBody SpuInfo spuInfo){ //使用@RequestBody接受json数据转化成对象
        manageService.saveSpuInfo(spuInfo);
        return "OK";
    }
}

