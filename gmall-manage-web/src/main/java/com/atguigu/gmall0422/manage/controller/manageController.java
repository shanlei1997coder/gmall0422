package com.atguigu.gmall0422.manage.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.gmall0422.bean.*;
import com.atguigu.gmall0422.service.ManageService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.xml.ws.RequestWrapper;
import java.util.List;

/**
 * @program: gmall0422
 * @description: 商品管理控制层
 * @author: Mr.Lei
 * @create: 2019-10-08 18:38
 **/
@RestController
@CrossOrigin  //跨域访问
public class manageController {

    //服务消费者
    @Reference
    private ManageService manageService;

    //业务流程  bean->mapper->service->service.impl->controller
    /**
     * 查询商品一级分类
     */
    //http://localhost:8082/getCatalog1
    @RequestMapping("getCatalog1")
    public List<BaseCatalog1> getCatalog1(){
        return manageService.getBaseCatalog1();
    }

    /**
     * 根据一级分类id查询二级分类
     */
    //http://localhost:8082/getCatalog2?catalog1Id=3
    @RequestMapping("getCatalog2")
    public List<BaseCatalog2> getCatalog2(String catalog1Id){

        List<BaseCatalog2> log2list = manageService.getBaseCatalog2(catalog1Id);
        return log2list;
    }
    /**
     * 根据二级分类id查询三级分类
     */
    //http://localhost:8082/getCatalog3?catalog2Id=16
    @RequestMapping("getCatalog3")
    public List<BaseCatalog3> getCatalog3(String catalog2Id){
        List<BaseCatalog3> log3list=manageService.getBaseCatalog3(catalog2Id);
        return log3list;
    }

    /**
     * 根据三级分类id查询平台属性
     */
    //http://localhost:8082/attrInfoList?catalog3Id=86
    @RequestMapping("attrInfoList")
    public List<BaseAttrInfo> attrInfoList(String catalog3Id){
        List<BaseAttrInfo> attrInfoList=manageService.getAttrInfoList(catalog3Id);
        return attrInfoList;
    }

    /**
     * 添加平台属性和对应的属性值
     */
    //http://localhost:8082/saveAttrInfo
    @RequestMapping("saveAttrInfo") //@RequestBody将前台传递过来的json格式转化成对象
    public String saveAttrInfo(@RequestBody BaseAttrInfo baseAttrInfo){

        manageService.saveAttrInfo(baseAttrInfo);
        return "OK";
    }

    /**
     *  修改平台属性值之回显属性值
     */
    //http://localhost:8082/getAttrValueList?attrId=96
//    @RequestMapping("getAttrValueList")
//    public List<BaseAttrValue> getAttrValueList(String attrId){
//        List<BaseAttrValue> attrValueList = manageService.getAttrValueList(attrId);
//        return attrValueList;
//    }
    //根据业务层面考虑，如果平台属性不存在
    @RequestMapping("getAttrValueList")
    public List<BaseAttrValue> getAttrValueList(String attrId){
        //先根据平台属性id查询平台属性对象
        BaseAttrInfo baseAttrInfo = manageService.getAttrInfo(attrId);
        //将平台属性中的平台属性值集合返回
        return baseAttrInfo.getAttrValueList();
    }
    /**
     * 修改平台属性值
     */
    // http://localhost:8082/saveAttrInfo 和添加平台属性值一个controller写在一起


    /**
     * 根据三级分类id查询所有商品sku列表
     */
    //http://localhost:8082/spuList?catalog3Id=61
    @RequestMapping("spuList")
    public List<SpuInfo> spuList(String catalog3Id){
        return manageService.getSpuList(catalog3Id);
    }


}

