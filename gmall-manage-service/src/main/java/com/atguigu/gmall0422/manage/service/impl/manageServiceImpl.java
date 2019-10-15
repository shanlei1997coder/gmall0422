package com.atguigu.gmall0422.manage.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.atguigu.gmall0422.bean.*;
import com.atguigu.gmall0422.config.RedisUtil;
import com.atguigu.gmall0422.manage.Constant.ManageConst;
import com.atguigu.gmall0422.manage.mapper.*;
import com.atguigu.gmall0422.service.ManageService;
import org.redisson.Redisson;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import redis.clients.jedis.Jedis;
import tk.mybatis.mapper.entity.Example;

import java.sql.Time;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;

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

    @Autowired
    private RedisUtil redisUtil;

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

    //根据skuId查询 商品基本信息 展示到商品详情页面
    @Override
    public SkuInfo getSkuInfo(String skuId) {

        return getSkuInfoFromRedisRedission(skuId);
    }

    //redission锁
    private SkuInfo getSkuInfoFromRedisRedission(String skuId) {
        Jedis jedis = null;
        SkuInfo skuInfo = null;
        RLock lock = null;
        try {
            //获取redis
            jedis = redisUtil.getJedis();
            // 设置存入redis中的key
            String skuInfoKey = ManageConst.SKUKEY_PREFIX+skuId+ManageConst.SKUKEY_SUFFIX;
            //从redis获取对应的value
            String skuInfoValue = jedis.get(skuInfoKey);
            //判断是否存在
            if(skuInfoValue==null || skuInfoValue.length()==0){
                //说明缓存中不存在
                Config config = new Config();
                config.useSingleServer().setAddress("redis://192.168.88.66:6379");
                //创建redission
                RedissonClient redisson = Redisson.create(config);

                //获得锁
                lock = redisson.getLock("mylock");
                //加锁
                //lock.lock(); 方式1
                //方式2
                //lock.lock(10, TimeUnit.SECONDS); //加锁10秒之后自动解锁
                //方式3
                //尝试加锁最多等待100s,上锁5s之后自动解锁
                boolean flag = lock.tryLock(100, 2, TimeUnit.SECONDS);
                System.out.println("flag:"+flag);
                //lock和unlock中间写业务逻辑代码
                if(flag){
                    //查询数据库
                    skuInfo = getSkuInfoFromDB(skuId);
                    //将查到的数据放入redis中
                    jedis.setex(skuInfoKey,ManageConst.SKUKEY_TIMEOUT, JSON.toJSONString(skuInfo));

                }
                return skuInfo;
            }else{
                //直接从redis中获取
                return JSON.parseObject(skuInfoValue,SkuInfo.class);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(jedis!=null){
                jedis.close();
            }
            //开锁
//            if(lock!=null){
//                lock.unlock();
//            }

        }
        return getSkuInfoFromDB(skuId);
    }

    //加分布式锁redis
    private SkuInfo getSkuInfoFromRedisLock(String skuId) {
        Jedis jedis = null;
        SkuInfo skuInfo = null;

        // 加分布式锁  解决缓存击穿
        try {
            //获取jedis
            jedis = redisUtil.getJedis();
            //定义 存在redis中的key  sku:skuId:skuInfo
            String skuInfoKey = ManageConst.SKUKEY_PREFIX+skuId+ManageConst.SKUKEY_SUFFIX;

            //从redis中获取值
            String skuInfoValue = jedis.get(skuInfoKey);

            //判断如果redis中没有 数据
            if(skuInfoValue==null || skuInfoValue.length()==0){
                //需要上锁
                //定义 锁的key
                String skuLockKey =ManageConst.SKUKEY_PREFIX+skuId+ManageConst.SKULOCK_SUFFIX;
                //定义 锁的value  UUID自定义
                String token = UUID.randomUUID().toString().replaceAll("-","");
                //上锁操作
                String lockresponse = jedis.set(skuLockKey, token, "nx", "ex", ManageConst.SKULOCK_EXPIRE_PX);

                //判断返回的数据 如果为OK 上锁成功
                if("OK".equals(lockresponse)){
                    //说明上锁成功
                    System.out.println("上锁成功");
                    //从数据库中查询数据
                    skuInfo= getSkuInfoFromDB(skuId);
                    //将 从数据库查询出的数据放入  redis中
                    jedis.setex(skuInfoKey,ManageConst.SKUKEY_TIMEOUT, JSON.toJSONString(skuInfo));

                    //删除锁
                    //jedis.del(skuLockKey);
                    // 保证删除锁的唯一性！使用lua脚本
                    String script ="if redis.call('get', KEYS[1]) == ARGV[1] then return redis.call('del', KEYS[1]) else return 0 end";
                    jedis.eval(script, Collections.singletonList(skuLockKey),Collections.singletonList(token));

                    return skuInfo;

                }else{
                    //说明有其他线程 准备操作
                    System.out.println("等待...");
                    Thread.sleep(1000);
                    //自旋 再次调用自己
                    return getSkuInfo(skuId);
                }

            }else{
                //说明 redis缓存中存在数据
                // 直接从redis中返回数据
                //将 json转化成对象
                skuInfo = JSON.parseObject(skuInfoValue, SkuInfo.class);
                return skuInfo;
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(jedis!=null){
                jedis.close();
            }

        }
        //redis宕机 直接从数据库中查
        return  getSkuInfoFromDB(skuId);
    }

    //未加锁的redis
    private SkuInfo getSkuInfoFromRedisUnlock(String skuId) {
        Jedis jedis = null;
        SkuInfo skuInfo = null;
        /*
            存在问题：
                1.redis宕机？  使用try-catch-finally 处理
                2.高并发下加锁
         */
        try {
            //优化  使用redis缓存
        /* 逻辑
            if(redis中存在){
                从redis中取数据
            }else{
                从数据库中取数据
            }
         */
            //获取jedis
            jedis = redisUtil.getJedis();
            //定义缓存中的key
            String skuInfoKey = ManageConst.SKUKEY_PREFIX+skuId+ManageConst.SKUKEY_SUFFIX;

            //判断缓存redis中是否存在该key
            if(jedis.exists(skuInfoKey)){
                //从缓存中取
                String skuJson = jedis.get(skuInfoKey);
                //将json转成 对象
                if(skuJson!=null && skuJson.length()>0) {
                    skuInfo = JSON.parseObject(skuJson, SkuInfo.class);
                }
                return skuInfo;

            }else{
                //从数据库中取
                skuInfo = getSkuInfoFromDB(skuId);
                //将数据转化成json
                String DBJson = JSON.toJSONString(skuInfo);
                //存入redis中
                //redis 五大数据类型  list队列 set交集 string 常量 hash 对象 zset 排序
                //使用string 存储
                jedis.setex(skuInfoKey,ManageConst.SKUKEY_TIMEOUT,DBJson);
                //使用hash 太麻烦也可以 适用于存储对象
                return skuInfo;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(jedis!=null){
                 jedis.close();
            }
        }

        // redis宕机后  直接查询数据库
        return getSkuInfoFromDB(skuId);
    }

    //从数据库中查询商品信息
    private SkuInfo getSkuInfoFromDB(String skuId) {
        //根据 主键 查询skuInfo
        SkuInfo skuInfo = skuInfoMapper.selectByPrimaryKey(skuId);
        skuInfo.setSkuImageList(getSkuImageList(skuId));

        //用于 es 商品上架
        //根据SkuId将 skuAttrValueList查询出来
        SkuAttrValue skuAttrValue = new SkuAttrValue();
        skuAttrValue.setSkuId(skuId);
        List<SkuAttrValue> skuAttrValueList = skuAttrValueMapper.select(skuAttrValue);
        //放到skuInfo 中返回
        skuInfo.setSkuAttrValueList(skuAttrValueList);
        //然后将 sku_Info对象 返回
        return skuInfo;
    }

    //根据skuId查询商品图片列表
    @Override
    public List<SkuImage> getSkuImageList(String skuId) {

        SkuImage skuImage = new SkuImage();
        skuImage.setSkuId(skuId);
        return skuImageMapper.select(skuImage);
    }

    // 根据skuId,spuId查询商品详情页面的销售属性
    @Override
    public List<SpuSaleAttr> getSpuSaleAttrListCheckBySku(SkuInfo skuInfo) {

        //需要多表联查  需要手写sql,xml
       return spuSaleAttrMapper.selectSpuSaleAttrListCheckBySku(skuInfo.getId(),skuInfo.getSpuId());

    }

    // 根据 spuId 查询商品切换时需要的销售属性值id和 skuId
    @Override
    public List<SkuSaleAttrValue> getSkuSaleAttrValueList(String spuId) {
        return skuSaleAttrValueMapper.selectSkuSaleAttrValueList(spuId);
    }
}

