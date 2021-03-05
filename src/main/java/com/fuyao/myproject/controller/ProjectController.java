package com.fuyao.myproject.controller;

import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @description:
 * @author: fuyao
 * @time: 2021/1/27 10:38
 */
@Api("防控计划相关接口")
@RestController
@RequestMapping("/Project")
public class ProjectController {
    @ApiOperation("防控计划处理之当前环节提交")
    @GetMapping("/submitPlan")
    public JSONObject submitPlan(@RequestParam(value = "map",required = false) Map<String,Object> map,@RequestParam(value = "userCode",required = true) String userCode,
                                 @RequestParam(value = "approvalMessage",required = true) String approvalMessage,@RequestParam(value = "planId",required = false)String planId,
                                 @RequestParam(value = "nodeId",required = false)String nodeId){
        return null;
    }

    @ApiOperation("防控计划处理之当前待处理环节查询")
    @GetMapping ("/getUserPlanNode")
    public JSONObject getUserPlanNode(@RequestParam(value = "userCode",required = true)String userCode, @RequestParam(value = "planId",required = true)String planId,
                                      @RequestParam(value = "beginTime",required = false)String beginTime, @RequestParam(value = "endTime",required = false)String endTime,
                                      @RequestParam(value = "userCity",required = false)String userCity ){
        return null;
    }
}
