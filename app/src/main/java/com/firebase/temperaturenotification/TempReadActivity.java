package com.firebase.temperaturenotification;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
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
    private TextView textview_StandBy;
    private static final String TAG = "TempReadActivityTAG";
    private boolean est1,est2;
    private Button b1,b2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_temp_read);
        tempList = new ArrayList<>();
        progressBar = findViewById(R.id.progressBarTemp);
        textview_StandBy = findViewById(R.id.textview_standby);
        loadUsers("Temperature");
        est1= true;
        est2=true;



        findViewById(R.id.buttonGoToMain2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startMainActivity();
            }
        });

        //-----------------TESTING------------------//
        //Me pone los campos de fecha mas actual a la mas vieja
        b1 = findViewById(R.id.buttonCompareByDate);
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Collections.sort(tempList,Collections.<Temperature>reverseOrder());
                if (est1){
                    Collections.sort(tempList, new Temperature());
                    est1=false;}
                else {
                    Collections.reverse(tempList);
                    est1=true; }
                TempAdapter adapter = new TempAdapter(TempReadActivity.this, tempList);
                recyclerView.setAdapter(adapter);

            }
        });

        //Sort by temp value
        b2 = findViewById(R.id.buttonCompareByTemp);
        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (est2){
                    Collections.sort(tempList);
                    est2=false;}
                else {
                    Collections.reverse(tempList);
                    est2=true; }
                TempAdapter adapter = new TempAdapter(TempReadActivity.this, tempList);
                recyclerView.setAdapter(adapter);
            }
        });


        findViewById(R.id.buttonCompareByDate).setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Collections.sort(tempList, new Comparator<Temperature>() {
                    @Override
                    public int compare(Temperature o1, Temperature o2) {
                        if (o1.getTempValueDouble() == null || o2.getTempValueDouble() == null)
                            return 0;
                        return (int) (o1.getTempValueDouble() - o2.getTempValueDouble());
                    }
                });
                TempAdapter adapter = new TempAdapter(TempReadActivity.this, tempList);
                recyclerView.setAdapter(adapter);
                return true;
            }
            //VER porque no anda el long click
        });


        //Inicializo arregle de temp
        tempList2 = new ArrayList<>(); //Testing


        //----------END TESTING-----------//


    }






    private void loadUsers(String Sensor_table) {
        progressBar.setVisibility(View.VISIBLE);
        textview_StandBy.setVisibility(View.GONE);

        recyclerView = findViewById(R.id.recyclerviewTemp);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        DatabaseReference dbTemp = FirebaseDatabase.getInstance().getReference(Sensor_table);
        dbTemp.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                progressBar.setVisibility(View.GONE);
                if (dataSnapshot.exists()) {


                    for (DataSnapshot dsTemp : dataSnapshot.getChildren()) {
                        Temperature temperature = dsTemp.getValue(Temperature.class);
                        tempList.add(temperature);

                    }
                    TempAdapter adapter = new TempAdapter(TempReadActivity.this, tempList);
                    recyclerView.setAdapter(adapter);
                } else {
                    Toast.makeText(TempReadActivity.this, "No temperature found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d(TAG, "DataBase Error");
            }
        });
    }


    public void startMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.tempread_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.Sensor1_button:
                loadUsers("Temperature");
            case R.id.Sensor2_button:
                //loadUsers("Temperature2");
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
