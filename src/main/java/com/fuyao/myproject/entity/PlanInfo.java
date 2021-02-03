package com.fuyao.myproject.entity;

import java.util.List;

/**
 * @description: 防控计划信息表
 * @author: fuyao
 * @time: 2021/2/3 17:50
 */
public class PlanInfo {
    String plan_id;
    String plan_name;
    String user_city;
    String project_content;
    String project_detail;
    String project_user_code;
    List<NodeInfo> nodes;

    public List<NodeInfo> getNodes() {
        return nodes;
    }

    public void setNodes(List<NodeInfo> nodes) {
        this.nodes = nodes;
    }

    public String getPlan_id() {
        return plan_id;
    }

    public void setPlan_id(String plan_id) {
        this.plan_id = plan_id;
    }

    public String getPlan_name() {
        return plan_name;
    }

    public void setPlan_name(String plan_name) {
        this.plan_name = plan_name;
    }

    public String getUser_city() {
        return user_city;
    }

    public void setUser_city(String user_city) {
        this.user_city = user_city;
    }

    public String getProject_content() {
        return project_content;
    }

    public void setProject_content(String project_content) {
        this.project_content = project_content;
    }

    public String getProject_detail() {
        return project_detail;
    }

    public void setProject_detail(String project_detail) {
        this.project_detail = project_detail;
    }

    public String getProject_user_code() {
        return project_user_code;
    }

    public void setProject_user_code(String project_user_code) {
        this.project_user_code = project_user_code;
    }
}
