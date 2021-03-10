package students_cards;

import java.util.ArrayList;
import java.util.Scanner;

public class students {

    public static void main(String[] args) {
        ArrayList<students1> list = new ArrayList<>();
        //定义一个大框架 switch语句进行构建
        //键盘输入
        //针对目前我们的所学内容，完成一个综合案例：学生管理系统！该系统主要功能如下：
        //添加学生：通过键盘录入学生信息，添加到集合中
        //删除学生：通过键盘录入要删除学生的学号，将该学生对象从集合中删除
        //修改学生：通过键盘录入要修改学生的学号，将该学生对象其他信息进行修改
        //查看学生：将集合中的学生对象信息进行展示
        ////创建集合对象 ArrayList<Student> array = new ArrayList<Student>(); //为了提高代码的复用性，我们用方法来改进程序 addStudent(array); addStudent(array); addStudent(array); //遍历集合，采用通用遍历格式实现 for (int i = 0; i < array.size(); i++) { Student s = array.get(i); System.out.println(s.getName() + "," + s.getAge()); } }/* 两个明确： 返回值类型：void 参数：ArrayList<Student> array */ public static void addStudent(ArrayList<Student> array) { //键盘录入学生对象所需要的数据 Scanner sc = new Scanner(System.in); System.out.println("请输入学生姓名:"); String name = sc.nextLine(); System.out.println("请输入学生年龄:"); String age = sc.nextLine(); //创建学生对象，把键盘录入的数据赋值给学生对象的成员变量 Student s = new Student(); s.setName(name); s.setAge(age); //往集合中添加学生对象 array.add(s); } } 121314151617181920212223242526272829303132333435363738394041424344454647484950
        //退出系统：结束程序
        //实现步骤
        //1. 定义学生类，包含以下成员变量
        //学生类： Student成员变量：
        //学号：sid
        //姓名：name
        //年龄：age
        //生日：birthday
        //构造方法：
        //无参构造
        //带四个参数的构造成员方法：
        //每个成员变量对应给出get/set方法
        //2. 学生管理系统主界面的搭建步骤
        //2.1 用输出语句完成主界面的编写
        //2.2 用Scanner实现键盘录入数据
        //2.3 用switch语句完成操作的选择
        //2.4 用循环完成再次回到主界面
        //3. 学生管理系统的添加学生功能实现步骤
        //3.1 用键盘录入选择添加学生
        //3.2 定义一个方法，用于添加学生
        //显示提示信息，提示要输入何种信息
        //键盘录入学生对象所需要的数据
        //创建学生对象，把键盘录入的数据赋值给学生对象的成员变量
        //将学生对象添加到集合中（保存）
        //给出添加成功提示
        //3.3 调用方法
        //4. 学生管理系统的查看学生功能实现步骤
        //4.1 用键盘录入选择查看所有学生信息
        //4.2 定义一个方法，用于查看学生信息
        //显示表头信息
        //将集合中数据取出按照对应格式显示学生信息，年龄显示补充“岁”
        //4.3 调用方法
        //5. 学生管理系统的删除学生功能实现步骤
        //5.1 用键盘录入选择删除学生信息
        //5.2 定义一个方法，用于删除学生信息
        //显示提示信息
        //键盘录入要删除的学生学号
        //调用getIndex方法，查找该学号在集合的索引
        //如果索引为-1，提示信息不存在
        //如果索引不是-1，调用remove方法删除并提示删除成功
        //5.3 调用方法
        //6. 学生管理系统的修改学生功能实现步骤
        //6.1 用键盘录入选择修改学生信息
        //6.2 定义一个方法，用于修改学生信息
        //显示提示信息
        //键盘录入要修改的学生学号
        //调用getIndex方法，查找该学号在集合的索引
        //如果索引为-1，提示信息不存在
        //如果索引不是-1，键盘录入要修改的学生信息
        //集合修改对应的学生信息
        //给出修改成功提示
        //6.3 调用方法
        //7. 退出系统
        //使用System.exit(0);退出JVM
        Scanner sc = new Scanner(System.in);
        // 定义一个列表并使用student1的对象

        all:
        while (true) {
            System.out.println("1.添加学生");
            System.out.println("2.删除学生");
            System.out.println("3.修改学生");
            System.out.println("4.查看学生");
            String option = sc.next();
            switch (option) {

                //添加学生：通过键盘录入学生信息，添加到集合中
                //删除学生：通过键盘录入要删除学生的学号，将该学生对象从集合中删除
                //修改学生：通过键盘录入要修改学生的学号，将该学生对象其他信息进行修改
                //查看学生：将集合中的学生对象信息进行展示
                case "1":
                    //System.out.println("添加学生");
                    //定义方法添加学生
                    appendstu(list);
                    break;
                case "2":
                    //System.out.println("删除学生");
                    //新建个查找的索引类
                    removestudent(list);

                    break;
                case "3":
                    //System.out.println("修改学生");
                    fixstudent(list);
                    break;
                case "4":
                    //System.out.println("查看学生");
                    //定义方法查看
                    looklist(list);
                    break;
                case "5":
                    System.out.println("退出");
                    break all;
                default:
                    System.out.println("输入错误");
                    break;


            }
        }

    }


    public static void appendstu(ArrayList<students1> list) {
        //提示输入学号，姓名，年龄，生日
        System.out.println("请输入要添加的学号");
        Scanner sc = new Scanner(System.in);
        String xuehao = sc.next();
        System.out.println("请输入要添加的姓名");
        String name = sc.next();
        System.out.println("请输入要添加的年龄");
        int age = sc.nextInt();
        System.out.println("请输入要添加的生日");
        String birthday = sc.next();
        students1 input = new students1(xuehao, name, age, birthday);
        list.add(input);
        System.out.println("输入成功");
    }

    public static void looklist(ArrayList<students1> list) {
        for (int i = 0; i < list.size(); i++) {
            students1 nm = list.get(i);
            System.out.println(nm.getXuehao() + "\t\t" + nm.getName() + "\t\t" + nm.getAge() + "\t\t" + nm.getBirthday());
        }

    }

    public static int finindex(ArrayList<students1> list, String input2) {
        int index = -1;
        for (int i = 0; i < list.size(); i++) {
            students1 nm1 = list.get(i);
            String HE = nm1.getXuehao();
            if (HE.equals(input2)) {
                index = i;
                break;
            }
        }
            return index;


    }
    public static void  removestudent(ArrayList<students1> list){
        System.out.println("请输入当前要删除的学生的学号");
        Scanner sc = new Scanner(System.in);
        String number = sc.next();
        int index1 = finindex(list,number);
        if (index1 == -1){
            System.out.println("没有这个人");
        }
        else {
            list.remove(index1);
            System.out.println("删除成功");
        }

    }
    public static void fixstudent(ArrayList<students1> list){
        System.out.println("请输入当前要修改的学生的学号");
        Scanner sc = new Scanner(System.in);
        String number1 = sc.next();
         int index = finindex(list,number1);
        if (index == -1){
            System.out.println("没有这个人");
        }
        else {
            System.out.println("请输入要修改的姓名");
            String name = sc.next();
            System.out.println("请输入要修改的年龄");
            int age = sc.nextInt();
            System.out.println("请输入要修改的生日");
            String birthday = sc.next();
            students1 stu = new students1(number1,name,age,birthday);
            list.set(index,stu);
            System.out.println("修改成功");





        }
    }
}
