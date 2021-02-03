package com.fuyao.myproject.controller;

import com.alibaba.fastjson.JSONObject;
import com.fuyao.myproject.entity.Good;
import com.fuyao.myproject.entity.Schedule;
import com.fuyao.myproject.service.GoodService;
import com.fuyao.myproject.util.ExcelUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @description:
 * @author: fuyao
 * @time: 2021/1/19 10:45
 */
@Api("医疗物资接口")
@RestController
@RequestMapping("/GoodController")
public class GoodsController {
    private static final Logger logger = LoggerFactory.getLogger(GoodsController.class);
    @Autowired
    private GoodService service;
    /**
     * @Description 批量导入信息接口
     * @param request
     * @param response
     * @return
     */
    @ApiOperation("批量导入医疗物资信息接口")
    @PostMapping("/batchImport")
    public JSONObject batchImport(HttpServletRequest request, HttpServletResponse response){

        JSONObject data = new JSONObject();
//        String processUserCode = request.getParameter("processUserCode")==null?"":request.getParameter("processUserCode");
//        FileOutputStream outputStream = null;
        try {
//            if (!(request instanceof MultipartHttpServletRequest)) {
//                data.put("msg","错误：请求类型错误");
//            }
//            // 表单数据和图片一起提交给后台,图片是以二进制对象形式
//            MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
//            MultipartFile file = multipartRequest.getFile("file");
//            //写入到临时文件夹内
////            String path = this.getClass().getResource("/").getPath().substring(1,this.getClass().getResource("/").getPath().length());
//
//            String path = request.getSession().getServletContext().getRealPath("");
//            logger.info("path:"+path);
//            outputStream = new FileOutputStream(new File(path+"importExcel") + ".xlsx");
//            outputStream.write(file.getBytes());
            String path = "C:\\Users\\FU\\Desktop\\";
            List<Map<String, String>> mapList = ExcelUtil.readExcel(path+"Medical.xls", 0);
            for (Map<String,String> map:mapList) {
                Good goods = new Good();
                goods.setGood_id(map.get("GOOD_ID"));
                goods.setCategory(map.get("CATEGORY"));
                goods.setCategory_type(map.get("CATEGORY_TYPE"));
                goods.setGood_name(map.get("GOOD_NAME"));
                goods.setNums(map.get("NUMS"));
                goods.setOperation(map.get("OPERATION"));
                goods.setPrice(map.get("PRICE"));
                goods.setQuality(map.get("QUALITY"));
                goods.setStart_time(map.get("START_TIME"));
                goods.setWarehousing_batch_number(map.get("WAREHOUSING_BATCH_NUMBER"));
                service.saveGood(goods);
            }
//            SimpleDateFormat dateFormat = new SimpleDateFormat(" yyyyMMdd ");
//            String currentDate =   dateFormat.format( new Date() );
//            Integer batch = (int)((Math.random()*9+1)*100000);
////            ftpFileUtil.uploadFile("/data/ftp_whcc/"+processId,file.getInputStream(),fileName,"10.76.220.204",21,"ftp_whcc","user@ftp01");
//            if(!StringUtils.isEmpty(batchFlag)&& !batchFlag.equals("0")){
//                ftpFileUtil.uploadFile("/data/ftp_whcc/"+batch.toString(),file.getInputStream(),fileName,"10.76.220.204",21,"ftp_whcc","user@ftp01");
////                ftpFileUtil.uploadFile("/root/fuyao/whcc_ftp/"+batch.toString(),file.getInputStream(),fileName,"20.26.20.14",21,"root","r00tadmin");
//            }else{
//                ftpFileUtil.uploadFile("/data/ftp_whcc/"+processId,file.getInputStream(),fileName,"10.76.220.204",21,"ftp_whcc","user@ftp01");
////                ftpFileUtil.uploadFile("/root/fuyao/whcc_ftp/"+processId,file.getInputStream(),fileName,"20.26.20.14",21,"root","r00tadmin");
//            }
//            data.put("code","0");
//            if(StringUtils.isEmpty(batchFlag)|| batchFlag.equals("0")){
////                formService.saveFileInfo(processId,fileType,fileName,"/root/fuyao/whcc_ftp/"+processId,processUserCode);
//                formService.saveFileInfo(processId,fileType,fileName,"/data/ftp_whcc/"+processId,processUserCode);
//            }else if(batchFlag.equals("1")){
//                for (String id:Ids) {
////                    formService.saveFileInfo(id,fileType,fileName,"/root/fuyao/whcc_ftp/"+batch.toString(),processUserCode);
//                    formService.saveFileInfo(id,fileType,fileName,"/data/ftp_whcc/"+batch.toString(),processUserCode);
//                }
//            }

        }catch (Exception e){
            e.printStackTrace();
            logger.error(e.getMessage());
            data.put("msg", "error,请联系安全管理员调试");
        }
        return data;
    }
    /**
     * @Description 根据条件查询所有的医疗物资信息
     * */
    @ApiOperation("根据条件查询医疗物资信息接口")
    @PostMapping("/findAllGood")
    public JSONObject findAllGood(@RequestParam(value = "goodId",required = false) String goodId, @RequestParam(value = "goodName",required = false) String goodName,
                                  @RequestParam(value = "beginTime",required = false) String beginTime,@RequestParam(value = "endTime",required = false) String endTime,
                                  @RequestParam(value = "categotyType",required = false) String categotyType,@RequestParam(value = "category",required = false) String category,
                                  @RequestParam(value = "batchCode",required = false) String batchCode,@RequestParam(value = "cityId",required =true) String cityId,
                                  @RequestParam(value = "page",required =true) String page,@RequestParam(value = "rows",required =true) String rows){
        Good good = new Good();
        Map<String, String> map = new HashMap<>();
//        String goodId =  request.getParameter("goodId")==null?"":request.getParameter("goodId");
       if(!StringUtils.isEmpty(goodId)){
           map.put("good_id",goodId);
           good.setGood_id(goodId);
       }
//       String goodName = request.getParameter("goodName")==null?"":request.getParameter("goodName");
        if(!StringUtils.isEmpty(goodName)){
            map.put("good_name",goodName);
            good.setGood_name(goodName);
        }
//       String beginTime = request.getParameter("beginTime")==null?"":request.getParameter("beginTime");
        if(!StringUtils.isEmpty(beginTime)){
            map.put("start_time",beginTime);
            good.setStart_time(beginTime);
        }
//       String endTime = request.getParameter("endTime")==null?"":request.getParameter("endTime");
        good.setEndTime(endTime);
//       String categotyType = request.getParameter("categoryType")==null?"":request.getParameter("categoryTYpe");
        if(!StringUtils.isEmpty(categotyType)){
            map.put("category_type",categotyType);
            good.setCategory_type(categotyType);
        }
//       String category = request.getParameter("category")==null?"":request.getParameter("category");
        if(!StringUtils.isEmpty(category)){
            map.put("category",category);
            good.setCategory(category);
        }
//       String batchCode = request.getParameter("batchCode")==null?"":request.getParameter("batchCode");
        if(!StringUtils.isEmpty(batchCode)){
            map.put("warehousing_batch_number",batchCode);
            good.setWarehousing_batch_number(batchCode);
        }
//       String cityId = request.getParameter("cityId")==null?"":request.getParameter("cityId");
        good.setCityId(cityId);
//        String page = request.getParameter("page")==null?"":request.getParameter("page");
//        String rows = request.getParameter("limit")==null?"":request.getParameter("limit");
        Integer start = (Integer.parseInt(page)-1)*Integer.parseInt(rows)+1;
        Integer end = (Integer.parseInt(page)*Integer.parseInt(rows));
        JSONObject data = service.getAllCondition(map, good, start + "", end + "");
        return data;
    }
    /**
     * @Description 调度建立
     * */
    @ApiOperation("医疗物资调度创建接口")
    @PostMapping("/cfgCreate")
    public JSONObject cfgCreate(@RequestParam(value = "goodId",required = true)String goodId,@RequestParam(value = "goodName",required = false)String goodName,
                                @RequestParam(value = "houseName",required = true)String houseName,@RequestParam(value = "houseCity",required = true)String houseCity,
                                @RequestParam(value = "number",required = true)String number,@RequestParam(value = "scheduleType",required = true)String scheduleType,
                                @RequestParam(value = "targetPlace",required = true)String targetPlace,@RequestParam(value = "batchCode",required = false)String batchCode,
                                @RequestParam(value = "startTime",required = false)String startTime){
        //
        Schedule schedule = new Schedule();
        schedule.setGood_id(goodId);
        schedule.setGood_name(goodName);
        schedule.setWarehouse_name(houseName);
        schedule.setCurrent_warehouse(houseCity);
        schedule.setNums(number);
        schedule.setSchedule_type(scheduleType);
        schedule.setTarget_place(targetPlace);
        schedule.setWarehousing_batch_number(batchCode);
        schedule.setExecute_time(startTime);
        JSONObject data = service.GoodCfgStart(schedule);
        return data;
    }
    /**
     * 新增单类医疗物资接口
     * */
    @ApiOperation("新增单类医疗物资接口")
    @PostMapping("/addGoods")
    public JSONObject addGoods(@RequestParam(value = "GoodName",required = true)String GoodName,@RequestParam(value = "quality",required = true)String quality,
                               @RequestParam(value = "number",required = true)String nums,@RequestParam(value = "categoryType",required = true)String categoryType,
                               @RequestParam(value = "operation",required = true)String operation,@RequestParam(value = "category",required = true)String category,
                               @RequestParam(value = "userId",required = true)String userId){
        Good good = new Good();
        good.setGood_name(GoodName);
        good.setQuality(quality);
        good.setNums(nums);
        good.setCategory_type(categoryType);
        good.setOperation(operation);
        good.setCategory(category);
        JSONObject data = service.insertGoodInfo(good, userId);
        return data;
    }
    /**
     * 修改医疗物资数量接口
     * */
    @ApiOperation("修改医疗物资接口")
    @PostMapping("/updateGoodInfo")
    public JSONObject updateGoodInfo(@RequestParam(value = "GoodId",required = true)String GoodId,@RequestParam(value = "number",required = false)String number,
                                     @RequestParam(value = "price",required = false)String price,@RequestParam(value = "userId",required = true)String userId){
        Good good = new Good();
        good.setGood_id(GoodId);
        good.setNums(number==null?"":number);
        good.setPrice(price==null?"":price);
        JSONObject data = service.updateGoodInfo(good, userId);
        return data;
    }
    /**
     * 删除医疗物资接口
     * */
    @ApiOperation("删除医疗物资接口")
    @GetMapping("deleteGoodInfo")
    public JSONObject deleteGoodInfo(@RequestParam(value = "GoodId",required = true)String GoodId,@RequestParam(value = "userId",required = true)String userId){
        JSONObject data = service.deleteGoodInfo(GoodId, userId);
        return data;
    }
}
