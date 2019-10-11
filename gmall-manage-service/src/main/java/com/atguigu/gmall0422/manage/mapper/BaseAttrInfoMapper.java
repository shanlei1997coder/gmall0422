package com.atguigu.gmall0422.manage.mapper;

import com.atguigu.gmall0422.bean.BaseAttrInfo;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface BaseAttrInfoMapper extends Mapper<BaseAttrInfo> {

    //根据三级分类id查询平台属性和对应的平台属性值
    List<BaseAttrInfo> selectAttrInfoList(String catalog3Id);
}
