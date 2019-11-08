package com.firebase.temperaturenotification;

import android.util.Log;
import android.widget.Toast;

import java.io.Serializable;
import java.text.ParseException;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.Locale;


public class Temperature implements Serializable,Comparable<Temperature>, Comparator<Temperature>{

    public String date;
    public String tempValue;
    public Date dateDate;
    public Double tempValueDouble;
    private String TAG= "TemperatureTAG";

    public Temperature(){

    }


    public Temperature(String dateString, String tempValueString) {
        this.dateDate = getDateDate();
        this.tempValueDouble = getTempValueDouble();
    }

    /*public Temperature(String date,String tempValue) {
        this.date = date;
        this.tempValue = tempValue;
    }*/


    public  Date getDateDate() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyy-MM-dd HH:mm:ss");
        Date date = new Date();
        try{
            date = formatter.parse(this.date);
            Log.d(TAG,"parse date"+date);
            this.dateDate = date;
            return date;
        }catch(ParseException e){
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public int compareTo(Temperature o) {
        if (tempValueDouble== null || o.tempValueDouble== null)
            return 0;
        return tempValueDouble.compareTo(o.tempValueDouble);
    }


    public int compare(Temperature o, Temperature o2){
        if (o.dateDate== null || o2.dateDate == null)
            return 0;
        return o.dateDate.compareTo(o2.dateDate);
    }


    public Double getTempValueDouble() {
        try {
            this.tempValueDouble= Double.parseDouble(this.tempValue);
            return this.tempValueDouble;
        }catch (NumberFormatException e){
            e.printStackTrace();
            return null;
        }
    }


}
