package com.example.dronlinef;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.dronlinef.Model.User;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

public class ProfileActivity extends AppCompatActivity {


    public static final String TAG = "TAG";
    EditText Fullname, Address, Phoneno, Dateofb;

    Button save;

    DatabaseReference Reff;


    Patient patient;


    FirebaseAuth mAuth;
    FirebaseUser currentUser;


    TextView elwad;


    FirebaseFirestore fstore;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        Fullname = (EditText) findViewById(R.id.userfullname);
        Address = (EditText) findViewById(R.id.useraddress);
        Phoneno = (EditText) findViewById(R.id.userphoneno);
        Dateofb = (EditText) findViewById(R.id.userdate);

        save = (Button) findViewById(R.id.usersave);

        patient = new Patient();

        currentUser = FirebaseAuth.getInstance().getCurrentUser();

        Reff = FirebaseDatabase.getInstance().getReference().child("Users").child(currentUser.getUid());

        mAuth = FirebaseAuth.getInstance();
        fstore = FirebaseFirestore.getInstance();


        elwad = findViewById(R.id.elwad);

        Reff.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                //   elwad.setText(user.getUsername());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String FN = Fullname.getText().toString().trim();
                String Addr = Address.getText().toString().trim();
                String Phone = Phoneno.getText().toString().trim();
                String Dateb = Dateofb.getText().toString().trim();
                if (FN.equals("") || Addr.equals("") || Phone.equals("") || Dateb.equals("")) {
                    Toast.makeText(ProfileActivity.this, "all fields are required", Toast.LENGTH_SHORT).show();
                } else {
                    patient.setFull_Name(FN);
                    patient.setAddress(Addr);
                    patient.setDateofb(Dateb);
                    patient.setPhoneno(Phone);

                    FirebaseUser firebaseUser = mAuth.getCurrentUser();
                    String userid = firebaseUser.getUid();

                    Reff = FirebaseDatabase.getInstance().getReference("Users").child(userid).child("user info");

                    DocumentReference documentReference = fstore.collection("users").document(userid);


                    HashMap<String, String> hashMap = new HashMap<>();

                    hashMap.put("Address", Addr);

                    hashMap.put("Phoneno", Phone);

                    hashMap.put("FullName", FN);
                    hashMap.put("Dateofb", Dateb);


                    documentReference.collection("info").document(userid).set(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(ProfileActivity.this, "Inserted Successfullyyyyyyyyy", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(ProfileActivity.this, MainActivity.class));
                            finish();
                            Log.d(TAG, "on Success : user profile is Updated");
                        }
                    });
                }
            }
        });
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            //  case R.id.item1 :
            //   startActivity(new Intent(ProfileActivity.this,MainActivity.class));
            //     break ;
        }


        return super.onOptionsItemSelected(item);
    }
}
