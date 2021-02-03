package com.fuyao.myproject.util;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class DS {

	public static String driver = "oracle.jdbc.driver.OracleDriver";
	public static String url = "jdbc:oracle:thin:@127.0.0.1:1521:XE";
	public static String user = "fuyao";
	public static String password = "fuyao123";
	
	public static Connection conn = null;
	public static Statement stat = null;
	public static ResultSet rs = null;
	static {
		try {
			Class.forName(driver);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	public static Connection conn() {
		try {
			conn = DriverManager.getConnection(url, user, password);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return conn;
	}
	/**
	 * 查询
	 * */
	public static ResultSet query(String sql) {
		Connection c2 = conn();
		try {
			stat = c2.createStatement();
			
			rs = stat.executeQuery(sql);
			
			return rs;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
/**
 * 查询 返回一个对象
 * @param sql select语句
 * @param beanClass 该select语句的结果集 
 * 					最终封装成该类型
 * @return T对象
 * 
 * 
 * select id,name
 * from student
 * 
 * */
public static <T> List<T> query(String sql,
				   Class<T> beanClass) {
	//构建list集合存放对象
	List<T> list = new ArrayList<T>();
	
	
	//1,获得beanClass的所有属性
	Field[] fields = beanClass.getDeclaredFields();
	//2,执行sql
	ResultSet rs = query(sql);
	try {
		//获得结果集的mate对象，用来获得结果集中的列名
		ResultSetMetaData md = rs.getMetaData();
		while(rs.next()) {
			//使用字节码对象 产生新对象
			T t = beanClass.newInstance();
			//本次循环 创建的新对象添加到集合中
			list.add(t);
			
			//获得当前结果集中总包含多少列
			int length = md.getColumnCount();
			for(int i = 1;i<=length;i++) {
				//获得列名 
				String columnName = 
						md.getColumnLabel(i);
				//列的类型
				String typeName = md.getColumnTypeName(i).toLowerCase();
				//列的值获取
				//Object value = rs.getObject(i);
				//通过列名 获得 对应的属性名
				Field fie = beanClass.getDeclaredField(
					columnName.toLowerCase());
				//设置私有属性可见
				fie.setAccessible(true);
				//给t对象的属性设置值
				//setName();
				//columnName.substring(0, 1).toUpperCase()
				//columnName.substring(1).toLowerCase()
				Method setMethod = beanClass.getMethod(
				 "set"+columnName.substring(0, 1).toUpperCase()
				 +columnName.substring(1).toLowerCase(), 
				 fie.getType());
				//判断当前列的类型
				if("number".equals(typeName)) {
					int value = rs.getInt(i);
					//反射调用set方法
					setMethod.invoke(t, value);
				}else if("varchar2".equals(typeName)) {
					String value = rs.getString(i);
					setMethod.invoke(t, value);
				}else if("date".equals(typeName)) {
					Date date = rs.getDate(i);
					if(date!=null) {
						java.util.Date value = new java.util.Date(date.getTime());
						setMethod.invoke(t, value);
					}
				}else {}
			}
		}
	}catch (Exception e) {
		e.printStackTrace();
	}finally {
		close();
	}
	return list;
}
/**
 * 通过id删除 参数二类对应的表中数据<br>
 * @param id 主键值
 * @param beanClass 被删除的表对应javaBean
 * */
public static <T> void delelte(Object id,
					Class<T> beanClass) {
	//delete  表名  where id = id
	StringBuffer sb = new StringBuffer();
	sb.append("delete");
	sb.append(" ");
	sb.append(beanClass.getSimpleName());
	sb.append(" ");
	sb.append("where id = ");
	sb.append(id);
	//执行sql
	query(sb.toString());
	close();
}
/**
 * 保存 ORM
 * */
public static <T> void save(T t) {
	Class<? extends Object> tc = t.getClass();
	//获得表名
	String tableName = tc.getSimpleName();
	//insert into 表名(列名,列名...) values(值,值,'值');
	//获得所有属性
	Field[] fields = tc.getDeclaredFields();
	//(列名,列名,)
	StringBuffer colNames = new StringBuffer();
	colNames.append("(");
	for(Field f :fields) {
		colNames.append(f.getName());
		colNames.append(",");
	}
	//去除最后多余的，号
	colNames.delete(colNames.length()-1,
					colNames.length());
	colNames.append(")");
	
	//拼接列的值  values(值,值,'值')
	StringBuffer colValues = new StringBuffer();
	colValues.append(" ");
	colValues.append("values");
	colValues.append("(");
	//遍历所有属性，获得属性值
	for(Field f :fields) {
		//设置可见性
		f.setAccessible(true);
		//获得值
		try {
			Object value = f.get(t);
			if(value.getClass().toString().contains("String")) {
				colValues.append("'");
				colValues.append(value);
				colValues.append("'");
				colValues.append(",");
			}else if(value.getClass().toString().contains("Date")) {
				java.util.Date d = (java.util.Date) value;
				// values(1,'tom',to_date('','yyyy-mm-dd hh24:mi:ss'))
				colValues.append("to_date('");
				colValues.append(d.toLocaleString());
				colValues.append("','yyyy-mm-dd hh24:mi:ss'),");
			}else{
				colValues.append(value);
				colValues.append(",");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	colValues.delete(colValues.length()-1, colValues.length());
	colValues.append(")");
	
	String sql = "insert into "+tableName+colNames.toString()+colValues.toString();
	//System.out.println(sql);
	
	query(sql);
	close();
}










	/**
	 * 关闭资源
	 * */
	public static void close() {
		try {
			if(rs!=null) {
				rs.close();
			}
			if(stat!=null) {
				stat.close();
			}
			if(conn!=null) {
				conn.close();
			}
		}catch (Exception e) {
			System.out.println("关闭资源异常");
			e.printStackTrace();
		}
	}
}
