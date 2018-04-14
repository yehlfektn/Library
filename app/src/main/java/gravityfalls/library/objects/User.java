package gravityfalls.library.objects;

public class User {
    private String name;
    private String family_name;

    public User(){

    }

    public User(String name, String family_name) {
        this.name = name;
        this.family_name = family_name;
    }

    public String getName() {
        return name;
    }

    public String getFamily_name() {
        return family_name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setFamily_name(String family_name) {
        this.family_name = family_name;
    }
}
