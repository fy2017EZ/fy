package com.fuyao.myproject.mapper;

import com.fuyao.myproject.entity.Good;
import com.fuyao.myproject.entity.Schedule;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface ScheduleMapper {
    @Insert(" insert into Medical_good_cfg_info(cfg_id,good_id,good_name,warehouse_name,current_warehouse,nums,execute_time," +
            " schedule_type,schedule_status,target_table_name,target_place,warehousing_batch_number) values(#{cfg_id},#{good_id},#{good_name},#{warehouse_name},#{current_warehouse}," +
            " #{nums},#{execute_time},#{schedule_type},'0',#{target_table_name},#{target_place},#{warehousing_batch_number})")
    void saveScheduleInfo(Schedule schedule);

    @Select("select * from Medical_good_cfg_info where schedule_status = '0'")
    List<Schedule> findAllSchedule();
    @Select("select schedule_status from Medical_good_cfg_info where cfg_id = #{cfgId}")
    String getScheduleStatus(String cfgId);
    @Select("select count(1) from user_tables where table_name = #{tableName}  order by last_analyzed desc")
    Integer iSExits(String tableName);
    @Select("select * from Medical_good_info where good_id = #{GoodId}")
    Good findGoodById(String GoodId);
//    @Select("select column_value from Medical_system_dim where column_name = #{columnName}")
//    String getSystemValue(String columName);

    @Update("update Medical_good_cfg_info set schedule_status = '1' where cfg_id = #{cfgId}")
    void updateScheduleStatus(String cfgId);
    @Update("update Medical_good_info set nums = #{Nums} where good_id = #{GoodId}")
    void updateGoodNums(String Nums,String GoodId);
    @Update("update Medical_good_cfg_info set start_time = sysdate,schedule_status = '1' where cfg_id = #{cfgId}")
    void updateStartTime(String cfgId);
    @Update("update Medical_good_cfg_info set end_time = sysdate,schedule_status = '0' where cfg_id = #{cfgId}")
    void updateEndTime(String cfgId);
//    @Update("update Medical_system_dim set column_value = #{columnValue} where column_name = #{columnName}")
//    void updateSystemValue(String cloumnName,String columnValue);
    @Select("select column_value from Medical_system_dim where column_name = #{columnName}")
    String getSystemValue(String columnName);
    @Update("update Medical_system_dim set column_value = #{columnValue} where column_name = #{columnName}")
    void updateSystemValue(String columnValue,String columnName);
}
