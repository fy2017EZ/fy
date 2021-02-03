package com.fuyao.myproject.util;
/**
 * 自定义异常类
 */
public class ParamException extends Exception {
        //异常信息
        private String message;

        //构造函数
        public ParamException(String message){
            super(message);
            this.message = message;
        }

    }
