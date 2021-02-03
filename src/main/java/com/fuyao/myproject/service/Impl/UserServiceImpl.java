package com.fuyao.myproject.service.Impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fuyao.myproject.entity.UserInfo;
import com.fuyao.myproject.mapper.ScheduleMapper;
import com.fuyao.myproject.mapper.UserMapper;
import com.fuyao.myproject.service.UserService;
import com.fuyao.myproject.util.AppMD5Util;
import com.fuyao.myproject.util.RedisUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @description:用户相关实现类
 * @author: fuyao
 * @time: 2021/1/27 10:42
 */
@Service
public class UserServiceImpl implements UserService {
    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);
    @Autowired
    private UserMapper mapper;
    @Autowired
    private ScheduleMapper scheduleMapper;
    private static JedisPool jedisPool = null;
    static {
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();

        // 设置最大对象数
        jedisPoolConfig.setMaxTotal(20);

        // 最大能够保持空闲状态的对象数
        jedisPoolConfig.setMaxIdle(10);

        // 超时时间
        jedisPoolConfig.setMaxWaitMillis(-1);

        // 在获取连接的时候检查有效性, 默认false
        jedisPoolConfig.setTestOnBorrow(true);

        // 在返回Object时, 对返回的connection进行validateObject校验
        jedisPoolConfig.setTestOnReturn(true);

        // 如果是集群，可以全部加入list中
//        List<JedisShardInfo> shardInfos = new ArrayList<JedisShardInfo>();
//        JedisShardInfo shardInfo = new JedisShardInfo("127.0.0.1", 6379);
//        shardInfo.setPassword("redis123.");
//        shardInfos.add(shardInfo);

        jedisPool = new JedisPool(jedisPoolConfig, "127.0.0.1",6379);

    }

    @Override
    public JSONObject load(String username, String password) {
        JSONObject data = new JSONObject();
        Jedis jedis = null;
        try{
            List<UserInfo> infos = mapper.load(username, password);
            if(!infos.isEmpty()){
                logger.info("userInfo:",infos.toString());
                //生成token
                String str = AppMD5Util.getMD5(infos.get(0).getUser_id());
                logger.info("token:"+str);
//                Map<String, String> map = new HashMap<>();
//                map.put("userId",infos.get(0).getUser_id());
//                map.put("userCode",infos.get(0).getUser_code());
//                map.put("userPassword",infos.get(0).getUser_password());
//                map.put("userName",infos.get(0).getUser_name());
//                map.put("userCity",infos.get(0).getUser_city());
//                map.put("roleId",infos.get(0).getRole_id());
                //作为key保存到数据库，并将user对象作为value保存进去
//                ShardedJedis jedis = RedisUtils.getJedis();
                Object json = JSONArray.toJSON(infos.get(0));
                String jsonStr = json.toString();
                jedis = jedisPool.getResource();
                logger.info("redis:"+jedis);
                jedis.set("LOGIN_TOKEN"+str,jsonStr);
                jedis.expire("LOGIN_TOKEN"+str,3600000);
                scheduleMapper.updateSystemValue("1","schedule_flag");
                data.put("code","200");
                data.put("token",str);
            }else{
                data.put("code","203");
                data.put("error","登录失败，请重新登录");
            }
        } catch (RuntimeException e) {
            logger.error("RuntimeException:"+e.getMessage());
            e.printStackTrace();
        }catch(Exception e){
            e.printStackTrace();
            logger.error(e.getMessage());
        }finally {
            if(jedis != null ) {
                jedisPool.close();//获取连接失败时，应该返回给pool,否则每次发生异常将导致一个jedis对象没有被回收。
            }
        }
        return data;
    }

    @Override
    public JSONObject getUserInfo(String token) {
        JSONObject data = new JSONObject();
        Jedis jedis = jedisPool.getResource();
        Boolean flag = jedis.exists("LOGIN_TOKEN" + token);
        if(flag){
//            String user0 = jedis.get("LOGIN_TOKEN" + token);
            String jsonStr = jedis.get("LOGIN_TOKEN" + token);

            UserInfo user = JSONObject.parseObject(jsonStr,UserInfo.class);
            if(!StringUtils.isEmpty(user.getUser_id())){
                UserInfo userInfo = mapper.getUserInfo(user.getUser_id());
                data.put("code","200");
                data.put("userInfo",userInfo);
                //校验数据并更新redis数据库里面的人员信息
                if(user.toString().equals(userInfo.toString())){
//                    Object json = JSONArray.toJSON(userInfo);
//                    String jsonStr2 = json.toString();
//                    jedis.set("LOGIN_TOKEN" + token,jsonStr2);
                }else{
//                    Map<Object, Object> map = new HashMap<>();
//                    map.put("userId",userInfo.getUser_id());
//                    map.put("userCode",userInfo.getUser_code());
//                    map.put("userPassword",userInfo.getUser_password());
//                    map.put("userName",userInfo.getUser_name());
//                    map.put("userCity",userInfo.getUser_city());
//                    map.put("roleId",userInfo.getRole_id());
                    Long ttl = jedis.ttl("LOGIN_TOKEN" + token);
                    if(ttl>10000){
                        Object json = JSONArray.toJSON(userInfo);
                        String jsonStr2 = json.toString();
                        jedis.set("LOGIN_TOKEN" + token,jsonStr2);
                    }else{
                        data.put("msg","token即将过期，过期时间:"+ttl);
                    }
                }
            }else{
                data.put("msg","用户信息不全,请核实");
            }
        }else{
            data.put("error","token已失效，无法获取用户信息");
        }
        return data;
    }


}
