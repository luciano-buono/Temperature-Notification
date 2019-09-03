package com.firebase.temperaturenotification;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;

public class Options_activity extends AppCompatActivity {

    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_options);

        //Notifications
        final CheckBox checkbox_notif = (CheckBox)findViewById(R.id.checkBox_notif);
        final SharedPreferences sharedPref = Options_activity.this.getSharedPreferences("Sensor_pref",Context.MODE_PRIVATE);
        boolean isMyValueChecked = sharedPref.getBoolean("checkbox_notif", false); //first value -preference name, secend value - default value if checbox not found
        checkbox_notif.setChecked(isMyValueChecked);


        //Radiogroup default sensor
        final RadioGroup rGroup = (RadioGroup)findViewById(R.id.RadioGroup_Sesonrs);
        int radioGroupValue = sharedPref.getInt("radiogroup_sensor",0);
        rGroup.getChildAt(radioGroupValue);

        TempReadActivity.Sensor_default = radioGroupValue;



// This will get the radiobutton in the radiogroup that is checked
        rGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                                              @Override
                                              public void onCheckedChanged(RadioGroup group, int checkedId) {
                                                  switch (checkedId) {
                                                      case R.id.radioButton_Sensor1:
                                                          TempReadActivity.Sensor_default = 0;
                                                          break;
                                                      case R.id.radioButton_Sensor2:
                                                          TempReadActivity.Sensor_default = 1;
                                                          break;
                                                  }
                                                  SharedPreferences.Editor editor = sharedPref.edit();
                                                  editor.putInt("radiogroup_sensor", rGroup.indexOfChild(findViewById(rGroup.getCheckedRadioButtonId())));
                                                  editor.commit();
                                              }
                                          });

        checkbox_notif.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (isChecked) {
                            NotificationHelper.isNotifEnabled = true;
                        } else {
                            NotificationHelper.isNotifEnabled = false;
                        }
                        SharedPreferences.Editor editor = sharedPref.edit();
                        editor.putBoolean("checkbox_notif", checkbox_notif.isChecked());
                        editor.commit();
                    }
                });

        findViewById(R.id.buttonOptions_Tempread).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(TempReadActivity.class);
            }
        });
    }


    private void startActivity(Class<?> activity){
        Intent intent = new Intent(this, activity);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }



}

