package com.parlakov.medic.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by georgi on 13-10-30.
 */
public class Patient implements Serializable {

    private String firstName;
    private String lastName;
    private int age;
    private String phone;
    private Collection<Examination> examinations;
    private String imagePath;
    private long id;

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

    public Collection<Examination> getExaminations() {
        return examinations;
    }

    public void setExaminations(List<Examination> examinations) {
        this.examinations = examinations;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getPhotoPath() {
        return imagePath;
    }
}
