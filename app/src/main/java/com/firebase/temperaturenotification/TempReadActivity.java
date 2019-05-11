package com.firebase.temperaturenotification;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;


public class TempReadActivity extends AppCompatActivity {


    private List<Temperature> tempList, tempList2;
    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private static final String TAG = "TempReadActivityTAG";

    //Testing date
    Temperature temperature2;

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

        //Inicializo arregle de temp
        tempList = new ArrayList<>();
        tempList2 = new ArrayList<>(); //Testing



    }


    private void loadUsers() {
        progressBar.setVisibility(View.VISIBLE);

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

                        //Testing
                        ParseTempList(temperature);
                        tempList2.add(temperature2);
                    }
                    Collections.sort(tempList);

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

    private void ParseTempList(Temperature temperature){

        SimpleDateFormat formatter = new SimpleDateFormat("yyy-mm-dd HH:mm:ss");
        try {
            temperature2.date_date = formatter.parse(temperature.date);
            temperature2.tempValue_tempValue = Double.parseDouble(temperature.tempValue);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }



}
