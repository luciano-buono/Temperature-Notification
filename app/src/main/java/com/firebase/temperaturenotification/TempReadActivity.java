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
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;


public class TempReadActivity extends AppCompatActivity {


    private List<Temperature> tempList, tempList2;
    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private static final String TAG = "TempReadActivityTAG";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_temp_read);

        tempList = new ArrayList<>();
        progressBar = findViewById(R.id.progressBarTemp);
        loadUsers();

        findViewById(R.id.buttonGoToMain2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startMainActivity();
            }
        });

        //-----------------TESTING------------------//
        //Me pone los campos de fecha mas actual a la mas vieja
        findViewById(R.id.buttonCompareByDate).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Collections.sort(tempList,Collections.<Temperature>reverseOrder());
                TempAdapter adapter = new TempAdapter(TempReadActivity.this,tempList);
                recyclerView.setAdapter(adapter);

            }
        });
        //Sort by temp value
        findViewById(R.id.buttonCompareByDate).setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Collections.sort(tempList, new Comparator<Temperature>() {
                    @Override
                    public int compare(Temperature o1, Temperature o2) {
                        if(o1.getTempValueDouble() == null || o2.getTempValueDouble() == null)
                            return 0;
                        return (int)(o1.getTempValueDouble() - o2.getTempValueDouble());
                    }
                });
                TempAdapter adapter = new TempAdapter(TempReadActivity.this,tempList);
                recyclerView.setAdapter(adapter);
                return true;
            }
        //VER porque no anda el long click
        });


        //Inicializo arregle de temp
        tempList2 = new ArrayList<>(); //Testing


        //----------END TESTING-----------//
    }

    private Comparator compareByDate = new Comparator<Temperature>() {

        @Override
        public int compare(Temperature o1, Temperature o2) {

            return 0;
        }
    };


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
