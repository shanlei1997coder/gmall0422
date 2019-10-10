package com.atguigu.gmall0422.bean;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

/**
 * @program: gmall0422
 * @description: 商品sku实体类
 * @author: Mr.Lei
 * @create: 2019-10-09 18:16
 **/
@Data
public class SpuInfo implements Serializable {

    @Column
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;

    @Column
    private String spuName;

    @Column
    private String description;

    @Column
    private  String catalog3Id;

    //数据库中没有这些字段   @Transient
    // 封装商品图片的集合
    @Transient
    private List<SpuImage> spuImageList;

    //封装商品销售属性集合  商品销售属性值在商品销售属性中进行封装
    @Transient
    private List<SpuSaleAttr> spuSaleAttrList;

}

