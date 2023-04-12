package com.example.dronlinef;

import android.os.Bundle;
import android.widget.TableLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.example.dronlinef.Model.User;
import com.example.dronlinef.fragments.ChatsFragment;
import com.example.dronlinef.fragments.DoctorsFragment;
import com.example.dronlinef.fragments.ProfileFragment;
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

import de.hdodenhof.circleimageview.CircleImageView;


public class ChatActivity extends AppCompatActivity {
    //  int TypeUser ;
    TableLayout tableLayout;
    ViewPager view_pager;

    String sa = "";


    CircleImageView profilepic;
    TextView Username;


    FirebaseUser firebaseUser;
    DatabaseReference reference;
    DatabaseReference reference2;

    FirebaseFirestore fstore;
    FirebaseAuth mAuth;
    FirebaseUser currentUser;
    String userid;
    String TheName = "";
    String TheImage = "";
    DocumentReference documentReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid());
        tableLayout = findViewById(R.id.tab_layout);
        view_pager = findViewById(R.id.view_pager);

        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());

        viewPagerAdapter.addFragment(new ChatsFragment(), "Chats");
        viewPagerAdapter.addFragment(new DoctorsFragment(), "Doctors");
        viewPagerAdapter.addFragment(new ProfileFragment(), "Info");


        view_pager.setAdapter(viewPagerAdapter);
        profilepic = findViewById(R.id.profilepic);
        Username = findViewById(R.id.usertv);

        fstore = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

        userid = currentUser.getUid();

        sa = getIntent().getStringExtra("hh");
        //  Log.d("TAG",sa);


        documentReference = fstore.collection("users").document(userid);
        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                TheName = documentSnapshot.getString("username");
                TheImage = documentSnapshot.getString("imageURL");
            }
        });


        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // if (TypeUser != 2){
                User user = dataSnapshot.getValue(User.class);
                Username.setText(TheName);


                //   if (user.getImageURL().equals("default")) {

                profilepic.setImageResource(R.mipmap.ic_launcher);

                //   } else {

                //        Glide.with(ChatActivity.this).load(user.getImageURL()).into(profilepic);
                //       }


            }
            //}

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    class ViewPagerAdapter extends FragmentPagerAdapter {

        private ArrayList<Fragment> fragments;
        private ArrayList<String> titles;

        ViewPagerAdapter(FragmentManager fm) {
            super(fm);
            this.fragments = new ArrayList<>();
            this.titles = new ArrayList<>();

        }


        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }

        public void addFragment(Fragment fragment, String title) {

            fragments.add(fragment);
            titles.add(title);

        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return titles.get(position);
        }
    }


}
