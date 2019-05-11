package com.firebase.temperaturenotification;

import java.io.Serializable;

public class Temperature implements Serializable{

    public String date;
    public String tempValue;

    public Temperature(){

    }

    public Temperature(String date,String tempValue) {
        this.date = date;
        this.tempValue = tempValue;
    }


}
