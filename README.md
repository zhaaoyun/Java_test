**客户管理系统**
# 客户管理系统

## 一、创建数据库表格

1.创建数据库表

```mysql
create database customers;
create table customer(
    -> id int primary key auto_increment,
    -> name varchar(30),
    -> sex varchar(5),
    -> age int,
    -> phone varchar(11),
    -> email varchar(30));
```

## 二、数据库



#### (一）.创建一个Customer对象

```java
package test2;

public class Customer {
    private int id;
    private String name;
    private String sex;
    private  int age;
    private String phone;
    private String email;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Customer() {
    }

    public Customer(int id, String name, String sex, int age, String phone, String email) {
        this.id = id;
        this.name = name;
        this.sex = sex;
        this.age = age;
        this.phone = phone;
        this.email = email;
    }

    @Override
    public String toString() {
        return
                "id=" + id +"\n"+
                "姓名='" + name + "\n"+
                "性别sex='" + sex + "\n"+
                "年龄=" + age +
                "手机号='" + phone + "\n"+
                "邮箱='" + email + '\'';
    }
}

```

###  （二）编写主要程序

#####  1.创建连接mysql的文本信息

建立一个文本文件sql.proparetis，存入一下信息

```java
url=数据库地址
user=数据库用户名
password=数据库密码
drive=com.mysql.jdbc.Driver
```

#####  2.对数据库增删改操作

1.编写主要方法

```java
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


```

2.编写main函数

```java
package test2;

import org.junit.Test;

import java.util.List;
import java.util.Scanner;

public class test {
    public static void main(String[] args) {
        boolean s=true;
        mysql mysql = new mysql();
        Scanner scanner = new Scanner(System.in);
        while (s){
            System.out.println("  -----------------客户信息管理软件-----------------\n");
            System.out.println("1.添加客户");
            System.out.println("2.修改客户");
            System.out.println("3.删除客户");
            System.out.println("4.客户列表");
            System.out.println("5.退出");
            System.out.print("请选择（1～5）:");
            int ipnut=scanner.nextInt();
            if(ipnut>5 || ipnut ==0){
                System.out.println("输入错误！！");
            }
            switch (ipnut){
             
                case 1:
                    System.out.println("  --------------------添加客户---------------------");
                    System.out.print("请输入客户姓名：");
                    String name=scanner.next();
                    System.out.print("请输入客户性别：");
                    String sex=scanner.next();
                    System.out.print("请输入客户年龄：");
                    int age =scanner.nextInt();
                    System.out.print("请输入客户手机号：");
                    String phone=scanner.next();
                    System.out.print("请输入客户邮箱：");
                    String email=scanner.next();
                    String sql="insert into customer(name,sex,age,phone,email) values(?,?,?,?,?)";
                    int i1 = mysql.modifyCustomer(sql, name, sex, age, phone, email);
                    System.out.println(i1>0?"修改成功":"修改失败");
                    System.out.println("  -----------------客户信息管理软件-----------------\n");
                    break;
                case 2:    
                    System.out.println("  --------------------修改客户---------------------");
                    System.out.print("请输入需要修改的客户ID：");
                    int Id2 =scanner.nextInt();
                    System.out.print("请输入客户姓名：");
                    String name1=scanner.next();
                    System.out.print("请输入客户性别：");
                    String sex1=scanner.next();
                    System.out.print("请输入客户年龄：");
                    int age1 =scanner.nextInt();
                    System.out.print("请输入客户手机号：");
                    String phone1=scanner.next();
                    System.out.print("请输入客户邮箱：");
                    String email1=scanner.next();
                    String sql3="update customer set name=?,sex=?,age=?,phone=?,email=? where id= ?";
                    int i = mysql.modifyCustomer(sql3,name1,sex1,age1,phone1,email1,Id2);
                    System.out.println(i>0?"添加成功":"添加失败");
                    System.out.println("  -----------------客户信息管理软件-----------------\n");
                    break;
                case 3:
                    System.out.println("  --------------------删除客户---------------------");
                    System.out.print("请输入客户ID：");
                    int id =scanner.nextInt();
                    String sql4="delete from customer where id= ?";
                    int i2 = mysql.modifyCustomer(sql4,id);
                    System.out.println(i2>0?"删除成功":"删除失败");
                    System.out.println("  -----------------客户信息管理软件-----------------\n");
                    break;
                case 4:
                    System.out.println("  --------------------客户列表---------------------");
                    System.out.println( "ID" +"\t"+ "姓名" + "\t\t"+ "性别" + "\t\t"+ "年龄" +"\t"+"手机号" + "\t\t"+ "电子邮件" );
                    String sql2="select*from customer";
                    List<Customer> query2 = mysql.query(Customer.class, sql2);
                    query2.forEach(System.out::print);
                    System.out.println("  -----------------客户信息管理软件-----------------\n");
                    break;
                case 5:
                    System.out.print("确认退出Y/N:");
                    String y_or_n = scanner.next();
                    if (y_or_n.equals("y")||y_or_n.equals("Y")){
                        s=true;   
                    }
                    break;
            }
        }
    }
    //测试
    @Test
    public void test(){
        mysql mysql = new mysql();
        String sql="insert into customer(name,sex,age,phone,eamil) values(?,?,?,?,?)";
        int i = mysql.modifyCustomer(sql,"姚明","男","12","156123422","yao@123.com");
        System.out.println(i>0?"成功":"失败");
    }
    //测试
    @Test
    public void test2(){
        mysql mysql = new mysql();
        String sql="select*from customer";
        List<Customer> query = mysql.query(Customer.class, sql);
        query.forEach(System.out::print);
    }
}
```

总结：注意sql语句。

