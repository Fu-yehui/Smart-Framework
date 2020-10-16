package com.roger.smartframework.helper;



import com.roger.smartframework.util.CollectionUtil;
import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;


/**
 * 数据库操作助手类
 */
public class DatabaseHelper {
    private static final Logger LOGGER= LoggerFactory.getLogger(DatabaseHelper.class);

    private static final QueryRunner QUERY_RUNNER=new QueryRunner();

    /**ThreadLocal存放本地线程变量
     * 将Connection放入ThreadLocal中，确保一个线程只有一个Connection
     */
    private static final ThreadLocal<Connection> CONNECTION_HOLDER=new ThreadLocal<>();



    /**
     * 配置数据库连接池
     */
    private static final BasicDataSource DATA_SOURCE;


    static{

        String driver= ConfigHelper.getJdbcDriver();
        String url=ConfigHelper.getJdbcUrl();
        String username=ConfigHelper.getJdbcUsername();
        String password=ConfigHelper.getJdbcPassword();

        DATA_SOURCE=new BasicDataSource();
        DATA_SOURCE.setDriverClassName(driver);
        DATA_SOURCE.setUrl(url);
        DATA_SOURCE.setUsername(username);
        DATA_SOURCE.setPassword(password);

    }

    /**
     * 获取数据库连接
     * @return
     */
    public static Connection getConnection(){
        /**
         * 先在ThreadLocal中查询Connection以存在
         */
        Connection conn=CONNECTION_HOLDER.get();
        if(conn==null) {
            try {
//                conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);
                conn=DATA_SOURCE.getConnection();
            } catch (SQLException e) {
                LOGGER.error("get connection failure", e);
            }finally {
                /**
                 * 创建一个新的Connection对象后将其放入ThreadLocal中
                 */
                CONNECTION_HOLDER.set(conn);
            }
        }
        return conn;
    }

    /**
     * 使用数据库连接池之后不用手动关闭连接
     */
//    /**
//     * 关闭数据库连接
//     *
//     */
//    public static void closeConnection(){
//        Connection conn=CONNECTION_HOLDER.get();
//        if(conn != null){
//            try{
//                conn.close();
//            }catch (SQLException e) {
//                LOGGER.error("close connection failure",e);
//            }finally {
//                /**
//                 * 使用完毕后，将其从ThreadLocal中移除
//                 */
//                CONNECTION_HOLDER.remove();
//            }
//        }
//    }

    /**
     * 查询实体列表
     * @param entityClass
     *
     * @param params
     * @param <T>
     * @return
     */
    public static <T> List<T> queryEntityList(Class<T> entityClass, Object... params){
        List<T> entityList;
        try{
            Connection conn=getConnection();
            String sql="SELECT * FROM "+getTableName(entityClass);
            entityList=QUERY_RUNNER.query(conn,sql,new BeanListHandler<T>(entityClass),params);
        }catch (SQLException e){
            LOGGER.error("query entity list failure",e);
            throw new RuntimeException(e);
        }
//        finally {
//            closeConnection();
//        }
        return entityList;
    }

    /**
     * 查询单个实体
     * @param entityClass
     * @param id
     * @param <T>
     * @return
     */
    public static <T> T queryEntity(Class<T> entityClass,long id){
        T entity;
        try{
            Connection conn=getConnection();
            String sql="SELECT * FROM "+getTableName(entityClass)+" WHERE id=?";
            entity=QUERY_RUNNER.query(conn,sql,new BeanHandler<T>(entityClass),id);

            LOGGER.debug("queryId= "+id+" result= "+entity);
        }catch (SQLException e){
            LOGGER.error("query entity failure",e);
            throw new RuntimeException(e);
        }
//        finally {
//            closeConnection();
//        }
        return entity;
    }

    /**
     * 执行更新语句（包括update,insert,delete）
     * @param sql
     * @param params
     * @return
     */
    private static int executeUpdate(String sql,Object... params){
        int rows=0;
        try{
            Connection conn=getConnection();
            rows=QUERY_RUNNER.update(conn,sql,params);
            LOGGER.debug("sql= "+sql+" params="+ Arrays.toString(params));
        }catch (SQLException e){
            LOGGER.error("execute update failure",e);
            throw new RuntimeException(e);
        }
//        finally {
//            closeConnection();
//        }

        return rows;
    }

    /**
     * 插入实体
     * @param entityClass
     * @param fieldMap
     * @param <T>
     * @return
     */
    public static <T> boolean insertEntity(Class<T> entityClass, Map<String,Object> fieldMap){
        if(CollectionUtil.isEmpty(fieldMap)){
            LOGGER.error("can not insert entity: fieldMap is empty");
            return false;
        }

        String sql="INSERT INTO "+getTableName(entityClass);
        StringBuilder columns = new StringBuilder("(");
        StringBuilder values= new StringBuilder("(");
        for(String fieldName:fieldMap.keySet()){
            columns.append(fieldName).append(",");
            /**
             * 添加占位符 "?"
             */
            values.append("?, ");
        }
        columns.replace(columns.lastIndexOf(","),columns.length(),")");
        values.replace(values.lastIndexOf(","),values.length(),")");
        sql+=columns.toString()+" VALUES "+values.toString();

        Object[] params=fieldMap.values().toArray();

        return executeUpdate(sql,params)==1;



    }


    /**
     * 更新实体
     * @param entity
     * @param id
     * @param fieldMap
     * @param <T>
     * @return
     */
    public static <T> boolean updateEntity(Class<T> entity,long id,Map<String,Object> fieldMap){
        if(CollectionUtil.isEmpty(fieldMap)){
            LOGGER.error("can not update entity: fieldMap is empty");
            return false;
        }

        String sql="UPDATE "+getTableName(entity)+" SET ";
        StringBuilder columns=new StringBuilder();
        for(String fieldName:fieldMap.keySet()){
            columns.append(fieldName).append("=?, ");
        }
        sql += columns.substring(0,columns.lastIndexOf(", "))+" WHERE id=?";
        List<Object> paramList=new ArrayList<>(fieldMap.values());
        paramList.add(id);
        Object[] params=paramList.toArray();

        return executeUpdate(sql,params)==1;
    }

    public static <T> boolean deleteEntity(Class<T> entityClass,long id){
        String sql="DELETE FROM "+getTableName(entityClass)+" WHERE id=?";
        return executeUpdate(sql,id)==1;
    }

    /**
     * 实体类的类名为表名
     * @param entityClass
     * @return
     */
    private static String getTableName(Class<?> entityClass){
        return entityClass.getSimpleName().toLowerCase();
    }


    /**
     * 执行sql文件
     * @param filePath
     */
    public static void executeSqlFile(String filePath){
        InputStream in=Thread.currentThread().getContextClassLoader().getResourceAsStream(filePath);
        BufferedReader reader=new BufferedReader(new InputStreamReader(in));
        try{

            String sql;
            while((sql=reader.readLine())!=null){
                executeUpdate(sql);
            }
        } catch (IOException e) {
            LOGGER.error("execute sql file failure",e);
            throw new RuntimeException(e);
        }
    }


    /**
     * 关闭数据库连接池
     */
    public static void shutdown(){
        try {
            if (!DATA_SOURCE.isClosed()) {
                DATA_SOURCE.close();
            }
        }catch (Exception e){
            LOGGER.error("DATA_SOURCE shutdown failure",e);
            throw new RuntimeException(e);
        }
    }
}
