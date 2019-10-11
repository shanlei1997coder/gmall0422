package com.atguigu.gmall0422.manage.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.atguigu.gmall0422.bean.*;
import com.atguigu.gmall0422.manage.mapper.*;
import com.atguigu.gmall0422.service.ManageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

/**
 * @program: gmall0422
 * @description: 商品管理接口实现类
 * @author: Mr.Lei
 * @create: 2019-10-08 18:51
 **/
@Service
public class manageServiceImpl implements ManageService{

    @Autowired
    private BaseCatalog1Mapper baseCatalog1Mapper;

    @Autowired
    private BaseCatalog2Mapper baseCatalog2Mapper;

    @Autowired
    private BaseCatalog3Mapper baseCatalog3Mapper;

    @Autowired
    private BaseAttrInfoMapper baseAttrInfoMapper;

    @Autowired
    private BaseAttrValueMapper baseAttrValueMapper;

    @Autowired
    private SpuInfoMapper spuInfoMapper;

    @Autowired
    private BaseSaleAttrMapper baseSaleAttrMapper;

    @Autowired
    private SpuSaleAttrMapper spuSaleAttrMapper;

    @Autowired
    private SpuSaleAttrValueMapper spuSaleAttrValueMapper;

    @Autowired
    private SpuImageMapper spuImageMapper;

    @Autowired
    private SkuAttrValueMapper skuAttrValueMapper;

    @Autowired
    private SkuImageMapper skuImageMapper;

    @Autowired
    private SkuSaleAttrValueMapper skuSaleAttrValueMapper;

    @Autowired
    private SkuInfoMapper skuInfoMapper;


    //查询一级分类
    @Override
    public List<BaseCatalog1> getBaseCatalog1() {
        return baseCatalog1Mapper.selectAll();
    }

    //根据一级分类Id查询二级分类
    @Override
    public List<BaseCatalog2> getBaseCatalog2(String catalog1Id) {
        //select * from base_catalog2 where catalog1Id = ?
        BaseCatalog2 baseCatalog2 = new BaseCatalog2();
        baseCatalog2.setCatalog1Id(catalog1Id);
        return baseCatalog2Mapper.select(baseCatalog2);
    }

    //根据二级分类id查询三级分类
    @Override
    public List<BaseCatalog3> getBaseCatalog3(String catalog2Id) {
        //select * from base_catalog3 where catalog2_id=?
        Example example = new Example(BaseCatalog3.class);
        //第一个参数是实体类的属性，
        //第二个参数是对应的值
        example.createCriteria().andEqualTo("catalog2Id",catalog2Id);
        return baseCatalog3Mapper.selectByExample(example);

    }

    //根据三级分类id查询平台属性
    @Override
    public List<BaseAttrInfo> getAttrInfoList(String catalog3Id) {
//        BaseAttrInfo baseAttrInfo = new BaseAttrInfo();
//        baseAttrInfo.setCatalog3Id(catalog3Id);
//        return baseAttrInfoMapper.select(baseAttrInfo);
        return baseAttrInfoMapper.selectAttrInfoList(catalog3Id);
    }

    //添加平台属性和对应的属性值
    //修改平台属性值
    @Override
    @Transactional  //用在方法上，表明该方法开启事务  mysql默认不开启事务,oracle默认开启事务
    public void saveAttrInfo(BaseAttrInfo baseAttrInfo) {

        //首先，需要判断 是添加还是修改平台属性
        //根据是否存在平台属性id判断
        if(baseAttrInfo.getId()!=null && baseAttrInfo.getId().length()>0){
            //存在属性Id  修改操作
            baseAttrInfoMapper.updateByPrimaryKeySelective(baseAttrInfo);
        }else{
            //不存在  添加操作
            baseAttrInfoMapper.insert(baseAttrInfo);
        }

        //制作一个异常
//        int i  =1/0;

        //如果是修改的话  先删除平台属性值再进行添加操作
        //如果是添加的话  不存在attr_id所以也不会进行删除操作
        //根据平台属性id删除平台属性值
        //delete from base_attr_value where attr_id= ?
        Example example = new Example(BaseAttrValue.class);
        example.createCriteria().andEqualTo("attrId",baseAttrInfo.getId());
        baseAttrValueMapper.deleteByExample(example);

        //再添加平台属性值
        //先获取到封装在平台属性baseAttrInfo中的平台属性值集合
        List<BaseAttrValue> attrValueList = baseAttrInfo.getAttrValueList();
        //循环遍历进行添加
        for (BaseAttrValue baseAttrValue : attrValueList) {
            //添加之前需要向属性对象中设置对应父级平台属性的id
            //需要在实体类上添加主键自动生成策略注解
            baseAttrValue.setAttrId(baseAttrInfo.getId());
            baseAttrValueMapper.insert(baseAttrValue);
        }

    }

    //修改 平台属性值之根据平台id查询对应的平台属性值  回显
    @Override
    public List<BaseAttrValue> getAttrValueList(String attrId) {
        Example example = new Example(BaseAttrValue.class);
        example.createCriteria().andEqualTo("attrId",attrId);
        return baseAttrValueMapper.selectByExample(example);

    }

