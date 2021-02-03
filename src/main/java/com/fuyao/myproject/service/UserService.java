package com.fuyao.myproject.service;

import com.alibaba.fastjson.JSONObject;
import com.fuyao.myproject.entity.UserInfo;

public interface UserService {
    JSONObject load(String username,String password);
    JSONObject getUserInfo(String token);
}
