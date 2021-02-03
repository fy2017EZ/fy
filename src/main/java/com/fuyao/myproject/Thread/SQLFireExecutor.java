package com.fuyao.myproject.Thread;


import com.fuyao.myproject.entity.Good;
import com.fuyao.myproject.entity.Queue;
import com.fuyao.myproject.entity.Schedule;
import com.fuyao.myproject.mapper.ScheduleMapper;
import com.fuyao.myproject.util.DBUtil;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.JdbcProperties;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import org.thymeleaf.spring5.context.SpringContextUtils;

import java.lang.reflect.Proxy;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;


public class SQLFireExecutor implements Runnable {
    private Queue qList;
    private static final int SQL_FIRE_EXECUTOR_NUM = 15;
    private static final Logger logger  = LoggerFactory.getLogger(SQLFireExecutor.class);
    // 线程池
    private static ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newCachedThreadPool();
    //数据库连接信息
    private static final String URL="jdbc:oracle:thin:@localhost:1521:ORCL";
    private static final String NAME="fuyao";
    private static final String PASSWORD="fuyao123";
	/*private String tmp_file_path;
	public String getTmp_file_path() {
		return tmp_file_path;
	}

	public void setTmp_file_path(String tmp_file_path) {
		this.tmp_file_path = tmp_file_path;
	}*/

    public SQLFireExecutor(Queue sqlList) {
        super();
        this.qList = sqlList;
    }

    public void start() {
        int num = SQL_FIRE_EXECUTOR_NUM >= this.qList.size() ? this.qList.size() : SQL_FIRE_EXECUTOR_NUM;
        logger.info("#start. num=" + num);
        for (int i = 0; i < num; i++) {
            //Thread tt = new Thread(this, "SQL_FIRE_EXECUTOR_Thread_" + (i+1));
            //tt.start();
            logger.info("#start. i=" + i);
            executor.execute(this);
        }
    }

    public static boolean isAlive() {
        logger.info("executor.getActiveCount()="+executor.getActiveCount());
        System.out.println("线程池中线程数目："+executor.getPoolSize()
                + "，队列中等待执行的任务数目："+executor.getQueue().size()
                + "，已执行玩别的任务数目："+executor.getCompletedTaskCount()
                + ", 线程池线程数量CorePoolSize: "+executor.getCorePoolSize()
                + ", 线程池线程活跃时间: "+executor.getKeepAliveTime(TimeUnit.SECONDS)
                + ", getTaskCount: "+executor.getTaskCount());
        return executor.getActiveCount() > 0;
    }

