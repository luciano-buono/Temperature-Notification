package com.firebase.temperaturenotification;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

public class Options extends AppCompatActivity {

    public String email;
    public String token;

    public Options(){

    }

    public Options(String email, String token) {
        this.email = email;
        this.token = token;
    }
}
