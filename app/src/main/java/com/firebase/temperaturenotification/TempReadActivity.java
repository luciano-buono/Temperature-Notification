package com.firebase.temperaturenotification;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class TempReadActivity extends AppCompatActivity {


    private List<String> tempList;
    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private static final String TAG = "TempReadActivityTAG";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_temp_read);

        loadUsers();

        findViewById(R.id.buttonGoToMain2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProfileActivity.startMainActivity();
            }
        });
    }


    private void loadUsers() {
        progressBar.setVisibility(View.VISIBLE);
        userList = new ArrayList<>();
        recyclerView = findViewById(R.id.recyclerviewProfile);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        DatabaseReference dbUsers = FirebaseDatabase.getInstance().getReference("users");
        dbUsers.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                progressBar.setVisibility(View.GONE);
                if (dataSnapshot.exists()) {

                    for (DataSnapshot dsUsers : dataSnapshot.getChildren()) {
                        User user = dsUsers.getValue(User.class);
                        userList.add(user);
                    }

                    UsersAdapter adapter = new UsersAdapter(ProfileActivity.this, userList);
                    recyclerView.setAdapter(adapter);
                } else {
                    Toast.makeText(ProfileActivity.this, "No user found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
