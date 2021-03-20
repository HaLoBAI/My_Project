package 练习题二;

import java.util.*;

public class DeMo_doudizu {
    public static void main(String[] args) {
        //各个数据装入容器再拼接
        ArrayList<String> number = new ArrayList<>();
        ArrayList<String> huase = new ArrayList<>();
        Map<Integer, String> poketed = new HashMap<>();
        Collections.addAll(number,"2", "A", "K", "Q", "J", "10", "9", "8", "7", "6", "5", "4", "3");
        Collections.addAll(huase, "♣", "♠", "♦", "♥");
        int id = 0;
        poketed.put(id++,"大王");
        poketed.put(id++,"小王");


        for (String s : number) {
            for (String s1 : huase) {
                String pai = s + s1;
                poketed.put(id++,pai);
            }

        }
     //把输入的键都取出来放在一个单列表中
        //取出索引键
        Set<Integer> ke = poketed.keySet();
        //建立个单列
        ArrayList<Integer> list = new ArrayList<>();
        list.addAll(ke);
        //用这个但列打乱顺序
        Collections.shuffle(list);
        //再定三个新单列表存储三个人的牌
        ArrayList<Integer> paiyou1 = new ArrayList<>();
        ArrayList<Integer> paiyou2 = new ArrayList<>();
        ArrayList<Integer> paiyou3 = new ArrayList<>();
        ArrayList<Integer> dipai = new ArrayList<>();
        //用循环再次分发三人牌与底牌
        for (int i = 0; i < list.size(); i++) {
            Integer it = list.get(i);
            if(i >= 51){
                dipai.add( it);
            }
            else if (i % 3 == 0 ){
                paiyou1.add(it);
            }
            else if (i %3  == 1){
                paiyou2.add(it);
            }
            else if (i % 3 == 2){
                paiyou3.add( it);
            }

        }
        Collections.sort(paiyou1);
        Collections.sort(paiyou2);
        Collections.sort(paiyou3);


        for (Integer pai : paiyou1) {
            String s1 = poketed.get(pai);
            System.out.print(s1 +" " );
        }
         System.out.println();
        for (Integer pai : paiyou2) {
            String s2 = poketed.get(pai);
            System.out.print(s2 +" " );
        }
        System.out.println();
        for (Integer pai : paiyou3) {
            String s3 = poketed.get(pai);
            System.out.print(s3 +" " );
        }
        System.out.println();
        for (Integer pai : dipai) {
            String s4 = poketed.get(pai);
            System.out.print(s4 +" " );
        }





    }
}