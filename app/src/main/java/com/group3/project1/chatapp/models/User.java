package com.group3.project1.chatapp.models;

import java.io.Serializable;
import java.util.Objects;

public class User implements Serializable {
    public static String FEMALE = "FEMALE";
    public static String MALE = "MALE";

    private String firstName;
    private String lastName;
    private String city;
    private String gender;
    private String email;

    public User(String email, String firstName, String lastName, String city, String gender) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.city = city;
        this.gender = gender;
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return firstName.equals(user.firstName) && lastName.equals(user.lastName) && city.equals(user.city) && gender.equals(user.gender) && email.equals(user.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(firstName, lastName, city, gender, email);
    }
}
