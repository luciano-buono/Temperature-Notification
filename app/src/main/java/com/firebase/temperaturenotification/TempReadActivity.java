package com.firebase.temperaturenotification;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
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
import android.widget.DatePicker;
import android.widget.EditText;
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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Year;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;


public class TempReadActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {


    private List<Temperature> tempList, tempList_2, tempList_Temp, tempList_Date, tempList_temporal;
    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private TextView textview_StandBy;
    private static final String TAG = "TempReadActivityTAG";
    private boolean est1, est2;// For sorting by date and temp
    private Button b1, b2;
    public static Integer Sensor_default;
    //Temp and date variables
    Double TempMax;
    Double TempMin;
    int YearMin;
    int MonthMin;
    int DayOfMonthMin;
    int YearMax;
    int MonthMax;
    int DayOfMonthMax;
    int SensorNumber;
    Date dateMin;
    Date dateMax;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_temp_read);
        tempList = new ArrayList<>();
        tempList_2 = new ArrayList<>();
        progressBar = findViewById(R.id.progressBarTemp);
        textview_StandBy = findViewById(R.id.textview_standby);

        //Preferencias guardadas para mostrar el ultimo sensor abierto al reabrir la actividad
        final SharedPreferences sharedPref = TempReadActivity.this.getSharedPreferences("Sensor_pref", Context.MODE_PRIVATE);
        Sensor_default = sharedPref.getInt("radiogroup_sensor", 0);
        switch (Sensor_default) {
            case 0:
                loadUsers("Temperature", tempList,1);
                break;
            case 1:
                loadUsers("Temperature_2", tempList_2,2);
                break;
        }
        //Used for Temperature sorting
        est1 = true;
        est2 = true;


        findViewById(R.id.buttonGoToMain2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(MainActivity.class);
            }
        });

        //Sort by date
        b1 = findViewById(R.id.buttonCompareByDate);
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tempList_temporal = new ArrayList<>();
                tempList_temporal.clear();
                switch (SensorNumber){
                    case 1:
                        tempList_temporal = tempList;
                        break;
                    case 2:
                        tempList_temporal = tempList_2;
                        break;
                }
                if (est2) {
                    Collections.sort(tempList_temporal, new Temperature());
                    est2 = false;
                } else {
                    Collections.reverse(tempList_temporal);
                    est2 = true;
                }
                TempAdapter adapter = new TempAdapter(TempReadActivity.this, tempList_temporal);
                recyclerView.setAdapter(adapter);
            }
        });



        //Sort by temp value
        b2 = findViewById(R.id.buttonCompareByTemp);
        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tempList_temporal = new ArrayList<>();
                tempList_temporal.clear();
                switch (SensorNumber){
                    case 1:
                        tempList_temporal = tempList;
                        break;
                    case 2:
                        tempList_temporal = tempList_2;
                        break;
                }
                if (est2) {
                            Collections.sort(tempList_temporal);
                    est2 = false;
                } else {
                            Collections.reverse(tempList_temporal);
                    est2 = true;
                }
                TempAdapter adapter = new TempAdapter(TempReadActivity.this, tempList_temporal);
                recyclerView.setAdapter(adapter);
            }
        });

    }


    private void loadUsers(String Sensor_table, final List<Temperature> list, int sensorNumber) {
        SensorNumber = sensorNumber;
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

                    list.clear();//If the list is not empty, clear it before adding more items (Avoid duplicates)
                    for (DataSnapshot dsTemp : dataSnapshot.getChildren()) {
                        Temperature temperature = dsTemp.getValue(Temperature.class);
                        list.add(temperature);
                        temperature.getDateDate();
                        temperature.getTempValueDouble();
                    }
                    TempAdapter adapter = new TempAdapter(TempReadActivity.this, list);
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


    private void startActivity(Class<?> activity) {
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

    //Opciones. 3 puntitos
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.Sensor1_button:
                loadUsers("Temperature", tempList,1);
                return true;
            case R.id.Sensor2_button:
                loadUsers("Temperature_2",tempList_2,2);
                return true;
            case R.id.Options_button2:
                startActivity(Options_activity.class);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    //POP up start-----------------------------------
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

        //SPINNER- Para mostrar 3, 5 o All. Componentes del recyclerView
        Spinner spinner = popupView.findViewById(R.id.spinner_PopUp1);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter_spinner = ArrayAdapter.createFromResource(this,
                R.array.PopUpOptions_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter_spinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter_spinner);
        spinner.setOnItemSelectedListener(this);

        //EditText para poner los 4 valores. 2 de temperatura min y max. 2 de fecha min y max
        final EditText edittext_PopUp_TempMin = popupView.findViewById(R.id.edittext_PopUp_TempMin);
        edittext_PopUp_TempMin.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                TempMin = Double.parseDouble(edittext_PopUp_TempMin.getText().toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        final EditText edittext_PopUp_TempMax = popupView.findViewById(R.id.edittext_PopUp_TempMax);
        edittext_PopUp_TempMax.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                TempMax = Double.parseDouble(edittext_PopUp_TempMax.getText().toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        final EditText edittext_PopUp_DateMin = popupView.findViewById(R.id.edittext_PopUp_DateMin);
        edittext_PopUp_DateMin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar mcurrentDate = Calendar.getInstance();
                int mYear = mcurrentDate.get(Calendar.YEAR);
                int mMonth = mcurrentDate.get(Calendar.MONTH);
                int mDay = mcurrentDate.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog mDatePicker = new DatePickerDialog(TempReadActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        YearMin = year;
                        MonthMin = month;
                        DayOfMonthMin = dayOfMonth;
                        edittext_PopUp_DateMin.setText(YearMin+"/"+MonthMin+"/"+DayOfMonthMin);
                        dateMin = new GregorianCalendar(YearMin,MonthMin,DayOfMonthMin).getTime();
                    }
                }, mYear, mMonth, mDay);
                mDatePicker.setTitle("Select date");
                mDatePicker.show();
            }
        });

        final EditText edittext_PopUp_DateMax = popupView.findViewById(R.id.edittext_PopUp_DateMax);
        edittext_PopUp_DateMax.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar mcurrentDate = Calendar.getInstance();
                int mYear = mcurrentDate.get(Calendar.YEAR);
                int mMonth = mcurrentDate.get(Calendar.MONTH);
                int mDay = mcurrentDate.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog mDatePicker = new DatePickerDialog(TempReadActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        YearMax = year;
                        MonthMax = month;
                        DayOfMonthMax = dayOfMonth;
                        edittext_PopUp_DateMax.setText(YearMax+"/"+MonthMax+"/"+DayOfMonthMax);

                    }
                }, mYear, mMonth, mDay);
                mDatePicker.setTitle("Select date");
                mDatePicker.show();

            }
        });

        //Botones para darle el OK a mostrar solo los elementos que cumplen la Temp o Fecha buscada
        popupView.findViewById(R.id.button_PopUp_Temp).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tempList_Temp = new ArrayList<>();
                tempList_Temp.clear();
                List<Temperature> tempList_Temporal;
                tempList_Temporal = new ArrayList<>();
                tempList_Temporal.clear();

                switch (SensorNumber){
                    case 1:
                        tempList_Temporal = tempList;
                        break;
                    case 2:
                        tempList_Temporal = tempList_2;
                        break;
                }

                if (TempMax == null || TempMin == null){
                    Toast.makeText(TempReadActivity.this, "Select min and max temp", Toast.LENGTH_SHORT).show();
                }
                else{
                    for (int i=0; i<tempList_Temporal.size(); i++) {
                        if(tempList_Temporal.get(i).tempValueDouble <= TempMax && tempList_Temporal.get(i).tempValueDouble >= TempMin){
                            tempList_Temp.add(tempList_Temporal.get(i));
                        }
                    }
                }

                if (tempList_Temp.size() == 0){
                    Toast.makeText(TempReadActivity.this, "No results found", Toast.LENGTH_SHORT).show();
                }
                else{
                    TempAdapter adapter = new TempAdapter(TempReadActivity.this, tempList_Temp);
                    recyclerView.setAdapter(adapter);
                }

            }
        });

        popupView.findViewById(R.id.button_PopUp_Date).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tempList_Date= new ArrayList<>();
                //Lo limpio para que no queden valores de anteriores busquedas
                tempList_Date.clear();

                dateMin = new GregorianCalendar(YearMin,MonthMin,DayOfMonthMin).getTime();
                dateMax = new GregorianCalendar(YearMax,MonthMax,DayOfMonthMax).getTime();
                for (int i=0; i<tempList.size(); i++) {
                    //No muestra si el dia es = al mayor o menor. Solo si esta dentro de ese invervalo
                    if( (tempList.get(i).dateDate.before(dateMax) && tempList.get(i).dateDate.after(dateMin))
                            || tempList.get(i).dateDate.equals(dateMin) ||tempList.get(i).dateDate.equals(dateMax)  ){
                        tempList_Date.add(tempList.get(i));
                    }
                }
                TempAdapter adapter = new TempAdapter(TempReadActivity.this, tempList_Date);
                recyclerView.setAdapter(adapter);
            }
        });

    }//End-PopUp-------------------------------------


    //Spinner-START. Para definir la maxima cantida de elementos a mostrar en el recycler
    public void onItemSelected(AdapterView<?> parent, View view,
                               int pos, long id) {

        tempList_temporal = new ArrayList<>();
        tempList_temporal.clear();
        switch (SensorNumber){
            case 1:
                tempList_temporal = tempList;
                break;
            case 2:
                tempList_temporal = tempList_2;
                break;
        }
        switch (pos) {
            case 0:
                TempAdapter adapter0 = new TempAdapter(TempReadActivity.this, tempList_temporal);
                recyclerView.setAdapter(adapter0);
                break;
            case 1:
                TempAdapter adapter1 = new TempAdapter(TempReadActivity.this, tempList_temporal, 5);
                recyclerView.setAdapter(adapter1);
                break;
            case 2:
                TempAdapter adapter2 = new TempAdapter(TempReadActivity.this, tempList_temporal, 3);
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
