package com.atguigu.gmall0422.manage.mapper;

import com.atguigu.gmall0422.bean.SkuSaleAttrValue;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface SkuSaleAttrValueMapper extends Mapper<SkuSaleAttrValue> {

    // 商品切换时  查询商品销售属性值id和skuId
    List<SkuSaleAttrValue> selectSkuSaleAttrValueList(String spuId);
}
