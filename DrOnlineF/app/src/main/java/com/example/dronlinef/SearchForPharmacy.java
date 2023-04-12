package com.example.dronlinef;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.dronlinef.Interface.IFirebaseLoadDone;
import com.example.dronlinef.Model.Pharmacy;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.squareup.picasso.Picasso;
import com.toptoche.searchablespinnerlibrary.SearchableSpinner;

import java.util.ArrayList;
import java.util.List;

public class SearchForPharmacy extends AppCompatActivity implements IFirebaseLoadDone {

    DocumentReference documentReference1;
    DocumentReference documentReference2;

    DatabaseReference reference;

    ValueEventListener valueEventListener;
    ArrayAdapter<String> arrayAdapter;
    ArrayAdapter<String> arrayAdapter2;

    ArrayList<String> spinngerDataList;
    ArrayList<String> spinngerDataList2;


    SearchableSpinner spinner;
    SearchableSpinner spinner2;
    IFirebaseLoadDone iFirebaseLoadDone;
    List<Pharmacy> pharmacies;
    BottomSheetDialog bottomSheetDialog;
    TextView Pharmacy_name, Pharmacy_address;
    ImageView Pharmacy_image;
    FloatingActionButton btn_fav;

    Boolean isFirstTimeClick = true;

    String RES;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_for_pharmacy);
        bottomSheetDialog = new BottomSheetDialog(this);
        final View bottom_sheet_dialog = getLayoutInflater().inflate(R.layout.layout_pharmacy, null);
        Pharmacy_address = (TextView) bottom_sheet_dialog.findViewById(R.id.pharmacy_address);
        Pharmacy_name = (TextView) bottom_sheet_dialog.findViewById(R.id.pharmacy_name);
        Pharmacy_image = (ImageView) bottom_sheet_dialog.findViewById(R.id.pharmacy_image);
        btn_fav = (FloatingActionButton) bottom_sheet_dialog.findViewById(R.id.btn_Fav);


        iFirebaseLoadDone = this;
        bottomSheetDialog.setContentView(bottom_sheet_dialog);

        spinngerDataList = new ArrayList<>();
        spinngerDataList2 = new ArrayList<>();


        arrayAdapter = new ArrayAdapter<String>(SearchForPharmacy.this, android.R.layout.simple_spinner_dropdown_item, spinngerDataList);
        arrayAdapter2 = new ArrayAdapter<String>(SearchForPharmacy.this, android.R.layout.simple_spinner_dropdown_item, spinngerDataList2);
        arrayAdapter2.add("ElMansoura");
        arrayAdapter2.add("Benha");
        arrayAdapter2.add("Cairo");


        spinner = findViewById(R.id.spinner);
        spinner.setAdapter(arrayAdapter2);
        RES = spinner.getSelectedItem().toString().trim();

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                RES = spinner.getSelectedItem().toString().trim();
                //    Toast.makeText(SearchForPharmacy.this, RES, Toast.LENGTH_SHORT).show();
                reference = FirebaseDatabase.getInstance().getReference("Pharmacy").child(RES);
                retrieveData();

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }

        });

        reference = FirebaseDatabase.getInstance().getReference("Pharmacy").child(RES);


        spinner2 = findViewById(R.id.spinner2);
        spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (!isFirstTimeClick) {
                    Pharmacy pharmacy = pharmacies.get(i);
                    Pharmacy_name.setText(pharmacy.getName());
                    Pharmacy_address.setText(pharmacy.getAddress());
                    Picasso.get().load(pharmacy.getImage()).into(Pharmacy_image);
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
                Toast.makeText(SearchForPharmacy.this, "Add Fav !!", Toast.LENGTH_SHORT).show();

            }
        });


        // Getting Data zay el address w el phone w kda


    }

    public void retrieveData() {

        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<Pharmacy> pharmacies = new ArrayList<>();
                for (DataSnapshot pharmacySnapShot : dataSnapshot.getChildren()) {
                    pharmacies.add(pharmacySnapShot.getValue(Pharmacy.class));
                }

                iFirebaseLoadDone.onFirebaseLoadSuccess(pharmacies);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                iFirebaseLoadDone.onFirebaseLoadFailed(databaseError.getMessage());
            }
        });

    }

    @Override
    public void onFirebaseLoadSuccess(List<Pharmacy> pharmacyList) {
        pharmacies = pharmacyList;
        List<String> name_list = new ArrayList<>();
        for (Pharmacy pharmacy : pharmacyList) {
            name_list.add(pharmacy.getName());
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, name_list);
        spinner2.setAdapter(adapter);

    }

    @Override
    public void onFirebaseLoadFailed(String message) {

    }
}
