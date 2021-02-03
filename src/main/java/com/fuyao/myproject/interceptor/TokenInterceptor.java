package com.fuyao.myproject.interceptor;

import com.alibaba.fastjson.JSONObject;
import com.fuyao.myproject.entity.UserInfo;
import com.fuyao.myproject.util.RedisUtils;
import com.fuyao.myproject.util.SpringContextUtil;
import liquibase.pro.packaged.R;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import redis.clients.jedis.ShardedJedis;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Method;

/**
 * @description: token 验证拦截器
 * @author: fuyao
 * @time: 2021/1/27 11:18
 */
public class TokenInterceptor implements HandlerInterceptor {
//    @Override
//	public boolean preHandle(HttpServletRequest req, HttpServletResponse resp, Object obj) throws Exception {
////		WebContextUtils.setRequest(req);
////		WebContextUtils.setResponse(resp);
//        String requestURI = req.getRequestURI().replace(req.getContextPath(), "");
//        System.out.println(requestURI);
//		// 检查签名
//		if (!validToken(req, obj)) {
//			return false;
//		}
//		return true;
//	}
    public static boolean doFilter(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json;charset=UTF-8");
        //默认为false，拦截获取token
        String token = null;
        try {
            token = request.getHeader("Authorization");
        } catch (NullPointerException e) {
            e.printStackTrace();
        }

        if (StringUtils.isEmpty(token)) {
            ShardedJedis jedis = RedisUtils.getJedis();
            String s = jedis.get("LOGIN_TOKEN" + token);
            //获取redis中token的key
//            String key = RedisKeyConfig.LOGIN_TOKEN + token;
            //获取redis中token的key储存的value
//            String user = JedisUtil.getInstance().STRINGS.get(key);
            UserInfo user = (UserInfo) JSONObject.parse(s);
            if (user != null) {
                //请求进来就刷新一次token的有效期
//                JedisUtil.getInstance().expire(key, RedisKeyConfig.LOGIN_TIME);
//                JedisUtil.getInstance().expire(RedisKeyConfig.LOGIN_USER +
//                        new JSONObject(user).getInt("id"), RedisKeyConfig.LOGIN_TIME);
                return true;
            } else {
                response.getWriter().print(new JSONObject().put("error","登录有效期已过，请重新登陆"));
                return false;
            }
        } else {
            response.getWriter().print(new JSONObject().put("error","您还没有登录，请登陆"));
            return false;
        }
    }
}
