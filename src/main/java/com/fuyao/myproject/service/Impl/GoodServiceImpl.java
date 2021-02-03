package com.fuyao.myproject.service.Impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fuyao.myproject.entity.Good;
import com.fuyao.myproject.entity.Schedule;
import com.fuyao.myproject.entity.UserInfo;
import com.fuyao.myproject.mapper.GoodMapper;
import com.fuyao.myproject.mapper.ScheduleMapper;
import com.fuyao.myproject.mapper.UserMapper;
import com.fuyao.myproject.service.GoodService;
import com.fuyao.myproject.util.ParamException;
import io.swagger.models.auth.In;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @description:
 * @author: fuyao
 * @time: 2021/1/19 11:37
 */
@Service
public class GoodServiceImpl implements GoodService {
    private static final Logger logger = LoggerFactory.getLogger(GoodServiceImpl.class);
    @Autowired
    private GoodMapper mapper;
    @Autowired
    private ScheduleMapper scheduleMapper;
    @Autowired
    private UserMapper userMapper;

    @Override
    public void saveGood(Good good) {
        mapper.saveGood(good);
    }

    @Override
    public JSONObject getAllCondition(Map<String, String> map, Good good, String start, String end) {
        JSONObject data = new JSONObject();
        JSONArray array = new JSONArray();
        List<Good> list = new ArrayList<>();
        Integer count = 0;
        try {
            logger.info("条件个数：" + map.size());
            switch (map.size()) {
                case 0:
                    list = (List<Good>) getGood(good, start, end).get("GoodList");
                    count = (Integer) getGood(good, start, end).get("count");
                    break;
                case 1:
                    list = (List<Good>) getGoodByOne(map, good, start, end).get("GoodList");
                    count = (Integer) getGoodByOne(map, good, start, end).get("count");
                    break;
                default:
                    list = (List<Good>) getGoodByMany(map, good, start, end).get("GoodList");
                    count = (Integer) getGoodByMany(map, good, start, end).get("count");
                    break;
            }
            data.put("code", "200");
            data.put("result", list);
            data.put("count", count);
            logger.info("共查询到" + count + "结果集为" + list);
        } catch (Exception e) {
            e.printStackTrace();
            data.put("203", e.getMessage());
            logger.error("医疗物资查询报错" + e.getMessage());
        }

        return data;
    }

    @Override
    public Map<String, Object> getGood(Good good, String start, String end) {
        List<Good> list = new ArrayList<>();
        Map<String, Object> map = new HashMap<>();
        Integer count = 0;
        if (!StringUtils.isEmpty(good.getCityId()) && good.getCityId().equals("569")) {//浙江省机构，查总表
            list = mapper.findAllGood(start, end);
            count = mapper.findAllGoodCount();
            map.put("count", count);
            map.put("GoodList", list);
        } else {//根据id查分表
            list = mapper.findCityGood(good.getCityId(), start, end);
            count = mapper.findCityGoodCount(good.getCityId());
            map.put("count", count);
            map.put("GoodList", list);
        }
        return map;
    }

    @Override
    public Map<String, Object> getGoodByOne(Map<String, String> map, Good good, String start, String end) {
        String WhereCol = "";
        String ColValue = "";
        List<Good> list = new ArrayList<>();
        Map<String, Object> GoodMap = new HashMap<>();
        Integer count = 0;
        for (Object obj : map.entrySet()) {
            Map.Entry entry = (Map.Entry) obj;
            if (entry.getKey().equals("start_time") && StringUtils.isEmpty(good.getEndTime())) {
                WhereCol = " where  a." + entry.getKey() + "  between  ";
                ColValue = " to_date(" + entry.getValue() + ",'YYYY/MM/DD') and to_date('" + good.getEndTime() + "','YYYY/MM/DD')  ";
            } else {
                WhereCol = " where  a." + entry.getKey() + " = ";
                ColValue = (String) entry.getValue();
            }
        }
        if (!StringUtils.isEmpty(good.getCityId()) && good.getCityId().equals("569")) {
            list = mapper.findAllGoodByOne(WhereCol, ColValue, start, end);
            count = mapper.findAllGoodByOneCount(WhereCol, ColValue);
            GoodMap.put("count", count);
            GoodMap.put("GoodList", list);
        } else {
            list = mapper.findCityGoodByOne(good.getCityId(), WhereCol, ColValue, start, end);
            count = mapper.findCityGoodByOneCount(good.getCityId(), WhereCol, ColValue);
            GoodMap.put("count", count);
            GoodMap.put("GoodList", list);
        }
        return GoodMap;
    }

