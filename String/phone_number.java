package String;

import java.util.Scanner;

public class phone_number {
    //1. 键盘录入一个字符串，用 Scanner 实现
    //2. 截取字符串前三位
    //3. 截取字符串后四位
    //4. 将截取后的两个字符串，中间加上进行拼接，输出结果
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.println("请输入十位数号码");
        String number = sc.nextLine();
        String sub_number1 = number.substring(0,3);
        String sub_number2 = number.substring(7,11);
        System.out.println(sub_number1+"****"+ sub_number2);


    }
}
