package com.example.dronlinef;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.dronlinef.Interface.IFirebaseLoadDone2;
import com.example.dronlinef.Model.Doctor;


import com.example.dronlinef.Model.Pharmacy;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.toptoche.searchablespinnerlibrary.SearchableSpinner;

import java.util.ArrayList;
import java.util.List;

public class SearchForDoctor extends AppCompatActivity implements IFirebaseLoadDone2 {


    DatabaseReference reference;

    ValueEventListener valueEventListener;
    ArrayAdapter<String> arrayAdapter;
    ArrayAdapter<String> arrayAdapter2;

    ArrayList<String> spinngerDataList;
    ArrayList<String> spinngerDataList2;


    SearchableSpinner spinner;
    SearchableSpinner spinner2;

    // IFirebaseLoadDone iFirebaseLoadDone ;
    IFirebaseLoadDone2 iFirebaseLoadDone2;

    List<Pharmacy> pharmacies;
    List<Doctor> doctors;
    BottomSheetDialog bottomSheetDialog;
    TextView doc_name, doc_address;
    ImageView doc_image;
    FloatingActionButton btn_fav;

    Boolean isFirstTimeClick = true;


    String RES;

    Button button;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_for_doctor);

        RES = "ElMansoura";

        bottomSheetDialog = new BottomSheetDialog(this);
        final View bottom_sheet_dialog = getLayoutInflater().inflate(R.layout.layout_doc, null);
        doc_address = (TextView) bottom_sheet_dialog.findViewById(R.id.doc_address);
        doc_name = (TextView) bottom_sheet_dialog.findViewById(R.id.doc_name);
        doc_image = (ImageView) bottom_sheet_dialog.findViewById(R.id.doc_image);
        btn_fav = (FloatingActionButton) bottom_sheet_dialog.findViewById(R.id.btn_Fav);


        iFirebaseLoadDone2 = this;


        button = findViewById(R.id.button);


        bottomSheetDialog.setContentView(bottom_sheet_dialog);


        spinngerDataList2 = new ArrayList<>();
        arrayAdapter2 = new ArrayAdapter<String>(SearchForDoctor.this, android.R.layout.simple_spinner_dropdown_item, spinngerDataList2);
        spinner = findViewById(R.id.spinner);
        arrayAdapter2.add("ElMansoura");
        arrayAdapter2.add("Benha");
        arrayAdapter2.add("Cairo");
        spinner.setAdapter(arrayAdapter2);
        RES = spinner.getSelectedItem().toString().trim();


        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                RES = spinner.getSelectedItem().toString().trim();
                //    Toast.makeText(SearchForPharmacy.this, RES, Toast.LENGTH_SHORT).show();
                reference = FirebaseDatabase.getInstance().getReference("Doctors").child(RES);
                retrieveData();

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }

        });


        reference = FirebaseDatabase.getInstance().getReference("Doctors").child(RES);


        spinngerDataList = new ArrayList<>();
        arrayAdapter = new ArrayAdapter<String>(SearchForDoctor.this, android.R.layout.simple_spinner_dropdown_item, spinngerDataList);

        spinner2 = findViewById(R.id.spinner2);
        spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (!isFirstTimeClick) {
                    Doctor doctor = doctors.get(i);
                    doc_name.setText(doctor.getName());
                    doc_address.setText(doctor.getAddress());
                    Picasso.get().load(doctor.getImage()).into(doc_image);
                    bottomSheetDialog.show();
                } else
                    isFirstTimeClick = false;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spinner2.setAdapter(arrayAdapter);
        retrieveData();


        btn_fav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //close the bottom sheet
                bottomSheetDialog.dismiss();
                Toast.makeText(SearchForDoctor.this, "Add Fav !!", Toast.LENGTH_SHORT).show();

            }
        });


        // Getting Data zay el address w el phone w kda


    }

    public void retrieveData() {

        //  reference.add;

        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<Doctor> doctors = new ArrayList<>();

                for (DataSnapshot docSnapShot : dataSnapshot.getChildren()) {
                    doctors.add(docSnapShot.getValue(Doctor.class));

                }

                iFirebaseLoadDone2.onFirebaseLoadSuccess(doctors);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                iFirebaseLoadDone2.onFirebaseLoadFailed(databaseError.getMessage());
            }
        });


    }


    @Override
    public void onFirebaseLoadSuccess(List<Doctor> doctorList) {
        doctors = doctorList;
        // Get the names
        List<String> name_list = new ArrayList<>();
        for (Doctor doctor : doctorList) {
            name_list.add(doctor.getName());
            // create adapter for the spinner
            ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, name_list);
            spinner2.setAdapter(adapter);

        }


    }

    @Override
    public void onFirebaseLoadFailed(String message) {

    }
}