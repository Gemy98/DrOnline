package com.example.dronlinef;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

public class SignupActivity extends AppCompatActivity {


    public static final String TAG = "TAG";
    Button btnDoctorS;

    EditText usernameid, emailid, password1, password2;
    Button btnSignUp;
    TextView tvSignin;
    TextView SSN;


    FirebaseAuth mFirebaseAuth;
    DatabaseReference reference;

    FirebaseFirestore fstore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        mFirebaseAuth = FirebaseAuth.getInstance();
        fstore = FirebaseFirestore.getInstance();

        btnDoctorS = (Button) findViewById(R.id.doctorb);
        usernameid = findViewById(R.id.susername);
        emailid = findViewById(R.id.emailt);
        password1 = findViewById(R.id.passwordt1);
        password2 = findViewById(R.id.passwordt2);
        tvSignin = findViewById(R.id.signuptv);
        SSN = findViewById(R.id.SSN);
        btnSignUp = findViewById(R.id.login);
        btnSignUp.setOnClickListener(new View.OnClickListener() {
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
                        Toast.makeText(SignupActivity.this, "fields are empty", Toast.LENGTH_SHORT).show();
                    } else if (!(email.isEmpty() && pwd.isEmpty() && qusername.isEmpty() && ssn.isEmpty())) {

                        mFirebaseAuth.createUserWithEmailAndPassword(email, pwd).addOnCompleteListener(SignupActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (!task.isSuccessful()) {

                                    Toast.makeText(SignupActivity.this, "SignUp unsuccessful , please try again", Toast.LENGTH_SHORT).show();
                                } else {
                                    FirebaseUser firebaseUser = mFirebaseAuth.getCurrentUser();
                                    //   String userid = firebaseUser.getUid();
                                    final String userid = firebaseUser.getUid();
                                    reference = FirebaseDatabase.getInstance().getReference("Users").child("Normal").child(userid);
                                    HashMap<String, String> hashMap = new HashMap<>();
                                    hashMap.put("id", userid);
                                    hashMap.put("username", qusername);
                                    hashMap.put("SSN", ssn);
                                    hashMap.put("imageURL", "default");
                                    hashMap.put("Type", "Patient");
                                    reference.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                            }
                                        }
                                    });
                                    DocumentReference documentReference = fstore.collection("users").document(userid);
                                    HashMap<String, String> hashMap2 = new HashMap<>();
                                    hashMap2.put("id", userid);
                                    hashMap2.put("username", qusername);
                                    hashMap2.put("SSN", ssn);
                                    hashMap2.put("imageURL", "default");
                                    hashMap2.put("type", "Normal");
                                    documentReference.set(hashMap2).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Log.d(TAG, "on Success : user profile is created" + userid);
                                            Intent intent = new Intent(SignupActivity.this, MainActivity.class);
                                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                            startActivity(intent);
                                            finish();
                                        }
                                    });
                                }
                            }
                        });
                    } else {

                        Toast.makeText(SignupActivity.this, "Error Occured ", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        tvSignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(SignupActivity.this, LoginActivity.class);
                startActivity(i);
                // finish();
            }
        });

        btnDoctorS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent k = new Intent(SignupActivity.this, DsignupActivity.class);
                startActivity(k);
                // finish();
            }
        });
    }
}


