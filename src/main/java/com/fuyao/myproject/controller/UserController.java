package com.fuyao.myproject.controller;

import com.alibaba.fastjson.JSONObject;
import com.fuyao.myproject.service.UserService;
import com.fuyao.myproject.util.RedisUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import redis.clients.jedis.ShardedJedis;

/**
 * @description:
 * @author: fuyao
 * @time: 2021/1/27 10:37
 */
@Api("用户相关接口")
@RestController
@RequestMapping("/User")
public class UserController {
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);
    @Autowired
    private UserService service;
    @PostMapping("/load")
    @ApiOperation("用户登录接口")
    public JSONObject load(@RequestParam(value = "username",required = true)String username,@RequestParam(value = "password",required = true)String password){
        JSONObject data = service.load(username, password);
        return data;
    }
    @PostMapping("/getUserInfo")
    @ApiOperation("获取用户信息接口")
    public JSONObject getUserInfo(@RequestParam(value = "token",required = true)String token){
        JSONObject data = service.getUserInfo(token);
        return data;
    }
}
