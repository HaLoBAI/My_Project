package String;

import java.util.Scanner;

public class denglu_project {
    // 已知用户名和密码，请用程序实现模拟用户登录。总共给三次机会，登录之后，给出相应的提示
    public static void main(String[] args) {
        //确定与之匹配的正常账号密码
        String Accessable = "1692554516";
        String passworld = "123456";
        Scanner sc = new Scanner(System.in);

        //三次机会登录
        for (int i = 1; i <= 3; i++) {

            //用户登录的输入
            System.out.println("请分别输入账号密码");
            String Accessable1 = sc.nextLine();
            String passworld1 = sc.nextLine();
            //登录成功给出提示
            if(Accessable.equals(Accessable1) && passworld.equals(passworld1) ) {
                System.out.println("登陆成功");
                break;
            }
            else{
                System.out.println("登陆失败您还有"+ (3-i) +"次机会");
                //给出相应的提示
                if (i == 3){
                    System.out.println("明天再来把，机会用完了");
                }

            }
        }
    }
}