    @Override
    public void run() {
        // TODO Auto-generated method stub
        logger.info("#run. start import data to sqlfire.");
        try {
            Class.forName("oracle.jdbc.OracleDriver");
            Connection conn = DriverManager.getConnection(URL, NAME, PASSWORD);
            Statement rs = conn.createStatement();
            //conn = SQLFireHandler.getConnection();

            while(true) {
                if (this.qList.empty()) {
                    logger.warn("#run. qList is empty");
                    break;
                }

                String msg = null;
                int result = 1;
                try {
                    //导入操作的实体类
                    Schedule callStrBean = this.qList.deq();   // 返回ImportBean 实体类对象 // 删除了此任务

                    logger.info("#run. cfgID=" + callStrBean.getCfg_id());
                    //result = invoke(callStrBean);

                    //Handler handler = newHandlerInstance(callStrBean);
//                    Handler handler = HandlerFactory.newHandlerInstance(callStrBean);
                    //HandlerProxy.newInstance(handler).handle();
//                    result = handler.handle();
                    //检查任务状态，是否和内存中一致，如果不一致，则说明该任务存在异常，后续不再执行
//                    String realStatus = mapper.getScheduleStatus(callStrBean.getCfg_id());
                    ResultSet set = rs.executeQuery("select schedule_status from Medical_good_cfg_info where cfg_id =" + callStrBean.getCfg_id());
                    String realStatus = "";
                    if(set.next()){
                         realStatus = set.getString("schedule_status");
                    }
                    if (realStatus.equals("0")) {
                        logger.error("beforeExecute. this task exec status is wrong, realStatus="+
                                realStatus);
                    }else {
                        //更新配置的运行时间
//                        importDispatchDao.updateRunTimeAndExecStatus(RUN_BEGIN_TIME_COLUMN_NAME, EXEC_STATUS_CFG_VALUE_EXECUTE_BEFORE,importBean);

//                    result = this.beforeExecute();
                        logger.info("调度：" + callStrBean.getCfg_id() + "执行中");
                        //更新状态为执行中
//                        mapper.updateScheduleStatus(callStrBean.getCfg_id());
                        String tableName = StringUtils.upperCase(callStrBean.getTarget_table_name());
                        //查看所在地市是否创建分表
                        String sql1 = "select table_name from user_tables where table_name = '" +tableName+ "'  order by last_analyzed desc";
                        ResultSet set1 = rs.executeQuery(sql1);
//                        List<Map<String, Object>> mapList = jdbcTemplate.queryForList(sql1);
                        String anInt = "" ;
                        if(set1.next()){
                            anInt = set1.getString("table_name");
                        }
                        if (StringUtils.isEmpty(anInt)) {
//                        if(mapList.size()==0){
                            logger.info("新建地市分表，表名：" + callStrBean.getTarget_table_name());
                            String sql = "create table " + callStrBean.getTarget_table_name() + " ( \n" +
                                    "  GOOD_ID                  VARCHAR2(64),\n" +
                                    "  GOOD_NAME                VARCHAR2(128),\n" +
                                    "  PRICE                    VARCHAR2(32),\n" +
                                    "  QUALITY                  VARCHAR2(128),\n" +
                                    "  NUMS                     VARCHAR2(32),\n" +
                                    "  START_TIME               DATE,\n" +
                                    "  CATEGORY_TYPE            VARCHAR2(32),\n" +
                                    "  OPERATION                VARCHAR2(32),\n" +
                                    "  CATEGORY                 VARCHAR2(32),\n" +
                                    "  WAREHOUSING_BATCH_NUMBER VARCHAR2(128)\n" +
                                    ")";
//                            jdbcTemplate.update(sql);
                                rs.executeUpdate(sql);
                        }

                            List parameter = new ArrayList();
                            //后续操作
//                        Good good = mapper.findGoodById(callStrBean.getGood_id());
                            String sql3 = "select * from Medical_good_info where good_id = "+callStrBean.getGood_id();
                        List<Map<String, Object>> mapList = new DBUtil().getJdbcTemplate(1).queryForList(sql3);
                        ResultSet set2 = rs.executeQuery(sql3);
                            String[] arr = new String[50];
                            String sql2 = " merge into " + callStrBean.getTarget_table_name() + "  a  \n" +
                                    "  using (select ? as good_id, ? as good_name, ? as price, ? as quality, ? as category_type, ? as operation,  \n" +
                                    "   ? as category, ? as warehousing_batch_number, ? as nums, ? as start_time from dual ) b \n" +
                                    "  ON （a.good_id = b.good_id）  \n" +
                                    "  WHEN MATCHED THEN  UPDATE  SET a.nums = to_number(a.nums)+to_number(b.nums)  \n" +
                                    "  WHEN NOT MATCHED THEN insert (a.good_id,a.good_name,a.price,a.quality,a.category_type,a.operation,a.category,a.warehousing_batch_number,a.nums,a.start_time)  \n" +
                                    "  VALUES(b.good_id,b.good_name,b.price,b.quality,b.category_type,b.operation,b.category,b.warehousing_batch_number,b.nums,to_date(b.start_time,'YYYY-MM-DD HH24:mi:ss'))  \n";

                        if(set2.next()){
                                arr[0] = set2.getString("good_id")==null?"":set2.getString("good_id");
                                arr[1] = set2.getString("good_name")==null?"":set2.getString("good_name");
                                arr[2] = set2.getString("price")==null?"0.0":set2.getString("price");
                                arr[3] = set2.getString("quality")==null?"":set2.getString("quality");
                                arr[4] = set2.getString("category_type")==null?"":set2.getString("category_type");
                                arr[5] = set2.getString("operation")==null?"":set2.getString("operation");
                                arr[6] = set2.getString("category")==null?"":set2.getString("category");
                                arr[7] = set2.getString("warehousing_batch_number")==null?"":set2.getString("warehousing_batch_number");
                                arr[8] = callStrBean.getNums();
                                SimpleDateFormat sdf = new SimpleDateFormat("YYYY-MM-DD HH:mm:ss");
                                Date date = new Date();
                                String startTime = sdf.format(date);
                                arr[9] = startTime;
                            }
                            //插入或更新相关数据
                        PreparedStatement ps = conn.prepareStatement(sql2);
                        ps.setString(1,arr[0]);
                        ps.setString(2,arr[1]);
                        ps.setString(3,arr[2]);
                        ps.setString(4,arr[3]);
                        ps.setString(5,arr[4]);
                        ps.setString(6,arr[5]);
                        ps.setString(7,arr[6]);
                        ps.setString(8,arr[7]);
                        ps.setString(9,arr[8]);
                        ps.setString(10,arr[9]);
                        ps.executeUpdate();
//                            rs.executeUpdate(sql2);
//                        parameter.add(good.getGood_id());
//                        parameter.add(good.getGood_name());
//                        parameter.add(good.getPrice());
//                        parameter.add(good.getQuality());
//                        parameter.add(good.getCategory_type());
//                        parameter.add(good.getOperation());
//                        parameter.add(good.getCategory());
//                        parameter.add(good.getWarehousing_batch_number());
//                        parameter.add(good.getNums());
//                        jdbcTemplate.update(sql1,parameter);
//                        String nums = String.valueOf(Integer.parseInt(good.getNums())-Integer.parseInt(callStrBean.getNums()));
//                        //更新总表数据
//                        mapper.updateGoodNums(nums,callStrBean.getGood_id());
                            if(StringUtils.isEmpty(callStrBean.getCurrent_warehouse())&&callStrBean.getCurrent_warehouse().equals("569")){
                                //更新总表数据
                                String oldNum = set2.getString("nums");
                                String Nums = String.valueOf(Integer.parseInt(oldNum)-Integer.parseInt(callStrBean.getNums()));
                                String sql4 = "update Medical_good_info set nums = "+Nums+" where good_id = "+callStrBean.getGood_id();
                                rs.executeUpdate(sql4);
                            }else{
                                //更新地市表
                                String sql5 = "select * from Medical_"+callStrBean.getCurrent_warehouse()+"_good_info where good_id = "+callStrBean.getGood_id();
                                ResultSet set4 = rs.executeQuery(sql5);
                                String oldNum = "";
                                if(set4.next()){
                                    oldNum = set4.getString("nums");
                                }
                                String Nums = String.valueOf(Integer.parseInt(oldNum)-Integer.parseInt(callStrBean.getNums()));
                                String sql4 = "update  Medical_"+callStrBean.getCurrent_warehouse()+"_good_info set nums = "+Nums+" where good_id = "+callStrBean.getGood_id();
                            }

//                        //更新调度状态
//                        mapper.updateEndTime(callStrBean.getCfg_id());
                            String sql5 = "update Medical_good_cfg_info set end_time = sysdate,schedule_status = '0' where cfg_id = "+callStrBean.getCfg_id();
                            rs.executeUpdate(sql5);
                        }

                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } finally {
                    //如果导入出错, 则通知维护
                    if (result != 1) {
//                        SyncUtil.noteAdmin(msg);
                    }
                }

            }
            rs.close();
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
            logger.error("#run. thread running wrong.", e);
        } finally {

        }

    }

//    public int invoke(Schedule callStrBean) {
//        int result = 1;
//
//        try {
//
//            Handler handler = HandlerFactory.newHandlerInstance(importBean);
//            /*handleProxy*/
//            HandlerProxy handlerProxy = new HandlerProxy(handler);
//
//            Handler proxy = (Handler) Proxy.newProxyInstance(
//                    handler.getClass().getClassLoader(),
//                    handler.getClass().getInterfaces(),
//                    handlerProxy);
//
//            result = proxy.handle();
//
//        } catch (Exception e) {
//            // TODO: handle exception
//            result = 0;
//            logger.error("#execute. error.", e);
//            e.printStackTrace();
//        } finally {
//
//        }
//
//        return result;
//    }
}
