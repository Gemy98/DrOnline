package com.example.dronlinef;

public class Patient {


    private String Full_Name;
    private String Address;
    private String Phoneno;
    private String Dateofb;


    public String getFull_Name() {
        return Full_Name;
    }

    public String getAddress() {
        return Address;
    }

    public String getPhoneno() {
        return Phoneno;
    }

    public String getDateofb() {
        return Dateofb;
    }


    public void setFull_Name(String full_Name) {
        Full_Name = full_Name;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public void setPhoneno(String phoneno) {
        Phoneno = phoneno;
    }

    public void setDateofb(String dateofb) {
        Dateofb = dateofb;
    }
}
