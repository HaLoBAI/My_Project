package String;

public class cut_String {
    private String name;
    private String age;


    public cut_String() {
    }

    public cut_String(String name, String age) {
        this.name = name;
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }
    public  void show(){
        System.out.println(name);
        System.out.println(age);
    }
}
