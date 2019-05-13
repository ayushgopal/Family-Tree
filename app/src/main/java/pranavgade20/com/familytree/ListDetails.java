package pranavgade20.com.familytree;

public class ListDetails {
    private String name = "name";
    private String relation = "relation";
    private String age = "";

    // Getters
    public String getName(){
        return this.name;
    }
    public String getRelation(){
        return this.relation;
    }
    public String getAge(){
        return this.age;
    }

    //Setters
    public void setName(String str) {
        this.name = str;
    }
    public void setRelation(String str) {
        this.relation = str;
    }
    public void setAge(String age) {
        this.age = age;
    }
}

//TODO : change this to add details in the list