    //根据平台属性获取平台属性对象
    @Override
    public BaseAttrInfo getAttrInfo(String attrId) {
        //先判断平台属性对象是否存在
        BaseAttrInfo baseAttrInfo = baseAttrInfoMapper.selectByPrimaryKey(attrId);
        //因为数据库中的平台属性中没有平台属性值集合的字段，所以需要手动设置
        //调用上面的方法 根据平台属性Id查询平台属性集合
        baseAttrInfo.setAttrValueList(getAttrValueList(attrId));
        return baseAttrInfo;
    }

    //根据三级分类id查询商品SKU列表
    @Override
    public List<SpuInfo> getSpuList(String catalog3Id) {
        SpuInfo spuInfo = new SpuInfo();
        spuInfo.setCatalog3Id(catalog3Id);
        return spuInfoMapper.select(spuInfo);
    }

    //查询所有商品基本属性
    @Override
    public List<BaseSaleAttr> getSaleAttrList() {

        return baseSaleAttrMapper.selectAll();
    }

    //保存商品SPU基本信息
    @Override
    public void saveSpuInfo(SpuInfo spuInfo) {
        //需要分别向四个数据库中添加数据

        //spu_info
        spuInfoMapper.insertSelective(spuInfo);

        //spu_image
        //先从spu_info中取出文件的url集合
        List<SpuImage> spuImageList = spuInfo.getSpuImageList();
        //判断不为空  顺序不能反过来，否则会出现nullPointerException
        if(spuImageList!=null && spuImageList.size()>0) {
            for (SpuImage spuImage : spuImageList) {
                //每个表都通过spu_id关联
                spuImage.setSpuId(spuInfo.getId());
                spuImageMapper.insertSelective(spuImage);
            }
        }

        //spu_sale_attr
        //先从 spu_info中取出存放商品销售属性的集合
        List<SpuSaleAttr> spuSaleAttrList = spuInfo.getSpuSaleAttrList();
        if(spuSaleAttrList!=null &&spuSaleAttrList.size()>0) {
            for (SpuSaleAttr spuSaleAttr : spuSaleAttrList) {
                //将spu_id商品id 设置到对象中
                spuSaleAttr.setSpuId(spuInfo.getId());
                spuSaleAttrMapper.insertSelective(spuSaleAttr);

                //spu_sale_attr_value
                //将 封装在商品销售属性中的销售属性值取出来
                List<SpuSaleAttrValue> spuSaleAttrValueList = spuSaleAttr.getSpuSaleAttrValueList();
                if(spuSaleAttrValueList!=null && spuSaleAttrValueList.size()>0) {
                    for (SpuSaleAttrValue spuSaleAttrValue : spuSaleAttrValueList) {
                        spuSaleAttrValue.setSpuId(spuInfo.getId());
                        spuSaleAttrValueMapper.insertSelective(spuSaleAttrValue);
                    }
                }
            }
        }

    }

    //根据spuId查询商品下的图品列表
    @Override
    public List<SpuImage> getSpuImageList(String spuId) {
        SpuImage spuImage = new SpuImage();
        spuImage.setSpuId(spuId);
        return spuImageMapper.select(spuImage);
    }

    // 根据spuId查询商品销售属性和销售属性值
    @Override
    public List<SpuSaleAttr> getSpuSaleAttrList(String spuId) {

        return spuSaleAttrMapper.selectSpuSaleAttrList(spuId);
    }

    //保存sku的基本信息
    @Override
    @Transactional  //添加事务
    public void saveSkuInfo(SkuInfo skuInfo) {

        //需要向四个表中添加数据
        //sku_info
        skuInfoMapper.insertSelective(skuInfo);

        //sku_image
        //需要先将封装在sku_info中的图片列表取出来
        List<SkuImage> skuImageList = skuInfo.getSkuImageList();
        //判断不为空
        if(skuImageList!=null && skuImageList.size()>0){
            for (SkuImage skuImage : skuImageList) {
                //前端传入的参数没有sku_id  需要手动设置sku_id
                skuImage.setSkuId(skuInfo.getId());
                skuImageMapper.insertSelective(skuImage);
            }
        }

        //sku_attr_value
        //从sku_info中取出
        List<SkuAttrValue> skuAttrValueList = skuInfo.getSkuAttrValueList();
        if(skuAttrValueList!=null && skuAttrValueList.size()>0){
            for (SkuAttrValue skuAttrValue : skuAttrValueList) {
                skuAttrValue.setSkuId(skuInfo.getId());
                skuAttrValueMapper.insertSelective(skuAttrValue);
            }
        }

        //sku_sale_attr_value
        List<SkuSaleAttrValue> skuSaleAttrValueList = skuInfo.getSkuSaleAttrValueList();
        if(skuSaleAttrValueList!=null && skuSaleAttrValueList.size()>0){
            for (SkuSaleAttrValue skuSaleAttrValue : skuSaleAttrValueList) {
                skuSaleAttrValue.setSkuId(skuInfo.getId());
                skuSaleAttrValueMapper.insertSelective(skuSaleAttrValue);
            }
        }

    }
}