    @Override
    public Map<String, Object> getGoodByMany(Map<String, String> map, Good good, String start, String end) {
        String whereCol0 = "";
        String ColValue0 = "";
        String andCol1 = "";
        String ColValue1 = "";
        String andCol2 = "";
        String ColValue2 = "";
        String andCol3 = "";
        String ColValue3 = "";
        String andCol4 = "";
        String ColValue4 = "";
        String andCol5 = "";
        String ColValue5 = "";
        List<Good> list = new ArrayList<>();
        Map<String, Object> GoodMap = new HashMap<>();
        Integer count = 0;
        int i = 0;
        for (Object obj : map.entrySet()) {
            Map.Entry entry = (Map.Entry) obj;
            if (i == 0 && entry.getKey().equals("start_time") && !StringUtils.isEmpty(good.getEndTime())) {
                whereCol0 = "where a." + entry.getKey() + " between  ";
                ColValue0 = "to_date(" + entry.getValue() + ",'yyyy/mm/dd') and to_date('" + good.getEndTime() + "','yyyy/mm/dd')";
                i++;
                continue;
            } else if (entry.getKey().equals("start_time") && !StringUtils.isEmpty(good.getEndTime())) {
                andCol5 = "and a." + entry.getKey() + " between  ";
                ColValue5 = "to_date(" + entry.getValue() + ",'yyyy/mm/dd') and to_date('" + good.getEndTime() + "','yyyy/mm/dd')";
                continue;
            }
            switch (i) {
                case 0: {
                    whereCol0 = "where a." + entry.getKey() + "=";
                    ColValue0 = " '" + (String) entry.getValue() + "' ";
                }
                ;
                break;
                case 1: {
                    andCol1 = "and  a." + entry.getKey() + "=";
                    ColValue1 = " '" + (String) entry.getValue() + "' ";
                }
                ;
                break;
                case 2: {
                    andCol2 = "and  a." + entry.getKey() + "=";
                    ColValue2 = " '" + (String) entry.getValue() + "' ";
                }
                ;
                break;
                case 3: {
                    andCol3 = "and  a." + entry.getKey() + " = ";
                    ColValue3 = " '" + (String) entry.getValue() + "' ";
                }
                ;
                break;
                case 4: {
                    andCol4 = "and  a." + entry.getKey() + "=";
                    ColValue4 = " '" + (String) entry.getValue() + "' ";
                }
                ;
                break;
            }
        }
        if (StringUtils.isEmpty(good.getCityId()) && good.getCityId().equals("569")) {
            list = mapper.findAllGoodByMany(whereCol0, ColValue0, andCol1, ColValue1, andCol2, ColValue2, andCol3, ColValue3, andCol4, ColValue4, andCol5, ColValue5, start, end);
            count = mapper.findAllGoodByManyCount(whereCol0, ColValue0, andCol1, ColValue1, andCol2, ColValue2, andCol3, ColValue3, andCol4, ColValue4, andCol5, ColValue5);
            GoodMap.put("count", count);
            GoodMap.put("GoodList", list);
        } else {
            list = mapper.findCityGoodByMany(good.getCityId(), whereCol0, ColValue0, andCol1, ColValue1, andCol2, ColValue2, andCol3, ColValue3, andCol4, ColValue4, andCol5, ColValue5, start, end);
            count = mapper.findCityGoodByManyCount(good.getCityId(), whereCol0, ColValue0, andCol1, ColValue1, andCol2, ColValue2, andCol3, ColValue3, andCol4, ColValue4, andCol5, ColValue5);
            GoodMap.put("count", count);
            GoodMap.put("GoodList", list);
        }
        return GoodMap;
    }

    @Override
    public JSONObject GoodCfgStart(Schedule schedule) {
        JSONObject data = new JSONObject();
        try {
            //判空
            if (!StringUtils.isEmpty(schedule.getGood_id()) && !StringUtils.isEmpty(schedule.getWarehouse_name())
                    && !StringUtils.isEmpty(schedule.getCurrent_warehouse()) && !StringUtils.isEmpty(schedule.getNums())
                    && !StringUtils.isEmpty(schedule.getSchedule_type()) && !StringUtils.isEmpty(schedule.getTarget_place())
                    && !StringUtils.isEmpty(schedule.getWarehousing_batch_number())) {
                String cfgId = String.valueOf(System.currentTimeMillis() + (long) (Math.random() * 1000000 + 1));
                schedule.setCfg_id(cfgId);
                schedule.setTarget_table_name("Medical_" + schedule.getTarget_place() + "_good_info");
                if (StringUtils.isEmpty(schedule.getGood_name())) {
                    String goodName = mapper.findGoodNameById(schedule.getGood_id());
                    schedule.setGood_name(goodName);
                }
                if (schedule.getSchedule_type().equals("0") && format(schedule.getSchedule_type(), schedule.getExecute_time())) {
                    if (StringUtils.isEmpty(schedule.getExecute_time())) {
                        Date date = new Date();
                        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
                        String executeTime = sdf.format(date);
                        schedule.setExecute_time(executeTime);
                    }
                } else if (schedule.getSchedule_type().equals("1") && format(schedule.getSchedule_type(), schedule.getExecute_time())) {
                    if (StringUtils.isEmpty(schedule.getExecute_time())) {
                        Date date = new Date();
                        SimpleDateFormat sdf = new SimpleDateFormat("DD HH:mm:ss");
                        String executeTime = sdf.format(date);
                        schedule.setExecute_time(executeTime);
                    }
                }else if(schedule.getSchedule_type().equals("2")&&format(schedule.getSchedule_type(), schedule.getExecute_time())){
                    if (StringUtils.isEmpty(schedule.getExecute_time())) {
                        Date date = new Date();
                        SimpleDateFormat sdf = new SimpleDateFormat("YYYY/MM/DD HH:mm:ss");
                        String executeTime = sdf.format(date);
                        schedule.setExecute_time(executeTime);
                    }
                }else{
                    logger.error("传入的日期格式不匹配");
                    data.put("code","203");
                }
                scheduleMapper.saveScheduleInfo(schedule);
                data.put("code","200");
            } else {
                logger.error("部分启动数据为空，调度启动失败");
                data.put("error", "该调度启动数据部分为空,调度启动失败，请重新发起调度");
            }
        } catch (Exception e) {
            e.printStackTrace();
            data.put("code", "203");
            data.put("error", e.getMessage());
        }

        return data;
    }

