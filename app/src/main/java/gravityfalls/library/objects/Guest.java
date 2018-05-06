package gravityfalls.library.objects;

/**
 * Created by 777 on 06.05.2018.
 */

public class Guest extends User {
    private int age;
    private String phone;
    private String arrives;
    private String located;

    public Guest(String name, String family_name, int age, String phone, String arrives, String located) {
        super(name, family_name);
        this.age = age;
        this.phone = phone;
        this.arrives = arrives;
        this.located = located;

    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getArrives() {
        return arrives;
    }

    public void setArrives(String arrives) {
        this.arrives = arrives;
    }

    public String getLocated() {
        return located;
    }

    public void setLocated(String located) {
        this.located = located;
    }
}
