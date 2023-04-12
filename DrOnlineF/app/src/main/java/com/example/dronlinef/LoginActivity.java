package com.example.dronlinef;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {
    public static final String TAG = "TAG";
    public String uName;

    EditText emailid, password;
    Button btnlogin;
    TextView tvSignup;

    Button btnDoctorL;

    FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        mFirebaseAuth = FirebaseAuth.getInstance();
        emailid = findViewById(R.id.emailt);
        password = findViewById(R.id.passwordt);
        tvSignup = findViewById(R.id.signuptv);

        btnlogin = findViewById(R.id.login);
        btnDoctorL = (Button) findViewById(R.id.doctorb);

        mAuthStateListener = new FirebaseAuth.AuthStateListener() {


            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser mFirebaseUser = mFirebaseAuth.getCurrentUser();

                if (mFirebaseUser != null) {

                    Toast.makeText(LoginActivity.this, "You Are Logged in", Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(i);
                } else {
                    Toast.makeText(LoginActivity.this, "Please Login", Toast.LENGTH_SHORT).show();
                }
            }
        };

        btnlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String email = emailid.getText().toString();
                String pwd = password.getText().toString();
                if (email.isEmpty()) {
                    emailid.setError("please enter your email");
                    emailid.requestFocus();
                } else if (pwd.isEmpty()) {
                    password.setError("please enter your papssword");
                    password.requestFocus();
                } else if (email.isEmpty() && pwd.isEmpty()) {
                    Toast.makeText(LoginActivity.this, "fields are empty", Toast.LENGTH_SHORT).show();
                } else if (!(email.isEmpty() && pwd.isEmpty())) {
                    mFirebaseAuth.signInWithEmailAndPassword(email, pwd).addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (!task.isSuccessful()) {
                                Toast.makeText(LoginActivity.this, "Login Error , please login Again ", Toast.LENGTH_SHORT).show();
                            } else {
                                uName = emailid.getText().toString().trim();
                                finish();
                                Intent intToHome = new Intent(LoginActivity.this, MainActivity.class);
                                startActivity(intToHome);
                            }
                        }
                    });
                } else {
                    Toast.makeText(LoginActivity.this, "Error Occured ", Toast.LENGTH_SHORT).show();
                }

            }


        });
        tvSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intSignUp = new Intent(LoginActivity.this, SignupActivity.class);
                startActivity(intSignUp);
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        mFirebaseAuth.addAuthStateListener(mAuthStateListener);
    }
}
