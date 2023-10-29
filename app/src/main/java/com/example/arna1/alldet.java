package com.example.arna1;

public class alldet {
    String name,age,address,blood,phone,gender;
    private double Longitude,Latitude;

    public alldet(String name, String age, String address, String blood, String phone, String gender, double longitude, double latitude) {
        this.name = name;
        this.age = age;
        this.address = address;
        this.blood = blood;
        this.phone = phone;
        this.gender = gender;
        Longitude = longitude;
        Latitude = latitude;
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getBlood() {
        return blood;
    }

    public void setBlood(String blood) {
        this.blood = blood;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public double getLongitude() {
        return Longitude;
    }

    public void setLongitude(double longitude) {
        Longitude = longitude;
    }

    public double getLatitude() {
        return Latitude;
    }

    public void setLatitude(double latitude) {
        Latitude = latitude;
    }
}
