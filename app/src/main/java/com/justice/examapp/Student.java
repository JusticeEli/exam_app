package com.justice.examapp;

public class Student {
    private String firstName;
    private String lastName;
    private String id;
    private int marks;
    private int outOf;

    public Student() {
    }

    public Student(String firstName, String lastName, String id, int marks) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.id = id;
        this.marks = marks;
    }
    public int getOutOf() {
        return outOf;
    }

    public void setOutOf(int outOf) {
        this.outOf = outOf;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public int getMarks() {
        return marks;
    }

    public void setMarks(int marks) {
        this.marks = marks;
    }
}
