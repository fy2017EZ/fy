package com.fuyao.myproject.util;

import com.alibaba.druid.util.StringUtils;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
@Configuration
public class SpringContextUtil implements ApplicationContextAware {
    private static ApplicationContext context = null;


    //设置上下文
    @Override
    public void setApplicationContext(ApplicationContext applicationContext)
            throws BeansException {
        SpringContextUtil .context = applicationContext;
    }

    //获取上下文
    public static ApplicationContext getApplicationContext(){
        return context;
    }


    // 通过名字获取上下文的Bean
    public static <T> T getBean(String beanName) {
        return (T) context.getBean(beanName);
    }

    //通过类型获取上下文的bean
    public static <T> T getBean(Class<T> requiredType){
        return context.getBean(requiredType);
    }


    // 国际化使用
    public static String getMessage(String key) {
        return context.getMessage(key, null, Locale.getDefault());
    }

    // 获取当前环境
    public static String[] getActiveProfiles() {
        return context.getEnvironment().getActiveProfiles();
    }

    // 判断当前环境是否为test/local
    public static boolean isDevEnv(){
        String[] activeProfiles = getActiveProfiles();
        if (activeProfiles.length<1){
            return false;
        }
        for (String activeProfile : activeProfiles) {
            if (StringUtils.equals(activeProfile,"dev")){
                return true;
            }
        }
        return false;
    }
}
