package com.fuyao.myproject.controller;

import com.alibaba.fastjson.JSONObject;
import com.fuyao.myproject.entity.NodeInfo;
import com.fuyao.myproject.service.PlanService;
import com.fuyao.myproject.util.ParamException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @description:
 * @author: fuyao
 * @time: 2021/2/3 17:57
 */
@Api("防控计划相关接口")
@RequestMapping("/PlanController")
@RestController
public class PlanController {
    private static final Logger logger = LoggerFactory.getLogger(PlanController.class);
    @Autowired
    private PlanService planService;

    @ApiOperation("新增防控计划")
    @PostMapping ("/addPlan")
    public JSONObject addPlan(@RequestParam(value = "planName",required = false)String planName, @RequestParam(value = "userCity",required = true)String userCity,
                              @RequestParam(value = "projectContent",required = false)String projectContent, @RequestParam(value = "projectDetail",required = false)StringBuffer projectDetail,
                              @RequestParam(value = "userCode",required = true)String userCode, @RequestParam(value = "nodes",required = true)List<NodeInfo> nodes){
        JSONObject data = new JSONObject();
        try{
            data = planService.addPlan(planName,userCity,projectContent,projectDetail,userCode,nodes);
        }catch(ParamException e){
            e.printStackTrace();
            logger.error(e.getMessage());
            data.put("code","202");
            data.put("msg",e.getMessage());
        }catch (Exception e){
            e.printStackTrace();
            logger.error(e.getMessage());
            data.put("code","203");
        }

        return data;
    }
    @ApiOperation("修改防控计划")
    @PostMapping ("/updatePlan")
    public JSONObject updatePlan(@RequestParam(value = "planId",required = true)String planId,@RequestParam(value = "nodes",required = true)List<NodeInfo> nodes,
                                 @RequestParam(value = "projectContent",required = false)String projectContent){
        JSONObject data = new JSONObject();
        try{
            data = planService.updatePlan(planId,projectContent,nodes);
        }catch(ParamException e){
            e.printStackTrace();
            logger.error(e.getMessage());
            data.put("code","202");
            data.put("msg",e.getMessage());
        }catch (Exception e){
            e.printStackTrace();
            data.put("code","203");
            logger.error(e.getMessage());
        }
        return data;
    }
    @ApiOperation("删除防控计划")
    @GetMapping ("/deletePlan")
    public JSONObject deletePlan(@RequestParam(value = "planId",required = true)String planId,@RequestParam(value = "userCode",required = false)String userCode){
        JSONObject data = new JSONObject();
        try{
            data = planService.deletePlan(planId,userCode);
        }catch(ParamException e){
            e.printStackTrace();
            logger.error(e.getMessage());
            data.put("code","202");
            data.put("msg",e.getMessage());
        }catch (Exception e){
            e.printStackTrace();
            logger.error(e.getMessage());
            data.put("code","203");
        }
        return data;
    }
    @ApiOperation("查询防控计划")
    @GetMapping ("/seletePlan")
    public JSONObject seletePlan(@RequestParam(value = "planId",required = true)String planId,@RequestParam(value = "beginTime",required = false)String beginTime,
                                 @RequestParam(value = "endTime",required = false)String endTime,@RequestParam(value = "userCode",required = false)String userCode,
                                 @RequestParam(value = "userCity",required = false)String userCity,@RequestParam(value = "page",required = true)String page,
                                 @RequestParam(value = "limit",required = true)String limit){
        return null;
    }
    @ApiOperation("防控计划处理之当前环节提交")
    @GetMapping ("/submitPlan")
    public JSONObject submitPlan(){
        return null;
    }

    @ApiOperation("防控计划处理之当前待处理环节查询")
    @GetMapping ("/getUserPlanNode")
    public JSONObject getUserPlanNode(@RequestParam(value = "userCode",required = true)String userCode,@RequestParam(value = "planId",required = true)String planId,
                                      @RequestParam(value = "beginTime",required = false)String beginTime, @RequestParam(value = "endTime",required = false)String endTime,
                                      @RequestParam(value = "userCity",required = false)String userCity ){
        return null;
    }
}
