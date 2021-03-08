package String;

public class String_differ {
    /*== 比较基本数据类型：比较的是具体的值
== 比较引用数据类型：比较的是对象地址值
*/
    public static void main(String[] args) {
        String name = "abc";
        String name1 = "ABC";
        String name2 = "abc";
        boolean a1 = name.equals(name1);
        boolean a2 = name.equalsIgnoreCase(name1);
        System.out.println(a1);
        System.out.println(a2);


    }
}
