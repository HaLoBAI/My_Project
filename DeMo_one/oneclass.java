package DeMo_one;

public class oneclass {
    public static void main(String[] args) {
        one o = new one();
        o.setname("张三");
        System.out.println(o.getname());
        o.setage(18);
        System.out.println(o.getage());
        one o1 = new one("张三",18);
        o1.show();

    }
}
