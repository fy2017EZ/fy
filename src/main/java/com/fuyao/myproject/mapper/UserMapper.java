package com.fuyao.myproject.mapper;

import com.fuyao.myproject.entity.UserInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface UserMapper {
    @Select("select * from Medical_user_info where user_code =#{username} and user_password = #{password} ")
    List<UserInfo> load(String username,String password);
    @Select("select * from Medical_user_info where user_id = #{userId}")
    UserInfo getUserInfo(String userId);
}
