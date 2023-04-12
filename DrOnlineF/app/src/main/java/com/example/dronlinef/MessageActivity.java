package com.example.dronlinef;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.dronlinef.Adapters.MessageAdapter;
import com.example.dronlinef.Model.Chat;
import com.example.dronlinef.Model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MessageActivity extends AppCompatActivity {


    CircleImageView Profile_image;
    TextView username;


    FirebaseUser fuser;
    DatabaseReference reference;
    DatabaseReference reference2;


    Intent intent;


    ImageButton btn_send, gallery;
    EditText text_send;


    FirebaseAuth mAuth;
    FirebaseFirestore fstore;
    DocumentReference documentReference2;


    DocumentReference R3;

    String ss;
    String sa;


    MessageAdapter messageAdapter;
    List<Chat> mchat;

    RecyclerView recyclerView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.mainacc);
        setContentView(R.layout.activity_message);

        Toolbar toolbar = findViewById(R.id.toolbar33);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);


        Profile_image = findViewById(R.id.profile_image);
        username = findViewById(R.id.usertv);


        text_send = findViewById(R.id.text_send);
        gallery = findViewById(R.id.gallery);
        btn_send = findViewById(R.id.btn_send);


        intent = getIntent();
        final String userid = intent.getStringExtra("userid");
        fuser = FirebaseAuth.getInstance().getCurrentUser();

        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                String msg = text_send.getText().toString().trim();

                if (!msg.equals("")) {

                    sendMessage(fuser.getUid(), userid, msg);

                } else {
                    Toast.makeText(MessageActivity.this, "You Can't Send Empty Message", Toast.LENGTH_SHORT).show();

                }

                text_send.setText("");
            }
        });


        final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();


        fstore = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        documentReference2 = fstore.collection("users").document(firebaseUser.getUid());
        documentReference2.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                ss = documentSnapshot.getString("type").trim();
                sa = ss;
                reference = FirebaseDatabase.getInstance().getReference("Users").child("Normal");
                reference2 = FirebaseDatabase.getInstance().getReference("Users").child("Doctors");

                if (sa.equals("Doctor")) {
                    reference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            User user = dataSnapshot.getValue(User.class);
                            assert user != null;
                            username.setText(user.getUsername());
                            //     Log.d("TAG",user.getId());


                            R3 = fstore.collection("users").document(userid);
                            R3.addSnapshotListener(new EventListener<DocumentSnapshot>() {
                                @Override
                                public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                                    username.setText(documentSnapshot.getString("username").trim());
                                }
                            });
                            //      if (user.getImageURL().equals("default")) {

                            Profile_image.setImageResource(R.mipmap.ic_launcher);
                            //     } else {

                            Glide.with(MessageActivity.this).load(user.getImageURL()).into(Profile_image);
                            //   }

                            readMessages(fuser.getUid(), userid, user.getImageURL());
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                        }
                    });
                } else {

                    reference2.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            User user = dataSnapshot.getValue(User.class);
                            assert user != null;

                            username.setText(user.getUsername());
//                         Log.d("TAG",user.getId());


                            // String fr ="" ;
                            //  fr = user.getId();
                            R3 = fstore.collection("users").document(userid);
                            R3.addSnapshotListener(new EventListener<DocumentSnapshot>() {
                                @Override
                                public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {

                                    username.setText(documentSnapshot.getString("username").trim());

                                }
                            });

//                        if (user.getImageURL().equals("default")){

                            Profile_image.setImageResource(R.mipmap.ic_launcher);
                            //        }
                            //  else {

                            //   Glide.with(MessageActivity.this).load(user.getImageURL()).into(Profile_image);
                            //    }

                            readMessages(fuser.getUid(), userid, user.getImageURL());
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }
            }
        });
        gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (sa.equals("Doctor")) {
                    Intent intent = new Intent(MessageActivity.this, ImagesActivity.class);
                    startActivity(intent);
                } else if (sa.equals("Normal")) {


                    Intent a = new Intent(MessageActivity.this, DataAdderDee.class);

                    //t.putExtra("hh",ss);

                    startActivity(a);
                } else {
                    Toast.makeText(MessageActivity.this, "please Wait", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    private void sendMessage(String sender, String reciever, String message) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("sender", sender);
        hashMap.put("reciever", reciever);
        hashMap.put("message", message);
        reference.child("Chats").push().setValue(hashMap);
    }

    private void readMessages(final String myid, final String userid, final String imageurl) {
        mchat = new ArrayList<>();
        reference = FirebaseDatabase.getInstance().getReference("Chats");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mchat.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Chat chat = snapshot.getValue(Chat.class);
                    if (chat.getReciever().equals(myid) && chat.getSender().equals(userid) || chat.getReciever().equals(userid) && chat.getSender().equals(myid)) {
                        mchat.add(chat);
                    }
                    messageAdapter = new MessageAdapter(MessageActivity.this, mchat, imageurl);
                    recyclerView.setAdapter(messageAdapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

    }
}
   /* String fr ="" ;
                            fr = user.getId();
                                    R3 = fstore.collection("users").document(fr) ;
                                    R3.addSnapshotListener(new EventListener<DocumentSnapshot>() {
@Override
public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {

        username.setText(documentSnapshot.getString("type").trim());

        }
        });

        */