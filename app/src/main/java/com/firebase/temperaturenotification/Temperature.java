package com.firebase.temperaturenotification;

import java.io.Serializable;
import java.util.Date;
import java.text.SimpleDateFormat;

public class Temperature implements Serializable{

    public String date;
    public String tempValue;

    public Temperature() {

    }

    public Temperature(Date date, Double tempValue) {
        this.date = getDateString(date);
        this.tempValue = getTempValueString(tempValue);
    }


    public static String getDateString(Date date) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyy-mm-dd HH:mm:ss");
        return formatter.format(date);
    }

    public static String getTempValueString(Double tempValue) {
        return String.valueOf(tempValue);
    }

}
