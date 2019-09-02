package com.firebase.temperaturenotification;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.Spinner;
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


public class TempReadActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {


    private List<Temperature> tempList, tempList_2;
    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private TextView textview_StandBy;
    private static final String TAG = "TempReadActivityTAG";
    private boolean est1,est2;
    private Button b1,b2;
    public static Integer Sensor_default;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_temp_read);
        tempList = new ArrayList<>();
        tempList_2 = new ArrayList<>();
        progressBar = findViewById(R.id.progressBarTemp);
        textview_StandBy = findViewById(R.id.textview_standby);

        final SharedPreferences sharedPref = TempReadActivity.this.getSharedPreferences("Sensor_pref", Context.MODE_PRIVATE);
        Sensor_default = sharedPref.getInt("radiogroup_sensor",0);
        switch (Sensor_default){
            case 0:
                loadUsers("Temperature");
                break;
            case 1:
                loadUsers_2("Temperature_2");
                break;
        }

        est1= true;
        est2=true;



        findViewById(R.id.buttonGoToMain2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(MainActivity.class);
            }
        });

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

                    tempList.clear();//If the list is not empty, clear it before adding more items (Avoid duplicates)
                    for (DataSnapshot dsTemp : dataSnapshot.getChildren()) {
                        Temperature temperature = dsTemp.getValue(Temperature.class);
                        tempList.add(temperature);
                        temperature.getDateDate();
                        temperature.getTempValueDouble();
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
    private void loadUsers_2(String Sensor_table) {
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

                    tempList_2.clear();//If the list is not empty, clear it before adding more items (Avoid duplicates)
                    for (DataSnapshot dsTemp : dataSnapshot.getChildren()) {
                        Temperature temperature = dsTemp.getValue(Temperature.class);
                        tempList_2.add(temperature);
                        temperature.getDateDate();
                        temperature.getTempValueDouble();
                    }
                    TempAdapter adapter = new TempAdapter(TempReadActivity.this, tempList_2);
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



    private void startActivity(Class<?> activity){
        Intent intent = new Intent(this, activity);
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
                return true;
            case R.id.Sensor2_button:
                loadUsers_2("Temperature_2");
                return true;
            case R.id.Options_button2:
                startActivity(Options_activity.class);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    //POP up start
    public void onButtonShowPopupWindowClick(View view) {

        // inflate the layout of the popup window
        LayoutInflater inflater = (LayoutInflater)
                getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.popup_window, null);

        // create the popup window
        int width = LinearLayout.LayoutParams.WRAP_CONTENT;
        int height = LinearLayout.LayoutParams.WRAP_CONTENT;


        boolean focusable = true; // lets taps outside the popup also dismiss it
        final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);

        // show the popup window
        // which view you pass in doesn't matter, it is only used for the window tolken
        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);

        // dismiss the popup window when touched
        popupView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                popupWindow.dismiss();
                return true;
            }
        });


        Spinner spinner = popupView.findViewById(R.id.spinner_PopUp1);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter_spinner = ArrayAdapter.createFromResource(this,
                R.array.PopUpOptions_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter_spinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter_spinner);
        spinner.setOnItemSelectedListener(this);
    }






    //Spinner-START
    public void onItemSelected(AdapterView<?> parent, View view,
                               int pos, long id) {

        switch (pos){
            case 0:
                TempAdapter adapter0 = new TempAdapter(TempReadActivity.this, tempList);
                recyclerView.setAdapter(adapter0);
                break;
            case 1:
                TempAdapter adapter1 = new TempAdapter(TempReadActivity.this, tempList, 5);
                recyclerView.setAdapter(adapter1);
                break;
            case 2:
                TempAdapter adapter2 = new TempAdapter(TempReadActivity.this, tempList, 3);
                recyclerView.setAdapter(adapter2);
                break;

        }
        // An item was selected. You can retrieve the selected item using
        // parent.getItemAtPosition(pos)
    }

    public void onNothingSelected(AdapterView<?> parent) {
        // Another interface callback
    }
    //Spinner-END
    //POP up end
}
