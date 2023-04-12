package com.example.dronlinef.Interface;

import com.example.dronlinef.Model.Doctor;

import java.util.List;

public interface IFirebaseLoadDone2 {
    void onFirebaseLoadSuccess(List<Doctor> doctorList);

    void onFirebaseLoadFailed(String message);
}
