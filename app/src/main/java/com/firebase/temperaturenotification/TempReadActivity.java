package com.firebase.temperaturenotification;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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


    private List<Temperature> tempList;
    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private static final String TAG = "TempReadActivityTAG";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_temp_read);

        progressBar = findViewById(R.id.progressBarTemp);
        loadUsers();

        findViewById(R.id.buttonGoToMain2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startMainActivity();
            }
        });


    }


    private void loadUsers() {
        progressBar.setVisibility(View.VISIBLE);
        tempList = new ArrayList<>();
        recyclerView = findViewById(R.id.recyclerviewTemp);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        DatabaseReference dbTemp = FirebaseDatabase.getInstance().getReference("Temperature");
        dbTemp.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                progressBar.setVisibility(View.GONE);
                if (dataSnapshot.exists()) {


                    for (DataSnapshot dsTemp : dataSnapshot.getChildren()) {
                        Temperature temperature = dsTemp.getValue(Temperature.class);
                        tempList.add(temperature);
                    }

                    TempAdapter adapter = new TempAdapter(TempReadActivity.this,tempList);
                    recyclerView.setAdapter(adapter);
                } else {
                    Toast.makeText(TempReadActivity.this, "No temperature found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d(TAG,"DataBase Error");
            }
        });
    }


    public void startMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }


}
