package com.atguigu.gmall0422.manage.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.gmall0422.bean.BaseAttrInfo;
import com.atguigu.gmall0422.bean.SkuInfo;
import com.atguigu.gmall0422.bean.SpuImage;
import com.atguigu.gmall0422.bean.SpuSaleAttr;
import com.atguigu.gmall0422.service.ManageService;
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

}

