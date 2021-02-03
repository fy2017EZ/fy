package com.fuyao.myproject.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiParam;
import lombok.Data;

/**
 * @description: 医疗物资
 * @author: fuyao
 * @time: 2021/1/19 10:41
 */
@Data
@ApiModel("医疗物资参数")
public class Good {
    @ApiModelProperty(value = "医疗物资编号")
    String good_id ="";
    @ApiModelProperty(value = "医疗物资名称")
    String good_name ="";
    @ApiModelProperty(value = "价格")
    String price ="";
    @ApiModelProperty(value = "型号参数")
    String quality ="";
    @ApiModelProperty(value = "数量")
    String nums ="";
    @ApiModelProperty(value = "开始时间")
    String start_time ="";
    @ApiModelProperty(value = "结束时间")
    String endTime = "";
    @ApiModelProperty(value = "一级分类")
    String category_type ="";
    @ApiModelProperty(value = "操作员")
    String operation ="";
    @ApiModelProperty(value = "二级分类")
    String category ="";
    @ApiModelProperty(value = "入库批次号")
    String warehousing_batch_number ="";
    @ApiModelProperty(value = "所在地市id")
    String cityId = "";
    @ApiModelProperty(value = "当前页码")
    String page ="";
    @ApiModelProperty(value = "每页条数")
    String rows ="";

    public String getPage() {
        return page;
    }

    public void setPage(String page) {
        this.page = page;
    }

    public String getRows() {
        return rows;
    }

    public void setRows(String rows) {
        this.rows = rows;
    }

    public String getCityId() {
        return cityId;
    }

    public void setCityId(String cityId) {
        this.cityId = cityId;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
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

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getQuality() {
        return quality;
    }

    public void setQuality(String quality) {
        this.quality = quality;
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

    public String getCategory_type() {
        return category_type;
    }

    public void setCategory_type(String category_type) {
        this.category_type = category_type;
    }

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getWarehousing_batch_number() {
        return warehousing_batch_number;
    }

    public void setWarehousing_batch_number(String warehousing_batch_number) {
        this.warehousing_batch_number = warehousing_batch_number;
    }
}
