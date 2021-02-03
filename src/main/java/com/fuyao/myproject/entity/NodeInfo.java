package com.fuyao.myproject.entity;

import java.util.List;
import java.util.Map;

/**
 * @description: 防控计划节点信息表
 * @author: fuyao
 * @time: 2021/2/3 17:52
 */
public class NodeInfo {
    String plan_id;
    String node_id;
    List<Map<String,Object>> params;
    String node_name;
    String user_code;

    public String getPlan_id() {
        return plan_id;
    }

    public void setPlan_id(String plan_id) {
        this.plan_id = plan_id;
    }

    public String getNode_id() {
        return node_id;
    }

    public void setNode_id(String node_id) {
        this.node_id = node_id;
    }

    public List<Map<String, Object>> getParams() {
        return params;
    }

    public void setParams(List<Map<String, Object>> params) {
        this.params = params;
    }

    public String getNode_name() {
        return node_name;
    }

    public void setNode_name(String node_name) {
        this.node_name = node_name;
    }

    public String getUser_code() {
        return user_code;
    }

    public void setUser_code(String user_code) {
        this.user_code = user_code;
    }
}
