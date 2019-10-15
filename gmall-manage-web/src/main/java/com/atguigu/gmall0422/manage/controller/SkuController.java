package com.atguigu.gmall0422.manage.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.gmall0422.bean.*;
import com.atguigu.gmall0422.service.ListService;
import com.atguigu.gmall0422.service.ManageService;

import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @program: gmall0422
 * @description: 商品sku
 * @author: Mr.Lei
 * @create: 2019-10-10 18:42
 **/
@RestController
@CrossOrigin
public class SkuController {

    @Reference
    private ManageService manageService;

    //商品检索上架
    @Reference
    private ListService listService;

    /**
     *  根据spuId查询商品的图片列表
     */
    //http://localhost:8082/spuImageList?spuId=61
    @RequestMapping("spuImageList")
    public List<SpuImage> spuImageList(String spuId){

        return manageService.getSpuImageList(spuId);
    }

    /**
     *  根据spuId查询商品的销售属性和销售属性值
     */
    //http://localhost:8082/spuSaleAttrList?spuId=61
    @RequestMapping("spuSaleAttrList")
    public List<SpuSaleAttr> spuSaleAttrList(String spuId){
        return manageService.getSpuSaleAttrList(spuId);
    }

    /**
     * 保存 sku信息  图片+平台属性+销售属性+Sku基本信息
     */
    //http://localhost:8082/saveSkuInfo
    @RequestMapping("saveSkuInfo")
    public void saveSkuInfo(@RequestBody SkuInfo skuInfo){ //使用skuInfo接收
        manageService.saveSkuInfo(skuInfo);
    }

    /**
     *  上架商品  从mysql数据库中传到elasticsearch
     */
    @RequestMapping("onSale")
    public void onSale(String skuId){

        SkuLsInfo skuLsInfo = new SkuLsInfo();
        //根据 skuId从数据库中查询商品的信息
        //在 根据skuId查询skuInfo的实现类中加上查询平台属性值集合 一起返回
        SkuInfo skuInfo = manageService.getSkuInfo(skuId);
        //将 skuInfo 的值 赋值拷贝 给skuLsInfo
        BeanUtils.copyProperties(skuInfo,skuLsInfo); //spring框架的(source,target)

        //apache.commons的BeanUtils包的 参数正好相反(target,source)

        System.out.println(skuLsInfo);
        //调用方法 将数据添加到es中
        listService.saveSkuInfo(skuLsInfo);
    }


}

