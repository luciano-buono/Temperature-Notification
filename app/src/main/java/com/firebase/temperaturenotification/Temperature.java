package com.firebase.temperaturenotification;

import java.io.Serializable;
import java.util.Date;
import java.text.SimpleDateFormat;

public class Temperature implements Serializable, Comparable<Temperature>{

    public String date;
    public String tempValue;

    public Date date_date;
    public Double tempValue_tempValue;

    public Temperature(){

    }

    public Temperature(String date,String tempValue) {
        this.date = date;
        this.tempValue = tempValue;
    }

    public Temperature(Date date, Double tempValue){
        this.date_date = date;
        this.tempValue_tempValue = tempValue;
    }


    public int compareTo(Temperature temperature){
        if(tempValue == null || temperature.tempValue == null){
            return 1;
        }
        return (int) (Double.parseDouble(tempValue) - Double.parseDouble(temperature.tempValue));
    }
}
