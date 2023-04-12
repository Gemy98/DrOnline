package com.example.dronlinef.fragments;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.dronlinef.Adapters.UserAdapter;
import com.example.dronlinef.Model.User;
import com.example.dronlinef.R;
import com.example.dronlinef.WhoType;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.ArrayList;
import java.util.List;


public class DoctorsFragment extends Fragment {
    private RecyclerView recyclerView;
    private UserAdapter userAdapter;

    private List<User> mUsers;

    SwipeRefreshLayout swipeRefreshLayout;

    static String sa = "";

    DocumentReference documentReference;
    DocumentReference documentReference2;
    DocumentReference documentReference3;


    DatabaseReference reference;
    DatabaseReference reference2;


    FirebaseFirestore fstore;
    DocumentReference R2;
    FirebaseAuth mAuth;
    FirebaseUser cr;
    WhoType whoType;

    static String ss = "";

    String Tester = "none";
    //      ٍString FF ;
    EditText search_users;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View view = inflater.inflate(R.layout.fragment_doctors, container, false);


        recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        mUsers = new ArrayList<>();

        swipeRefreshLayout = view.findViewById(R.id.swipe);


        readUsers();


        search_users = view.findViewById(R.id.search_users);
        search_users.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                searchUsers(charSequence.toString().toLowerCase());

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {


                readUsers();
                searchUsers(search_users.getText().toString());
                swipeRefreshLayout.setRefreshing(false);

            }
        });


        return view;
    }

    private void searchUsers(final String s) {
        final FirebaseUser fuser = FirebaseAuth.getInstance().getCurrentUser();
        if (!(s.equals(""))) {

            if (sa.equals("Doctor")) {

                Query query = FirebaseDatabase.getInstance().getReference("Users").child("Normal").orderByChild("username")
                        .startAt(s.toUpperCase())
                        .endAt(s.toLowerCase() + "\uf8ff");
                query.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                        mUsers.clear();

                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            User user = snapshot.getValue(User.class);

                            assert user != null;
                            assert fuser != null;
                            if (!user.getId().equals(fuser.getUid()) && user.getUsername().toLowerCase().equals(s)) {
                                mUsers.add(user);
                            }
                        }

                        userAdapter = new UserAdapter(getContext(), mUsers);
                        recyclerView.setAdapter(userAdapter);

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            } else {
                Query query = FirebaseDatabase.getInstance().getReference("Users").child("Doctors").orderByChild("username")
                        .startAt(s.toUpperCase())
                        .endAt(s.toLowerCase() + "\uf8ff");
                query.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                        mUsers.clear();

                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            User user = snapshot.getValue(User.class);

                            assert user != null;
                            assert fuser != null;
                            if (!user.getId().equals(fuser.getUid()) && user.getUsername().toLowerCase().equals(s)) {
                                mUsers.add(user);
                            }
                        }

                        userAdapter = new UserAdapter(getContext(), mUsers);
                        recyclerView.setAdapter(userAdapter);

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        } else {
            readUsers();
        }
    }


    private void readUsers() {


        final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();


        fstore = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        documentReference2 = fstore.collection("users").document(firebaseUser.getUid());

        documentReference2.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable final DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                ss = documentSnapshot.getString("type").trim();
                sa = ss;
                reference = FirebaseDatabase.getInstance().getReference("Users").child("Normal");
                reference2 = FirebaseDatabase.getInstance().getReference("Users").child("OnlineDoctors");


                if (sa.equals("Doctor")) {


                    reference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            if (search_users.getText().toString().equals("")) {

                                mUsers.clear();
                                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                                    final User user = snapshot.getValue(User.class);

                                    assert user != null;
                                    assert firebaseUser != null;

                                    if (!user.getId().equals(firebaseUser.getUid())) {
                                        documentReference = fstore.collection("users").document(user.getId());
                                        //  Log.d("TAG", user.getId());
                                        Log.d("TAG", user.getId());

                                        documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
                                            @Override
                                            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                                                Tester = documentSnapshot.getString("type").trim();

                                                Log.d("TAG", "hehe");
                                                Log.d("TAG", "hehe");

                                            }

                                        });
                                        Log.d("TAG", "HaHa");
                                        Log.d("TAG", "HaHa");
                                        mUsers.add(user);
                                        Log.d("TAG", user.getId());
                                        Log.d("TAG", "ana wasalt hna");
                                    }
                                }
                                userAdapter = new UserAdapter(getContext(), mUsers);
                                recyclerView.setAdapter(userAdapter);
                            }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                        }
                    });
                } else if (sa.equals("Normal")) {
                    reference2.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            if (search_users.getText().toString().equals("")) {

                                mUsers.clear();
                                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                                    final User user = snapshot.getValue(User.class);

                                    assert user != null;
                                    assert firebaseUser != null;

                                    if (!user.getId().equals(firebaseUser.getUid())) {
                                        Log.d("TAG", user.getId());
                                        Log.d("TAG", "HaHa66");

                                        String Se = "";


                                        mUsers.add(user);

                                        Log.d("TAG", user.getId());
                                        Log.d("TAG", "ana wasalt hna");

                                    }
                                }
                                userAdapter = new UserAdapter(getContext(), mUsers);
                                recyclerView.setAdapter(userAdapter);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                        }
                    });
                } else {
                    Log.d("TAG", "fe 7aga 8lt");
                }
            }
        });

    }
}




