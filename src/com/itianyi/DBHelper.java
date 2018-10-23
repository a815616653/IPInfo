package com.itianyi;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 数据库连接工具
 *
 **/
public class DBHelper {
    private Connection conn = null;
    private PreparedStatement ps = null;
    private ResultSet rs = null;

    //加载驱动
    static {
        try {
            //方式一  固定写死  不方便更改数据库操作
//          Class.forName("com.mysql.jdbc.Driver"); //Oracle驱动为: oracle.jdbc.driver.OracleDriver
            //方式二  读取配置文件操作  如果需要更换数据库只需要修改配置文件即可   推荐
            try {
                Class.forName(MyProperties.getInstance().getProperty("driverName"));
            } catch (IOException e) {
                e.printStackTrace();
            }

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }


    //驱动管理连接指定数据库  获取连接对象
    public Connection getConnection() {

        try {
            //方式一   固定写死  不方便更改数据库操作
            //驱动管理指定数据库并连接          参数说明  端口信息  数据库账户  密码
//          conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/test", "root", "a");
            //方式二  读取配置文件操作  如果需要更换数据库只需要修改配置文件即可   推荐
            try {
                conn = DriverManager.getConnection(MyProperties.getInstance().getProperty("url"),MyProperties.getInstance());
            } catch (IOException e) {
                e.printStackTrace();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return conn;
    }

    //关闭所有对象              连接                预处理                 结果集
    public void closeAll(Connection conn,PreparedStatement ps,ResultSet rs) {
        if(conn!=null) {
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if(ps!=null) {
            try {
                ps.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if(rs!=null) {
            try {
                rs.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }



    /**单条语句的增删改   说明:在执行成功后函数返回的结果为非零整数
     * @param sql  需要执行的sql语句
     * @param params 执行sql语句的参数
     * @return
     */
    public int doUpdate(String sql,List<Object> params) {
        int result = 0;
        //获取连接
        conn = getConnection();
        //预处理
        try {
            ps = conn.prepareStatement(sql);
            //设置参数得到结果集
            this.setParams(ps,params);
            //预处理执行得到结果反馈
            result = ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            //关闭所有对象
            this.closeAll(conn, ps, rs);
        }
        return result;
    }


    /**
     * 多条语句的增删改     说明:这些语句要么同时成功，要么都失败
     * @param sqls   多条sql语句
     * @param params 执行参数
     * @return
     */
    public int doUpdate(List<String> sqls, List<Object> params) {
        int result = 0;
        //获取连接
        conn = getConnection();
        try {
            //多条语句的执行涉及到事务    设置事务提交方式为手动
            conn.setAutoCommit(false);
            //判断sql语句集合
            if(sqls!=null&&sqls.size()>0) {
                //循环每一条语句执行
                for(int i=0;i<sqls.size();i++) {
                    ps = conn.prepareStatement(  sqls.get(i) );
                    //设置参数
                    ps.setObject(i+1, params.get(i));
                    //执行并反馈
                    result = ps.executeUpdate();
                }
            }
            //手动提交数据
            conn.commit();
        } catch (SQLException e) {
            //出现错误则回滚数据
            try {
                conn.rollback();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
            e.printStackTrace();
        } finally {
            //回复事务自动提交
            try {
                conn.setAutoCommit(true);
            } catch (SQLException e) {
                e.printStackTrace();
            }
            //关闭所有对象
            this.closeAll(conn, ps, rs);
        }
        return result;
    }



    /**
     * 设置预处理的参数
     * @param ps      预处理
     * @param params 参数集合
     * @throws SQLException
     */
    public void setParams(PreparedStatement ps,List<Object> params) throws SQLException {
        if(params!=null&&params.size()>0) {
            for(int i=0;i<params.size();i++) {
                ps.setObject(i+1, params.get(i));
            }
        }
    }


    /**
     * 查询sql语句单条结果
     * @param sql   查询的sql语句
     * @param params执行sql所需参数
     * @return
     */
    public Map<String,Object> findSingleObject(String sql,List<Object> params){
        Map<String,Object> map = new HashMap<String,Object>();
        //获取连接
        conn = getConnection();
        //预处理
        try {
            ps = conn.prepareStatement(sql);
            //设置参数
            this.setParams(ps, params);
            //执行查询得到结果集
            rs = ps.executeQuery();
            //获取数据库该表所有字段名
            List<String> names = getAllColumnName(rs);
            if(rs.next()) {
                //循环names
                for(String name:names) {
                    map.put(name,  rs.getObject(name) );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            //关闭对象
            this.closeAll(conn, ps, rs);
        }
        return map;
    }


    /**
     * 查询sql语句多条结果
     * @param sql
     * @param params
     * @return
     */
    public List<Map<String,Object>> findMulitObject(String sql,List<Object> params){
        List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
        //连接
        conn = this.getConnection();
        //预处理
        try {
            ps = conn.prepareStatement(sql);
            //设置参数
            this.setParams(ps, params);
            //执行查询得到结果集
            rs = ps.executeQuery();
            //获取所有列名
            List<String> names = this.getAllColumnName(rs);
            while(rs.next()) { //注意这里不能用if 因为使用if的话只能执行一次 最后得到的结果就成了查一条数据
                Map<String, Object> map = new HashMap<String,Object>();
                //循环迭代
                for(String name:names) {
                    map.put(name, rs.getObject(name));
                }
                list.add( map );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }



    /**
     * 根据结果集获取数据库中的所有列表名
     * @param rs
     * @return
     */
    private List<String> getAllColumnName(ResultSet rs) {
        List<String> names = new ArrayList<String>();
        try {
            ResultSetMetaData rsmd = rs.getMetaData();
            for(int i=0;i<rsmd.getColumnCount();i++) {
                names.add(   rsmd.getColumnName(i+1)  );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return names;
    }

}
