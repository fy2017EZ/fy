package com.fuyao.myproject.service;

import com.alibaba.fastjson.JSONObject;
import com.fuyao.myproject.entity.Good;
import com.fuyao.myproject.entity.Schedule;

import java.util.List;
import java.util.Map;

public interface GoodService {
    void saveGood(Good good);
    //医疗物资查询
    JSONObject getAllCondition(Map<String,String> map, Good good,String start,String end);
    Map<String,Object> getGood(Good good,String start,String end);
    Map<String,Object> getGoodByOne(Map<String,String> map,Good good,String start,String end);
    Map<String,Object> getGoodByMany(Map<String,String> map,Good good,String start,String end);
    //医疗物资调度
    JSONObject GoodCfgStart(Schedule schedule);
    //增删改
    JSONObject insertGoodInfo(Good good,String userId);
    JSONObject updateGoodInfo(Good good,String userId);
    JSONObject deleteGoodInfo(String goodId,String userId);
//    static Boolean format(String type,String str);
}
