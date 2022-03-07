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
                    System.out.print("请输入客服姓名：");
                    String name=scanner.next();
                    System.out.print("请输入客服性别：");
                    String sex=scanner.next();
                    System.out.print("请输入客服年龄：");
                    int age =scanner.nextInt();
                    System.out.print("请输入客服手机号：");
                    String phone=scanner.next();
                    System.out.print("请输入客服邮箱：");
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
                    System.out.print("请输入客服姓名：");
                    String name1=scanner.next();
                    System.out.print("请输入客服性别：");
                    String sex1=scanner.next();
                    System.out.print("请输入客服年龄：");
                    int age1 =scanner.nextInt();
                    System.out.print("请输入客服手机号：");
                    String phone1=scanner.next();
                    System.out.print("请输入客服邮箱：");
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
