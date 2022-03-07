package test2;

import org.junit.Test;

import java.io.IOException;
import java.lang.reflect.Field;
import java.sql.*;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Properties;

public class mysql {
    //查询客户列表
    public <T> List<T> query(Class<T> tClass, String sql, Object...args){

        Connection connection =null;
        ResultSet resultSet =null;
        PreparedStatement preparedStatement=null;

        try {
            Properties properties = new Properties();
            //括号里内容改为mysql的文本信息的地址
            properties.load(getClass().getClassLoader().getResourceAsStream("test2/sql.proparetis"));
            //获取mysql的文本信息的地址,账户.....
            String user = properties.getProperty("user");
            String url = properties.getProperty("url");
            String password = properties.getProperty("password");
            String drive = properties.getProperty("drive");
            Class.forName(drive);//注册驱动，可以不用写
            connection = DriverManager.getConnection(url, user, password);
            preparedStatement = connection.prepareStatement(sql);
            for (int i = 0; i < args.length; i++) {
                preparedStatement.setObject(i+1,args[i]);
            }
            //获取数据库返回数据
            resultSet = preparedStatement.executeQuery();
            ResultSetMetaData metaData = resultSet.getMetaData();
            //获取数据列数
            int columnCount = metaData.getColumnCount();
            //创建一个集合
            ArrayList<T> ts = new ArrayList<>();
            while (resultSet.next()){
                T t = tClass.newInstance();
                for (int i = 0; i < columnCount; i++) {
                    //获取列的数据
                    Object object = resultSet.getObject(i + 1);
                    //获取列的名
                    String columnLabel = metaData.getColumnLabel(i+1);
                    //利用反射
                    Field declaredField = tClass.getDeclaredField(columnLabel);
                    declaredField.setAccessible(true);
                    declaredField.set(t,object);
                }
                ts.add(t);
            }
            //关闭资源 并返回结果
            return ts;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            close2(connection,preparedStatement,resultSet);
        }
        return null;
    }
    //添加,删除客户
    public int modifyCustomer(String sql,Object...args){
        Connection connection=null;
        PreparedStatement preparedStatement=null;
        try {
            Properties properties = new Properties();
            properties.load(getClass().getClassLoader().getResourceAsStream("test2/sql.proparetis"));
            String user = properties.getProperty("user");
            String url = properties.getProperty("url");
            String password = properties.getProperty("password");
            String drive = properties.getProperty("drive");
            Class.forName(drive);//注册驱动，可以不用写
            connection = DriverManager.getConnection(url,user,password);
            preparedStatement = connection.prepareStatement(sql);
            for (int i = 0; i < args.length; i++) {
                preparedStatement.setObject(i+1,args[i]);
            }
            //sql语句执行失败或没执行返回的是0，
            int i = preparedStatement.executeUpdate();
            return i;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally {
            close(connection,preparedStatement);
        }
        return 0;
    }
    //关闭资源
    public void close(Connection c,PreparedStatement p){
       try {
           if (c !=null)
           c.close();
       }catch (Exception e){
           System.out.println(e);
       }
        try {
            if (p !=null)
                p.close();
        }catch (Exception e){
            System.out.println(e);
        }

    }
    public void close2(Connection c,PreparedStatement p, ResultSet r){
        try {
            if (c !=null)
                c.close();
        }catch (Exception e){
            System.out.println(e);
        }
        try {
            if (p !=null)
                p.close();
        }catch (Exception e){
            System.out.println(e);
        }
        try {
            if (c !=null)
                r.close();
        }catch (Exception e){
            System.out.println(e);
        }

    }
}
