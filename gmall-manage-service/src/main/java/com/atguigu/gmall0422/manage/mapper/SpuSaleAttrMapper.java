package com.atguigu.gmall0422.manage.mapper;

import com.atguigu.gmall0422.bean.SpuSaleAttr;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface SpuSaleAttrMapper extends Mapper<SpuSaleAttr> {

    //根据商品id查询 商品销售属性信息
    List<SpuSaleAttr> selectSpuSaleAttrList(String spuId);
}
