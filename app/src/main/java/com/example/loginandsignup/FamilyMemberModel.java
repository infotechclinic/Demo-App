package com.example.loginandsignup;

public class FamilyMemberModel {
    private int id;
    private String name;
    private String age;
    private String relation;

    public FamilyMemberModel() {}

    public FamilyMemberModel(int id, String name, String age, String relation) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.relation = relation;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public String getAge() {
        return age;
    }

    public String getRelation() {
        return relation;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public void setRelation(String relation) {
        this.relation = relation;
    }
}
