package com.atguigu.gmall0422.user.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.atguigu.gmall0422.bean.UserAddress;
import com.atguigu.gmall0422.bean.UserInfo;
import com.atguigu.gmall0422.user.mapper.UserAddressMapper;
import com.atguigu.gmall0422.user.mapper.UserInfoMapper;
import com.atguigu.gmall0422.service.UserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

/**
 * @program: gmall0422
 * @description: 接口实现类
 * @author: Mr.Lei
 * @create: 2019-10-03 20:19
 **/
@Service  //alibaba.dubbo包的  作为提供端
public class UserInfoServiceImpl implements UserInfoService {

    @Autowired
    private UserInfoMapper userInfoMapper;

    @Autowired
    private UserAddressMapper userAddressMapper;


    @Override
    public List<UserInfo> getAll() {
        return userInfoMapper.selectAll();
    }

    @Override
    public List<UserInfo> getUserByLoginName(UserInfo userInfo) {
        return null;
    }

    @Override
    public void addUser(UserInfo userInfo) {

    }

    @Override
    public void updateUser(UserInfo userInfo) {

    }

    @Override
    public void deleteUser(UserInfo userInfo) {

    }

    //根据用户id查询用户地址
    @Override
    public List<UserAddress> getAddressByUserId(String userid) {
        //select
//        UserAddress userAddress = new UserAddress();
//        userAddress.setUserId(userid);
//        return userAddressMapper.select(userAddress);
        //select by example
        Example example = new Example(UserAddress.class);
        example.createCriteria().andEqualTo("userId", userid);
        return userAddressMapper.selectByExample(example);

    }



}

