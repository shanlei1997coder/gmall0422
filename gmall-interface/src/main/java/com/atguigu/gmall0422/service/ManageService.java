package com.atguigu.gmall0422.service;

import com.atguigu.gmall0422.bean.*;

import java.util.List;

/**
 * 商品后台管理接口
 */
public interface ManageService {

    /**
     *  查询所有一级分类
     */
    List<BaseCatalog1> getBaseCatalog1();

    /**
     * 查询对应一级分类下的二级分类
     * @param catalog1Id
     * @return
     */
    List<BaseCatalog2> getBaseCatalog2(String catalog1Id);

    /**
     * 查询对应二级分类下的三级分类
     * @param catalog2Id
     * @return
     */
    List<BaseCatalog3> getBaseCatalog3(String catalog2Id);

    /**
     * 查询三级分类下的平台属性
     * @param catalog3Id
     * @return
     */
    List<BaseAttrInfo> getAttrInfoList(String catalog3Id);

    /**
     * 添加平台属性
     */
    void saveAttrInfo(BaseAttrInfo baseAttrInfo);

    /**
     * 根据 平台id查询平台属性
     * @param attrId
     */
    List<BaseAttrValue> getAttrValueList(String attrId);

    /**
     * 根据 平台属性id判断平台获取平台属性对象从而判断其 是否存在
     * @param attrId
     * @return
     */
    BaseAttrInfo getAttrInfo(String attrId);

    /**
     * 根据三级分类id查询商品SKU列表
     */
    List<SpuInfo> getSpuList(String catalog3Id);

    /**
     * 查询所有商品基本属性
     */
    List<BaseSaleAttr> getSaleAttrList();

    /**
     * 保存商品属性SPU信息
     */
    void saveSpuInfo(SpuInfo spuInfo);

    /**
     * 根据SpuId查询商品下的图片列表
     * @param spuId
     * @return
     */
    List<SpuImage> getSpuImageList(String spuId);

    /**
     * 根据spuId查询 商品销售属性和销售属性值
     * @param spuId
     * @return
     */
    List<SpuSaleAttr> getSpuSaleAttrList(String spuId);

    /**
     * 保存 skuInfo基本信息
     * @param skuInfo
     */
    void saveSkuInfo(SkuInfo skuInfo);
}
