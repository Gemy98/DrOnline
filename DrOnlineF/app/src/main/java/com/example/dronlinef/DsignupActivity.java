package com.example.dronlinef;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.nfc.Tag;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

import io.opencensus.tags.TagValue;

public class DsignupActivity extends AppCompatActivity {

    public static final String TAG = "TAG";
    TextView tvLogin;

    Button btnSignup;


    EditText usernameid, emailid, password1, password2;

    TextView SSN;


    FirebaseAuth mFirebaseAuth;
    DatabaseReference reference;

    FirebaseFirestore fstore;

    // public int TypeUser = 0  ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dsignup);


        mFirebaseAuth = FirebaseAuth.getInstance();
        fstore = FirebaseFirestore.getInstance();
        usernameid = findViewById(R.id.susername);
        emailid = findViewById(R.id.emailt);
        password1 = findViewById(R.id.passwordt1);
        password2 = findViewById(R.id.passwordt2);
        SSN = findViewById(R.id.SSN);


        tvLogin = findViewById(R.id.signuptv);
        btnSignup = findViewById(R.id.login);

        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final String qusername = usernameid.getText().toString();
                String email = emailid.getText().toString();
                final String ssn = SSN.getText().toString();
                String pwd1 = password1.getText().toString();
                String pwd2 = password2.getText().toString();

                String pwd = "";

                if (!pwd1.equals(pwd2)) {
                    password1.setError("please enter the same password");
                    password1.requestFocus();

                    password2.setError("please enter the same password");
                    password2.requestFocus();
                } else {
                    pwd = pwd1;
                    if (email.isEmpty()) {
                        emailid.setError("please enter your email");
                        emailid.requestFocus();
                    } else if (qusername.isEmpty()) {
                        usernameid.setError("please choose a username");
                        usernameid.requestFocus();
                    } else if (ssn.isEmpty()) {
                        SSN.setError("please enter your SSN");
                        SSN.requestFocus();
                    } else if (pwd.isEmpty()) {
                        password1.setError("please enter your papssword");
                        password1.requestFocus();

                        password2.setError("please enter your papssword");
                        password2.requestFocus();

                    } else if (email.isEmpty() && pwd.isEmpty()) {

                        Toast.makeText(DsignupActivity.this, "fields are empty", Toast.LENGTH_SHORT).show();

                    } else if (!(email.isEmpty() && pwd.isEmpty() && qusername.isEmpty() && ssn.isEmpty())) {

                        mFirebaseAuth.createUserWithEmailAndPassword(email, pwd).addOnCompleteListener(DsignupActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (!task.isSuccessful()) {

                                    Toast.makeText(DsignupActivity.this, "SignUp unsuccessful , please try again", Toast.LENGTH_SHORT).show();

                                } else {


                                    FirebaseUser firebaseUser = mFirebaseAuth.getCurrentUser();
                                    //  String userid = firebaseUser.getUid();
                                    final String userid = firebaseUser.getUid();
                                    reference = FirebaseDatabase.getInstance().getReference("Users").child("Doctors").child(userid);

                                    HashMap<String, String> hashMap = new HashMap<>();
                                    hashMap.put("id", userid);
                                    hashMap.put("username", qusername);
                                    hashMap.put("SSN", ssn);
                                    hashMap.put("imageURL", "default");
                                    reference.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {

                                                // TypeUser = 2 ;


                                            }
                                        }
                                    });

                                    //     FirebaseUser firebaseUser = mFirebaseAuth.getCurrentUser();

                                    DocumentReference documentReference = fstore.collection("users").document(userid);
                                    HashMap<String, String> hashMap2 = new HashMap<>();
                                    hashMap2.put("id", userid);
                                    hashMap2.put("username", qusername);
                                    hashMap2.put("SSN", ssn);
                                    hashMap2.put("Status", "Offline");
                                    hashMap2.put("imageURL", "default");
                                    hashMap2.put("type", "Doctor");
                                    documentReference.set(hashMap2).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Log.d(TAG, "on Success : user profile is created" + userid);

                                            Intent intent = new Intent(DsignupActivity.this, MainActivity.class);

                                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                            startActivity(intent);
                                            finish();


                                        }
                                    });

                                }


                            }
                        });
                    } else {

                        Toast.makeText(DsignupActivity.this, "Error Occured ", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });


        tvLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intSignUp = new Intent(DsignupActivity.this, LoginActivity.class);
                startActivity(intSignUp);
            }
        });


    }
}
