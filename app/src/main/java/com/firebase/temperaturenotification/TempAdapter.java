package com.firebase.temperaturenotification;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import java.text.ParseException;
import java.util.Date;
import java.text.SimpleDateFormat;

import java.util.List;

public class TempAdapter extends RecyclerView.Adapter<TempAdapter.TempViewHolder> {


    private Context context;
    private List<Temperature> tempList;


    public TempAdapter(Context context, List<Temperature> tempList) {
        this.context = context;
        this.tempList = tempList;
    }

    @NonNull
    @Override
    public TempViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.recyclerview_temp, parent, false );
        return new TempViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull TempViewHolder holder, int position) {
        Temperature temperature = tempList.get(position);
        holder.textViewTemp.setText(temperature.date+": "+temperature.tempValue);

    }

    @Override
    public int getItemCount() {
        return tempList.size();
    }


    class TempViewHolder extends RecyclerView.ViewHolder {
        TextView textViewTemp;

        public TempViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewTemp = itemView.findViewById(R.id.textviewTemp);

        }
    }
}
