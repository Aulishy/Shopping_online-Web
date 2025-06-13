package com.example.keshe.mapper;

import com.example.keshe.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;


@Mapper
public interface AuthMapper {
    // 根据手机号、密码和角色查找用户
    User findByPhoneAndPasswordAndRole(@Param("phone") String phone, @Param("password") String password, @Param("role") String role);

    // 检查手机号是否已存在
    boolean existsByPhone(@Param("phone") String phone);

    // 插入新用户
    int insertUser(User user);
}