package com.example.dronlinef.Interface;

import com.example.dronlinef.Model.Pharmacy;

import java.util.List;

public interface IFirebaseLoadDone {

    void onFirebaseLoadSuccess(List<Pharmacy> pharmacyList);

    void onFirebaseLoadFailed(String message);

}
