package com.example.covidregister.Utilities;

import com.google.firebase.database.IgnoreExtraProperties;
@IgnoreExtraProperties
public class Location {
    private String user_id;
    private String names;
    private String surname;
    private String address;
    private String id;
    private String sex;
    private String age;
    private String PhoneNumber;
    private String Temperature;
    private String DateTime;


    public Location(String user_id, String names, String surname, String address, String id, String sex, String age, String phoneNumber, String temperature, String dateTime) {
        this.user_id = user_id;
        this.names = names;
        this.surname = surname;
        this.address = address;
        this.id = id;
        this.sex = sex;
        this.age = age;
        PhoneNumber = phoneNumber;
        Temperature = temperature;
        DateTime = dateTime;
    }

    public Location() {

    }


    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getNames() {
        return names;
    }

    public void setNames(String names) {
        this.names = names;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getPhoneNumber() {
        return PhoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.PhoneNumber = phoneNumber;
    }

    public String getTemperature() {
        return Temperature;
    }

    public void setTemperature(String temperature) {
        Temperature = temperature;
    }

    public String getDateTime() {
        return DateTime;
    }

    public void setDateTime(String dateTime) {
        DateTime = dateTime;
    }

    @Override
    public String toString() {
        return "Location{" +
                "user_id='" + user_id + '\'' +
                ", names='" + names + '\'' +
                ", surname='" + surname + '\'' +
                ", address='" + address + '\'' +
                ", id='" + id + '\'' +
                ", sex='" + sex + '\'' +
                ", age='" + age + '\'' +
                ", PhoneNumber='" + PhoneNumber + '\'' +
                ", Temperature='" + Temperature + '\'' +
                ", DateTime='" + DateTime + '\'' +
                '}';
    }
}
