package com.parlakov.medic.models;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by georgi on 13-10-30.
 */
public class Patient {

    private String firstName;
    private String lastName;
    private int age;
    private String phone;
    private List<Examination> examinations;

    public  Patient(){
        this(null, null, 0, null);
    }

    public Patient(String firstName, String lastName, int age, String phone) {
        this.examinations = new ArrayList<Examination>();
        setFirstName(firstName);
        setLastName(lastName);
        setAge(age);
        setPhone(phone);
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

    public List<Examination> getExaminations() {
        return examinations;
    }

    public void setExaminations(List<Examination> examinations) {
        this.examinations = examinations;
    }
}
