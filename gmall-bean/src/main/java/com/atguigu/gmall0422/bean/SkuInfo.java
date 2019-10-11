package com.atguigu.gmall0422.bean;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * @program: gmall0422
 * @description: 商品sku基本信息
 * @author: Mr.Lei
 * @create: 2019-10-10 21:13
 **/
@Data
public class SkuInfo implements Serializable {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column
    String id;

    @Column
    String spuId;

    @Column
    BigDecimal price;

    @Column
    String skuName;

    @Column
    BigDecimal weight;

    @Column
    String skuDesc;

    @Column
    String catalog3Id;

    @Column
    String skuDefaultImg;

    //保存 sku信息中需要封装 sku_image sku_attr_value sku_sale_attr_value
    @Transient
    List<SkuImage> skuImageList; //图片

    @Transient
    List<SkuAttrValue> skuAttrValueList;  //平台属性

    @Transient
    List<SkuSaleAttrValue> skuSaleAttrValueList;//销售属性
}