    @Override
    public JSONObject insertGoodInfo(Good good, String userId) {
        JSONObject data = new JSONObject();
        UserInfo info = userMapper.getUserInfo(userId);
        if(!StringUtils.isEmpty(info.getUser_city())&&info.getUser_city().equals("579")){
            int goodId = (int)Math.random()*1000000+1;
            String batchCode = UUID.randomUUID().toString().replace("-", "").toUpperCase();
            good.setGood_id(String.valueOf(goodId));
            good.setWarehousing_batch_number(batchCode);
            mapper.saveGood(good);
            data.put("code","200");
        }else if(!StringUtils.isEmpty(info.getUser_city())){
            int goodId = (int)Math.random()*1000000+1;
            String batchCode = UUID.randomUUID().toString().replace("-", "").toUpperCase();
            good.setGood_id(String.valueOf(goodId));
            good.setWarehousing_batch_number(batchCode);
            mapper.saveGoodByCity(good,info.getUser_city());
            data.put("code","200");
        }else{
            data.put("code","202");
            data.put("msg","保存信息失败，当前人员所处地市id为空，请核实人员信息");
        }
        return data;
    }

    @Override
    public JSONObject updateGoodInfo(Good good, String userId) {
        JSONObject data = new JSONObject();
        UserInfo info = userMapper.getUserInfo(userId);
        if(!StringUtils.isEmpty(info.getUser_city())&&info.getUser_city().equals("579")){
            int goodId = (int)Math.random()*1000000+1;
            String batchCode = UUID.randomUUID().toString().replace("-", "").toUpperCase();
            good.setGood_id(String.valueOf(goodId));
            good.setWarehousing_batch_number(batchCode);
            mapper.updateGoodinfo(good);
            data.put("code","200");
        }else if(!StringUtils.isEmpty(info.getUser_city())){
            int goodId = (int)Math.random()*1000000+1;
            String batchCode = UUID.randomUUID().toString().replace("-", "").toUpperCase();
            good.setGood_id(String.valueOf(goodId));
            good.setWarehousing_batch_number(batchCode);
            mapper.updateGoodinfoByCity(good,info.getUser_city());
            data.put("code","200");
        }else{
            data.put("code","202");
            data.put("msg","更新信息失败，当前人员所处地市id为空，请核实人员信息");
        }
        return data;
    }

    @Override
    public JSONObject deleteGoodInfo(String goodId, String userId) {
        JSONObject data = new JSONObject();
        UserInfo info = userMapper.getUserInfo(userId);
        if(!StringUtils.isEmpty(info.getUser_city())&&info.getUser_city().equals("579")){
            mapper.deleteGoodInfoById(goodId);
            data.put("code","200");
        }else if(!StringUtils.isEmpty(info.getUser_city())){
            mapper.deleteGoodInfoByCityAndId(goodId,info.getUser_city());
            data.put("code","200");
        }else{
            data.put("code","202");
            data.put("msg","更新信息失败，当前人员所处地市id为空，请核实人员信息");
        }
        return data;
    }

    public static Boolean format(String type, String str) {
        Pattern pattern = Pattern.compile("\\d{2}:\\d{2}:\\d{2}");
        Pattern pattern2 = Pattern.compile("\\d{2} \\d{2}:\\d{2}:\\d{2}");
        Pattern pattern3 = Pattern.compile("\\d{4}/\\d{2}/\\d{2} \\d{2}:\\d{2}:\\d{2}");
        if (!StringUtils.isEmpty(type) && type.equals("0")) {
            if (pattern.matcher(str).matches()) {
                return true;
            } else {
                return false;
            }
        }
        if (!StringUtils.isEmpty(type) && type.equals("1")) {
            if (pattern2.matcher(str).matches()) {
                return true;
            } else {
                return false;
            }
        }
        if (!StringUtils.isEmpty(type) && type.equals("2")) {
            if (pattern2.matcher(str).matches()) {
                return true;
            } else {
                return false;
            }
        }
        return false;
    }
//    public static void main(String[] args){
//        System.out.println("测试");
//        format("0","11:23:12");
//    }
}
