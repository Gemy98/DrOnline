package com.example.dronlinef;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class DloginActivity extends AppCompatActivity {

    public static final String TAG = "TAG";
    public String uName;

    EditText emailid, password;
    Button btnlogin;
    TextView tvSignup;

    Button btnDoctorL;

    FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;


    //  TextView tvSignup ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dlogin);


        tvSignup = findViewById(R.id.signuptv);


        mFirebaseAuth = FirebaseAuth.getInstance();
        emailid = findViewById(R.id.emailt);
        password = findViewById(R.id.passwordt);
        tvSignup = findViewById(R.id.signuptv);

        btnlogin = findViewById(R.id.login);
        btnDoctorL = findViewById(R.id.doctorb);


        btnlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                //   btnlogin.setBackgroundResource(R.drawable.buttonbg2);
                //   btnlogin.setBackground(getResources().getDrawable(R.drawable.buttonbg2));


                String email = emailid.getText().toString();
                String pwd = password.getText().toString();
                if (email.isEmpty()) {
                    emailid.setError("please enter your email");
                    emailid.requestFocus();

                } else if (pwd.isEmpty()) {
                    password.setError("please enter your papssword");
                    password.requestFocus();
                } else if (email.isEmpty() && pwd.isEmpty()) {

                    Toast.makeText(DloginActivity.this, "fields are empty", Toast.LENGTH_SHORT).show();


                } else if (!(email.isEmpty() && pwd.isEmpty())) {

                    mFirebaseAuth.signInWithEmailAndPassword(email, pwd).addOnCompleteListener(DloginActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (!task.isSuccessful()) {
                                Toast.makeText(DloginActivity.this, "Login Error , please login Again ", Toast.LENGTH_SHORT).show();

                            } else {

                                uName = emailid.getText().toString().trim();
                                finish();
                                Intent intToHome = new Intent(DloginActivity.this, MainActivity.class);
                                startActivity(intToHome);
                            }
                        }
                    });


                } else {

                    Toast.makeText(DloginActivity.this, "Error Occured ", Toast.LENGTH_SHORT).show();

                }

            }


        });


        tvSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intSignUp = new Intent(DloginActivity.this, DsignupActivity.class);
                startActivity(intSignUp);
            }
        });


    }


}
