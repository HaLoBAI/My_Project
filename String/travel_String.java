package String;

import java.util.Scanner;

public class travel_String {
    //键盘录入一个字符串，用 Scanner 实现
    //遍历字符串，首先要能够获取到字符串中的每一个字符,
    // public char charAt(int index)：返回指定索引处的
    //char值，字符串的索引也是从0开始的
    //遍历字符串，其次要能够获取到字符串的长度, public int length()：返回此字符串的长度
    //遍历打印
    public static void main(String[] args) {
        //键盘录入一个字符串，用 Scanner 实现
        Scanner sc = new Scanner(System.in);
        String s = sc.nextLine();
        for (int i = 0; i <s.length() ; i++) {
            //char ss = s.charAt(i);
            //System.out.println(ss);
          char[]  sss = s.toCharArray();
          System.out.println(sss[i]);


        }
    }
}