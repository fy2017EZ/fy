package com.fuyao.myproject.mapper;

import com.fuyao.myproject.entity.PlanInfo;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface PlanMapper {
    @Insert("insert into Medical_control_plan_detail_info(plan_id,plan_name,user_city,project_content,project_detail,project_user_code) values(#{plan_id}, " +
            "#{plan_name},#{user_city},#{project_content},#{project_detail},#{project_user_code})")
    void savePlanInfo(PlanInfo planInfo);
    @Insert("insert into  Medical_control_plan_node_info(plan_id,node_id,param_key,param_value,node_name,user_code) values(#{planId},#{nodeId},#{paramKey},#{paramValue},#{nodeName},#{userCode})")
    void saveNodeInfo(String planId,String nodeId,String paramKey,String paramValue,String nodeName,String userCode);

    @Update("update Medical_control_plan_detail_info set project_content = #{projectContent} where plan_id = #{planId}")
    void updatePlanInfo(String projectContent,String planId);
    @Update(" merge into Medical_control_plan_node_info a " +
            " using (select #{planId} plan_id,#{nodeId} nodeId,#{paramKey} param_key,#{paramValue} param_value,#{nodeName} node_name,#{userCode} user_code from dual) b " +
            " on a.plan_id = b.plan_id and a.node_id = b.node_id  " +
            " when matched " +
            " then update set a.param_key = b.param_key,a.param_value = b.param_value,a.node_name = b.node_name,a.user_code = b.user_code" +
            " when not matched " +
            " then insert values(b.plan_id,b.node_id,b.param_key,b.param_value,b.node_name,b.user_code)" +
            "")
    void updateNodes(String planId,String nodeId,String paramKey,String paramValue,String nodeName,String userCode);

    @Delete("delete from Medical_control_plan_detail_info where plan_id = #{planId}")
    void deletePlanInfoById(String planId);
    @Delete("delete from Medical_control_plan_node_info where plan_id = #{planId}")
    void deleteNodeInfo(String planId);

    @Select("select * from(select a.*,rownum rn from Medical_control_plan_detail_info a ) where rn between #{start} and #{end} ")
    List<PlanInfo> findAllPlan(String start,String end);
    @Select("select count(*) from Medical_control_plan_detail_info a  ")
    Integer findAllPlanCount();

    @Select("select * from(select a.*,rownum rn from Medical_control_plan_detail_info a where a.project_user_code = #{userCode}) where rn between #{start} and #{end} ")
    List<PlanInfo> findPlanByUserCode(String userCode,String start,String end);

    @Select(" select  count(*) from Medical_control_plan_detail_info a where a.project_user_code = #{userCode} ")
    Integer findPlanByUserCodeCount(String userCode);

    @Select("select * from(select a.*,rownum rn from Medical_control_plan_detail_info a where a.project_user_code = #{userCode}) where rn between #{start} and #{end}")
    List<PlanInfo> findPlanByAll(String start,String end,String planId,String createTime,String endTime,String userCity);
}
