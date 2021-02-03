package com.fuyao.myproject.controller;

import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

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
    @ApiOperation("新增防控计划")
    @PostMapping ("/addPlan")
    public JSONObject addPlan(){
        return null;
    }
    @ApiOperation("修改防控计划")
    @PostMapping ("/updatePlan")
    public JSONObject updatePlan(){
        return null;
    }
    @ApiOperation("删除防控计划")
    @GetMapping ("/deletePlan")
    public JSONObject deletePlan(){
        return null;
    }
    @ApiOperation("查询防控计划")
    @GetMapping ("/seletePlan")
    public JSONObject seletePlan(){
        return null;
    }
    @ApiOperation("防控计划处理之当前环节提交")
    @GetMapping ("/submitPlan")
    public JSONObject submitPlan(){
        return null;
    }

    @ApiOperation("防控计划处理之当前待处理环节查询")
    @GetMapping ("/getUserPlanNode")
    public JSONObject getUserPlanNode(){
        return null;
    }
}
