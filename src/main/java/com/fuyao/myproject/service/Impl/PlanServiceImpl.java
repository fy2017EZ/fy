package com.fuyao.myproject.service.Impl;

import com.alibaba.fastjson.JSONObject;
import com.fuyao.myproject.entity.NodeInfo;
import com.fuyao.myproject.entity.PlanInfo;
import com.fuyao.myproject.entity.UserInfo;
import com.fuyao.myproject.mapper.PlanMapper;
import com.fuyao.myproject.mapper.UserMapper;
import com.fuyao.myproject.service.PlanService;
import com.fuyao.myproject.util.ParamException;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service
public class PlanServiceImpl implements PlanService {
    private static final Logger logger = LoggerFactory.getLogger(PlanServiceImpl.class);

    @Autowired
    private PlanMapper mapper;

    @Autowired
    private UserMapper userMapper;
    @Override
    public JSONObject addPlan(String planName, String userCity, String projectContent, StringBuffer projectDetail, String userCode, List<NodeInfo> nodes) throws Exception {
        JSONObject data = new JSONObject();
        Integer planId = (int) Math.random()*1000000+1;
        PlanInfo planInfo = new PlanInfo();
        planInfo.setPlan_id(String.valueOf(planId));
//        planName!=null?planInfo.setPlan_name(planName):planInfo.setPlan_name(planId+"-未命名计划");
        if(planName!=null&&!planName.equals("")){
            planInfo.setPlan_name(planName);
        }else{
            planInfo.setPlan_name(String.valueOf(planId)+"-未命名计划");
        }
        planInfo.setUser_city(userCity);
        planInfo.setProject_content(projectContent==null?"":projectContent);
        planInfo.setProject_detail(projectDetail==null?"":projectDetail.toString());
        planInfo.setProject_user_code(userCode);
        mapper.savePlanInfo(planInfo);

        for(int i = 0;i< nodes.size();i++){
            String paramKey = "";
            String paramValue = "";
            for (Map map:nodes.get(i).getParams()) {
                for (Object o:map.entrySet()) {
                    Map.Entry<String,Object> entry = (Map.Entry<String,Object>)o;
                    paramKey = paramKey+entry.getKey()+",";
                    paramValue=paramValue+entry.getValue()+",";
                }
            }
            nodes.get(i).setNode_name(nodes.get(i).getNode_name()==null?"":nodes.get(i).getNode_name());
            if(StringUtils.isEmpty(nodes.get(i).getUser_code())){
                throw new ParamException("人员配置信息不全，请输入正确的配置信息");
            }
            mapper.saveNodeInfo(String.valueOf(planId),String.valueOf(planId)+"-"+String.valueOf(i+1),paramKey,paramValue,nodes.get(i).getNode_name(),nodes.get(i).getUser_code());
        }
        data.put("code","200");
        return data;
    }

    @Override
    public JSONObject updatePlan(String planId, String projectContent, List<NodeInfo> nodes) throws Exception {
        JSONObject data = new JSONObject();
            if(!StringUtils.isEmpty(projectContent)){
                mapper.updatePlanInfo(projectContent,planId);
            }else{
                for(int i = 0;i<nodes.size();i++){
                    String paramKey = "";
                    String paramValue = "";
                    for (Map map:nodes.get(i).getParams()) {
                        for (Object o:map.entrySet()) {
                            Map.Entry<String,Object> entry = (Map.Entry<String,Object>)o;
                            paramKey = paramKey+entry.getKey()+",";
                            paramValue=paramValue+entry.getValue()+",";
                        }
                    }
                    nodes.get(i).setNode_name(nodes.get(i).getNode_name()==null?"":nodes.get(i).getNode_name());
                    if(StringUtils.isEmpty(nodes.get(i).getUser_code())){
                        throw new ParamException("人员配置信息不全，请输入正确的配置信息");
                    }
                    mapper.updateNodes(planId,nodes.get(i).getNode_id(),paramKey,paramValue,nodes.get(i).getNode_name(),nodes.get(i).getUser_code());
                }
                data.put("code","200");
            }

        return data;
    }

    @Override
    public JSONObject deletePlan(String planId, String userCode) throws Exception{
        JSONObject data = new JSONObject();
        //获取权限
        List<UserInfo> userInfos = userMapper.getUserInfoByUserCode(userCode);
        if(userInfos!=null&&userInfos.size()>0){
            if(userInfos.get(0).getRole_id().equals("1")){
                mapper.deleteNodeInfo(planId);
                mapper.deletePlanInfoById(planId);
                data.put("code","200");
            }else{
                throw new ParamException("权限不足");
            }
        }else{
            throw new ParamException("人员信息输入有误，数据库中查无此人");
        }
        return data;
    }

    @Override
    public JSONObject deletePlan(String planId) {
        return null;
    }

    @Override
    public JSONObject selectPlan(String planId, String beginTime, String endTime, String userCode, String userCity,String page,String limit) {
        JSONObject data = new JSONObject();
        Map<String, Object> map = new HashMap<>();
        if(!StringUtils.isEmpty(planId)){
            map.put("plan_id",planId);
        }
        if(!StringUtils.isEmpty(beginTime)){
            map.put("create_time",beginTime);
        }
        if(!StringUtils.isEmpty(endTime)){
            map.put("endTime",endTime);
        }
        if(!StringUtils.isEmpty(userCity)){
            map.put("user_city",userCity);
        }
        Integer start = (Integer.parseInt(page)-1)*Integer.parseInt(limit)+1;
        Integer end = (Integer.parseInt(page)-1)*Integer.parseInt(limit);
        List<PlanInfo> planInfos = null;
        Integer count = 0;
        switch (map.size()){
            case 0:
                if(userMapper.getUserInfoByUserCode(userCode).get(0).getRole_id().equals(1)){
                    planInfos = mapper.findAllPlan(start + "", end + "");
                    count = mapper.findAllPlanCount();
                }else{
                    planInfos = mapper.findPlanByUserCode(userCode,start+"",end+"");
                    count = mapper.findPlanByUserCodeCount(userCode);
                };break;
            case 4:
                if(userMapper.getUserInfoByUserCode(userCode).get(0).getRole_id().equals(1)){
                    planInfos = mapper.findAllPlan(start + "", end + "");
                    count = mapper.findAllPlanCount();
                }else{
                    planInfos = mapper.findPlanByUserCode(userCode,start+"",end+"");
                    count = mapper.findPlanByUserCodeCount(userCode);
                };break;
            case 1:
                findPlanInfoByOne(map,start+"",end+"");break;
            default:
                findPlanInfoByMap(map,start+"",end+"");break;
        }
        return null;
    }

    @Override
    public JSONObject findPlanInfoByOne(Map<String, Object> map,String start,String end) {

        return null;
    }

    @Override
    public JSONObject findPlanInfoByMap(Map<String, Object> map,String start,String end) {
        return null;
    }

    @Override
    public JSONObject getUserPlanNode(String planId, String userCode, String beginTime, String endTime, String userCity) {
        return null;
    }
}
