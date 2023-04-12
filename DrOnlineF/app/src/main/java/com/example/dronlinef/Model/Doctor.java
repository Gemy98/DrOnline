package com.example.dronlinef.Model;

public class Doctor {

    private String name, image, address;


    public Doctor() {
    }

    public Doctor(String name, String phone, String address) {
        this.name = name;
        this.image = phone;
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
