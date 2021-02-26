package com.fuyao.myproject.service;

import com.alibaba.fastjson.JSONObject;
import com.fuyao.myproject.entity.NodeInfo;
import com.fuyao.myproject.entity.PlanInfo;

import java.util.List;
import java.util.Map;

public interface PlanService {
    JSONObject addPlan(String planName, String userCity, String projectContent, StringBuffer projectDetail, String userCode, List<NodeInfo> nodes)throws Exception;
    JSONObject updatePlan(String planId,String projectContent,List<NodeInfo> nodes)throws Exception;
    JSONObject deletePlan(String planId,String userCode) throws Exception;
    JSONObject deletePlan(String planId);
    JSONObject selectPlan(String planId,String beginTime,String endTime,String userCode,String userCity,String page,String limit);
    JSONObject findPlanInfoByOne(Map<String,Object> map,String start,String end);
    JSONObject findPlanInfoByMap(Map<String,Object> map,String start,String end);
    JSONObject getUserPlanNode(String planId,String userCode,String beginTime,String endTime,String userCity);
}
