package com.example.dronlinef;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity {


    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle mToggle;
    private NavigationView navigationView;

    Button save;
    TextView name;
    TextView number;
    String sname;
    String snumber;


    Button logout;

    Menu menu;


    TextView textviewUsername;

    String ss;
    View mheaderview;

    //public String uName ="" ;


    TextView T1, T2, T3, T4;

    FirebaseAuth mAuth;
    FirebaseUser currentUser = null;
    FirebaseFirestore fstore;
    String userid;
    String UserType;
    FirebaseUser mFirebaseUser = null;

    Button buttoncheck;
    Switch status;
    DocumentReference documentReference;
    DocumentReference documentReference2;
    DocumentReference documentReference3;
    DocumentReference documentReference4;
    DocumentReference documentReference5;

    DatabaseReference reference;

    Button buttonPics;
    Button buttonWaiting;


    String Fe = "";

    String Status = "";

    DatabaseReference myRef;
    final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //   mm = findViewById(R.id.usern);
        //     mm.setTitle("hehe");


        status = findViewById(R.id.onlineswitch);
        buttonPics = findViewById(R.id.buttonPics);
        buttonWaiting = findViewById(R.id.buttonWaiting);


        // mm.removeItem(1);


        navigationView = findViewById(R.id.drawer_nav);


        //   textviewUsername = (TextView) mheaderview.findViewById(R.id.usern);


        drawerLayout = findViewById(R.id.drawer);
        mToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.Open, R.string.Close);
        drawerLayout.addDrawerListener(mToggle);
        mToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        navigationView.setNavigationItemSelectedListener(nav);

        mAuth = FirebaseAuth.getInstance();
        fstore = FirebaseFirestore.getInstance();


        userid = mAuth.getCurrentUser().getUid();

        documentReference3 = fstore.collection("users").document(userid);
        documentReference5 = fstore.collection("users").document(userid);
        documentReference5.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {


                Fe = documentSnapshot.getString("type");
                if (Fe.equals("Doctor")) {
                    buttonPics.setVisibility(View.VISIBLE);
                    status.setVisibility(View.VISIBLE);
                    buttonWaiting.setVisibility(View.VISIBLE);

                    documentReference3.addSnapshotListener(new EventListener<DocumentSnapshot>() {
                        @Override
                        public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                            Status = documentSnapshot.getString("Status");
                            if (Status.equals("Online")) {
                                status.setChecked(true);
                                status.setText("Online");
                            } else {
                                status.setChecked(false);
                                status.setText("Offline");
                            }
                        }
                    });
                }
            }
        });
        documentReference4 = fstore.collection("users").document(userid);
        status.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                if (!b) {
                    documentReference4.update("Status", "Offline");
                    reference = FirebaseDatabase.getInstance().getReference("Users").child("OnlineDoctors").child(userid);
                    reference.removeValue();


                } else {
                    documentReference4.update("Status", "Online");

                    reference = FirebaseDatabase.getInstance().getReference("Users").child("OnlineDoctors").child(userid);


                    HashMap<String, String> hashMap = new HashMap<>();
                    hashMap.put("id", userid);
                    reference.setValue(hashMap);

                }


            }
        });


        T1 = findViewById(R.id.textView);
        T2 = findViewById(R.id.textView2);
        T3 = findViewById(R.id.textView3);
        T4 = findViewById(R.id.textView4);

        buttoncheck = findViewById(R.id.buttoncheck);


        currentUser = mAuth.getCurrentUser();
        assert firebaseUser != null;
        if (currentUser != null) {
            //  fstore = FirebaseFirestore.getInstance();
            documentReference = fstore.collection("users").document(userid).collection("info").document(userid);
            documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
                @Override
                public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                    T1.setText(documentSnapshot.getString("FullName"));
                    T2.setText(documentSnapshot.getString("Phoneno"));
                    T3.setText(documentSnapshot.getString("Dateofb"));
                    T4.setText(documentSnapshot.getString("Address"));
                    if (T1.getText().equals("") || T2.getText().equals("") || T3.getText().equals("") || T4.getText().equals("")) {
                        buttoncheck.setVisibility(View.VISIBLE);
                    }
                }
            });
            documentReference2 = fstore.collection("users").document(userid);
            documentReference2.addSnapshotListener(new EventListener<DocumentSnapshot>() {
                @Override
                public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                    ss = documentSnapshot.getString("type").trim();
                    WhoType whoType = new WhoType(ss);
                    whoType.setWhot(ss);
                }
            });

        }
        buttoncheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, ProfileActivity.class));
            }
        });

        buttonPics.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, ImagesActivity.class);
                startActivity(intent);
            }
        });

        buttonWaiting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, ChatActivity.class);

                // i.putExtra("hh",ss);
                startActivity(i);
            }
        });


        updateNavHeader();

    }


    private NavigationView.OnNavigationItemSelectedListener nav = new NavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

            switch (menuItem.getItemId()) {

                case R.id.home:
                    startActivity(new Intent(MainActivity.this, Main2Activity.class));
                    finish();
                    break;

                case R.id.Profile:
                    startActivity(new Intent(MainActivity.this, ProfileActivity.class));
                    finish();
                    break;
                case R.id.ChatD:
                    Intent i = new Intent(MainActivity.this, ChatActivity.class);

                    i.putExtra("hh", ss);
                    startActivity(i);

                    //   finish();
                    break;

                case R.id.search:
                    Intent t = new Intent(MainActivity.this, SearchForPharmacy.class);

                    //  t.putExtra("hh",ss);
                    startActivity(t);

                    //   finish();


                    break;

                case R.id.search2:
                    if (!(Fe.equals("Doctor"))) {

                        Intent kk = new Intent(MainActivity.this, SearchForDoctor.class);

                        // t.putExtra("hh",ss);
                        startActivity(kk);
                        break;
                    } else {
                        Toast.makeText(MainActivity.this, "هذه خدمة للمريض فقط", Toast.LENGTH_SHORT).show();
                        break;
                    }
                    //   finish();


                case R.id.Logoutm:
                    currentUser = null;
                    FirebaseAuth.getInstance().signOut();

                    Intent aa = new Intent(MainActivity.this, LoginActivity.class);
                    startActivity(aa);
                    finish();
                    break;

                case R.id.addqq:

                    if (!(Fe.equals("Doctor"))) {


                        Intent a = new Intent(MainActivity.this, DataAdderDee.class);

                        //t.putExtra("hh",ss);

                        startActivity(a);
                        break;
                    } else {
                        Toast.makeText(MainActivity.this, "هذه خدمة للمريض فقط", Toast.LENGTH_SHORT).show();
                        break;
                    }


            }

            return true;
        }
    };


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (mToggle.onOptionsItemSelected(item)) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    public void updateNavHeader() {

        NavigationView navigationView = findViewById(R.id.drawer_nav);
        mheaderview = navigationView.getHeaderView(0);

        TextView navUserName = mheaderview.findViewById(R.id.header_username);

        if (currentUser != null) {
            navUserName.setText(currentUser.getEmail());
        }
    }


}
