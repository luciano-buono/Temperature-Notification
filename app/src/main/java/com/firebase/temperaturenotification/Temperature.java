package com.firebase.temperaturenotification;

import android.util.Log;

import java.io.Serializable;
import java.text.ParseException;
import java.util.Comparator;
import java.util.Date;
import java.text.SimpleDateFormat;


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
        SimpleDateFormat formatter = new SimpleDateFormat("yyy-mm-dd HH:mm:ss");
        try{
            Date date = formatter.parse(this.date);
            Log.d(TAG,"parse date"+date);
            return date;
        }catch(ParseException e){
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public int compareTo(Temperature o) {
        if (getTempValueDouble() == null || o.getTempValueDouble() == null)
            return 0;
        return getTempValueDouble().compareTo(o.getTempValueDouble());
    }


    public int compare(Temperature o, Temperature o2){
        if (o.getDateDate() == null || o2.getDateDate() == null)
            return 0;
        return o.getDateDate().compareTo(o2.getDateDate());
    }


    public Double getTempValueDouble() {
        try {
            return Double.parseDouble(this.tempValue);
        }catch (NumberFormatException e){
            e.printStackTrace();
            return null;
        }
    }




}
