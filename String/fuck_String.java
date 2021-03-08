package String;

import java.util.Scanner;

public class fuck_String {
    //1. 键盘录入一个字符串，用 Scanner 实现
    //2. 替换敏感词
    // String replace(CharSequence target, CharSequence replacement)
    // 将当前字符串中的target内容，使用replacement进行替换，返回新的字符串
    //3. 输出结果
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        String s = sc.nextLine();
        String ss = s.replace("tmd","***");
        System.out.println(ss);
    }
}
