package com.fuyao.myproject.entity;

/**
 * @description:
 * @author: fuyao
 * @time: 2021/1/20 14:30
 */
public class Schedule {
    String cfg_id ="";
    String good_id ="";
    String good_name ="";
    String warehouse_name ="";
    String current_warehouse ="";
    String nums ="";
    String start_time ="";
    String schedule_type ="";
    String schedule_status ="";
    String target_table_name ="";
    String target_place ="";
    String end_time ="";
    String warehousing_batch_number ="";
    String execute_time ="";

    public String getExecute_time() {
        return execute_time;
    }

    public void setExecute_time(String execute_time) {
        this.execute_time = execute_time;
    }

    public String getCfg_id() {
        return cfg_id;
    }

    public void setCfg_id(String cfg_id) {
        this.cfg_id = cfg_id;
    }

    public String getGood_id() {
        return good_id;
    }

    public void setGood_id(String good_id) {
        this.good_id = good_id;
    }

    public String getGood_name() {
        return good_name;
    }

    public void setGood_name(String good_name) {
        this.good_name = good_name;
    }

    public String getWarehouse_name() {
        return warehouse_name;
    }

    public void setWarehouse_name(String warehouse_name) {
        this.warehouse_name = warehouse_name;
    }

    public String getCurrent_warehouse() {
        return current_warehouse;
    }

    public void setCurrent_warehouse(String current_warehouse) {
        this.current_warehouse = current_warehouse;
    }

    public String getNums() {
        return nums;
    }

    public void setNums(String nums) {
        this.nums = nums;
    }

    public String getStart_time() {
        return start_time;
    }

    public void setStart_time(String start_time) {
        this.start_time = start_time;
    }

    public String getSchedule_type() {
        return schedule_type;
    }

    public void setSchedule_type(String schedule_type) {
        this.schedule_type = schedule_type;
    }

    public String getSchedule_status() {
        return schedule_status;
    }

    public void setSchedule_status(String schedule_status) {
        this.schedule_status = schedule_status;
    }

    public String getTarget_table_name() {
        return target_table_name;
    }

    public void setTarget_table_name(String target_table_name) {
        this.target_table_name = target_table_name;
    }

    public String getTarget_place() {
        return target_place;
    }

    public void setTarget_place(String target_place) {
        this.target_place = target_place;
    }

    public String getEnd_time() {
        return end_time;
    }

    public void setEnd_time(String end_time) {
        this.end_time = end_time;
    }

    public String getWarehousing_batch_number() {
        return warehousing_batch_number;
    }

    public void setWarehousing_batch_number(String warehousing_batch_number) {
        this.warehousing_batch_number = warehousing_batch_number;
    }
}
