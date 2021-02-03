package com.fuyao.myproject.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Component
public class DBUtil {
    @Resource
    private JdbcTemplate jt;

    @Value("${tables.Medical_db_info}")
    private String db_info;//数据库信息

    public JdbcTemplate getJt() {
        return jt;
    }

    /**
     * 从Medical_db_info表里获取数据库信息，并创建JdbcTemplate返回
     * @param dbId 数据库ID
     * @return
     */
    public JdbcTemplate getJdbcTemplate(int dbId){
        JdbcTemplate jdbcTemplate = null;

        try{
            List<Map<String,Object>> list = jt.queryForList("select * from "+db_info+" where db_id = "+dbId);
            String driveClassName = list.get(0).get("DB_DRIVER_CLAS")==null?"":list.get(0).get("DB_DRIVER_CLAS").toString();
            String url = list.get(0).get("DB_URL")==null?"":list.get(0).get("DB_URL").toString();
            String userId = list.get(0).get("DB_USER_ID")==null?"":list.get(0).get("DB_USER_ID").toString();
            String pwd = list.get(0).get("DB_USER_PASSWORD")==null?"":list.get(0).get("DB_USER_PASSWORD").toString();
            if(StringUtils.isEmpty(driveClassName)) {
                throw new ParamException("DBUtil Error:driveClassName is empty.");
            }
            if(StringUtils.isEmpty(url)) {
                throw new ParamException("DBUtil Error:url is empty.");
            }
            if(StringUtils.isEmpty(userId)) {
                throw new ParamException("DBUtil Error:userId is empty.");
            }
            if(StringUtils.isEmpty(pwd)) {
                throw new ParamException("DBUtil Error:pwd is empty.");
            }
//        //创建数据库连接池,（该数据库连接池是spring框架自己的连接池）
            DriverManagerDataSource detaSource = new DriverManagerDataSource();
//        //配置链接mysql的操作
            detaSource.setDriverClassName(driveClassName);
            detaSource.setUrl(url);
            detaSource.setUsername(userId);
            detaSource.setPassword(pwd);
            //创建一个模板对象
            jdbcTemplate = new JdbcTemplate(detaSource);
        }catch (Exception e){
            e.printStackTrace();
            throw new ParamException("DBUtil Error:"+e.getMessage());
        }finally {
            return jdbcTemplate;
        }
    }

}
