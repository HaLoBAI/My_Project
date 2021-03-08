package DeMo_one;

public class one {
    private String name;
    private int age;
       public one(String name ,int age) {
           this.name = name;
           this.age = age;
       }

    public one(){
           }


    public String getname() {
        return name;
    }

    public void setname(String name) {
        this.name = name;
    }

    public int getage() {
        return age;
    }

    public void setage(int age) {
        this.age = age;
    }
           public void show(){
               System.out.println(name +"...."+ age);
           }
       }
