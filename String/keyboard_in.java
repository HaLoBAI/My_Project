package String;

import java.util.Scanner;

public class keyboard_in {

    /*  next() : 遇到了空格, 就不再录入数据了
        结束标记: 空格, tab键
    nextLine() : 可以将数据完整的接收过来
        结束标记: 回车换行符
    */
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
       // System.out.println("请输入1'a b c'");
      //  String next = sc.next();
     //   System.out.println(next);
        System.out.println("请输入2'a b c'");
        String nextLine = sc.nextLine();
        System.out.println(nextLine);
    }
}
